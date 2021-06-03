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
    UNIQUE KEY `idx_sys_admin_username` (`username`)
);
CREATE INDEX IF NOT EXISTS idx_sys_admin_sys_role_id ON sys_admin (`sys_role_id`);

insert INTO `sys_admin`(`id`, `login_mode`, `sys_role_id`, `username`, `password`, `name`, `phone_number`, `email`, `description`)
select 1, 1, 1, 'admin', 'ebc255e6a0c6711a4366bc99ebafb54f', '超级管理员', '18000000000', 'zhangningkid@163.com', '超级管理员'
    where  NOT EXISTS  (select * from sys_admin  where id = 1);


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
    PRIMARY KEY (`id`)
);

CREATE INDEX IF NOT EXISTS idx_sys_menu_idx_url ON sys_menu (`url`);

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
    UNIQUE KEY `idx_sys_role_id_menu_id` (`sys_role_id`, `sys_menu_id`)
) ;



CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`                        BIGINT UNSIGNED                 NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      VARCHAR(64)                     NOT NULL COMMENT '角色名称',
    `super_admin`               TINYINT(1)                      NOT NULL COMMENT '是否是超级管理员(1:是, 0:否)',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '角色描述信息',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_sys_role_name` (`name`)
);


CREATE TABLE IF NOT EXISTS `sys_dic`
(
    `id`                        BIGINT(0) UNSIGNED              NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                      VARCHAR(64)                     NOT NULL COMMENT '属性名称',
    `value`                     VARCHAR(128)                    NOT NULL COMMENT '属性值',
    `description`               VARCHAR(64)                     NOT NULL COMMENT '描述信息',
    `create_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `update_time`               DATETIME(3)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) ,
    UNIQUE KEY `idx_sys_dic_name`(`name`)
);

CREATE INDEX IF NOT EXISTS idx_sys_admin_sys_role_id ON sys_dic (`value`);

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
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_route_gateway_route_id`(`route_id`)
    ) ;


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
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_route_zuul_route_id`(`route_id`)
    );


INSERT INTO `sys_role`(`id`, `name`, `super_admin`, `description`) select  1, '超级管理员', 1, '超级管理员, 拥有最高权限' where NOT EXISTS  (select * from sys_role where id = 1);

INSERT INTO `sys_role`(`id`, `name`, `super_admin`, `description`) select 2, '研发人员', 0, '研发人员' where NOT EXISTS  (select * from sys_role where id = 2);

INSERT INTO `sys_dic`(`id`, `name`, `value`, `description`)  select 1, 'super_admin', '', ' 超级管理员的登陆账号, 多个用逗号分隔' where not exists (select * from sys_dic where id = 1);


INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 1, 'DashBoard', 'http://www.nepxion.com', '1', '1', '0', 'layui-icon-chart-screen', 0, 1, 'DashBoard' where NOT EXISTS  (select * from `sys_menu` where id = 1);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 2, '服务发布', '', '1', '0', '0', 'layui-icon-release', 0, 2, 'Spring Cloud服务发布' where NOT EXISTS  (select * from `sys_menu` where id = 2);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 3, '蓝绿发布', '/blue-green/list', '1', '0', '0', '', 2, 1, '蓝绿发布' where NOT EXISTS  (select * from `sys_menu` where id = 3);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 4, '灰度发布', '/gray/list', '1', '0', '0', '', 2, 2, '灰度发布' where NOT EXISTS  (select * from `sys_menu` where id = 4);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 5, '流量侦测', '/inspector/list', '1', '0', '0', '', 2, 3, '流量侦测' where NOT EXISTS  (select * from `sys_menu` where id = 5);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 6, '路由配置', '', '1', '0', '0', 'layui-icon-website', 0, 3, '路由配置' where NOT EXISTS  (select * from `sys_menu` where id = 6);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 7, 'Gateway网关路由', '/route-gateway/list', '1', '0', '0', '', 6, 1, 'Spring Cloud Gateway路由配置' where NOT EXISTS  (select * from `sys_menu` where id = 7);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 8, 'Zuul网关路由', '/route-zuul/list', '1', '0', '0', '', 6, 2, 'Zuul路由配置' where NOT EXISTS  (select * from `sys_menu` where id = 8);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 9, '服务管理', '', '1', '0', '0', 'layui-icon-template-1', 0, 4, '服务管理' where NOT EXISTS  (select * from `sys_menu` where id = 9);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 10, '流量染色启动', '/service/list1', '1', '0', '0', '', 9, 1, '启动的时候把-Dmetadata.version加上去，类似于运维侧的功能' where NOT EXISTS  (select * from `sys_menu` where id = 10);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 11, '服务拉入拉出', '/service/list2', '1', '0', '0', '', 9, 1, '服务从注册中心生效和注销' where NOT EXISTS  (select * from `sys_menu` where id = 11);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 12, '服务负载屏蔽', '/service/list3', '1', '0', '0', '', 9, 2, '服务负载屏蔽' where NOT EXISTS  (select * from `sys_menu` where id = 12);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 13, '安全管理', '', '1', '0', '0', 'layui-icon-app', 0, 5, '安全管理' where NOT EXISTS  (select * from `sys_menu` where id = 13);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 14, '应用列表', '/app/list', '1', '0', '0', '', 13, 1, '应用列表' where NOT EXISTS  (select * from `sys_menu` where id = 14);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 15, '接口列表', '/api/list', '1', '0', '0', '', 13, 2, '接口列表' where NOT EXISTS  (select * from `sys_menu` where id = 15);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 16, '权限列表', '/auth/list', '1', '0', '0', '', 13, 3, '权限列表' where NOT EXISTS  (select * from `sys_menu` where id = 16);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 17, '白名单列表', '/-url/list', '1', '0', '0', '', 13, 4, '白名单列表' where NOT EXISTS  (select * from `sys_menu` where id = 17);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 18, '注册中心', '', '1', '0', '0', 'layui-icon-flag', 0, 6, '注册中心' where NOT EXISTS  (select * from `sys_menu` where id = 18);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 19, 'Nacos', 'http://127.0.0.1:8848/nacos', '1', '0', '1', '', 18, 1, 'Nacos注册中心' where NOT EXISTS  (select * from `sys_menu` where id = 19);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 20, '配置中心', '', '1', '0', '0', 'layui-icon-survey', 0, 7, '配置中心' where NOT EXISTS  (select * from `sys_menu` where id = 20);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 21, 'Apollo', 'http://106.54.227.205/', '1', '0', '1', '', 20, 1, 'Apollo配置中心' where NOT EXISTS  (select * from `sys_menu` where id = 21);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 22, '监控中心', '', '1', '0', '0', 'layui-icon-app', 0, 8, '监控中心' where NOT EXISTS  (select * from `sys_menu` where id = 22);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 23, 'Spring Boot Admin', 'http://127.0.0.1:6002', '1', '0', '1', '', 22, 1, 'Spring Boot Admin监控中心' where NOT EXISTS  (select * from `sys_menu` where id = 23);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 24, 'Grafana', 'http://127.0.0.1:3000', '1', '0', '1', '', 22, 2, 'Grafana监控中心' where NOT EXISTS  (select * from `sys_menu` where id = 24);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 25, 'SkyWalking', 'http://127.0.0.1:8080', '1', '0', '1', '', 22, 3, 'SkyWalking监控中心' where NOT EXISTS  (select * from `sys_menu` where id = 25);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 26, '限流中心', '', '1', '0', '0', 'layui-icon-transfer', 0, 9, '限流中心' where NOT EXISTS  (select * from `sys_menu` where id = 26);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 27, 'Sentinel', 'http://127.0.0.1:8075', '1', '0', '1', '', 26, 1, 'Sentinel限流中心' where NOT EXISTS  (select * from `sys_menu` where id = 27);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 28, '日志中心', '', '1', '0', '0', 'layui-icon-list', 0, 10, '日志中心' where NOT EXISTS  (select * from `sys_menu` where id = 28);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 29, 'Kibana', 'http://127.0.0.1:5601 ', '1', '0', '1', '', 28, 1, 'Kibana日志中心' where NOT EXISTS  (select * from `sys_menu` where id = 29);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 30, '告警中心', '', '1', '0', '0', 'layui-icon-about', 0, 11, '告警中心' where NOT EXISTS  (select * from `sys_menu` where id = 30);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 31, '灰度蓝绿变更告警', '/blue-green-change-warning/list', '1', '0', '0', '', 30, 1, '灰度蓝绿变更告警' where NOT EXISTS  (select * from `sys_menu` where id = 31);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 32, '灰度蓝绿不一致告警', '/blue-green-warning/list', '1', '0', '0', '', 30, 2, '灰度蓝绿不一致告警' where NOT EXISTS  (select * from `sys_menu` where id = 32);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 33, '网关路由不一致告警', '/route-warning/list', '1', '0', '0', '', 30, 3, '网关路由不一致告警' where NOT EXISTS  (select * from `sys_menu` where id = 33);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 34, '文档中心', '', '1', '0', '0', 'layui-icon-read', 0, 12, '文档中心' where NOT EXISTS  (select * from `sys_menu` where id = 34);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 35, 'Swagger', 'http://127.0.0.1:6001/swagger-ui.html', '1', '0', '1', '', 34, 1, 'Swagger文档中心' where NOT EXISTS  (select * from `sys_menu` where id = 35);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 36, '系统配置', '', '1', '0', '0', 'layui-icon-set', 0, 13, '系统设置' where NOT EXISTS  (select * from `sys_menu` where id = 36);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 37, '页面配置', '/menu/list', '1', '0', '0', '', 36, 1, '页面配置' where NOT EXISTS  (select * from `sys_menu` where id = 37);

INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 38, '授权配置', '', '1', '0', '0', 'layui-icon-password', 0, 14, '权限设置' where NOT EXISTS  (select * from `sys_menu` where id = 38);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 39, '管理员配置', '/admin/list', '1', '0', '0', '', 38, 1, '管理员配置' where NOT EXISTS  (select * from `sys_menu` where id = 39);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 40, '角色配置', '/role/list', '1', '0', '0', '', 38, 2, '角色管理' where NOT EXISTS  (select * from `sys_menu` where id = 40);
INSERT  INTO `sys_menu`(`id`, `name`, `url`, `show_flag`, `default_flag`, `blank_flag`, `icon_class`, `parent_id`, `order`, `description`) SELECT 41, '权限配置', '/permission/list', '1', '0', '0', '', 38, 3, '权限管理' where NOT EXISTS  (select * from `sys_menu` where id = 41);

INSERT INTO `sys_dic`(`id`, `name`, `value`, `description`)  select 1, 'super_admin', '', ' 超级管理员的登陆账号, 多个用逗号分隔' where not exists (select * from sys_dic where id = 1);
