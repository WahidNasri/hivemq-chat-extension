package com.db;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface MessageMapper {
    @Select("SELECT from_id FROM message WHERE id = #{messageId}")
    String getMessageAuthorId(@Param("messageId") String messageId);
}
