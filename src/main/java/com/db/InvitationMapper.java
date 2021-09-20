package com.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface InvitationMapper {
    @Select("SELECT * FROM invitation WHERE (from_id = #{fromId} AND to_id = #{toId} OR from_id = #{toId} AND to_id = #{fromId}) AND state = 'ingoing'")
    Invitation getInvitation(@Param("fromId") String fromId, @Param("toId") String toId);

    @Insert("INSERT INTO invitation (id, from_id, to_id) VALUES (#{id}, #{from_id}, #{to_id})")
    void insertInvitation(Invitation invitation);

    @Update("UPDATE invitation SET state = #{state} WHERE id = #{id}")
    void updateInvitation(@Param("id") String id, @Param("state") String state);
}
