# LiteIM-App
LiteIM安卓端
## 简介
LiteIM是一个即时聊天应用, 实现了包括 普通文本、表情、图片、语音的
单聊/群聊, 离线消息的拉取,未读消息的小红点提示, 消息历史记录的查看；
多图/视频+文字动态的发布，预览;用户、群组、动态的搜索，群组成员的拉取，群解散。
### 技术栈
使用Netty作为脚手架，在TCP协议的基础上做定制，实现了连接的登录认证、断线重连、心跳包的构建
与消费、空闲检测等。

Android端整体使用了MVP架构，主要使用了RxJava + RxAndroid + RxBus + Retrofit

项目部分截图如下

|  |  |
| --- | --- |
|![chat1](https://github.com/Xchuanshuo/LiteIM-App/blob/master/images/chat_1.png)| ![chat1](https://github.com/Xchuanshuo/LiteIM-App/blob/master/images/chat_2.png) |

|![dynamic](https://github.com/Xchuanshuo/LiteIM-App/blob/master/images/dyanmic.png) | ![message](https://github.com/Xchuanshuo/LiteIM-App/blob/master/images/message.png) |

