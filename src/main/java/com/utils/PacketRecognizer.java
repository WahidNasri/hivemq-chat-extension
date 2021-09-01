package com.utils;

import com.hivemq.extension.sdk.api.packets.publish.ModifiablePublishPacket;

import java.util.Locale;

public class PacketRecognizer {
    private static String MESSAGES_PREFIX = "messages/";
    private static String EVENTS_PREFIX = "events/";
    private static String PERSONAL_EVENTS_PREFIX = "personalevents/";
    private static String PRESENCE_PREFIX = "presence/";
    public static boolean isToMessagesTopic(ModifiablePublishPacket publishPacket){
        return isMessageToTopic(publishPacket, MESSAGES_PREFIX);
    }
    public static boolean isToRoomsEventsTopic(ModifiablePublishPacket publishPacket){
        return isMessageToTopic(publishPacket, EVENTS_PREFIX);
    }

    public static boolean isToPersonalEventsTopic(ModifiablePublishPacket publishPacket){
        return isMessageToTopic(publishPacket, PERSONAL_EVENTS_PREFIX);
    }
    public static boolean isToPresenceTopic(ModifiablePublishPacket publishPacket){
        return isMessageToTopic(publishPacket, PRESENCE_PREFIX);
    }
    public static String getRoomId(ModifiablePublishPacket publishPacket){
        if(publishPacket == null){
            return null;
        }
        if(publishPacket.getTopic() == null){
            return null;
        }
        String[] parts = publishPacket.getTopic().split("/");
        if(parts.length < 2){
            return null;
        }
        return parts[1];
    }

    private static boolean isMessageToTopic(ModifiablePublishPacket publishPacket, String prefix){
        if(publishPacket == null){
            return false;
        }
        if(publishPacket.getTopic() == null){
            return false;
        }
        return publishPacket.getTopic().toLowerCase().startsWith(prefix);
    }
}
