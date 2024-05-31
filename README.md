![](https://nepxion.github.io/Discovery//docs/discovery-doc/Banner.png)

# Discovery【探索】云原生微服务解决方案
![Total visits](https://visitor-badge.laobi.icu/badge?page_id=Nepxion&title=total%20visits)  [![Total lines](https://tokei.rs/b1/github/Nepxion/Discovery?category=lines)](https://tokei.rs/b1/github/Nepxion/Discovery?category=lines)  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license)](https://github.com/Nepxion/Discovery/blob/6.x.x/LICENSE)  [![Maven Central](https://img.shields.io/maven-central/v/com.nepxion/discovery.svg?label=maven)](https://search.maven.org/artifact/com.nepxion/discovery)  [![Javadocs](http://www.javadoc.io/badge/com.nepxion/discovery-plugin-framework-starter.svg)](http://www.javadoc.io/doc/com.nepxion/discovery-plugin-framework-starter)  [![Build Status](https://github.com/Nepxion/Discovery/workflows/build/badge.svg)](https://github.com/Nepxion/Discovery/actions)  [![Codacy Badge](https://app.codacy.com/project/badge/Grade/5c42eb719ef64def9cad773abd877e8b)](https://www.codacy.com/gh/Nepxion/Discovery/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Nepxion/Discovery&amp;utm_campaign=Badge_Grade)  [![Stars](https://img.shields.io/github/stars/Nepxion/Discovery.svg?label=Stars&style=flat&logo=GitHub)](https://github.com/Nepxion/Discovery/stargazers)  [![Stars](https://gitee.com/Nepxion/Discovery/badge/star.svg?theme=gvp)](https://gitee.com/Nepxion/Discovery/stargazers)

[![Wiki](https://badgen.net/badge/icon/wiki?icon=wiki&label=GitHub)](https://github.com/Nepxion/Discovery/wiki)  [![Wiki](https://badgen.net/badge/icon/wiki?icon=wiki&label=Gitee)](https://gitee.com/nepxion/Discovery/wikis/pages?sort_id=3993615&doc_id=1124387)  [![Discovery PPT](https://img.shields.io/badge/Discovery%20-ppt-brightgreen?logo=Microsoft%20PowerPoint)](https://nepxion.github.io/Discovery//docs/link-doc/discovery-ppt.html)  [![Discovery Page](https://img.shields.io/badge/Discovery%20-page-brightgreen?logo=Microsoft%20Edge)](https://nepxion.github.io/Discovery//)  [![Discovery Platform Page](https://img.shields.io/badge/Discovery%20Platform%20-page-brightgreen?logo=Microsoft%20Edge)](https://nepxion.github.io/Discovery/platform)  [![Polaris Page](https://img.shields.io/badge/Polaris%20-page-brightgreen?logo=Microsoft%20Edge)](http://polaris-paas.gitee.io/polaris-sdk)

<a href="https://github.com/Nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery//docs/icon-doc/github.png"></a>&nbsp;  <a href="https://gitee.com/Nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery//docs/icon-doc/gitee.png"></a>&nbsp;  <a href="https://search.maven.org/search?q=g:com.nepxion" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery//docs/icon-doc/maven.png"></a>&nbsp;  <a href="https://nepxion.github.io/Discovery//docs/contact-doc/wechat.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery//docs/icon-doc/wechat.png"></a>&nbsp;  <a href="https://nepxion.github.io/Discovery//docs/contact-doc/dingding.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery//docs/icon-doc/dingding.png"></a>&nbsp;  <a href="https://nepxion.github.io/Discovery//docs/contact-doc/gongzhonghao.jpg" tppabs="#" target="_blank"><img width="25" height="25" src="https://nepxion.github.io/Discovery//docs/icon-doc/gongzhonghao.png"></a>&nbsp;  <a href="mailto:1394997@qq.com" tppabs="#"><img width="25" height="25" src="https://nepxion.github.io/Discovery//docs/icon-doc/email.png"></a>

如果您觉得本框架具有一定的参考价值和借鉴意义，请帮忙在页面右上角 [**Star**]

## 简介

### 作者简介
- Nepxion开源社区创始人
- 2020年阿里巴巴中国云原生峰会出品人
- 2020年被Nacos和Spring Cloud Alibaba纳入相关开源项目
- 2021年阿里巴巴技术峰会上海站演讲嘉宾
- 2021年荣获陆奇博士主持的奇绩资本，进行风险投资的关注和调研
- 2021年入选Gitee最有价值开源项目
- 阿里巴巴官方书籍《Nacos架构与原理》作者之一
- Spring Cloud Alibaba Steering Committer、Nacos Group Member
- Spring Cloud Alibaba、Nacos、Sentinel、OpenTracing Committer & Contributor

<img src="https://nepxion.github.io/Discovery//docs/discovery-doc/CertificateGVP.jpg" width="43%"><img src="https://nepxion.github.io/Discovery//docs/discovery-doc/AwardNacos1.jpg" width="28%"><img src="https://nepxion.github.io/Discovery//docs/discovery-doc/AwardSCA1.jpg" width="28%">

### 商业合作
① Discovery系列

| 框架名称 | 框架版本 | 支持Spring Cloud版本 | 使用许可 |
| --- | --- | --- | --- |
| Discovery | 1.x.x ~ 6.x.x | Camden ~ Hoxton | 开源，永久免费 |
| DiscoveryX | 7.x.x ~ 10.x.x | 2020 ~ 2023 | 闭源，商业许可 |

② Polaris系列

Polaris为Discovery高级定制版，特色功能

- 基于Nepxion Discovery集成定制
- 多云、多活、多机房流量调配
- 跨云动态域名、跨环境适配
- DCN、DSU、SET单元化部署
- 组件灵活装配、配置对外屏蔽
- 极简低代码PaaS平台

| 框架名称 | 框架版本 | 支持Discovery版本 | 支持Spring Cloud版本 | 使用许可 |
| --- | --- | --- | --- | --- |
| Polaris | 1.x.x | 6.x.x | Finchley ~ Hoxton | 闭源，商业许可 |
| Polaris | 2.x.x | 7.x.x ~ 10.x.x | 2020 ~ 2023 | 闭源，商业许可 |

有商业版需求的企业和用户，请添加微信1394997，联系作者，洽谈合作事宜

### 入门资料
![](https://nepxion.github.io/Discovery//docs/discovery-doc/Logo64.png) Discovery【探索】企业级云原生微服务开源解决方案

① 快速入门
- [快速入门Github版](https://github.com/Nepxion/Discovery/wiki)
- [快速入门Gitee版](https://gitee.com/Nepxion/Discovery/wikis/pages)

② 解决方案
- [解决方案WIKI版](http://nepxion.com/discovery)
- [解决方案PPT版](https://nepxion.github.io/Discovery//docs/link-doc/discovery-ppt.html)

③ 最佳实践
- [最佳实践PPT版](https://nepxion.github.io/Discovery//docs/link-doc/discovery-ppt-1.html)

④ 平台界面
- [平台界面WIKI版](http://nepxion.com/discovery-platform)

⑤ 框架源码
- [框架源码Github版](https://github.com/Nepxion/Discovery)
- [框架源码Gitee版](https://gitee.com/Nepxion/Discovery)

⑥ 指南示例源码
- [指南示例源码Github版](https://github.com/Nepxion/DiscoveryGuide)
- [指南示例源码Gitee版](https://gitee.com/Nepxion/DiscoveryGuide)

⑦ 指南示例说明
- Spring Cloud Finchley ~ Hoxton版本
    - [极简版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-simple)，分支为6.x.x-simple
    - [极简版域网关部署指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-simple-domain-gateway)，分支为6.x.x-simple-domain-gateway
    - [极简版非域网关部署指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-simple-non-domain-gateway)，分支为6.x.x-simple-non-domain-gateway
    - [集成版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x)，分支为6.x.x
    - [高级版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/6.x.x-complex)，分支为6.x.x-complex
- Spring Cloud 202x版本
    - [极简版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/master-simple)，分支为master-simple
    - [极简版本地化指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/master-simple-native)，分支为master-simple-native
    - [集成版指南示例](https://github.com/Nepxion/DiscoveryGuide/tree/master)，分支为master

![](https://nepxion.github.io/Discovery//docs/polaris-doc/Logo64.png) Polaris【北极星】企业级云原生微服务商业解决方案

① 解决方案
- [解决方案WIKI版](http://nepxion.com/polaris)

② 框架源码
- [框架源码Github版](https://github.com/polaris-paas/polaris-sdk)
- [框架源码Gitee版](https://gitee.com/polaris-paas/polaris-sdk)

③ 指南示例源码
- [指南示例源码Github版](https://github.com/polaris-paas/polaris-guide)
- [指南示例源码Gitee版](https://gitee.com/polaris-paas/polaris-guide)

④ 指南示例说明
- Spring Cloud Finchley ~ Hoxton版本
    - [指南示例](https://github.com/polaris-paas/polaris-guide/tree/1.x.x)，分支为1.x.x
- Spring Cloud 202x版本
    - [指南示例](https://github.com/polaris-paas/polaris-guide/tree/master)，分支为master

### 功能概述
Nepxion Discovery Platform基于Nepxion Discovery 6.x.x版和Spring Cloud Hoxton版制作，也支持和兼容Spring Cloud Edgware版 ~ 202x版接入，支持如下功能

- 支持四个注册中心
- 支持六个配置中心
- 支持MySQL数据库和H2内存数据库，用户可以无缝扩展到其它数据库（例如，Oracle）
- 支持数据库方式登录和Ldap方式登录
- 支持Shiro和JWT的登录以及鉴权
- 支持管理员/角色/权限配置
- 支持页面配置，在线添加、删除、修改各类中间件主页或者业务系统主页的集成以及跳转
- 支持蓝绿灰度链路编排
    - 支持链路单写数据，采用类似Apollo版本控制模式，界面标识增/删/改标识，通过发布方式达到数据库和配置中心最终数据一致性
    - 支持版本和区域维度链路编排
- 支持蓝绿灰度混合发布
    - 支持蓝绿灰度策略双写数据库和配置中心，采用类似Apollo版本控制模式，界面标识增/删/改标识，通过发布方式达到数据库和配置中心最终数据一致性
    - 支持版本和区域维度蓝绿灰度
    - 支持蓝绿灰度策略启用/禁用模式
    - 支持蓝绿灰度策略多实例动态路由一致性检查
    - 支持网关、服务、组为入口
    - 支持全局兜底、蓝绿兜底、灰度兜底策略编排
    - 支持无限级蓝绿灰度策略编排
    - 支持自定义蓝绿条件策略
    - 支持蓝绿条件策略校验
    - 支持内置Header
- 支持双网关动态路由
    - 支持网关动态路由双写数据库和配置中心，采用类似Apollo版本控制模式，界面标识增/删/改标识，通过发布方式达到数据库和配置中心最终数据一致性
    - 支持网关动态路由启用/禁用模式
    - 支持网关动态路由多实例一致性检查
    - 支持Spring Cloud Gateway内置断言器（基于Path、Host、Header、Cookie、Query、Method、RemoteAddr、Weight等无代码方式）和过滤器（基于StripPrefix、PrefixPath、RewritePath、RequestRateLimiter、CircuitBreaker、AddRequestHeader、AddRequestParameter、AddResponseHeader、RedirectTo等无代码方式）
    - 支持用户自定义断言器和过滤器，可以实现类似Access Token、网页访问黑/白名单，自定义用户数据（List和Map结构）过滤等低代码方式
    - 支持Zuul网关内置动态路由
- 支持服务负载屏蔽的黑名单实例摘除
    - 支持黑名单双写数据库和配置中心，采用类似Apollo版本控制模式，界面标识增/删/改标识，通过发布方式达到数据库和配置中心最终数据一致性
    - 支持黑名单启用/禁用模式
    - 支持黑名单多实例一致性检查
    - 基于时间戳前缀的全局唯一ID黑名单
    - 基于IP地址和端口黑名单
- 支持界面显示所连的注册中心和配置中心

### 郑重致谢
感谢如下小伙伴参与本平台的开发、测试和部署。下面名单根据加入次序进行排序
- 张宁
- 付向阳
- 刘辉
- 赵胜杰
- 任学会
- 肖龙
- 伊安娜

### 请联系我
微信、钉钉、公众号和文档

![](https://nepxion.github.io/Discovery//docs/contact-doc/wechat-1.jpg)![](https://nepxion.github.io/Discovery//docs/contact-doc/dingding-1.jpg)![](https://nepxion.github.io/Discovery//docs/contact-doc/gongzhonghao-1.jpg)![](https://nepxion.github.io/Discovery//docs/contact-doc/document-1.jpg)

## Star走势图
[![Stargazers over time](https://starchart.cc/Nepxion/Discovery.svg)](https://starchart.cc/Nepxion/Discovery)