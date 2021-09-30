package com.db;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserMapper {
    @Select("SELECT * FROM user WHERE email = #{email} AND password = #{password}")
    User getUserByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    @Select("SELECT count(*) FROM room_membership WHERE room_id = #{roomId} AND user_id = #{userId}")
    int getMembershipCount(@Param("userId") String userId, @Param("roomId") String roomId);

    //FIXME: fix this query, returns duplication and wrong data (for example: members of the same group are returned as contacts)
    @Select("SELECT DISTINCT\n" +
            "    u.id,\n" +
            "    m.room_id roomId,\n" +
            "    u.first_name firstName,\n" +
            "    u.last_name lastName,\n" +
            "    u.email,\n" +
            "    u.avatar,\n" +
            "    r.is_group isGroup\n" +
            "FROM\n" +
            "    USER u\n" +
            "JOIN room_membership m ON\n" +
            "    u.id = m.user_id\n" +
            "JOIN room r ON\n" +
            "    r.id = m.room_id\n" +
            "WHERE\n" +
            "    m.room_id IN(\n" +
            "    SELECT\n" +
            "        m.room_id\n" +
            "    FROM\n" +
            "        room_membership m\n" +
            "    WHERE\n" +
            "        m.user_id = #{userId}\n" +
            ") AND u.id <> #{userId} AND r.is_group = 0\n" +
            "UNION ALL\n" +
            "SELECT\n" +
            "    r.id id,\n" +
            "    r.id roomId,\n" +
            "    r.name firstName,\n" +
            "    '' lastName,\n" +
            "    '' email,\n" +
            "    r.avatar avatar,\n" +
            "    r.is_group isGroup\n" +
            "FROM\n" +
            "    `room_membership` m\n" +
            "JOIN room r ON\n" +
            "    m.room_id = r.id\n" +
            "WHERE\n" +
            "    r.is_group = 1 AND user_id = #{userId};")
    List<ContactChat> getUserContacts(@Param("userId") String userId);

    @Select("SELECT id, first_name firstName, last_name lastName, avatar FROM user where id = #{userId}")
    ContactChat getUserById(@Param("userId") String userId);

    @Select("SELECT id FROM user WHERE email = #{username}")
    String getUserIDByUserName(@Param("username") String username);
}
