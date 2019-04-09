drop table "user" cascade;
drop table "group" cascade;
drop table shoppinglist cascade;
drop table item cascade;
drop table itemcontainer cascade;
drop table "member" cascade;


create table "user"(email varchar primary key not null, password varchar not null);

create table "group"(groupid serial primary key not null, name varchar not null);

create table shoppinglist (shoppinglistid serial primary key not null, groupid serial references "group"(groupid), name varchar not null);

create table item(itemid serial not null primary key, groupid serial references "group"(groupid), name varchar not null, defunit varchar not null);

create table itemcontainer(itemcontainerid serial primary key not null, itemid serial references item(itemid) not null, unit varchar not null, count integer not null, shoppinglistid serial references shoppinglist(shoppinglistid));

create table member(groupid serial references "group"(groupid) not null, email varchar references "user"(email) not null, primary key(groupid, email));



