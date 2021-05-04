CREATE TABLE IF NOT EXISTS `sys_admin`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `login_mode`      INT UNSIGNED NOT NULL COMMENT '角色类型(1:DB 2:LDAP)',
    `sys_role_id`     BIGINT UNSIGNED NOT NULL COMMENT '角色id(sys_role表的主键)',
    `username`        VARCHAR(64)         NOT NULL COMMENT '管理员的登陆用户名',
    `password`        VARCHAR(128)        NOT NULL COMMENT '管理员的登陆密码',
    `name`            VARCHAR(64)         NOT NULL COMMENT '管理员姓名',
    `phone_number`    VARCHAR(64)         NOT NULL COMMENT '管理员手机号码',
    `email`           VARCHAR(128)        NOT NULL COMMENT '管理员邮箱',
    `remark`          VARCHAR(255)        NOT NULL COMMENT '管理员备注信息',
    `row_create_time` DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time` DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_admin_username` (`username`),
    INDEX `idx_sys_admin_sys_role_id` (`sys_role_id`)
) COMMENT ='管理员信息';
INSERT IGNORE INTO `sys_admin`(`id`, `login_mode`, `sys_role_id`, `username`, `password`, `name`, `phone_number`, `email`, `remark`)
VALUES (1, 1, 1, 'admin', 'ebc255e6a0c6711a4366bc99ebafb54f', '超级管理员', '18000000000', 'zhangningkid@163.com', '超级管理员');



CREATE TABLE IF NOT EXISTS `sys_page`
(
    `id`              BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`            VARCHAR(64)          NOT NULL COMMENT '页面名称',
    `url`             VARCHAR(256)         NOT NULL COMMENT '页面地址',
    `is_menu`         TINYINT(1)           NOT NULL COMMENT '页面是否出现在菜单栏',
    `is_default`      TINYINT(1)           NOT NULL COMMENT '是否是默认页(只允许有一个默认页，如果设置多个，以第一个为准)',
    `is_blank`        TINYINT(1)           NOT NULL COMMENT '是否新开窗口打开页面',
    `icon_class`      VARCHAR(64)          NOT NULL COMMENT 'html中的图标样式',
    `parent_id`       BIGINT(20) UNSIGNED  NOT NULL COMMENT '父级id(即本表的主键id)',
    `order_num`       BIGINT(128) UNSIGNED NOT NULL COMMENT '顺序号(值越小, 排名越靠前)',
    `remark`          VARCHAR(256)         NOT NULL COMMENT '备注',
    `row_create_time` DATETIME(3)          NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time` DATETIME(3)          NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_url` (`url`) USING BTREE
) COMMENT ='页面配置';



CREATE TABLE IF NOT EXISTS `sys_permission`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `sys_role_id`     BIGINT UNSIGNED NOT NULL COMMENT 'sys_role的主键id',
    `sys_page_id`     BIGINT UNSIGNED NOT NULL COMMENT 'sys_page的主键id',
    `can_insert`      TINYINT(1)          NOT NULL COMMENT '是否能新增(true:能; false:不能)',
    `can_delete`      TINYINT(1)          NOT NULL COMMENT '是否能删除(true:能; false:不能)',
    `can_update`      TINYINT(1)          NOT NULL COMMENT '是否能修改(true:能; false:不能)',
    `can_select`      TINYINT(1)          NOT NULL COMMENT '是否能读取(true:能; false:不能)',
    `row_create_time` DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time` DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_id_page_id` (`sys_role_id`, `sys_page_id`)
) COMMENT ='用户权限信息';



CREATE TABLE IF NOT EXISTS `sys_role`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`            VARCHAR(64)         NOT NULL COMMENT '角色名称',
    `super_admin`     TINYINT(1)          NOT NULL COMMENT '是否是超级管理员(1:是; 0:否)',
    `remark`          VARCHAR(256)        NOT NULL COMMENT '角色说明',
    `row_create_time` DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    `row_update_time` DATETIME(3)         NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `idx_sys_role_name` (`name`)
) COMMENT ='角色信息';
INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `remark`) VALUES (1, '超级管理员', 1, '超级管理员, 拥有最高权限');
INSERT IGNORE INTO `sys_role`(`id`, `name`, `super_admin`, `remark`) VALUES (2, '研发人员', 0, '研发人员');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`,
                              `order_num`, `remark`)
VALUES (1, '权限设置', '', b'1', b'0', b'0', 'layui-icon-password', 0, 1, '权限设置');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`,
                              `order_num`, `remark`)
VALUES (2, '管理员配置', '/admin/tolist', b'1', b'0', b'0', '', 1, 1, '管理员配置');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`,
                              `order_num`, `remark`)
VALUES (3, '角色管理', '/role/tolist', b'1', b'0', b'0', '', 1, 2, '角色管理');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`,
                              `order_num`, `remark`)
VALUES (4, '权限管理', '/permission/tolist', b'1', b'0', b'0', '', 1, 3, '权限管理');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`,
                              `order_num`, `remark`)
VALUES (5, '系统设置', '', b'1', b'0', b'0', 'layui-icon-set', 0, 2, '系统设置');

INSERT IGNORE INTO `sys_page`(`id`, `name`, `url`, `is_menu`, `is_default`, `is_blank`, `icon_class`, `parent_id`,
                              `order_num`, `remark`)
VALUES (6, '页面配置', '/page/tolist', b'1', b'0', b'0', '', 5, 1, '页面配置');