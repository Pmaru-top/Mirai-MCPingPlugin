package tax.cute.mcpingplugin;

import net.mamoe.mirai.console.command.java.JSimpleCommand;

import java.io.IOException;
import java.util.List;

public class Console extends JSimpleCommand {
    Plugin plugin;
    public Console(Plugin plugin) {
        super(plugin,"lp",new String[]{}, plugin.getParentPermission());
        this.plugin = plugin;
    }

    @Handler
    public void command(String operation,long args) {
        try {
            switch (operation.toLowerCase()) {
                case "add":
                    plugin.config.addOwner(args);
                    plugin.getLogger().info("已将 " + args + "添加为主人");
                    break;

                case "remove":
                    plugin.config.removeOwner(args);
                    plugin.getLogger().info(args + "不再是主人了");
                    break;

                default:
                    plugin.getLogger().info("Usage:lp [add/remove/list] [qqNum]");
            }
        } catch (IOException e) {
            plugin.getLogger().info("读写配置时出现异常\n" + e);
        }
    }

    @Handler
    public void command() {
        plugin.getLogger().info("Usage:lp [add/remove/list] [qqNum]");
    }

    @Handler
    public void command(String operation) {
        if (!operation.equalsIgnoreCase("list")) {
            plugin.getLogger().info("Usage:lp [add/remove/list] [qqNum]");
            return;
        }

        List<Long> owners = plugin.config.getOwner();
        plugin.getLogger().info("主人:");
        for (Long i:owners) {
            plugin.getLogger().info(String.valueOf(i));
        }

    }
}