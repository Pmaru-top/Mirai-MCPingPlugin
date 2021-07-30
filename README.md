# Mirai-MCPingPlugin
用Mirai机器人获取MC服务器信息并发送

这是一个做的很屑的[`Mirai`](https://github.com/mamoe/mirai)机器人插件

本`README`适用于`MCPing2.0`

欲使用[`MCPing1.0`](https://github.com/MX233/Mirai-MCPingPlugin/releases/tag/v1.0-Beta-lib) 请翻阅老版本README或[翻阅源码](https://github.com/MX233/Mirai-MCPingPlugin/archive/refs/tags/v1.0-Beta.zip)

# 主要功能
- `获取Minecraft服务器信息`
- `群绑定服务器`

###笔者的一些话
翻阅1.0源码可以发现 一堆命令堆在一个类里 非常乱
为此笔者使用了[MrXiaoM/MiraiUtils](https://github.com/MrXiaoM/MiraiUtils) 的命令系统( 非常感谢大佬开源 )
并重制发布了`MCPing2.0`

# 指令和介绍
所有指令不分大小写

**使用本插件,机器人会在`群聊信息`或`好友私聊信息`中响应以下命令**
***
- `/mcping <域名/IP>` bot会发送该MC(`JE` `BE`)服务器的MOTD(所有人可用)

使用示例:`/mcping mc.hypixel.net`

另有一个指令为`/mcMotd`的命令(和MCPing一样) 

你可以通过/mcPingCmd set <命令>修改它 

PS:支持`Srv`和`中文域名`

***
- `/lp`(主人可用)
     * `list` 获取主人列表
     * `add` `<qq号>` 添加主人
     * `remove` `<qq号>` 移除主人
     
     使用示例:`/lp add 114514`

当机器人没有主人时,向机器人私聊发送:`/getOwner <Pin码>`机器人回复:"你已成为主人",你便获得权限

Pin码是什么? Pin码是笔者为了提高用户安全系数新增的功能

Pin码在data\MCPing\Pin.txt 里面文本的数字就是Pin码
***

- `/enable set <布尔值>`(主人可用)

使用示例:`/enable set true`

可用该命令启用或停用本插件功能

停用后 只有/enable可用
***
- `/reload`(主人可用)

使用该命令后,插件会重新读取配置文件载入到内存中
***
- `/bindServer`(主人可用)
     * `list` 获取绑定列表
     * `add` `<群号> ` `命令` `域名/IP` 绑定一个服务器到群
     * `remove` `<群号>` 解绑一个群
     * `remove all` 解绑所有群
     
     PS:可用"this"代指当前群
     
     比如你在群号为114514的群发送:/bindServer add this /status mc.hypixel.net
     
     机器人回复"绑定成功 可在该群发送 /status 获取mc.hypixel.net的信息"就说明绑定成功了
     
     当有人在群号为`114514`(刚刚那个)发送 `/status` 机器人就会发送`mc.hypixel.net`的服务器信息
     
     这点非常适合服主架设在自己的玩家群方便查询
         
     ## 用到的依赖
     [`MX233/MinecraftServerPing`](https://github.com/MX233/MinecraftServerPing/releases/tag/MinecraftServerPingV1.0)
     
     [`MX233/MinecraftServerPing-BE`](https://github.com/MX233/MinecraftServerPing-BE)
     
     [`alibaba/fastjson`](https://github.com/alibaba/fastjson)
     
     如果你不会寻找和调用这些`lib` 请下载带依赖版本(命名带-lib)
***
# 可自定义性
你可以在`data\MCPing`下发现名称为`typeset.txt`和`Betypeset.txt`的文件,里面的文本是笔者预先设定的排版

`typeset.txt`为java版的Motd排版

`Betypeset.txt`为BE版的Motd排版

你可以~~~自♂由~~~排版

~~用记事本打开会乱 这玩意没格式化,建议用专业点的工具比如VSC~~

弄乱了不会弄回去可以删掉`typeset.txt`或`Betypeset.txt`然后`/reload`重载恢复
***
Java版Motd变量如下

- `%favicon` 表示发送图标,不加则不发送
- `%description` 描述文本
- `%version_name` 版本名称
- `%version_protocol` 版本协议号
- `%online_players` 在线玩家数量
- `%max_players` 最大玩家数量
- `%delay` 延迟(ms)
- `%type` 类型(服务端)
- `%mod_count` mod数量
- `%mod_list` 模组列表 (相对1.0版本,此版本已改用合并转发聊天记录 理论上不会刷屏)
***
BE版Motd变量如下
- `%type` 类型(服务端)
- `%delay` 延迟(ms)
- `%version` 版本
- `%description` 描述
- `%default_mode` 默认模式
- `%protocol_num` 协议号
- `%online_players` 在线玩家数量
- `%max_players` 最大玩家数量
- `%world_name` 世界名称(仅BDS可用)
