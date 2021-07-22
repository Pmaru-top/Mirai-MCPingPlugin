package tax.cute.mcpingplugin;

public class Menu {
    public static String mcPingMenu() {
        return " [ MCPing ] " +
                "\n可获取MC(JE)服务器的MOTD" +
                "\n使用方法:/mcping <域名/IP>" +
                "\n支持Srv和中文域名"
                ;
    }

    public static String ownerMenu() {
        return "/by" +
                "\n绑定服务器等命令需主人权限" +
                "\n参数:" +
                "\nowner list -- 查看主人列表" +
                "\nowner add <qq号码> -- 添加主人" +
                "\nowner remove <qq号码> -- 移除主人";
    }

    public static String bindServerMenu() {
        return
                "本功能可以让你的机器人" +
                        "\n一个群MCPing绑定一个地址" +
                        "\n在绑定的群输入主人设置的命令机器人自动发送Motd" +
                        "\n/bindServer" +
                        "\n参数:" +
        "\nadd <群号> <命令> <Host> -- 绑定一个服务器" +
                "\nremove <群号> -- 解绑服务器" +
                        "\nremove all -- 清空所有绑定" +
                        "\nPS:\"this\"可代指本群";
    }

    public static String menu() {
        return "[ MCPing ] " +
                "\n/MCPing -- 查看帮助" +
                "\n/by -- 查看帮助" +
                "\n/bindServer -- 查看帮助" +
                "\n/enable set <布尔值>--开启或禁用机器人" +
                "\n/cmd set <命令> -- 修改mcping功能指令" +
                "\n/reload -- 重载配置";
    }
}
