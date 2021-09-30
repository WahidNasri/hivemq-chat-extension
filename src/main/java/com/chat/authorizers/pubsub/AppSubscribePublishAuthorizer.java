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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class AppSubscribePublishAuthorizer implements SubscriptionAuthorizer, PublishAuthorizer {
    private static final Logger log = LoggerFactory.getLogger(AppSubscribePublishAuthorizer.class);

    @Override
    public void authorizeSubscribe(@NotNull SubscriptionAuthorizerInput input, @NotNull SubscriptionAuthorizerOutput output) {
        String clientId = input.getClientInformation().getClientId();
        String topicFilter = input.getSubscription().getTopicFilter();

        try {
            final String userId = MyBatis.getUserIdBySessionId(clientId);
            log.info(">>authorizeSubscribe: Client ID = " + clientId + " | Topic = " + topicFilter + " | User ID = " + userId);

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
            else if(topicFilter.toLowerCase().startsWith("personalevents/")){
                //=== TODO: Handle personalevents/<ID>

                authorizer = new JustAllowAuthorizer(topicFilter, clientId, userId);
            }
            //============= MUC =============//
            else if(topicFilter.startsWith("muc/")){
                //TODO: Handle permissions
                authorizer = new JustAllowAuthorizer(topicFilter, clientId, userId);
            }

            if(authorizer != null) {
                Result result = authorizer.authorize();
                switch (result) {
                    case AUTHORIZE:
                        output.authorizeSuccessfully();
                        log.info("AUTHORIZED");
                        break;
                    case REJECT:
                        output.failAuthorization();
                        log.info("REJECTED");
                        break;
                    case NEXT:
                        //output.nextExtensionOrDefault();
                        log.info("NEXT");
                        break;
                }
            }
            else{
                log.info("No Authorizer found");
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
            final String userId = MyBatis.getUserIdBySessionId(clientId);

            log.info(">>authorizePublish: Client ID = " + clientId + " | Topic = " + topic + " | User ID = " + userId);

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
            else if (publishPacket.getTopic().toLowerCase().startsWith("messages/") || publishPacket.getTopic().toLowerCase().startsWith("filemessages/")) {
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

            //====== Handle invitations/ topic, authorize logged in users and only if there is no ingoing invitation
            else if(publishPacket.getTopic().toLowerCase().startsWith("invitations/")){
                authorizer = new InvitationPublishAuthorizer(topic, clientId, userId, payload);
            }
            else if(topic.startsWith("personalevents/")){
                authorizer = new PersonalEventsPublishAuthorizer(topic, clientId, userId, payload);

            }

            //============= MUC =============//
            else if(topic.startsWith("muc/")){
                authorizer = new MucPublishAuthorizer(topic, clientId, userId, payload);
            }

            if(authorizer != null){
                Result result = authorizer.authorize();
                switch (result){
                    case AUTHORIZE:
                        output.authorizeSuccessfully();
                        log.info("AUTHORIZED");
                        break;
                    case REJECT:
                        output.failAuthorization();
                        log.info("REJECTED");
                        break;
                    case NEXT:
                        //output.nextExtensionOrDefault();
                        log.info("NEXT");
                        break;
                }
            }
            else {
                log.info("No Authorizer found");
                //output.nextExtensionOrDefault();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
