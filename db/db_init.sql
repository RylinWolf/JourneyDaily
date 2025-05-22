use journeyDailyDev;

create database if not exists journeyDailyDev;

create table if not exists user(
    user_id bigint primary key ,
    user_name varchar(255) comment '用户名',
    email varchar(255) unique comment '邮箱地址',
    pwd_hash varchar(255) not null comment '密码哈希',
    tagline varchar(255) comment '个性签名',
    is_delete tinyint comment '逻辑删除'
);

create table if not exists journey(
    journey_id bigint primary key ,
    title varchar(255) not null comment '日记标题',
    content text not null comment '日记内容',
    summary text comment '日记总结',
    author_id bigint
);

create table if not exists category(
    category_id bigint,
    user_id bigint,
    title varchar(255) not null comment '分类标题',
    info text comment '分类简介',
    parent bigint comment '分类所属上级',
    primary key (category_id, user_id)
);