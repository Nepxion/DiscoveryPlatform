CREATE TABLE IF NOT EXISTS `sys_admin`
(
    `id`                                        BIGINT UNSIGNED                     NOT NULL COMMENT '主键',
    `login_mode`                                INT UNSIGNED                        NOT NULL COMMENT '登录类型(1:database, 2:ldap)',
    `sys_role_id`                               BIGINT UNSIGNED                     NOT NULL COMMENT '角色id(sys_role表的主键)',
    `username`                                  VARCHAR(64)                         NOT NULL COMMENT '管理员的登陆用户名',
    `password`                                  VARCHAR(128)                        NOT NULL COMMENT '管理员的登陆密码',
    `name`                                      VARCHAR(64)                         NOT NULL COMMENT '管理员姓名',
    `phone_number`                              VARCHAR(32)                         NOT NULL COMMENT '管理员手机号码',
    `email`                                     VARCHAR(128)                        NOT NULL COMMENT '管理员邮箱',
    `description`                               VARCHAR(64)                         NOT NULL COMMENT '管理员备注信息',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_admin_username` (`username`),
    INDEX `idx_sys_admin_sys_role_id` (`sys_role_id`)
) COMMENT ='管理员信息';

CREATE TABLE IF NOT EXISTS `sys_menu`
(
    `id`                                        BIGINT UNSIGNED                     NOT NULL COMMENT '主键',
    `name`                                      VARCHAR(64)                         NOT NULL COMMENT '菜单名称',
    `url`                                       VARCHAR(128)                        NOT NULL COMMENT '菜单的链接跳转地址',
    `show_flag`                                 TINYINT(1) UNSIGNED                 NOT NULL COMMENT '菜单是否出现在菜单栏',
    `default_flag`                              TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否是默认菜单页(只允许有一个默认页，如果设置多个，以第一个为准)',
    `blank_flag`                                TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否新开窗口打开页面',
    `icon_class`                                VARCHAR(64)                         NOT NULL COMMENT '图标样式',
    `parent_id`                                 BIGINT(20) UNSIGNED                 NOT NULL COMMENT '父级id(即本表的主键id)',
    `order`                                     BIGINT(128) UNSIGNED                NOT NULL COMMENT '顺序号(值越小, 排名越靠前)',
    `description`                               VARCHAR(64)                         NOT NULL COMMENT '菜单描述信息',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_url` (`url`) USING BTREE
) COMMENT ='菜单配置';


CREATE TABLE IF NOT EXISTS `sys_permission`
(
    `id`                                        BIGINT UNSIGNED                     NOT NULL COMMENT '主键',
    `sys_role_id`                               BIGINT UNSIGNED                     NOT NULL COMMENT 'sys_role的主键id',
    `sys_menu_id`                               BIGINT UNSIGNED                     NOT NULL COMMENT 'sys_menu的主键id',
    `can_insert`                                TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否能新增(true:能, false:不能)',
    `can_delete`                                TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否能删除(true:能, false:不能)',
    `can_update`                                TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否能修改(true:能, false:不能)',
    `can_select`                                TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否能读取(true:能, false:不能)',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_id_menu_id` (`sys_role_id`, `sys_menu_id`)
) COMMENT ='用户权限信息';

CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`                                        BIGINT UNSIGNED                     NOT NULL COMMENT '主键',
    `name`                                      VARCHAR(64)                         NOT NULL COMMENT '角色名称',
    `super_admin`                               TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否是超级管理员(1:是, 0:否)',
    `description`                               VARCHAR(64)                         NOT NULL COMMENT '角色描述信息',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_name` (`name`)
) COMMENT ='角色信息';

