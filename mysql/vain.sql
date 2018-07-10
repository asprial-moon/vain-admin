/*
SQLyog Ultimate - MySQL GUI v8.2 
MySQL - 5.5.56 : Database - vain
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`vain` /*!40100 DEFAULT CHARACTER SET latin1 */;

USE `vain`;

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单id规则\r\n 11 00 00，高位代表目录，中位代表菜单，低位代表操作\r\n            ',
  `name` varchar(128) NOT NULL,
  `parentId` bigint(20) NOT NULL COMMENT '根目录的父id填0',
  `menuKey` varchar(128) NOT NULL,
  `type` int(11) NOT NULL COMMENT '1：目录\r\n            2：菜单\r\n            3：按钮\r\n            ',
  `url` varchar(256) DEFAULT NULL COMMENT '点击菜单进入的页面，相对路径',
  `icon` varchar(80) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `deleted` tinyint(4) NOT NULL COMMENT '0：正常\r\n            1：已删除',
  `createTime` datetime NOT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_menuKey` (`menuKey`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=120304 DEFAULT CHARSET=utf8 COMMENT='保存菜单数据';

/*Data for the table `t_menu` */

insert  into `t_menu`(`id`,`name`,`parentId`,`menuKey`,`type`,`url`,`icon`,`description`,`deleted`,`createTime`,`modifyTime`) values (110000,'系统管理',0,'system',1,NULL,'am-icon-cog','系统管理',0,'2017-09-23 15:55:16','2018-05-13 16:21:25'),(110100,'系统日志',110000,'system.log',2,NULL,NULL,NULL,0,'2017-10-18 16:03:20','2018-06-17 15:08:45'),(110101,'日志信息',110100,'system.log.getPagedList',3,'/log/getPagedList',NULL,NULL,0,'2017-10-24 16:24:44',NULL),(110102,'日志删除',110100,'system.log.delete',3,'/log/delete',NULL,NULL,0,'2018-06-13 13:37:28',NULL),(110200,'系统配置',110000,'system.config',2,NULL,NULL,NULL,0,'2017-10-14 21:31:30',NULL),(110201,'获取配置',110200,'system.config.getPagedList',3,'/systemConfig/getPagedList',NULL,NULL,0,'2017-10-28 12:12:13',NULL),(110202,'修改配置',110200,'system.config.modify',3,'/systemConfig/modify',NULL,NULL,0,'2017-10-24 10:09:26',NULL),(110300,'系统在线',110000,'system.online',2,NULL,NULL,NULL,0,'2017-10-27 16:29:54',NULL),(110301,'在线用户',110300,'system.online.onLineUser',3,'/user/onLineUser',NULL,NULL,0,'2017-10-28 12:14:39',NULL),(110302,'用户下线',110300,'system.online.forcedOffLine',3,'/user/forcedOffLine',NULL,NULL,0,'2017-10-27 16:56:51',NULL),(110400,'定时任务',110000,'system.scheduleJob',2,NULL,NULL,NULL,0,'2017-11-01 10:08:20',NULL),(110401,'任务获取',110400,'system.scheduleJob.getList',3,'/scheduleJob/getList',NULL,NULL,0,'2017-11-01 11:33:23',NULL),(110402,'任务暂停',110400,'system.scheduleJob.pause',3,'/scheduleJob/pauseJob',NULL,NULL,0,'2017-11-01 11:31:49',NULL),(110403,'任务恢复',110400,'system.scheduleJob.resume',3,'/scheduleJob/resumeJob',NULL,NULL,0,'2017-11-01 11:31:49',NULL),(110404,'任务测试',110400,'system.scheduleJob.trigger',3,'/scheduleJob/triggerJob',NULL,NULL,0,'2017-11-01 11:31:49',NULL),(110405,'任务修改',110400,'system.scheduleJob.modify',3,'/scheduleJob/modify',NULL,NULL,0,'2017-11-01 11:31:49',NULL),(110406,'任务删除',110400,'system.scheduleJob.delete',3,'/scheduleJob/delete',NULL,NULL,0,'2017-11-01 11:31:49',NULL),(120000,'账号管理',0,'account',1,NULL,'am-icon-user','账号管理',0,'2017-10-14 20:36:34','2018-06-10 15:28:18'),(120100,'用户管理',120000,'account.user',2,NULL,NULL,NULL,0,'2017-09-25 10:35:07','2018-06-10 15:26:54'),(120101,'用户添加',120100,'account.user.add',3,'/user/add',NULL,NULL,0,'2017-10-12 21:18:01',NULL),(120102,'用户删除',120100,'account.user.delete',3,'/user/delete',NULL,NULL,0,'2017-10-25 14:33:37',NULL),(120103,'用户修改',120100,'account.user.modify',3,'/user/modify',NULL,NULL,0,'2017-10-25 14:33:37',NULL),(120104,'用户授权',120100,'account.user.grant',3,'/user/assignUserMenu',NULL,NULL,0,'2017-10-25 14:33:37',NULL),(120105,'用户角色',120100,'account.user.role',3,'/role/grantUserRole',NULL,NULL,0,'2017-10-25 17:49:39',NULL),(120106,'密码重置',120100,'account.user.resetPwd',3,'/user/resetPwd',NULL,NULL,0,'2017-10-25 17:49:39',NULL),(120107,'用户锁定',120100,'account.user.lock',3,'/user/lock',NULL,NULL,0,'2017-10-25 17:49:39',NULL),(120108,'用户查询',120100,'account.user.getPagedList',3,'/user/getPagedList',NULL,NULL,0,'2017-10-25 18:00:54',NULL),(120200,'角色管理',120000,'account.role',2,NULL,NULL,NULL,0,'2017-09-25 14:05:51',NULL),(120201,'角色添加',120200,'account.role.add',3,'/role/add',NULL,NULL,0,'2017-10-25 10:23:07',NULL),(120202,'角色删除',120200,'account.role.delete',3,'/role/delete',NULL,NULL,0,'2017-10-25 10:23:43',NULL),(120203,'角色修改',120200,'account.role.modify',3,'/role/modify',NULL,NULL,0,'2017-10-25 10:24:23',NULL),(120204,'角色权限',120200,'account.role.grant',3,'/role/assignRoleMenu',NULL,NULL,0,'2017-10-25 10:31:15',NULL),(120205,'查询角色',120200,'account.role.getPagedList',3,'/role/getList',NULL,NULL,0,'2017-10-28 12:17:22',NULL),(120206,'角色获取',120200,'account.role.getById',3,'/role/getById/**',NULL,NULL,0,'2018-06-09 15:40:44',NULL),(120300,'菜单管理',120000,'account.menu',2,'',NULL,NULL,0,'2017-10-11 19:34:25',NULL),(120301,'菜单查询',120300,'account.menu.getPagedList',3,'/menu/getMenuList',NULL,NULL,0,'2017-10-28 12:18:42',NULL),(120302,'菜单修改',120300,'account.menu.modify',3,'/menu/modify',NULL,NULL,0,'2017-10-25 10:16:44',NULL),(120303,'个人菜单',120300,'account.menu.getMenusByUserId',3,'/menu/getMenusByUserId',NULL,NULL,0,'2018-06-10 09:26:46',NULL);

/*Table structure for table `t_operation_log` */

DROP TABLE IF EXISTS `t_operation_log`;

CREATE TABLE `t_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) DEFAULT NULL COMMENT '操作用户id',
  `operationType` int(11) DEFAULT NULL COMMENT '日志操作类型',
  `operationData` varchar(256) DEFAULT NULL COMMENT '操作数据',
  `createTime` datetime NOT NULL,
  `info` varchar(256) DEFAULT NULL COMMENT '操作信息',
  `methodName` varchar(40) DEFAULT NULL COMMENT '操作方法',
  `className` varchar(60) DEFAULT NULL COMMENT '操作类',
  `operationIP` varchar(20) DEFAULT NULL,
  `exceptionMessage` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `AK_Key_1` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8;

