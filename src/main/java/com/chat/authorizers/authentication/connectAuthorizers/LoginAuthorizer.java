package com.chat.authorizers.authentication.connectAuthorizers;

import com.client.ChatClient;
import com.db.ContactChat;
import com.db.MyBatis;
import com.db.User;
import com.chat.authorizers.BaseConnectAuthorizer;
import com.chat.authorizers.Result;
import com.utils.JsonParser;

import java.util.List;

public class LoginAuthorizer extends BaseConnectAuthorizer {
    public LoginAuthorizer(String username, String password, String clientId) {
        super(username, password, clientId);
    }

    @Override
    public Result authorize() {

        try {
            User dbUser = MyBatis.getUser(username, password);
            if (dbUser == null) {
                return Result.REJECT;
            } else {
                MyBatis.addSession(clientId, dbUser);

                sendRoomsArchives(dbUser);
                sendIdArchives(dbUser);

                return  Result.AUTHORIZE;
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.REJECT;
        }
    }
    private void sendRoomsArchives(User user){
        Thread publish = new Thread(() -> {
            //fetch groups and contacts then publish them to the topic
            List<ContactChat> chats = MyBatis.getUserContacts(user.getId());
            String payload = JsonParser.toJson(chats);

            String roomsTopic = "archivesrooms/" + clientId;
            ChatClient.connectAndPublish(payload, roomsTopic, true);
        });
        publish.start();
    }
    private void sendIdArchives(User dbUser){
        new Thread(()->{
            ContactChat user = MyBatis.getUserByID(dbUser.getId());
            String payload = JsonParser.toJson(user);

            String idTopic = "archivesmyid/" + clientId;
            ChatClient.connectAndPublish(payload, idTopic, true);
        }).start();
    }
}
