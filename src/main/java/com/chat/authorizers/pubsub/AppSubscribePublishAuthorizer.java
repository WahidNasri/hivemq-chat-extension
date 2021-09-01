package com.chat.authorizers.pubsub;

import com.db.MyBatis;
import com.chat.authorizers.pubsub.publishAuthorizers.*;
import com.chat.authorizers.pubsub.subscribeAuthorizers.*;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.auth.PublishAuthorizer;
import com.hivemq.extension.sdk.api.auth.SubscriptionAuthorizer;
import com.hivemq.extension.sdk.api.auth.parameter.PublishAuthorizerInput;
import com.hivemq.extension.sdk.api.auth.parameter.PublishAuthorizerOutput;
import com.hivemq.extension.sdk.api.auth.parameter.SubscriptionAuthorizerInput;
import com.hivemq.extension.sdk.api.auth.parameter.SubscriptionAuthorizerOutput;
import com.hivemq.extension.sdk.api.packets.publish.PublishPacket;
import com.utils.JsonParser;
import com.chat.authorizers.BaseChatAuthorize;
import com.chat.authorizers.Result;
import org.example.authorizers.pubsub.publishAuthorizers.*;
import org.example.authorizers.pubsub.subscribeAuthorizers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class AppSubscribePublishAuthorizer implements SubscriptionAuthorizer, PublishAuthorizer {
    private static final Logger log = LoggerFactory.getLogger(AppSubscribePublishAuthorizer.class);

    @Override
    public void authorizeSubscribe(@NotNull SubscriptionAuthorizerInput input, @NotNull SubscriptionAuthorizerOutput output) {
        String clientId = input.getClientInformation().getClientId();
        log.info(">>authorizeSubscribe: Client ID = " + clientId);

        String topicFilter = input.getSubscription().getTopicFilter();
        log.info(">>authorizeSubscribe: topic Filter = " + topicFilter);

        try {
            final String userId = MyBatis.getUserIdBySessionId(clientId);
            log.info(">>authorizeSubscribe: User ID = " + userId);

            BaseChatAuthorize authorizer = null;
            //===== Handle archivesrooms/<clientId>. ==> Allow and publish contacts and groups to the topic
            if (topicFilter.toLowerCase().startsWith("archivesrooms/")){
                authorizer = new ArchiveRoomsSubscribeAuthorizer(topicFilter, clientId, userId);
            }
            else if(topicFilter.toLowerCase().startsWith("archivesmessages/") ){
                authorizer = new MessagesSubscribeAuthorizer(topicFilter, clientId, userId);
            }
            else if(topicFilter.toLowerCase().startsWith("archivesmyid/")){
                authorizer = new ArchiveIdSubscribeAuthorizer(topicFilter, clientId, userId);

            }
            //==== Handle messages/<roomId> and events/<roomId>, only members can subscribe
            else if (topicFilter.toLowerCase().startsWith("messages/") || topicFilter.toLowerCase().startsWith("events/")) {
                authorizer = new MessagesAndEventsSubscribeAuthorizer(topicFilter, clientId, userId);
            }
            //==== Handle presence/<ID>, only contacts can subscribe
            else if (topicFilter.toLowerCase().startsWith("presence/")) {
                authorizer = new PresenceSubscribeAuthorizer(topicFilter, clientId, userId);
            }
            //=== TODO: Handle personalevents/<ID>

            if(authorizer != null) {
                Result result = authorizer.authorize();
                switch (result) {
                    case AUTHORIZE:
                        output.authorizeSuccessfully();
                        break;
                    case REJECT:
                        output.failAuthorization();
                    case NEXT:
                        output.nextExtensionOrDefault();
                }
            }
        }catch (Exception e){
            log.info("XXXX");
            e.printStackTrace();
        }
    }

    @Override
    public void authorizePublish(@NotNull PublishAuthorizerInput input, @NotNull PublishAuthorizerOutput output) {
        //get the PUBLISH packet contents
        final PublishPacket publishPacket = input.getPublishPacket();
        try {

            final String clientId = input.getClientInformation().getClientId();
            final String topic = publishPacket.getTopic();
            final String payload = JsonParser.byteBufferToString(publishPacket.getPayload().get(), StandardCharsets.UTF_8);
            log.info(">>authorizePublish: Client ID = " + clientId);

            final String userId = MyBatis.getUserIdBySessionId(clientId);
            log.info(">>authorizePublish: User ID = " + userId);
            log.info(">>authorizePublish: Topic = " + publishPacket.getTopic());

            BaseChatAuthorize authorizer = null;
            //allow lastwills
            if(publishPacket.getTopic().equalsIgnoreCase("lastwills")){
               authorizer = new JustAllowAuthorizer(topic, clientId, userId);
            }
            //allow supreme user to publish to archiverooms
            else if (publishPacket.getTopic().toLowerCase().startsWith("archivesrooms/") || publishPacket.getTopic().toLowerCase().startsWith("archivesmessages/") || publishPacket.getTopic().toLowerCase().startsWith("archivesmyid/")){
                authorizer = new ArchivePublishAuthorizer(topic, clientId, userId);
            }
            //====== Handle messages/ topic, only room members can publish=====/
            else if (publishPacket.getTopic().toLowerCase().startsWith("messages/")) {
                authorizer = new MessagesPublishAuthorizer(topic, clientId, userId);
            }

            //===== Handle events/ topic, only room members can publish. for ChatMarker, msg owner cannot publish
            else if (publishPacket.getTopic().toLowerCase().startsWith("events/")) {
                authorizer = new EventsPublishAuthorizer(topic, clientId, userId, payload);
            }

            //====== Handle presence/ topic, only Id owner can publish
            else if (publishPacket.getTopic().toLowerCase().startsWith("presence/")) {
                authorizer = new PresencePublishAuthorizer(topic, clientId, userId);
            }
            //==== TODO: Handle personalevents/ topic, if invitation check there is no ingoing invitation

            if(authorizer != null){
                Result result = authorizer.authorize();
                switch (result){
                    case AUTHORIZE:
                        output.authorizeSuccessfully();
                        break;
                    case REJECT:
                        output.failAuthorization();
                    case NEXT:
                        output.nextExtensionOrDefault();
                }
            }
            else {
                output.nextExtensionOrDefault();
            }
        }catch (Exception e){
            log.info("XXXX");
            log.info(e.getMessage());
        }
    }
}
