package com.chat.authorizers.authentication.connectAuthorizers;

import com.db.MyBatis;
import com.db.User;
import com.chat.authorizers.BaseConnectAuthorizer;
import com.chat.authorizers.Result;

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

                return  Result.AUTHORIZE;
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.REJECT;
        }
    }
}
