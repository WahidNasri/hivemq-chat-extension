package com.db;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class MyBatis {
    public static SqlSessionFactory getSession() throws IOException {
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory =
                    new SqlSessionFactoryBuilder().build(inputStream);

            return sqlSessionFactory;
    }

    public static User getUser(String email, String password){
        try(SqlSession sqlSession = getSession().openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            return mapper.getUserByEmailAndPassword(email, password);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addOrUpdateSession(String clientId, User user, String presence){
        try(SqlSession sqlSession = getSession().openSession()){
            SessionMapper mapper = sqlSession.getMapper(SessionMapper.class);
            mapper.addOrUpdateSession(clientId, user.getId(), presence);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUserIdBySessionId(String sessionId){
        try(SqlSession sqlSession = getSession().openSession()){
            SessionMapper mapper = sqlSession.getMapper(SessionMapper.class);
            return mapper.getUserIdBySessionId(sessionId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isUSerMemberOfRoom(String userId, String roomId){
        try(SqlSession sqlSession = getSession().openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int count = mapper.getMembershipCount(userId, roomId);
            return count > 0;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<ContactChat> getUserContacts(String userId){
        try(SqlSession sqlSession = getSession().openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            return mapper.getUserContacts(userId);
        } catch (IOException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
    public static String getMessageAuthorId(String messageId){
        try(SqlSession sqlSession = getSession().openSession()){
            MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
            return mapper.getMessageAuthorId(messageId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Room getRoomById(String roomId){
        try(SqlSession sqlSession = getSession().openSession()){
            RoomMapper mapper = sqlSession.getMapper(RoomMapper.class);
            return mapper.getRoomById(roomId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static List<String> getRoomsIdThatContainUsers(List<String> userIds){
        try(SqlSession sqlSession = getSession().openSession()){
            RoomMapper mapper = sqlSession.getMapper(RoomMapper.class);
            //String listParam = "(" + String.join(",", userIds) + ")";
            String[] itemsArray = new String[userIds.size()];
            itemsArray = userIds.toArray(itemsArray);

            String idsStr = String.join(",", itemsArray);
            return mapper.getRoomsContainUsers(userIds);
        } catch (IOException e) {
            e.printStackTrace();
            return new LinkedList<>();
        }
    }
    public static ContactChat getUserByID(String userId){
        try(SqlSession sqlSession = getSession().openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            return mapper.getUserById(userId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void insertMessage(Message message){
        try(SqlSession sqlSession = getSession().openSession()){
            MessageMapper mapper = sqlSession.getMapper(MessageMapper.class);
            mapper.insertMessage(message);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getUserIdByUsername(String username){
        try(SqlSession sqlSession = getSession().openSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            return mapper.getUserIDByUserName(username);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Invitation getIngoingInvitation(String fromId, String toId){
        try(SqlSession sqlSession = getSession().openSession()){
            InvitationMapper mapper = sqlSession.getMapper(InvitationMapper.class);
            return mapper.getInvitation(fromId, toId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void insertInvitation(Invitation invitation){
        try(SqlSession sqlSession = getSession().openSession()){
            InvitationMapper mapper = sqlSession.getMapper(InvitationMapper.class);
            mapper.insertInvitation(invitation);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void updateInvitationState(String id, String state){
        try(SqlSession sqlSession = getSession().openSession()){
            InvitationMapper mapper = sqlSession.getMapper(InvitationMapper.class);
            mapper.updateInvitation(id, state);

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void  createRoomWithMembers(Room room, List<String> userIds){
        try(SqlSession sqlSession = getSession().openSession()){
            RoomMapper mapper = sqlSession.getMapper(RoomMapper.class);
            if(room.isGroup()){
                mapper.createGroupRoom(room);
            }
            else {
                mapper.createPrivateRoom(room);
            }
            for (String userId : userIds){
                mapper.createRoomMemberShip(room.getId(), userId);
            }

            sqlSession.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLastPresenceByUserId(String userId){
        try(SqlSession sqlSession = getSession().openSession()){
            SessionMapper mapper = sqlSession.getMapper(SessionMapper.class);
            return mapper.getLAstPresenceInfo(userId);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
