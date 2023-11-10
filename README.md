
# 胡说八道
> ------------------------------------------------------------------------------------------------

# 消息通知模块


后面可以直接引入微服务作为一个模块

目前实现了系统消息推送，和获取消息（还需要优化）



## 思路
如果有消息，就保存到数据库（mongodb）

**实时**

发送到websocket判断是否在线，如果在线了就直接实时发送，然后保存到数据库中，
如果是


主模块创建一个websocket

发送消息保存到数据库的时候，先在主模块判断用户是否在线，如果在线那么就实时推送

如果消息模块有新的消息，那么推送到主模块的websocket发送给用户

## 消息思路

用户登录-> 查询数据库获取未读消息保存到stroage中->如果有新消息发送给用户，那么就通过onmessage发送给用户，然后用户继续更新stroage里面对应的未读消息，然后将消息写入数据库
-> 如果用户读取消息，那么就把storage里面的消息删除，然后数据库更新全部消息都为已读

主页通过storage里面是否有消息来查看是否需要有消息的提示

前端如果有新消息发送给用户，如果用户刚刚好在那个接收消息的界面，那么就直接不用更新对应storage，然后刷新消息全部为已读



























## 最新
用户登录的时候，会获取是否是否有未读消息，如果有未读消息，那么消息那里就有红点
后面如果有新消息，就判断如果所有消息都未读，那么就发送一个ws请求给前端将消息设置为红

如果请求消息页面，那么会按照时间查询对应的会话，查询的数组里面包含用户名字，用户头像，最新消息，所有未读消息数量

如果用户有发送消息，如果页面正好是在消息页面，那么会实时更新对应会话的最新消息和未读条数
如果发送消息的时候，用户刚刚好在对应会话，那么直接清空所有未读消息数量

如果用户点进某个会话，那么就直接清空所有的未读消息
