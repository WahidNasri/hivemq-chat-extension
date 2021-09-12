
/*
 * Copyright 2018-present HiveMQ GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chat.interceptors;

import com.db.MyBatis;
import com.db.User;
import com.files.FileSaver;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundInput;
import com.hivemq.extension.sdk.api.interceptor.publish.parameter.PublishInboundOutput;
import com.hivemq.extension.sdk.api.packets.publish.ModifiablePublishPacket;
import com.models.ChatMessage;
import com.models.MessageOriginality;
import com.models.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.utils.JsonParser;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;


public class HelloWorldInterceptor implements PublishInboundInterceptor {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldInterceptor.class);

    @Override
    public void onInboundPublish(final @NotNull PublishInboundInput publishInboundInput, final @NotNull PublishInboundOutput publishInboundOutput) {
        final ModifiablePublishPacket publishPacket = publishInboundOutput.getPublishPacket();

        String topic = publishPacket.getTopic();
        String clientID = publishInboundInput.getClientInformation().getClientId();
        String payload = JsonParser.byteBufferToString(publishPacket.getPayload().get().asReadOnlyBuffer(), StandardCharsets.UTF_8);

        //do not intercept supreme user messages
        if(clientID.startsWith("supreme_")){
            return;
        }
        if(topic.toLowerCase().startsWith("messages/") || topic.toLowerCase().startsWith("events/")) {
            try {
                Type tp = new TypeToken<Map<String, String>>() {
                }.getType();
                Map<String, String> map = new Gson().fromJson(payload, tp);

                //add sendTime if not present
                long now = Instant.now().toEpochMilli();
                map.computeIfAbsent("sendTime", k -> String.valueOf(now));
                map.computeIfAbsent("roomId", k -> topic.split("/")[1
                        ]);//TODO: decide force topic or not

                //set the fromId, to not let anyone claim that he is another person
                String userId = MyBatis.getUserIdBySessionId(clientID);
                if (userId != null) {
                    map.put("fromId", userId);
                }

                //tempered payload
                String tempered = new Gson().toJson(map);
                publishPacket.setPayload(ByteBuffer.wrap(tempered.getBytes(StandardCharsets.UTF_8)));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(topic.toLowerCase().startsWith("filemessages/")){
            String room = topic.split("/")[1];
            try {
                String userId = MyBatis.getUserIdBySessionId(clientID);

                ChatMessage msg =  FileSaver.saveAndGetMessageObject(payload, room, userId);

                String newp = JsonParser.toJson(msg);

                publishPacket.setPayload(ByteBuffer.wrap(newp.getBytes(StandardCharsets.UTF_8)));
                publishPacket.setTopic("messages/" + room);
            }catch (Exception ee){
                ee.printStackTrace();
            }
        }
    }


}