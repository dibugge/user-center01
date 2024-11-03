-- auto-generated definition
create table user
(
    id           bigint auto_increment comment 'id
'
        primary key,
    username     varchar(256)                             null comment '用户昵称',
    userAccount  varchar(255)                             null comment '账号',
    avatarUrl    varchar(1024)                            null comment '用户头像',
    gender       int                                      null comment '性别',
    profile      varchar(512)                             null comment '个人简介',
    userPassword varchar(255)                             not null comment '密码',
    email        varchar(255)                             null comment '邮箱',
    userStatus   int          default 0                   null comment '状态',
    phone        varchar(255) default ''                  null comment '电话',
    createTime   datetime     default current_timestamp() null comment '创建时间',
    updateTime   datetime     default current_timestamp() null on update current_timestamp() comment '更新时间',
    isDelete     tinyint      default 0                   not null comment '是否删除',
    userRole     int          default 0                   null comment '用户角色0-普通用户1管理员',
    planetCode   varchar(255)                             null comment '星球编号',
    tags         varchar(1024)                            null comment '标签列表'
);

