drop table users cascade;
drop table "group" cascade;
drop table shoppinglist cascade;
drop table item cascade;
drop table itemcontainer cascade;
drop table "member" cascade;
drop table invite cascade;
drop table changeset cascade;

create table users(userID serial primary key not null,name varchar not null, email varchar not null, password varchar not null);

create table "group"(groupid serial primary key not null, name varchar not null);

create table shoppinglist (shoppinglistid serial primary key not null, groupid serial references "group"(groupid), name varchar not null);

create table item(itemid serial not null primary key, userid serial references users(userid), name varchar not null, defunit varchar not null, category varchar not null);

create table itemcontainer(itemcontainerid serial primary key not null, userid serial references users(userid), unit varchar not null, count integer not null, shoppinglistid serial references shoppinglist(shoppinglistid), name varchar not null, isTicked boolean not null);

create table member(groupid serial references "group"(groupid) not null, userID serial references users(userID) not null, primary key(groupid, userID));

Create table invite(inviteid serial primary key not null, receiverid serial references users(userid) not null, senderid serial references users(userid) not null, groupid serial references "group"(groupid) not null);

Create table changeset(Changeid int not null, groupid serial references "group"(groupid) not null, action varchar not null, data varchar not null, primary key(groupid, changeid));


