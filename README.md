# vain-admin
[![](https://img.shields.io/badge/version-1.0-brightgreen.svg)](https://github.com/vainhope/vain-admin)

#### 简介：
 vain-admin 是一个no-session后端脚手架，它基于 vueAdmin-template 和 Spring Boot 2.0
 它使用了最新的前后端技术栈，动态路由，权限验证，搭建后台产品原型。
 支持动态权限控制，后端生成路由，Token验证，spring boot admin监控等  
 采用docker部署，部署方便  

 前端脚手架来自于[vueAdmin-template](https://github.com/PanJiaChen/vueAdmin-template)
 
#### 前端使用技术
 - ES2015+
 - vue
 - vuex 
 - vue-router 
 - axios 
 - element-ui
 
#### 后端使用技术
 - Spring Boot 2.0
 - Shiro
 - MyBatis
 - Redis
 - Druid
 - Json Web Token(JWT)
 - MySQL
 - Quartz
 - Maven
 - Nginx
 - docker
 - Spring Boot Admin 
 - aliyun-OSS
 - Lombok
 - Elasticsearch

#### 本地项目运行
 web 分支为前端项目源码  
 推荐使用Visual Studio Code编辑项目  
 > npm install 
 
 > npm run dev
 
 前端项目默认为8088端口  
 运行web项目
 按照nginx/conf配置本地的nginx配置  
 访问本地环境访问http://127.0.0.1/  
  ![vain-admin登录](https://github.com/vainhope/vain-admin/raw/master/screenshots/vain-admin.png)
  ![vain-admin配置](https://github.com/vainhope/vain-admin/raw/master/screenshots/vain-admin-config.png)
  ![vain-admin日志](https://github.com/vainhope/vain-admin/raw/master/screenshots/vain-admin-log.png)
  ![vain-admin菜单](https://github.com/vainhope/vain-admin/raw/master/screenshots/vain-admin-menu.png)
  ![vain-admin定时](https://github.com/vainhope/vain-admin/raw/master/screenshots/vain-admin-schedule.png)
  ![vain-admin用户](https://github.com/vainhope/vain-admin/raw/master/screenshots/vain-admin-user.png)

#### 打包静态文件
 现将config下的prod.env.js中BASE_API修改为改动后的IP地址  
 在web分支中运行
 >  npm run build prod.env  
 
 将生成后的dist文件拷贝到nginx的dist的目录下

#### docker环境运行
 安装完成docker,docker-compose  
 将application.yml修改为application-docker.yml  
 或者运行SpringBoot时候指定配置文件  
 在项目根目录下运行  
 > docker-compose up -d
 
 访问http://127.0.0.1/即可  
 登录账号为admin 密码为a12345  
 Spring Boot Admin 的访问地址为http://127.0.0.1:8080
 更多SpringBootAdmin功能请参考[vueAdmin-template](https://github.com/codecentric/spring-boot-admin)
 ![SpringBootAdmin界面](https://github.com/vainhope/vain-admin/raw/master/screenshots/admin.png)
 ![SpringBootAdmin Debug界面](https://github.com/vainhope/vain-admin/raw/master/screenshots/admin-debug.png)
 ![SpringBootAdmin 详情界面](https://github.com/vainhope/vain-admin/raw/master/screenshots/admin-detail.png)
 ![SpringBootAdmin 日志界面](https://github.com/vainhope/vain-admin/raw/master/screenshots/admin-logfile.png)
 ![SpringBootAdmin 线程界面](https://github.com/vainhope/vain-admin/raw/master/screenshots/admin-thread.png)