CREATE TABLE IF NOT EXISTS `sys_dic`
(
    `id`                                        BIGINT(0) UNSIGNED                  NOT NULL COMMENT '主键',
    `name`                                      VARCHAR(64)                         NOT NULL COMMENT '属性名称',
    `value`                                     VARCHAR(128)                        NOT NULL COMMENT '属性值',
    `description`                               VARCHAR(64)                         NOT NULL COMMENT '描述信息',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_sys_dic_name`(`name`) USING BTREE,
    INDEX `idx_sys_admin_sys_role_id`(`value`) USING BTREE
) COMMENT ='字典信息';

CREATE TABLE IF NOT EXISTS `t_route_gateway`  (
    `id`                                        BIGINT(0) UNSIGNED                  NOT NULL COMMENT '主键',
    `route_id`                                  VARCHAR(64)                         NOT NULL COMMENT '路由id',
    `portal_name`                               VARCHAR(128)                        NOT NULL COMMENT '网关名称',
    `portal_type`                               INT(0) UNSIGNED                     NOT NULL COMMENT '入口类型(1: 网关)',
    `uri`                                       VARCHAR(256)                        NOT NULL COMMENT '转发目标url',
    `predicates`                                VARCHAR(2048)                       NOT NULL COMMENT '断言器字符串',
    `user_predicates`                           VARCHAR(2048)                       NOT NULL COMMENT '自定义断言器字符串',
    `filters`                                   VARCHAR(2048)                       NOT NULL COMMENT '过滤器字符串',
    `user_filters`                              VARCHAR(2048)                       NOT NULL COMMENT '自定义过滤器字符串',
    `metadata`                                  VARCHAR(2048)                       NOT NULL COMMENT '路由元数据',
    `order`                                     INT(0) UNSIGNED                     NOT NULL COMMENT '路由执行顺序',
    `service_name`                              VARCHAR(64)                         NOT NULL COMMENT '所属的服务名称(即:服务的spring.application.name)',
    `description`                               VARCHAR(64)                         NOT NULL COMMENT '描述信息',
    `create_times_in_day`                       INT(0) UNSIGNED                     NOT NULL COMMENT '一天内创建路由的次数',
    `operation`                                 INT(0) UNSIGNED                     NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`                               TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否启用(0:禁用, 1:启用)',
    `publish_flag`                              TINYINT(1) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`                               TINYINT(1) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_route_id`(`route_id`) USING BTREE
) COMMENT = 'Gateway网关路由信息';

CREATE TABLE IF NOT EXISTS `t_route_zuul`  (
    `id`                                        BIGINT(0) UNSIGNED                  NOT NULL COMMENT '主键',
    `route_id`                                  VARCHAR(64)                         NOT NULL COMMENT '路由id',
    `portal_name`                               VARCHAR(128)                        NOT NULL COMMENT '网关名称',
    `portal_type`                               INT(0) UNSIGNED                     NOT NULL COMMENT '入口类型(1: 网关)',
    `service_id`                                VARCHAR(128)                        NOT NULL COMMENT '服务id',
    `path`                                      VARCHAR(128)                        NOT NULL COMMENT '转发目标路径',
    `url`                                       VARCHAR(128)                        NOT NULL COMMENT '转发目标uri',
    `strip_prefix`                              TINYINT(1) UNSIGNED                 NOT NULL COMMENT '代理前缀默认会从请求路径中移除，通过该设置关闭移除功能',
    `retryable`                                 TINYINT(1) UNSIGNED                 NULL COMMENT '路由是否进行重试',
    `sensitive_headers`                         VARCHAR(256)                        NOT NULL COMMENT '过滤客户端附带的headers, 用逗号分隔',
    `custom_sensitive_headers`                  TINYINT(1) UNSIGNED                 NOT NULL COMMENT '',
    `description`                               VARCHAR(64)                         NOT NULL COMMENT '描述信息',
    `create_times_in_day`                       INT(0) UNSIGNED                     NOT NULL COMMENT '一天内创建路由的次数',
    `operation`                                 TINYINT(1) UNSIGNED                 NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`                               TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否启用',
    `publish_flag`                              TINYINT(1) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`                               TINYINT(1) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_route_id`(`route_id`) USING BTREE
) COMMENT = 'Zuul网关路由信息';

CREATE TABLE IF NOT EXISTS `t_blacklist`  (
    `id`                                        BIGINT(0) UNSIGNED                  NOT NULL COMMENT '主键',
    `portal_name`                               VARCHAR(128)                        NOT NULL COMMENT '网关/服务/组名称',
    `portal_type`                               INT(0) UNSIGNED                     NOT NULL COMMENT '入口类型(1: 网关, 2:服务, 3:组)',
    `service_name`                              VARCHAR(64)                         NOT NULL COMMENT '服务名称',
    `service_blacklist_type`                    INT(0) UNSIGNED                     NOT NULL COMMENT '黑名单类型(1:UUID, 2:ADDRESS)',
    `service_blacklist`                         VARCHAR(128)                        NOT NULL COMMENT '黑名单内容',
    `description`                               VARCHAR(128)                        NOT NULL COMMENT '服务无损屏蔽的描述信息',
    `operation`                                 TINYINT(1) UNSIGNED                 NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`                               TINYINT(1) UNSIGNED                 NOT NULL COMMENT '是否启用',
    `publish_flag`                              TINYINT(1) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`                               TINYINT(1) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_gateway_name`(`portal_name`) USING BTREE,
    INDEX `idx_service_name`(`service_name`) USING BTREE
) COMMENT = '实例摘除信息';

