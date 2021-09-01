package com.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SessionMapper {
    @Insert("INSERT INTO session (id, user_id, is_active) VALUES (#{id}, #{userId}, 1)")
    void addSession(@Param("id") String id, @Param("userId") String userId);

    @Select("SELECT user_id FROM session WHERE id = #{sessionId} AND is_active = 1 ORDER BY last_presence DESC LIMIT 1")
    String getUserIdBySessionId(@Param("sessionId") String sessionId);
}
