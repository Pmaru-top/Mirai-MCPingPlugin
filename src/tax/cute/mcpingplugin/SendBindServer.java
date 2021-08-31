package tax.cute.mcpingplugin;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import tax.cute.mcpingplugin.commands.MCPing;
import tax.cute.minecraftserverping.Punycode;

public class SendBindServer extends SimpleListenerHost {
    Plugin plugin;
    public SendBindServer(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private ListeningStatus awa(GroupMessageEvent event) {
        if(!plugin.config.isEnable()) return ListeningStatus.LISTENING;
        long groupNum = event.getGroup().getId();
        if(!plugin.config.isBindServer(groupNum)) return ListeningStatus.LISTENING;
        Server server = plugin.config.getServer(groupNum);
            String msg = event.getMessage().contentToString();
            Group group = event.getGroup();
            String cmd = server.getCmd();
            if (msg.equalsIgnoreCase(cmd)) {
                String host = server.getHost();

                String ip;
                int port;
                if (host.contains(":")) {
                    ip = host.split(":")[0];
                    port = Integer.parseInt(host.split(":")[1]);
                } else {
                    ip = host;
                    port = -1;
                }

                //中文域名转码
                ip = Punycode.encodeURL(ip);
                MCPing.sendMCPing(plugin,group,ip,port);
            }
        return ListeningStatus.LISTENING;
    }
}