CREATE TABLE IF NOT EXISTS `t_strategy`  (
    `id`                                        BIGINT(0) UNSIGNED                  NOT NULL COMMENT '主键',
    `portal_name`                               VARCHAR(128)                        NOT NULL COMMENT '网关/服务/组名称',
    `portal_type`                               INT(0) UNSIGNED                     NOT NULL COMMENT '入口类型(1: 网关, 2:服务, 3:组)',
    `strategy_type`                             INT(0) UNSIGNED                     NOT NULL COMMENT '策略类型(1: 版本, 2: 区域)',
    `basic_blue_green_strategy_route_id`        VARCHAR(128)                        NOT NULL COMMENT '用于蓝绿兜底的链路编排标识',
    `blue_green_strategy`                       TEXT                                NOT NULL COMMENT '蓝绿策略信息',
    `basic_gray_strategy`                       TEXT                                NOT NULL COMMENT '灰度兜底信息',
    `gray_strategy`                             TEXT                                NOT NULL COMMENT '灰度策略信息',
    `header`                                    TEXT                                NOT NULL COMMENT 'header请求头编排',
    `description`                               VARCHAR(128)                        NOT NULL COMMENT '描述信息',
    `operation`                                 TINYINT(0) UNSIGNED                 NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`                               TINYINT(0) UNSIGNED                 NOT NULL COMMENT '是否启用',
    `publish_flag`                              TINYINT(0) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`                               TINYINT(0) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_gateway_name`(`portal_name`) USING BTREE
) COMMENT = '蓝绿或灰度信息';

CREATE TABLE IF NOT EXISTS `t_gray`  (
    `id`                                        BIGINT(0) UNSIGNED                  NOT NULL COMMENT '主键',
    `portal_name`                               VARCHAR(128)                        NOT NULL COMMENT '网关/服务/组名称',
    `portal_type`                               INT(0) UNSIGNED                     NOT NULL COMMENT '入口类型(1: 网关, 2:服务, 3:组)',
    `basic_strategy`                            TEXT                                NOT NULL COMMENT '灰度兜底信息',
    `gray_strategy`                             TEXT                                NOT NULL COMMENT '灰度策略信息',
    `route_service`                             TEXT                                NOT NULL COMMENT '灰度发布路由服务编排',
    `header`                                    TEXT                                NOT NULL COMMENT '灰度发布header请求头编排',
    `description`                               VARCHAR(128)                        NOT NULL COMMENT '描述信息',
    `operation`                                 TINYINT(0) UNSIGNED                 NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`                               TINYINT(0) UNSIGNED                 NOT NULL COMMENT '是否启用',
    `publish_flag`                              TINYINT(0) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`                               TINYINT(0) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_gateway_name`(`portal_name`) USING BTREE
) COMMENT = '灰度发布信息';

