CREATE TABLE `room` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `type` int(11) NOT NULL COMMENT '房间类型 1群聊 2单聊',
  `hot_flag` int(11) DEFAULT '0' COMMENT '是否全员展示 0否 1是',
  `active_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '群最后消息的更新时间（热点群不需要写扩散，只更新这里）',
  `last_msg_id` bigint(20) DEFAULT NULL COMMENT '会话中的最后一条消息id',
  `ext_json` json DEFAULT NULL COMMENT '额外信息（根据不同类型房间有不同存储的东西）',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='房间表';


CREATE TABLE `message`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `contact_id` bigint(20) NOT NULL COMMENT '会话表id',
  `from_uid` bigint(20) NOT NULL COMMENT '消息发送者uid',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送的消息内容',
  `reply_msg_id` bigint(20) NULL DEFAULT NULL COMMENT '回复的消息内容(引用)',
  `status` int(11) NOT NULL COMMENT '消息状态 0正常 1删除',
  `gap_count` int(11) NULL DEFAULT NULL COMMENT '与回复的消息间隔多少条,保存前需要判断上面对方发了多少条，然后更新这个数据',
  `type` int(11) NULL DEFAULT 1 COMMENT '消息类型 1正常文本 2.撤回消息',
  `extra` json DEFAULT NULL COMMENT '扩展信息',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;


# 找出所有这个房间的信息，找到最新的信息，根据间隔补全上面的对应信息
CREATE TABLE `contact` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id1` bigint(20) NOT NULL COMMENT '用户1对应id',
  `user_id2` bigint(20) NOT NULL COMMENT '用户2对应id',
  `active_time` datetime(3) DEFAULT NULL COMMENT '会话内消息最后更新的时间(只有普通会话需要维护，全员会话不需要维护)',
  `last_msg_id` bigint(20) DEFAULT NULL COMMENT '会话最新消息id',
  'set_top'   tinyint(1)  COMMENT   '是否置顶会话',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  `is_delete`        tinyint(1) DEFAULT 0 COMMENT '是否删除（1为删除，0为没删）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话列表';

CREATE TABLE `contact_config` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `contact_id` bigint(20) NOT NULL COMMENT '房间id',
  `status` tinyint(1) NOT NULL COMMENT '状态（0为正常 1为删除）',
  'set_top'   tinyint(1)  COMMENT   '是否置顶会话',
  `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
  `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户对应会话配置表';
