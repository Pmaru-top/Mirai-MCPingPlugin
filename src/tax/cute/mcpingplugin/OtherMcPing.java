package tax.cute.mcpingplugin;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import tax.cute.mcpingplugin.commands.MCPing;
import tax.cute.minecraftserverping.Punycode;

public class OtherMcPing extends SimpleListenerHost {
    Plugin plugin;
    public OtherMcPing(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public ListeningStatus onGroup(GroupMessageEvent event) {
        if(!plugin.config.isEnable()) return ListeningStatus.LISTENING;
        Group group = event.getGroup();
        String msg = event.getMessage().contentToString();
        String cmd = plugin.config.getMcPingCmd();
        if(!msg.toLowerCase().startsWith(cmd)) return ListeningStatus.LISTENING;
        String host = msg.split(" ")[1];

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
        return ListeningStatus.LISTENING;
    }

    @EventHandler
    public ListeningStatus onFriend(FriendMessageEvent event) {
        if(!plugin.config.isEnable()) return ListeningStatus.LISTENING;
        Friend friend = event.getFriend();
        String msg = event.getMessage().contentToString();
        String cmd = plugin.config.getMcPingCmd();
        if(!msg.toLowerCase().startsWith(cmd)) return ListeningStatus.LISTENING;
        String host = msg.split(" ")[1];

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
        MCPing.sendMCPing(plugin,friend,ip,port);
        return ListeningStatus.LISTENING;
    }
}