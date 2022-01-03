# JavaB_Library
Homework - library system

MSQL Workbench:

create table users (
user_id int not null auto_increment,
fullname varchar (55) not null,
email varchar (55) not null,
password varchar (55) not null,
primary key(user_id)
);
insert into users(fullname, email, password) 
value ('Kia Lapsina', 'kial@gmail.com', '1234567');

create table books(
book_id int not null auto_increment,
author varchar (55) not null,
title varchar (55) not null,
exemplars int,
primary key(book_id)
);

insert into books(author, title, exemplars) 
value ('C.S. Lewis', 'The Lion, the Witch, and the Wardrobe', '3');

create table library(
user_id int not null,
fullname varchar (55) not null,
book_id int not null,
author varchar (55) not null,
title varchar (55) not null,
exemplars int not null
);
