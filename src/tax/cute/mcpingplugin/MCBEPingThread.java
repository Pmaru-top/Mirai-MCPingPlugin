package tax.cute.mcpingplugin;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;

import java.io.IOException;

public class MCBEPingThread extends Thread {
    Plugin plugin;
    String host;
    int port;
    Object object;
    public int status = -1;

    public MCBEPingThread(Plugin plugin, String host, int port, Object object) {
        this.host = host;
        this.port = port;
        this.object = object;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ping();
    }

    public void ping() {
        if(port == -1) port = 19132;
        BETypeset typeset;
        try {
            typeset = BETypeset.getTypeset(host,port,plugin.BETypesetText);
        } catch (Exception e) {
            status = 0;
            return;
        }
        if (object instanceof Group) {
            Group group = (Group) object;
            group.sendMessage(typeset.getMotdText());
        } else if (object instanceof Friend) {
            Friend friend = (Friend)object;
            friend.sendMessage(typeset.getMotdText());
        }
        status = 1;
    }
}