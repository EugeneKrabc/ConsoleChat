DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users(
    id serial primary key,
    username varchar,
    password varchar
);

drop table if exists messages;
create table if not exists messages(
                id serial primary key,
                senderId bigint references users(id),
                text varchar,
                chatroomId bigint references chatroom(id),
                sendingTime timestamp);

drop table if exists chatroom;
create table if not exists chatroom(
    id serial primary key ,
    chatRoomName varchar
);
