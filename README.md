##This Extension defines the topic rules and the messages formats to use HiveMQ as a chat server.

##Features:
- Login
- Get Contacts
- Join a conversation room
- Messaging
- Chat Markers
- Presence

## Add your implementation
To define Database information, please edit the file [mybatis-config.xml](src/main/resources/mybatis-config.xml).

To change the way the users log in, implement your logic inside [MyAuthenticationProvider.java](src/main/java/com/chat/authorizers/authentication/MyAuthenticatorProvider.java).

## The Supreme User
After a successful login, the user will need some information from the server (Like list of contacts and groups, his profile, messages archives if implemented). 
Those information are published by a special user (called supreme user), he is responsible to send retained messages to specific topics.

## Topics
| topic         | Who can subscribe           | Who can publish  |
| :------------- |:-------------:| :-----:|
| messages/[room id]      | User that exists and member of the room | User that exists and member of the room |
| events/[room id]      | User that exists and member of the room      |   User that exists and member of the room. for chat marker, the message owner cannot publish |
| presence/[user id] | Check if the specified user id is a contact of the subscription requester      |    only the owner of the specified id |
|personalevents/[user id] | only the owner of the specified user id | For invitation, only authorize if there is no ongoing invitation (in progress)