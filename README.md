## This Extension defines the topic rules and the messages formats to use HiveMQ as a chat server.

# How to use
## 1. Install HiveMQ on your machine or Docker.
Please refer to their [documentation](https://github.com/hivemq/hivemq-community-edition).

## 2. Install Chat Extension
Extract the [Chat Extension Build](ChatExtension-1.0-SNAPSHOT-distribution.zip).

Move the Folder ChatExtension folder under the extensions folder of your HiveMQ instance.

## 3. Import the database schema
On your database server, import the schema from [Chat.sql](chat.sql).
## 4. Specify your database url and credentials on the Environment variables
To be able to run the extension, we have to define four environment variables:
1. chat_db_driver: the database driver (example: com.mysql.jdbc.Driver)
2. chat_db_url: The url of your database (example: jdbc:mysql://localhost:3306/chat)
3. chat_db_username: username of the database
4. chat_db_password: password for the database

## 5. Create test users on your database
To be able to test the application, please insert some users on the table user.


## Features:
- Login
- Get Archives on login (list of rooms [groups and contacts] and user information)
- Send invitation to a user
- Create group chat with members.
- Messaging (Text, and file messages)
- Chat Markers
- Presence

## Add your implementation
To define Database information, please edit the file [mybatis-config.xml](src/main/resources/mybatis-config.xml).

To change the way the users log in, implement your logic inside [MyAuthenticationProvider.java](src/main/java/com/chat/authorizers/authentication/MyAuthenticatorProvider.java).

## The Supreme User
After a successful login, the user will need some information from the server (Like list of contacts and groups, his profile, messages archives if implemented).
Those information are published by a special user (called supreme user), he is responsible to send retained messages to specific topics.

# Use cases
## Invitations
### 1. Send invitation
To be able to send an invitation, the user shoudl send this payload to "invitations/{username to invite}":

### 2. Receiving an invitation
### 3. Error about the invitation
If there is any error, the user should receive on "personalevents/{userId}" this payload, the id is the same of the sent invitation:

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