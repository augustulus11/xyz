create type post_type as enum('blog', 'blerb');

create table blog (
       url_name varchar(255) primary key not null,
       content text not null,
       post_type post_type not null,
       created timestamp default now(),
       last_updated timestamp default now(),
       image_path varchar(255) null,
       author varchar(50) null,
       previous_blog varchar(255) null,
       next_blog varchar(255) null
);
