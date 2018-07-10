use mysql;
select host, user from user;
-- 因为mysql版本是5.7，因此新建用户为如下命令：
create user vain identified by '123456';
-- 将vain数据库的权限授权给创建的vain用户，密码为123456：
GRANT ALL PRIVILEGES ON *.* TO 'vain'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
-- 这一条命令一定要有：
flush privileges;