CREATE TABLE IF NOT EXISTS `sys_admin`
(
    `id`                        BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `login_mode`                INT UNSIGNED                    NOT NULL COMMENT '登录类型(1:database, 2:ldap)',
    `sys_role_id`               BIGINT UNSIGNED                 NOT NULL COMMENT '角色id(sys_role表的主键)',
    `username`                  VARCHAR(64)                     NOT NULL COMMENT '管理员的登陆用户名',
    `password`                  VARCHAR(128)                    NOT NULL COMMENT '管理员的登陆密码',
    `name`                      VARCHAR(64)                     NOT NULL COMMENT '管理员姓名',
    `phone_number`              VARCHAR(32)                     NOT NULL COMMENT '管理员手机号码',
    `email`                     VARCHAR(128)                    NOT NULL COMMENT '管理员邮箱',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '管理员备注信息',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_admin_username` (`username`),
    INDEX `idx_sys_admin_sys_role_id` (`sys_role_id`)
) COMMENT ='管理员信息';

CREATE TABLE IF NOT EXISTS `sys_menu`
(
    `id`                        BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      VARCHAR(64)                     NOT NULL COMMENT '菜单名称',
    `url`                       VARCHAR(128)                    NOT NULL COMMENT '菜单的链接跳转地址',
    `show_flag`                 TINYINT(1)                      NOT NULL COMMENT '菜单是否出现在菜单栏',
    `default_flag`              TINYINT(1)                      NOT NULL COMMENT '是否是默认菜单页(只允许有一个默认页，如果设置多个，以第一个为准)',
    `blank_flag`                TINYINT(1)                      NOT NULL COMMENT '是否新开窗口打开页面',
    `icon_class`                VARCHAR(64)                     NOT NULL COMMENT '图标样式',
    `parent_id`                 BIGINT(20) UNSIGNED             NOT NULL COMMENT '父级id(即本表的主键id)',
    `order`                     BIGINT(128) UNSIGNED            NOT NULL COMMENT '顺序号(值越小, 排名越靠前)',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '菜单描述信息',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_url` (`url`) USING BTREE
) COMMENT ='菜单配置';


CREATE TABLE IF NOT EXISTS `sys_permission`
(
    `id`                         BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `sys_role_id`                BIGINT UNSIGNED                 NOT NULL COMMENT 'sys_role的主键id',
    `sys_menu_id`                BIGINT UNSIGNED                 NOT NULL COMMENT 'sys_menu的主键id',
    `can_insert`                 TINYINT(1)                      NOT NULL COMMENT '是否能新增(true:能, false:不能)',
    `can_delete`                 TINYINT(1)                      NOT NULL COMMENT '是否能删除(true:能, false:不能)',
    `can_update`                 TINYINT(1)                      NOT NULL COMMENT '是否能修改(true:能, false:不能)',
    `can_select`                 TINYINT(1)                      NOT NULL COMMENT '是否能读取(true:能, false:不能)',
    `create_time`                DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`                DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_id_menu_id` (`sys_role_id`, `sys_menu_id`)
) COMMENT ='用户权限信息';

CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`                        BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      VARCHAR(64)                     NOT NULL COMMENT '角色名称',
    `super_admin`               TINYINT(1)                      NOT NULL COMMENT '是否是超级管理员(1:是, 0:否)',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '角色描述信息',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_name` (`name`)
) COMMENT ='角色信息';

CREATE TABLE IF NOT EXISTS `sys_dic`
(
    `id`                        BIGINT(0) UNSIGNED              NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      VARCHAR(64)                     NOT NULL COMMENT '属性名称',
    `value`                     VARCHAR(128)                    NOT NULL COMMENT '属性值',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '描述信息',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_sys_dic_name`(`name`) USING BTREE,
    INDEX `idx_sys_admin_sys_role_id`(`value`) USING BTREE
) COMMENT ='字典信息';

CREATE TABLE IF NOT EXISTS `t_route_gateway`  (
    `id`                        BIGINT(0) UNSIGNED              NOT NULL AUTO_INCREMENT COMMENT '主键',
    `route_id`                  VARCHAR(64)                     NOT NULL COMMENT '路由id',
    `gateway_name`              VARCHAR(128)                    NOT NULL COMMENT '网关名称',
    `uri`                       VARCHAR(256)                    NOT NULL COMMENT '转发目标url',
    `predicates`                VARCHAR(2048)                   NOT NULL COMMENT '断言器字符串',
    `user_predicates`           VARCHAR(2048)                   NOT NULL COMMENT '自定义断言器字符串',
    `filters`                   VARCHAR(2048)                   NOT NULL COMMENT '过滤器字符串',
    `user_filters`              VARCHAR(2048)                   NOT NULL COMMENT '自定义过滤器字符串',
    `metadata`                  VARCHAR(2048)                   NOT NULL COMMENT '路由元数据',
    `order`                     INT(0) UNSIGNED                 NOT NULL COMMENT '路由执行顺序',
    `service_name`              VARCHAR(64)                     NOT NULL COMMENT '所属的服务名称(即:服务的spring.application.name)',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '描述信息',
    `create_times_in_day`       INT(0) UNSIGNED                 NOT NULL COMMENT '一天内创建路由的次数',
    `operation`                 INT(0) UNSIGNED                 NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`               TINYINT(1)                      NOT NULL COMMENT '是否启用(0:禁用, 1:启用)',
    `publish_flag`              TINYINT(1)                      NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`               TINYINT(1)                      NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_route_id`(`route_id`) USING BTREE
) COMMENT = 'Gateway网关的路由信息';

