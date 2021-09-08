package com.chat.authorizers.authentication;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.annotations.Nullable;
import com.hivemq.extension.sdk.api.auth.Authenticator;
import com.hivemq.extension.sdk.api.auth.SimpleAuthenticator;
import com.hivemq.extension.sdk.api.auth.parameter.AuthenticatorProviderInput;
import com.hivemq.extension.sdk.api.packets.connect.ConnectPacket;
import com.hivemq.extension.sdk.api.services.auth.provider.AuthenticatorProvider;
import com.chat.authorizers.BaseConnectAuthorizer;
import com.chat.authorizers.Result;
import com.chat.authorizers.authentication.connectAuthorizers.AnonymousAuthorizer;
import com.chat.authorizers.authentication.connectAuthorizers.LoginAuthorizer;
import com.chat.authorizers.authentication.connectAuthorizers.SupremeAuthorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class MyAuthenticatorProvider implements AuthenticatorProvider {
    private static final Logger log = LoggerFactory.getLogger(MyAuthenticatorProvider.class);

    @Override
    public @Nullable Authenticator getAuthenticator(@NotNull AuthenticatorProviderInput authenticatorProviderInput) {

        return (SimpleAuthenticator) (simpleAuthInput, simpleAuthOutput) -> {
            try {
                //get the contents of the MQTT connect packet from the input object
                ConnectPacket connect = simpleAuthInput.getConnectPacket();
                String clientId = connect.getClientId();

                BaseConnectAuthorizer authorizer = null;

                //authorize the supreme user
                if (clientId.startsWith("supreme_")) {
                    authorizer = new SupremeAuthorizer(null, null, clientId);
                }
                //check if the client set username and password
                else if (!connect.getUserName().isPresent() || !connect.getPassword().isPresent()) {
                    authorizer = new AnonymousAuthorizer(null, null, clientId);
                } else {
                    //get username and password from the connect packet
                    String username = connect.getUserName().get();
                    String password = StandardCharsets.UTF_8.decode(connect.getPassword().get()).toString();

                    authorizer = new LoginAuthorizer(username, password, clientId);
                }

                Result result = authorizer.authorize();
                if(result == Result.AUTHORIZE){
                    //when authorized, send retained messages to archives topics (rooms, user id..)

                }
                switch (result) {
                    case AUTHORIZE:
                        simpleAuthOutput.authenticateSuccessfully();
                        break;
                    case REJECT:
                        simpleAuthOutput.failAuthentication();
                    case NEXT:
                        simpleAuthOutput.nextExtensionOrDefault();
                        break;
                }
            }catch (Exception e){
                log.info(e.getMessage());
            }
        };
    }
}
