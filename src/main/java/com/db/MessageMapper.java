package com.db;

import com.models.ChatMessage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MessageMapper {
    @Select("SELECT from_id FROM message WHERE id = #{messageId}")
    String getMessageAuthorId(@Param("messageId") String messageId);

    @Insert("INSERT into message (id, type, from_id, text, originality, attachment, thumbnail, original_id, room, send_time) VALUES (#{id}, #{type}, #{from_id}, #{text}, #{originality}, #{attachment}, #{thumbnail}, #{original_id}, #{room}, #{send_time})")
    void insertMessage(Message message);
}
