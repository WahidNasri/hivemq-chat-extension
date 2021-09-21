## This Extension defines the topic rules and the messages formats to use HiveMQ as a chat server.

## Features:
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
| :------------------ |:------------------| :---------------|
| messages/[room id]      | User that exists and member of the room | User that exists and member of the room |
| filemessages/[room id] | User that exists and member of the room | User that exists and member of the room |
| events/[room id]      | User that exists and member of the room      |   User that exists and member of the room. for chat marker, the message owner cannot publish |
| presence/[user id] | Check if the specified user id is a contact of the subscription requester      |    only the owner of the specified id |
| personalevents/[user id] | only the owner of the specified user id | 
| invitations/[username] | No one can subscribe to this topic | Logged in users, it is used to search and invite a user to a chat

# Use cases
## Invitations
### 1. Send invitation
To be able to send an invitation, the user shoudl send this payload to "invitations/{username to invite}":

### 2. Receiving an invitation
### 3. Error about the invitation
If there is any error, the user should receive on "personalevents/{userId}" this payload, the id is the same of the sent invitation:
```json
{
   "id":"e2ea206e-0d77-4629-8be0-fb85028669a1",
   "type":"EventInvitationResponseReject",
   "fromId":"supreme_",
   "sendTime":0,
   "text":"User not found",
   "invitationMessageType":"ERROR"
}
```

### 4. Info about the invitation
#### 4.1 Receiving room details 

## Messaging
### 1. Send text message
### 2. Send File message
### 3. Receiving message

## Room Events
### 1. Typing indicator
### 2. Chat Marker

## Archives


## Database schema
You can find a simple schema for the database [here](chat.sql)

## Client Example
Here is a simple [Flutter client](https://github.com/WahidNasri/flutter-mqtt-chat-client) .