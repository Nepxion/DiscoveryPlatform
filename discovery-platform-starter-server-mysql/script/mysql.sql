CREATE TABLE IF NOT EXISTS `sys_admin`
(
    `id`                    BIGINT UNSIGNED        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `login_mode`            INT UNSIGNED           NOT NULL COMMENT '角色类型(1:DB 2:LDAP)',
    `sys_role_id`           BIGINT UNSIGNED        NOT NULL COMMENT '角色id(sys_role表的主键)',
    `username`              VARCHAR(64)            NOT NULL COMMENT '管理员的登陆用户名',
    `password`              VARCHAR(128)           NOT NULL COMMENT '管理员的登陆密码',
    `name`                  VARCHAR(64)            NOT NULL COMMENT '管理员姓名',
    `phone_number`          VARCHAR(64)            NOT NULL COMMENT '管理员手机号码',
    `email`                 VARCHAR(128)           NOT NULL COMMENT '管理员邮箱',
    `remark`                VARCHAR(255)           NOT NULL COMMENT '管理员备注信息',
    `row_create_time`       DATETIME(3)            NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time`       DATETIME(3)            NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_admin_username` (`username`),
    INDEX `idx_sys_admin_sys_role_id` (`sys_role_id`)
) COMMENT ='管理员信息';
INSERT IGNORE INTO `sys_admin`(`id`, `login_mode`, `sys_role_id`, `username`, `password`, `name`, `phone_number`, `email`, `remark`)
VALUES (1, 1, 1, 'admin', 'ebc255e6a0c6711a4366bc99ebafb54f', '超级管理员', '18000000000', 'zhangningkid@163.com', '超级管理员');



CREATE TABLE IF NOT EXISTS `sys_page`
(
    `id`                    BIGINT UNSIGNED         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                  VARCHAR(64)             NOT NULL COMMENT '页面名称',
    `url`                   VARCHAR(256)            NOT NULL COMMENT '页面地址',
    `is_menu`               TINYINT(1)              NOT NULL COMMENT '页面是否出现在菜单栏',
    `is_default`            TINYINT(1)              NOT NULL COMMENT '是否是默认页(只允许有一个默认页，如果设置多个，以第一个为准)',
    `is_blank`              TINYINT(1)              NOT NULL COMMENT '是否新开窗口打开页面',
    `icon_class`            VARCHAR(64)             NOT NULL COMMENT 'html中的图标样式',
    `parent_id`             BIGINT(20) UNSIGNED     NOT NULL COMMENT '父级id(即本表的主键id)',
    `order`                 BIGINT(128) UNSIGNED    NOT NULL COMMENT '顺序号(值越小, 排名越靠前)',
    `remark`                VARCHAR(256)            NOT NULL COMMENT '备注',
    `row_create_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_url` (`url`) USING BTREE
) COMMENT ='页面配置';



CREATE TABLE IF NOT EXISTS `sys_permission`
(
    `id`                    BIGINT UNSIGNED         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `sys_role_id`           BIGINT UNSIGNED         NOT NULL COMMENT 'sys_role的主键id',
    `sys_page_id`           BIGINT UNSIGNED         NOT NULL COMMENT 'sys_page的主键id',
    `can_insert`            TINYINT(1)              NOT NULL COMMENT '是否能新增(true:能; false:不能)',
    `can_delete`            TINYINT(1)              NOT NULL COMMENT '是否能删除(true:能; false:不能)',
    `can_update`            TINYINT(1)              NOT NULL COMMENT '是否能修改(true:能; false:不能)',
    `can_select`            TINYINT(1)              NOT NULL COMMENT '是否能读取(true:能; false:不能)',
    `row_create_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_id_page_id` (`sys_role_id`, `sys_page_id`)
) COMMENT ='用户权限信息';



CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`                    BIGINT UNSIGNED         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                  VARCHAR(64)             NOT NULL COMMENT '角色名称',
    `super_admin`           TINYINT(1)              NOT NULL COMMENT '是否是超级管理员(1:是; 0:否)',
    `remark`                VARCHAR(256)            NOT NULL COMMENT '角色说明',
    `row_create_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_name` (`name`)
) COMMENT ='角色信息';


CREATE TABLE IF NOT EXISTS `sys_dic`
(
    `id`                    BIGINT(0) UNSIGNED      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`                  VARCHAR(64)             NOT NULL COMMENT '属性名称',
    `value`                 VARCHAR(256)            NOT NULL COMMENT '属性值',
    `description`           VARCHAR(128)            NOT NULL COMMENT '描述信息',
    `row_create_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_sys_dic_name`(`name`) USING BTREE,
    INDEX `idx_sys_admin_sys_role_id`(`value`) USING BTREE
) COMMENT ='字典信息';


CREATE TABLE IF NOT EXISTS `t_route_gateway`  (
    `id`                    BIGINT(0) UNSIGNED      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `route_id`              VARCHAR(64)             NOT NULL COMMENT '路由id',
    `gateway_name`          VARCHAR(128)            NOT NULL COMMENT '网关名称',
    `uri`                   VARCHAR(256)            NOT NULL COMMENT '转发目标uri',
    `predicates`            VARCHAR(2048)           NOT NULL COMMENT '断言字符串',
    `filters`               VARCHAR(2048)           NOT NULL COMMENT '过滤器字符串',
    `metadata`              VARCHAR(2048)           NOT NULL COMMENT '路由元数据',
    `order`                 INT(0) UNSIGNED         NOT NULL COMMENT '路由执行顺序',
    `service_name`          VARCHAR(64)             NOT NULL COMMENT '所属的服务名称(即: 服务的spring.application.name)',
    `enabled`               TINYINT(1)              NOT NULL COMMENT '是否启用',
    `description`           VARCHAR(256)            NOT NULL COMMENT '描述信息',
    `row_create_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_route_id`(`route_id`) USING BTREE
    ) COMMENT = 'Gateway网关的路由信息';