CREATE TABLE IF NOT EXISTS `t_route_zuul`  (
    `id`                        BIGINT(0) UNSIGNED              NOT NULL AUTO_INCREMENT COMMENT '主键',
    `route_id`                  VARCHAR(64)                     NOT NULL COMMENT '路由id',
    `gateway_name`              VARCHAR(128)                    NOT NULL COMMENT '网关名称',
    `service_id`                VARCHAR(128)                    NOT NULL COMMENT '服务id',
    `path`                      VARCHAR(128)                    NOT NULL COMMENT '转发目标路径',
    `url`                       VARCHAR(128)                    NOT NULL COMMENT '转发目标uri',
    `strip_prefix`              TINYINT(1)                      NOT NULL COMMENT '代理前缀默认会从请求路径中移除，通过该设置关闭移除功能',
    `retryable`                 TINYINT(1)                      NULL COMMENT '路由是否进行重试',
    `sensitive_headers`         VARCHAR(256)                    NOT NULL COMMENT '过滤客户端附带的headers, 用逗号分隔',
    `custom_sensitive_headers`  TINYINT(1)                      NOT NULL COMMENT '',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '描述信息',
    `create_times_in_day`       INT(0) UNSIGNED                 NOT NULL COMMENT '一天内创建路由的次数',
    `operation`                 TINYINT(1) UNSIGNED             NOT NULL COMMENT '最后一次执行的操作类型(1:INSERT, 2:UPDATE, 3:DELETE)',
    `enable_flag`               TINYINT(1)                      NOT NULL COMMENT '是否启用',
    `publish_flag`              TINYINT(1)                      NOT NULL DEFAULT 0 COMMENT '是否发布(0:未发布, 1:已发布)',
    `delete_flag`               TINYINT(1)                      NOT NULL DEFAULT 0 COMMENT '是否删除(0:未删除, 1:已删除)',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_route_id`(`route_id`) USING BTREE
) COMMENT = 'Zuul网关的路由信息';


INSERT IGNORE INTO `sys_admin`(`id`, `login_mode`, `sys_role_id`, `username`, `password`, `name`, `phone_number`, `email`, `description`)VALUES (1, 1, 1, 'admin', 'ebc255e6a0c6711a4366bc99ebafb54f', '超级管理员', '18000000000', 'administrator@nepxion.com', '超级管理员');

INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `description`) VALUES (1, '超级管理员', 1, '超级管理员, 拥有最高权限');
INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `description`) VALUES (2, '研发人员', 0, '研发人员');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (10000, 'DashBoard', 'http://www.nepxion.com', b'1', b'1', b'0', 'layui-icon-chart-screen', 0, 1, 'DashBoard');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1, '服务发布', '', b'1', b'0', b'0', 'layui-icon-release', 0, 2, 'Spring Cloud服务发布');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (101, '蓝绿发布', '/blue-green/list', b'1', b'0', b'0', '', 1, 1, '蓝绿发布');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (102, '灰度发布', '/gray/list', b'1', b'0', b'0', '', 1, 2, '灰度发布');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (103, '流量侦测', '/inspector/list', b'1', b'0', b'0', '', 1, 3, '流量侦测');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (2, '路由配置', '', b'1', b'0', b'0', 'layui-icon-website', 0, 3, '路由配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (201, 'Gateway网关路由', '/route-gateway/list', b'1', b'0', b'0', '', 2, 1, 'Spring Cloud Gateway路由配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (202, 'Zuul网关路由', '/route-zuul/list', b'1', b'0', b'0', '', 2, 2, 'Zuul路由配置');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (3, '服务管理', '', b'1', b'0', b'0', 'layui-icon-template-1', 0, 4, '服务管理');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (301, '服务染色启动', '/service/list1', b'1', b'0', b'0', '', 3, 1, '服务通过元数据染色方式启动');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (302, '服务拉入拉出', '/service/list2', b'1', b'0', b'0', '', 3, 2, '服务从注册中心注册和注销');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (303, '服务无损屏蔽', '/service/list3', b'1', b'0', b'0', '', 3, 3, '服务无损屏蔽');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (304, '服务批量下线', '/service/list4', b'1', b'0', b'0', '', 3, 4, '服务批量下线');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (4, '安全管理', '', b'1', b'0', b'0', 'layui-icon-app', 0, 5, '安全管理');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (401, '应用列表', '/app/list', b'1', b'0', b'0', '', 4, 1, '应用列表');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (402, '接口列表', '/api/list', b'1', b'0', b'0', '', 4, 2, '接口列表');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (403, '权限列表', '/auth/list', b'1', b'0', b'0', '', 4, 3, '权限列表');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (404, '白名单列表', '/ignore-url/list', b'1', b'0', b'0', '', 4, 4, '白名单列表');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (5, '注册中心', '', b'1', b'0', b'0', 'layui-icon-flag', 0, 6, '注册中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (501, 'Nacos', 'http://127.0.0.1:8848/nacos', b'1', b'0', b'1', '', 5, 1, 'Nacos注册中心');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (6, '配置中心', '', b'1', b'0', b'0', 'layui-icon-survey', 0, 7, '配置中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (601, 'Apollo', 'http://106.54.227.205/', b'1', b'0', b'1', '', 6, 1, 'Apollo配置中心');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (7, '监控中心', '', b'1', b'0', b'0', 'layui-icon-app', 0, 8, '监控中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (701, 'Spring Boot Admin', 'http://127.0.0.1:6002', b'1', b'0', b'1', '', 7, 1, 'Spring Boot Admin监控中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (702, 'Grafana', 'http://127.0.0.1:3000', b'1', b'0', b'1', '', 7, 2, 'Grafana监控中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (703, 'SkyWalking', 'http://127.0.0.1:8080', b'1', b'0', b'1', '', 7, 3, 'SkyWalking监控中心');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (8, '限流中心', '', b'1', b'0', b'0', 'layui-icon-transfer', 0, 9, '限流中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (801, 'Sentinel', 'http://127.0.0.1:8075', b'1', b'0', b'1', '', 8, 1, 'Sentinel限流中心');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (9, '日志中心', '', b'1', b'0', b'0', 'layui-icon-list', 0, 10, '日志中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (901, 'Kibana', 'http://127.0.0.1:5601 ', b'1', b'0', b'1', '', 9, 1, 'Kibana日志中心');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (10, '告警中心', '', b'1', b'0', b'0', 'layui-icon-about', 0, 11, '告警中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1001, '灰度蓝绿变更告警', '/blue-green-change-warning/list', b'1', b'0', b'0', '', 10, 1, '灰度蓝绿变更告警');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1002, '灰度蓝绿不一致告警', '/blue-green-warning/list', b'1', b'0', b'0', '', 10, 2, '灰度蓝绿不一致告警');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1003, '网关路由不一致告警', '/route-warning/list', b'1', b'0', b'0', '', 10, 3, '网关路由不一致告警');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (11, '文档中心', '', b'1', b'0', b'0', 'layui-icon-read', 0, 12, '文档中心');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1101, 'Swagger', 'http://127.0.0.1:6001/swagger-ui.html', b'1', b'0', b'1', '', 11, 1, 'Swagger文档中心');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (12, '系统配置', '', b'1', b'0', b'0', 'layui-icon-set', 0, 13, '系统设置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1201, '页面配置', '/menu/list', b'1', b'0', b'0', '', 12, 1, '页面配置');

INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (13, '授权配置', '', b'1', b'0', b'0', 'layui-icon-password', 0, 14, '权限设置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1301, '管理员配置', '/admin/list', b'1', b'0', b'0', '', 13, 1, '管理员配置');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1302, '角色配置', '/role/list', b'1', b'0', b'0', '', 13, 2, '角色管理');
INSERT IGNORE INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) VALUES (1303, '权限配置', '/permission/list', b'1', b'0', b'0', '', 13, 3, '权限管理');

INSERT IGNORE INTO `sys_dic`(`id`, `name`, `value`, `description`) VALUES (1, 'super_admin', '', ' 超级管理员的登陆账号, 多个用逗号分隔');