#### 官网地址

[官网地址](http://frsimple.cn)

#### 前端源码地址

[vue3](https://gitee.com/frsimple/view)

#### simple中后台框架交流微信群 **_(获取初始化脚本)_**

![输入图片说明](https://frsimple.cn/_nuxt/11662564543_.pic.34eebea0.jpg)

**群共享文件中提供初始化脚本和docker生产部署方案**

#### 演示地址

[vue3版本](http://vue.frsimple.cn)

#### 软件架构

基础框架：SpringBoot 2.7+

授权认证：Spring Security Oauth2.0 (支持自定义手机验证码登录)

高可用缓存：Redis

持久层：MyBatis Plus （支持动态数据源）

数据库连接池：Alibaba.druid

文件存储：minio

短信服务： 阿里云 ｜ 腾讯云

工具类： HuTool | SnowFlake | IdGenerator | SpringDoc

#### 开发部署

开发工具：idea
数据库版本： mysql8.0
redis版本： 5.0.14

- 启动redis

- 启动服务
  SimpleApplication

#### 目录结构

├─ simple-boot //父级工程
│ │ ├─ simple-base //基础公共模块
│ │ ├─ simple-center //系统管理业务功能
│ │ ├─ simple-start //springboot启动模块