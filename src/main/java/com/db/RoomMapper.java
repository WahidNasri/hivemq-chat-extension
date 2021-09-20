package com.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoomMapper {
    @Select("<script>SELECT room_id FROM `room_membership` GROUP BY room_id HAVING MIN(user_id) IN (<foreach collection='userids' item='userid' separator=', '>#{userid}</foreach>) and MAX(user_id) IN (<foreach collection='userids' item='userid' separator=', '>#{userid}</foreach>)</script>")
    String getRoomContainUsers(@Param("userids") List<String> userids);

    @Insert("INSERT INTO room (id) VALUES (#{id})")
    void createPrivateRoom(Room room);

    @Insert("INSERT INTO room_membership (room_id, user_id) VALUES (#{roomId}, #{userId})")
    void createRoomMemberShip(@Param("roomId") String roomId, @Param("userId") String userId);
}