/*Data for the table `t_operation_log` */

insert  into `t_operation_log`(`id`,`userId`,`operationType`,`operationData`,`createTime`,`info`,`methodName`,`className`,`operationIP`,`exceptionMessage`) values (88,NULL,6,'{\"userName\":\"admin\"}','2018-06-18 22:03:45','登录','login','com.vain.controller.UserController',NULL,NULL),(89,NULL,5,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:45','','lock','com.vain.service.impl.UserServiceImpl',NULL,NULL),(90,1,6,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:45','锁定/解锁','lockUser','com.vain.controller.UserController','127.0.0.1',NULL),(91,NULL,5,'{\"id\":\"3\"}','2018-06-18 22:03:45','','lock','com.vain.service.impl.UserServiceImpl',NULL,NULL),(92,1,6,'{\"id\":\"3\"}','2018-06-18 22:03:45','锁定/解锁','lockUser','com.vain.controller.UserController','127.0.0.1',NULL),(93,NULL,5,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:45','','lock','com.vain.service.impl.UserServiceImpl',NULL,NULL),(94,1,6,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:45','锁定/解锁','lockUser','com.vain.controller.UserController','127.0.0.1',NULL),(103,1,6,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:46','锁定/解锁','lockUser','com.vain.controller.UserController','127.0.0.1',NULL),(104,1,6,'{\"id\":\"3\"}','2018-06-18 22:03:46','锁定/解锁','lockUser','com.vain.controller.UserController','127.0.0.1',NULL),(105,NULL,5,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:46','','lock','com.vain.service.impl.UserServiceImpl',NULL,NULL),(106,1,6,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:46','锁定/解锁','lockUser','com.vain.controller.UserController','127.0.0.1',NULL),(107,NULL,5,'{\"id\":\"3\"}','2018-06-18 22:03:46','','lock','com.vain.service.impl.UserServiceImpl',NULL,NULL),(108,1,6,'{\"id\":\"3\"}','2018-06-18 22:03:46','锁定/解锁','lockUser','com.vain.controller.UserController','127.0.0.1',NULL),(109,NULL,5,'{\"state\":\"1\",\"id\":\"3\"}','2018-06-18 22:03:46','','lock','com.vain.service.impl.UserServiceImpl',NULL,NULL),(222,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:58','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(223,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:58','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(224,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:58','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(225,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:58','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(226,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:59','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(227,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:59','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(228,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:59','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(229,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:09:59','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(230,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:39:58','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(231,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 15:39:58','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(232,NULL,6,'{\"userName\":\"admin\"}','2018-06-23 19:49:59','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(233,NULL,6,'{\"userName\":\"test\"}','2018-06-23 20:39:59','登录','login','com.vain.controller.UserController','127.0.0.1',NULL),(234,NULL,6,'{\"userName\":\"admin\"}','2018-07-01 09:30:01','登录','login','com.vain.controller.UserController',NULL,NULL),(235,NULL,6,'{\"userName\":\"admin\"}','2018-07-01 13:50:01','登录','login','com.vain.controller.UserController',NULL,NULL),(236,NULL,6,'{\"userName\":\"admin\"}','2018-07-01 14:40:01','登录','login','com.vain.controller.UserController',NULL,NULL),(237,NULL,6,'{\"userName\":\"admin\"}','2018-07-01 20:20:01','登录','login','com.vain.controller.UserController',NULL,NULL),(238,NULL,6,'{\"userName\":\"admin\"}','2018-07-01 22:20:01','登录','login','com.vain.controller.UserController',NULL,NULL);

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `roleKey` varchar(128) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `deleted` tinyint(4) NOT NULL COMMENT '0：正常\r\n            1：已删除',
  `createTime` datetime NOT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_roleKey` (`roleKey`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='保存角色数据';

/*Data for the table `t_role` */

insert  into `t_role`(`id`,`name`,`roleKey`,`description`,`deleted`,`createTime`,`modifyTime`) values (1,'超级管理员','SUPER_ADMINISTRATOR','超级管理员',0,'2017-09-23 16:17:24','2017-09-23 16:17:25'),(2,'管理员','ADMINISTRATOR','管理员',0,'2017-09-23 16:17:56','2017-10-19 18:00:23'),(3,'普通用户','USER','用户',0,'2017-09-23 16:19:01','2018-05-13 14:16:55'),(4,'测试','CS','测试添加',0,'2018-05-12 22:00:54',NULL),(5,'测试角色权限','ROLE_TEST','测试',0,'2018-05-13 14:22:24',NULL);

/*Table structure for table `t_role_menu` */

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleId` bigint(20) NOT NULL,
  `menuId` bigint(20) NOT NULL,
  `createTime` datetime NOT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='保存角色和菜单的关联关系数据';

/*Data for the table `t_role_menu` */

insert  into `t_role_menu`(`id`,`roleId`,`menuId`,`createTime`,`modifyTime`) values (1,1,110000,'2017-10-28 12:20:36',NULL),(2,1,110100,'2017-10-28 12:20:36',NULL),(3,1,110101,'2017-10-28 12:20:36',NULL),(4,1,110102,'2017-10-28 12:20:36',NULL),(5,1,110200,'2017-10-28 12:20:36',NULL),(6,1,110201,'2017-10-28 12:20:36',NULL),(7,1,110202,'2017-10-28 12:20:36',NULL),(8,1,110300,'2017-10-28 12:20:36',NULL),(9,1,110301,'2017-10-28 12:20:36',NULL),(10,1,110302,'2017-10-28 12:20:36',NULL),(11,1,120000,'2017-10-28 12:20:36',NULL),(12,1,120100,'2017-10-28 12:20:36',NULL),(13,1,120101,'2017-10-28 12:20:36',NULL),(14,1,120102,'2017-10-28 12:20:36',NULL),(15,1,120103,'2017-10-28 12:20:36',NULL),(16,1,120104,'2017-10-28 12:20:36',NULL),(17,1,120105,'2017-10-28 12:20:36',NULL),(18,1,120106,'2017-10-28 12:20:36',NULL),(19,1,120107,'2017-10-28 12:20:36',NULL),(20,1,120108,'2017-10-28 12:20:36',NULL),(21,1,120200,'2017-10-28 12:20:36',NULL),(22,1,120201,'2017-10-28 12:20:36',NULL),(23,1,120202,'2017-10-28 12:20:36',NULL),(24,1,120203,'2017-10-28 12:20:36',NULL),(25,1,120204,'2017-10-28 12:20:36',NULL),(26,1,120205,'2017-10-28 12:20:36',NULL),(27,1,120300,'2017-10-28 12:20:36',NULL),(28,1,120301,'2017-10-28 12:20:36',NULL),(29,1,120302,'2017-10-28 12:20:36',NULL),(30,1,130000,'2017-10-28 12:20:36',NULL),(31,1,130100,'2017-10-28 12:20:36',NULL),(32,1,110400,'2017-11-01 10:08:58',NULL),(33,1,110401,'2017-11-01 14:39:49',NULL),(34,1,110402,'2017-11-01 14:39:49',NULL),(35,1,110403,'2017-11-01 14:39:49',NULL),(36,1,110404,'2017-11-01 14:39:49',NULL),(37,1,110405,'2017-11-01 14:39:49',NULL),(38,1,110406,'2017-11-01 14:40:16',NULL),(41,5,110100,'2018-06-09 22:16:00',NULL),(42,5,110101,'2018-06-09 22:16:00',NULL),(43,5,110102,'2018-06-09 22:16:00',NULL),(44,5,110000,'2018-06-09 22:16:00',NULL),(45,1,120206,'2018-06-09 15:44:45',NULL),(46,1,120303,'2018-06-10 09:27:14',NULL),(47,1,110102,'2018-06-13 13:38:18',NULL);

/*Table structure for table `t_schedule_job` */

DROP TABLE IF EXISTS `t_schedule_job`;

CREATE TABLE `t_schedule_job` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `jobName` varchar(255) DEFAULT NULL,
  `jobGroup` varchar(255) DEFAULT NULL,
  `jobStatus` varchar(255) DEFAULT NULL,
  `cronExpression` varchar(40) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `jobClass` varchar(255) DEFAULT NULL,
  `isConcurrent` varchar(255) DEFAULT NULL COMMENT '1',
  `springName` varchar(255) DEFAULT NULL,
  `jobMethod` varchar(255) NOT NULL,
  `deleted` tinyint(4) NOT NULL,
  `createTime` timestamp NULL DEFAULT NULL,
  `modifyTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_group` (`jobName`,`jobGroup`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

/*Data for the table `t_schedule_job` */

insert  into `t_schedule_job`(`id`,`jobName`,`jobGroup`,`jobStatus`,`cronExpression`,`description`,`jobClass`,`isConcurrent`,`springName`,`jobMethod`,`deleted`,`createTime`,`modifyTime`) values (1,'定时任务测试','1','1','0 0/5 * * * ?','数据库备份','com.vain.quartz.task.AutoTask','0','autoTask','autoTaskTest',0,'2017-11-01 09:51:18','2018-06-18 21:57:43'),(2,'日志写入','1','1','0 0/10 * * * ?','日志写入','com.vain.quartz.task.LogTask','0','logTask','saveLogs',0,'2017-11-01 09:51:18','2018-06-18 22:03:26');

/*Table structure for table `t_system_config` */

DROP TABLE IF EXISTS `t_system_config`;

CREATE TABLE `t_system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置表的起始值设置为100，1-100用于通用配置项',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '配置项名字',
  `code` varchar(128) COLLATE utf8_bin NOT NULL COMMENT '配置项键',
  `value` varchar(2048) COLLATE utf8_bin NOT NULL COMMENT '配置项值',
  `valueType` tinyint(1) NOT NULL DEFAULT '1' COMMENT '配置项值类型\n1：String\n2：INT\n3：String[]\n',
  `type` tinyint(4) NOT NULL COMMENT '配置项类型\n1：系统基础配置',
  `description` varchar(256) COLLATE utf8_bin NOT NULL COMMENT '配置项描述',
  `visible` bit(1) NOT NULL DEFAULT b'1' COMMENT '配置项在管理界面是否可见',
  `createTime` datetime NOT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统配置表';

/*Data for the table `t_system_config` */

insert  into `t_system_config`(`id`,`name`,`code`,`value`,`valueType`,`type`,`description`,`visible`,`createTime`,`modifyTime`) values (1,'上传文件访问根url','SYS_FILE_UPLOAD_ROOT_URL','http://118.24.103.116/vain',1,1,'上传文件访问根url(配置nginx)','','2017-10-14 17:57:57','2017-10-14 17:57:57'),(2,'上传文件ftp的主机ip','SYS_FILE_UPLOAD_HOST_IP','118.24.103.116',1,1,'上传文件ftp的主机ip','','2017-10-14 17:57:57','2017-10-14 17:57:57'),(3,'上传文件ftp的主机端口','SYS_FILE_UPLOAD_HOST_PORT','22',2,1,'上传文件ftp的主机端口（默认22）','','2017-10-14 17:57:57','2018-06-04 22:13:40'),(4,'上传文件ftp的主机账号','SYS_FILE_UPLOAD_HOST_USER','root',1,2,'上传文件ftp的主机账号o','','2017-10-14 17:57:57','2017-10-27 17:22:03'),(5,'上传文件ftp的主机密码','SYS_FILE_UPLOAD_HOST_PASSWD','vain123456',1,1,'上传文件ftp的主机密码','','2017-10-14 17:57:57','2017-10-28 12:23:28'),(6,'上传文件存放目录','SYS_FILE_UPLOAD_ROOT_PATH','/usr/share/nginx/vain',1,1,'上传文件存放目录','','2017-10-14 17:57:57','2017-10-14 17:57:57');

/*Table structure for table `t_upload_file` */

DROP TABLE IF EXISTS `t_upload_file`;

CREATE TABLE `t_upload_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(256) NOT NULL,
  `name` varchar(128) NOT NULL,
  `type` bigint(2) NOT NULL,
  `length` int(11) DEFAULT NULL,
  `userId` int(11) NOT NULL,
  `url` varchar(256) NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifyTime` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_upload_file_id_uindex` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1 COMMENT='上传文件';

/*Data for the table `t_upload_file` */

insert  into `t_upload_file`(`id`,`uuid`,`name`,`type`,`length`,`userId`,`url`,`createTime`,`modifyTime`) values (1,'2018-06-20/1/e1d91957-170a-41aa-8bc8-191c9c56dece','mmexport1528862562716.jpg',1,0,1,'/usr/share/nginx/vain/2018-06-20/1/e1d91957-170a-41aa-8bc8-191c9c56dece','2018-06-20 22:29:34','0000-00-00 00:00:00'),(2,'2018-06-20/1/614b95a5-eaee-47a2-be39-19d0c33822b6','mmexport1528862562716.jpg',1,0,1,'/usr/share/nginx/vain/2018-06-20/1/614b95a5-eaee-47a2-be39-19d0c33822b6','2018-06-20 22:41:57','0000-00-00 00:00:00'),(3,'2018-06-20/1/c94204b8-b6cf-4764-a9eb-c52cb8face32','mmexport1528862562716.jpg',1,0,1,'/usr/share/nginx/vain/2018-06-20/1/c94204b8-b6cf-4764-a9eb-c52cb8face32','2018-06-20 22:42:21','0000-00-00 00:00:00'),(4,'057e8684-163d-406a-bf19-4abb54f3ed63','file',1,0,1,'/usr/share/nginx/vain/057e8684-163d-406a-bf19-4abb54f3ed63','2018-06-20 22:43:57','0000-00-00 00:00:00'),(5,'30c7d32c-2a61-4bf7-a5fc-cb1f05d3aa71','file',1,0,1,'http://118.24.103.116/vain/30c7d32c-2a61-4bf7-a5fc-cb1f05d3aa71','2018-06-20 22:57:06','0000-00-00 00:00:00'),(6,'66cccd58-1dde-4a14-adba-f19c6c45b6b9','file',1,0,1,'http://118.24.103.116/vain/66cccd58-1dde-4a14-adba-f19c6c45b6b9','2018-06-20 22:58:17','0000-00-00 00:00:00'),(7,'314d0774-929f-4fa4-9029-d7ea4393e738','mmexport1528862562716.jpg',1,0,1,'http://118.24.103.116/vain/314d0774-929f-4fa4-9029-d7ea4393e738..jpg','2018-06-20 23:03:07','0000-00-00 00:00:00'),(8,'c180ee4f-08cc-47c1-92b7-9657a9e64580','mmexport1528862562716.jpg',1,0,1,'http://118.24.103.116/vain/c180ee4f-08cc-47c1-92b7-9657a9e64580.jpg','2018-06-23 11:35:15','0000-00-00 00:00:00'),(9,'33090cb0-a211-4682-8544-28fb38b05858','mmexport1528862562716.jpg',1,0,1,'/http://118.24.103.116/vain/1/33090cb0-a211-4682-8544-28fb38b05858.jpg','2018-06-23 12:02:08','0000-00-00 00:00:00'),(10,'46c4d298-5536-49bc-b894-0139a6dafd6a','mmexport1528862562716.jpg',1,0,1,'/http://118.24.103.116/vain/1/46c4d298-5536-49bc-b894-0139a6dafd6a.jpg','2018-06-23 14:11:41','0000-00-00 00:00:00'),(11,'61861bf1-87b4-4772-8810-9223b3885a83','mmexport1528862562716.jpg',1,0,1,'/http://118.24.103.116/vain/1/61861bf1-87b4-4772-8810-9223b3885a83.jpg','2018-06-23 14:15:34','0000-00-00 00:00:00'),(12,'197cce3a-3b26-464d-9909-ccf88808c8c0','mmexport1528862562716.jpg',1,0,1,'http://118.24.103.116/vain/1/197cce3a-3b26-464d-9909-ccf88808c8c0.jpg','2018-06-23 14:16:22','0000-00-00 00:00:00'),(13,'7c42cfdd-5f27-445d-928d-534fcc96bb91','mmexport1528862562716.jpg',1,0,1,'http://118.24.103.116/vain/1/7c42cfdd-5f27-445d-928d-534fcc96bb91.jpg','2018-06-23 14:22:25','0000-00-00 00:00:00');

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用户类型',
  `userName` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(256) NOT NULL COMMENT '密码加密算法：MD5(passwd+salt)',
  `salt` varchar(128) NOT NULL,
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(256) DEFAULT NULL COMMENT '头像',
  `sex` int(11) DEFAULT NULL COMMENT '1：男\r\n            2：女',
  `birthday` date DEFAULT NULL,
  `state` tinyint(4) NOT NULL DEFAULT '0' COMMENT '用户状态\n0：正常 1：锁定 ',
  `phone` varchar(18) DEFAULT NULL,
  `email` varchar(64) DEFAULT NULL,
  `deleted` tinyint(4) NOT NULL COMMENT '0:正常\r\n            1：已删除',
  `createTime` datetime NOT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

/*Data for the table `t_user` */

insert  into `t_user`(`id`,`type`,`userName`,`password`,`salt`,`nickname`,`avatar`,`sex`,`birthday`,`state`,`phone`,`email`,`deleted`,`createTime`,`modifyTime`) values (1,1,'admin','a0bca5d6fede811f6238a0eb475e2931','f74518bf-9b68-4f42-a680-eb3abdeaa1d6','管理员','http://cdn.aixifan.com/dotnet/20130418/umeditor/dialogs/emotion/images/ac/26.gif',NULL,NULL,0,NULL,NULL,0,'2017-09-17 16:42:25','2017-10-13 20:36:08'),(2,2,'小明121','d6f175817754c5c0dae2520d0b7effe3','6acf8206-0869-4e50-af61-835ad15c05f3',NULL,NULL,0,NULL,0,NULL,'21212166',0,'2017-09-30 21:41:46','2018-06-18 22:21:35'),(3,2,'张三丰','28573cf398dcdfb9085fdc4f5dcb382d','b96fedde-0c1b-424e-bf50-c3a2480d267a',NULL,NULL,0,NULL,1,NULL,NULL,0,'2017-10-14 13:31:05','2018-06-18 17:52:22'),(4,3,'用户A','b78e9cc1f14d182ea1f4d09f83300740','0f237372-228a-404b-a7be-f690dd2a478b',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-14 14:01:46','2018-06-09 22:16:53'),(5,2,'admin1','1525b57115d81b10956b5150fe99c14d','af400b17-6e6a-4f97-ba60-916bec67def6',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-26 15:18:56','2018-06-09 22:10:18'),(6,2,'admin2','909f2105d2a6271da2299b13d356c7b3','f1843ae5-e9a2-49d8-8b92-c92dd6e7f65a',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-26 15:21:06','2017-10-27 11:34:55'),(7,3,'admin5','32b92f2864cada4f2e3056e2ecdac4ef','6a1aab2f-6b53-47fc-90ce-ddd057c0da2a',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-26 15:31:44','2017-10-27 11:15:36'),(8,2,'admin4','91243404dd46595e84f2d59ecdc4d08e','b0070f46-ff38-4c43-83eb-0f0d94cba132',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-26 15:36:15','2017-10-28 12:24:43'),(9,2,'admin3','2f35b10abb0a67879f44046554662edc','5f42b8af-a7cc-4601-8d7d-4a686d9767ce',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-26 15:38:41','2018-05-12 16:13:34'),(10,3,'admin99','e98cb23aa4675434681c35c047d88199','c36ae8e8-8a0e-4dda-afb8-614e7cbec6de',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-26 15:42:27','2018-05-12 16:11:01'),(11,3,'admin111','3a8932e2ea7a531ad08f9247188724d2','7ea5dfb5-c502-43cc-9902-dbb8afc873c2',NULL,NULL,0,NULL,0,NULL,NULL,0,'2017-10-26 15:50:09','2018-05-12 16:06:56'),(13,3,'vue-admin','fbe56e0a226294e7ba394bc2d993e379','0f825496-cafa-4fd0-8cd7-9a73225843d3',NULL,NULL,0,NULL,0,NULL,NULL,0,'2018-05-12 21:02:39','2018-05-13 21:26:09'),(14,3,'test','d313f4c6b94e6bc04a61ed8aa43f1ab1','0faf619c-2e45-445b-bb79-e92d2f6a6060',NULL,NULL,0,NULL,0,NULL,NULL,1,'2018-06-23 20:29:20','2018-06-23 20:34:44');

/*Table structure for table `t_user_menu` */

DROP TABLE IF EXISTS `t_user_menu`;

CREATE TABLE `t_user_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `menuId` bigint(20) NOT NULL,
  `createTime` datetime NOT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COMMENT='保存用户与菜单的管理关系数据';

/*Data for the table `t_user_menu` */

insert  into `t_user_menu`(`id`,`userId`,`menuId`,`createTime`,`modifyTime`) values (1,13,110000,'2018-05-12 21:28:36',NULL),(2,13,110100,'2018-05-12 21:28:36',NULL),(3,13,110101,'2018-05-12 21:28:36',NULL),(4,13,110102,'2018-05-12 21:28:36',NULL),(5,13,110200,'2018-05-12 21:28:36',NULL),(6,13,110201,'2018-05-12 21:28:36',NULL),(7,13,110202,'2018-05-12 21:28:36',NULL),(8,13,110300,'2018-05-12 21:28:36',NULL),(9,13,110301,'2018-05-12 21:28:36',NULL),(10,13,110302,'2018-05-12 21:28:36',NULL),(11,13,110400,'2018-05-12 21:28:36',NULL),(12,13,110401,'2018-05-12 21:28:36',NULL),(13,13,110402,'2018-05-12 21:28:36',NULL),(14,13,110403,'2018-05-12 21:28:36',NULL),(15,13,110404,'2018-05-12 21:28:36',NULL),(16,13,110405,'2018-05-12 21:28:36',NULL),(17,13,110406,'2018-05-12 21:28:36',NULL),(33,2,110100,'2018-06-10 20:47:57',NULL),(34,2,110101,'2018-06-10 20:47:57',NULL),(35,2,110102,'2018-06-10 20:47:57',NULL),(36,2,110000,'2018-06-10 20:47:57',NULL),(37,14,110100,'2018-06-23 20:30:27',NULL),(38,14,110101,'2018-06-23 20:30:27',NULL),(39,14,110102,'2018-06-23 20:30:27',NULL),(40,14,110000,'2018-06-23 20:30:27',NULL);

/*Table structure for table `t_user_role` */

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `userId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  `createTime` datetime NOT NULL,
  `modifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='保存用户与角色的关联关系数据';

/*Data for the table `t_user_role` */

insert  into `t_user_role`(`id`,`userId`,`roleId`,`createTime`,`modifyTime`) values (5,2,2,'2018-06-09 21:47:07',NULL),(6,2,3,'2018-06-09 21:47:07',NULL),(7,4,5,'2018-06-09 22:16:53',NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
