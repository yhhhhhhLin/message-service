create database message;

CREATE TABLE `msg_info`
(
    `id`              bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`         bigint(20) DEFAULT NULL COMMENT '用户id',
    'send_user_id'    bigint(20) DEFAULT NULL COMMENT '发送人id(发送人id如果是0，那么就是系统消息)',
    'msg_content'         varchar(255) COMMENT '发送的消息内容',
    'is_read'         tinyint(1)  COMMENT   '是否已读',
    `create_time`     datetime                               DEFAULT NULL COMMENT '创建时间',
    `update_time`     datetime                               DEFAULT NULL COMMENT '修改时间',
    `is_delete`        tinyint(1) DEFAULT 0 COMMENT '是否删除（1为删除，0为没删）',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='信息表';