CREATE TABLE IF NOT EXISTS `t_route_zuul`  (
    `id`                    BIGINT(0) UNSIGNED      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `route_id`              VARCHAR(64)             NOT NULL COMMENT '路由id',
    `gateway_name`          VARCHAR(128)            NOT NULL COMMENT '网关名称',
    `service_id`            VARCHAR(64)             NOT NULL COMMENT '服务id',
    `path`                  VARCHAR(256)            NOT NULL COMMENT '转发目标路径',
    `uri`                   VARCHAR(256)            NOT NULL COMMENT '转发目标uri',
    `strip_prefix`          BIT                     NOT NULL COMMENT '',
    `retryable`             TINYINT(1)              NOT NULL COMMENT '',
    `sensitive_headers`     VARCHAR(256)            NOT NULL COMMENT '用逗号分隔',
    `enabled`               TINYINT(1)              NOT NULL COMMENT '是否启用',
    `description`           VARCHAR(256)            NOT NULL COMMENT '描述信息',
    `row_create_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time`       DATETIME(3)             NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `idx_route_id`(`route_id`) USING BTREE
    ) COMMENT = 'Zuul网关的路由信息';


INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `remark`) VALUES (1, '超级管理员', 1, '超级管理员, 拥有最高权限');
INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `remark`) VALUES (2, '研发人员', 0, '研发人员');

INSERT IGNORE INTO `sys_admin`(`id`, `sys_role_id`, `username`, `password`, `name`, `phone_number`, `email`, `remark`)VALUES (1, 1, 'admin', 'ebc255e6a0c6711a4366bc99ebafb54f', '超级管理员', '18000000000', 'administrator@sjb.com', '超级管理员');

INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `remark`) VALUES (1, '超级管理员', 1, '超级管理员, 拥有最高权限');
INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `remark`) VALUES (2, '研发人员', 0, '研发人员');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (1, '灰度发布', '/gray/tolist', b'1', b'1', b'0', 'layui-icon-release', 0, 1, '动态灰度发布');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (2, '路由配置', '', b'1', b'0', b'0', 'layui-icon-website', 0, 2, '动态路由配置');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (3, 'Gateway网关', '/routegateway/tolist', b'1', b'0', b'0', 'layui-icon-website', 2, 1, 'Gateway动态路由配置');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (4, 'Zuul网关', '/routezuul/tolist', b'1', b'0', b'0', 'layui-icon-website', 2, 2, 'Zuul动态路由配置');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (5, '授权管理', '', b'1', b'0', b'0', 'layui-icon-app', 0, 3, '授权管理');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (6, '接口列表', '/api/tolist', b'1', b'0', b'0', '', 5, 1, '接口列表');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (7, '应用列表', '/app/tolist', b'1', b'0', b'0', '', 5, 2, '应用列表');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (8, '权限列表', '/auth/tolist', b'1', b'0', b'0', '', 5, 3, '权限列表');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (9, '白名单列表', '/ignoreurl/tolist', b'1', b'0', b'0', '', 5, 4, '白名单列表');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (10, '链路跟踪', 'http://192.168.6.167:9411', b'1', b'0', b'1', 'layui-icon-location', 0, 4, 'Zipkin链路跟踪');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (11, '配置中心', 'http://apollo.wyyt.com/signin', b'1', b'0', b'1', 'layui-icon-form', 0, 5, 'Apollo配置中心');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (12, '注册中心', 'http://192.168.5.21:8500', b'1', b'0', b'1', 'layui-icon-component', 0, 6, 'Consul注册中心');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (13, '监控中心', 'http://springadmin.wyyt.com', b'1', b'0', b'1', 'layui-icon-chart-screen', 0, 7, 'SpringBootAdmin监控中心');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (14, '文档中心', 'http://127.0.0.1:10010/doc.html', b'1', b'0', b'1', 'layui-icon-read', 0, 8, '接口文档中心');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (15, '工具集合', '', b'1', b'0', b'0', 'layui-icon-util', 0, 9, '工具集合');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (16, '注册中心管理', '/consul/tolist', b'1', b'0', b'0', '', 15, 1, '注册中心管理');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (17, '系统设置', '', b'1', b'0', b'0', 'layui-icon-set', 0, 10, '系统设置');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (18, '页面配置', '/page/tolist', b'1', b'0', b'0', '', 17, 1, '页面配置');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (19, '权限设置', '', b'1', b'0', b'0', 'layui-icon-password', 0, 11, '权限设置');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (20, '管理员配置', '/admin/tolist', b'1', b'0', b'0', '', 19, 1, '管理员配置');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (21, '角色管理', '/role/tolist', b'1', b'0', b'0', '', 19, 2, '角色管理');
INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`, `order`, `remark`) VALUES (22, '权限管理', '/permission/tolist', b'1', b'0', b'0', '', 19, 3, '权限管理');

INSERT IGNORE INTO `sys_dic`(`id`, `name`, `value`, `description`) VALUES (1, 'super_admin', '', ' 超级管理员的登陆账号, 多个用逗号分隔');
