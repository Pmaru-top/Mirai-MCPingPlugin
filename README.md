# Mirai-MCPingPlugin
用Mirai机器人获取MC服务器信息并发送

这是一个做的很屑的[`Mirai`](https://github.com/mamoe/mirai)机器人插件

# 主要功能
- `获取Minecraft服务器信息`
- `群绑定服务器`

# 指令和介绍
所有指令不分大小写

**使用本插件,机器人会在`群聊信息`或`好友私聊信息`中响应以下命令**
***
- `/mcping <域名/IP>` bot会发送该MC(JE)服务器的MOTD(所有人可用)

使用示例:`/mcping mc.hypixel.net`

可通过/cmd set <命令> 修改该指令

PS:支持`Srv`和`中文域名`

***
- `/by owner`(主人可用)
     * `list` 获取主人列表
     * `add` `<qq号>` 添加主人
     * `remove` `<qq号>` 移除主人
     
     使用示例:`/by owner add 114514`

PS:当机器人没有主人时,向机器人私聊发送:`/getOwner`机器人回复:"你已成为主人",你便获得权限
***

- `/enable set <布尔值>`(主人可用)

使用示例:`/enable set true`

可用该命令启用或停用本插件功能
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
     
     机器人回复"已将 mc.hypixel.net 绑定到 114514 "就说明绑定成功了
     
     当有人在群号为`114514`(刚刚那个)发送 `/status` 机器人就会发送`mc.hypixel.net`的服务器信息
     
     这点非常适合服主架设在自己的玩家群方便查询
     
     ## 用到的依赖
     [`MX233/MinecraftServerPing`](https://github.com/MX233/MinecraftServerPing/releases/tag/MinecraftServerPingV1.0)
     
     [`alibaba/fastjson`](https://github.com/alibaba/fastjson)
     
     百度`java` `-cp`让本插件能使用这些库即可
***
#可自定义性
你可以在Mirai框架`data\MCPing`下发现一个叫`typeset.txt`的文件,里面的文本是笔者预先设定的排版

你可以~~~自♂由~~~排版

~~用记事本打开会乱 这玩意没格式化,建议用专业点的工具比如VSC~~

弄乱了不会弄回去可以删掉`typeset.txt`然后`/reload`重载恢复

变量如下

- `%favicon` 表示发送图标,不加则不发送
- `%description` 描述文本
- `%version_name` 版本名称
- `%version_protocol` 版本协议号
- `%online_players` 在线玩家数量
- `%max_players` 最大玩家数量
- `%delay` 延迟(ms)
- `%type` 服务器类型(核心)
- `%mod_count` mod数量
- `%mod_list` 模组列表 (`慎用此变量`,一般mod服会装很多mod,可能会造成刷屏)