CREATE TABLE `t_route_arrange`  (
    `id`                                        BIGINT(0) UNSIGNED                  NOT NULL COMMENT '主键',
    `route_id`                                  VARCHAR(128)                        NOT NULL COMMENT '服务链路标识',
    `index`                                     BIGINT(0) UNSIGNED                  NOT NULL COMMENT '链路标识的下标索引',
    `strategy_type`                             INT(0) UNSIGNED                     NOT NULL COMMENT '策略类型(1: 版本, 2:区域)',
    `service_arrange`                           TEXT                                NOT NULL COMMENT '服务编排',
    `description`                               VARCHAR(128)                        NOT NULL COMMENT '链路编排的描述信息',
    `operation`                                 TINYINT(0) UNSIGNED                 NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`                               TINYINT(0) UNSIGNED                 NOT NULL COMMENT '是否启用',
    `publish_flag`                              TINYINT(0) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`                               TINYINT(0) UNSIGNED                 NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3)                         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_route_id`(`route_id`) USING BTREE
) COMMENT = '服务编排信息';

CREATE TABLE `t_route_strategy`  (
    `id`                                        BIGINT(0)                           UNSIGNED NOT NULL COMMENT '主键',
    `portal_name`                               VARCHAR(128)                        NOT NULL COMMENT '网关/服务/组名称',
    `portal_type`                               INT(0) UNSIGNED                     NOT NULL COMMENT '入口类型(1: 网关, 2:服务, 3:组)',
    `route_id`                                  VARCHAR(128)                        NOT NULL COMMENT '服务链路标识',
    `create_time`                               DATETIME(3) NOT                     NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                               DATETIME(3) NOT                     NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_portal_name_type`(`portal_name`, `portal_type`) USING BTREE,
    UNIQUE INDEX `idx_unique`(`route_id`, `portal_name`, `portal_type`) USING BTREE
) COMMENT = '路由与蓝绿(或灰度)的映射关系';

INSERT IGNORE INTO `sys_admin`(`id`, `login_mode`, `sys_role_id`, `username`, `password`, `name`, `phone_number`, `email`, `description`) VALUES (1, 1, 1, 'admin', 'ebc255e6a0c6711a4366bc99ebafb54f', '超级管理员', '18000000000', 'administrator@nepxion.com', '超级管理员');

INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `description`) VALUES (1, '超级管理员', 1, '超级管理员, 拥有最高权限');
INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `description`) VALUES (2, '研发人员', 0, '研发人员');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (10, '主页', 'http://www.nepxion.com', b'1', b'1', b'0', 'layui-icon-home', 0, 1, '主页');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (20, '服务发布', '', b'1', b'0', b'0', 'layui-icon-release', 0, 2, '服务发布');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (2001, '链路编排', '/route-arrange/list', b'1', b'0', b'0', '', 20, 1, '链路编排');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (2002, '蓝绿灰度', '/strategy/list', b'1', b'0', b'0', '', 20, 2, '蓝绿灰度');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (2003, '灰度发布-待删除', '/gray/list', b'1', b'0', b'0', '', 20, 3, '灰度发布-待删除');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (2004, '流量侦测', '/inspector/list', b'1', b'0', b'0', '', 20, 4, '流量侦测');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (30, '实例管理', '', b'1', b'0', b'0', 'layui-icon-template-1', 0, 3, '实例管理');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (3001, '实例信息', '/instance/list', b'1', b'0', b'0', '', 30, 1, '实例信息');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (3002, '实例摘除', '/blacklist/list', b'1', b'0', b'0', '', 30, 2, '实例摘除');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (40, '路由配置', '', b'1', b'0', b'0', 'layui-icon-website', 0, 4, '路由配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (4001, 'Gateway网关路由', '/route-gateway/list', b'1', b'0', b'0', '', 40, 1, 'Spring Cloud Gateway路由配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (4002, 'Zuul网关路由', '/route-zuul/list', b'1', b'0', b'0', '', 40, 2, 'Zuul路由配置');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (50, '安全设置', '', b'1', b'0', b'0', 'layui-icon-vercode', 0, 5, '安全设置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (5001, '应用列表', '/app/list', b'1', b'0', b'0', '', 50, 1, '应用列表');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (5002, '接口列表', '/api/list', b'1', b'0', b'0', '', 50, 2, '接口列表');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (5003, '权限列表', '/auth/list', b'1', b'0', b'0', '', 50, 3, '权限列表');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (5004, '白名单列表', '/ignore-url/list', b'1', b'0', b'0', '', 50, 4, '白名单列表');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (60, '基础应用', '', b'1', b'0', b'0', 'layui-icon-app', 0, 6, '基础应用');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6001, 'Nacos', 'http://127.0.0.1:8848/nacos', b'1', b'0', b'1', '', 60, 1, 'Nacos注册中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6002, 'Apollo', 'http://106.54.227.205/', b'1', b'0', b'1', '', 60, 2, 'Apollo配置中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6003, 'Sentinel', 'http://127.0.0.1:8075', b'1', b'0', b'1', '', 60, 3, 'Sentinel限流中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6004, 'SkyWalking', 'http://127.0.0.1:8080', b'1', b'0', b'1', '', 60, 4, 'SkyWalking监控中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6005, 'Spring Boot Admin', 'http://127.0.0.1:6002', b'1', b'0', b'1', '', 60, 5, 'Spring Boot Admin监控中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6006, 'Grafana', 'http://127.0.0.1:3000', b'1', b'0', b'1', '', 60, 6, 'Grafana监控中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6007, 'Kibana', 'http://127.0.0.1:5601 ', b'1', b'0', b'1', '', 60, 7, 'Kibana日志中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6008, 'Swagger', 'http://127.0.0.1:6001/swagger-ui.html', b'1', b'0', b'1', '', 60, 8, 'Swagger文档中心');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (70, '系统设置', '', b'1', b'0', b'0', 'layui-icon-set', 0, 7, '系统设置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (7001, '页面设置', '/menu/list', b'1', b'0', b'0', '', 70, 1, '页面设置');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (80, '授权配置', '', b'1', b'0', b'0', 'layui-icon-password', 0, 8, '授权配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (8001, '管理员配置', '/admin/list', b'1', b'0', b'0', '', 80, 1, '管理员配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (8002, '角色配置', '/role/list', b'1', b'0', b'0', '', 80, 2, '角色配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (8003, '权限配置', '/permission/list', b'1', b'0', b'0', '', 80, 3, '权限配置');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (90, '低代码平台', '', b'1', b'0', b'0', 'layui-icon-template', 0, 9, '低代码平台');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (9001, '脚手架创建', '/skeleton/add', b'1', b'0', b'0', '', 90, 1, '脚手架创建');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (100, '操作日志', '', b'1', b'0', b'0', 'layui-icon-form', 0, 10, '操作日志');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (10001, '服务发布日志', '/release-log/list', b'1', b'0', b'0', '', 100, 1, '服务发布日志');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (10002, '实例管理日志', '/instance-log/list', b'1', b'0', b'0', '', 100, 2, '实例管理日志');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (10003, '路由配置日志', '/route-log/list', b'1', b'0', b'0', '', 100, 3, '路由配置日志');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (100000, '指南手册', 'http://nepxion.com/discovery-platform', b'1', b'0', b'1', 'layui-icon-read', 0, 11, '指南手册');

INSERT IGNORE INTO `sys_dic`(`id`, `name`, `value`, `description`) VALUES (1, 'super_admin', '', ' 超级管理员的登陆账号, 多个用逗号分隔');