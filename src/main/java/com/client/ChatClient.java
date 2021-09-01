package com.client;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.util.UUID;

public class ChatClient {
    public static void connectAndPublish(String payload, String topic){
        try {
            final Mqtt5BlockingClient client = Mqtt5Client.builder()
                    .identifier("supreme_" + UUID.randomUUID().toString())
                    .serverHost("localhost")
                    .buildBlocking();

            client.connect();

            client.publishWith().topic(topic).qos(MqttQos.AT_LEAST_ONCE).payload(payload.getBytes()).send();
            client.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
