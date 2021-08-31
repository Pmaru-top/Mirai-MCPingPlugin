package tax.cute.mcpingplugin;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;
import tax.cute.mcpingplugin.util.*;

import java.io.IOException;
import java.util.List;

public class MCJEPingThread extends Thread {
    Plugin plugin;
    String host;
    int port;
    Object sendObject;
    public int status = -1;

    public MCJEPingThread(Plugin plugin, String host, int port, Object sendObject) {
        this.host = host;
        this.port = port;
        this.sendObject = sendObject;
        this.plugin = plugin;
    }

    @Override
    public void run() {
        ping();
    }

    public void ping() {
        if (port == -1) port = 25565;

        if (sendObject instanceof Group) {
            Group group = (Group) sendObject;
            Srv srv = Srv.getSrv(host,Util.MC_SRV);
            if (srv != null) {
                host = srv.getSrvHost();
                port = srv.getSrvPort();
                group.sendMessage("检测到存在Srv记录 已自动跳转到\n>>\n" + host + ":" + port);
            }
            JETypeset typeset;
            try {
                //获取信息并排版
                typeset = JETypeset.getTypeset(host, port, plugin.JETypesetText);
            } catch (Exception e) {
                this.status = 0;
                return;
            }

            //是否发送图标(如果要求发送的话)
            if (typeset.getFavicon_bytes() != null) {
                Image image = group.uploadImage(ExternalResource.create(typeset.getFavicon_bytes()));
                group.sendMessage(image.plus(typeset.getMotdText()));
            } else {
                group.sendMessage(typeset.getMotdText());
            }

            //发送ModList(如果要求发送的话)
            //不存在直接结束
            if (typeset.getModList() == null) return;
            if (typeset.getModList().size() < 1) return;
            //构建合并转发聊天记录
            ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
            List<String> modList = typeset.getModList();
            int count = 0;
            for (int i = 0; i < modList.size(); i++) {
                count++;
                builder.add(group.getBot().getId(), "Mod" + (i + 1), new PlainText(modList.get(i)));
                //等于100才发送,超过100发不了
                if (count == 100) {
                    count = 0;
                    group.sendMessage(builder.build());
                    builder = new ForwardMessageBuilder(group);
                }
                if (count > 100) {
                    group.sendMessage("程序遇到量子异常,程序无法定位此异常,请联系外星人");
                    return;
                }
                //大于500停止发送,防止刷屏
                if (i > 500) {
                    group.sendMessage("Mod数量大于500,出于安全策略,无法查看更多(什么量子服会装500个Mod?)");
                    return;
                }
            }
            //即使小于100,最后也会发送
            if (count > 0) group.sendMessage(builder.build());
            status = 1;
        }

        if (sendObject instanceof Friend) {
            Friend friend = (Friend) sendObject;
            Srv srv = Srv.getSrv(host, Util.MC_SRV);
            if (srv != null) {
                host = srv.getSrvHost();
                port = srv.getSrvPort();
                friend.sendMessage("检测到存在Srv记录 已自动跳转到\n>>\n" + host + ":" + port);
            }

            JETypeset typeset;
            try {
                //获取信息并排版
                typeset = JETypeset.getTypeset(host, port, plugin.JETypesetText);
            } catch (Exception e) {
                this.status = 0;
                return;
            }

            //是否发送图标(如果要求发送的话)
            if (typeset.getFavicon_bytes() != null) {
                Image image = friend.uploadImage(ExternalResource.create(typeset.getFavicon_bytes()));
                friend.sendMessage(image.plus(typeset.getMotdText()));
            } else {
                friend.sendMessage(typeset.getMotdText());
            }

            //发送ModList(如果要求发送的话)
            //不存在直接结束
            if (typeset.getModList() == null) return;
            if (typeset.getModList().size() < 1) return;
            //构建合并转发聊天记录
            ForwardMessageBuilder builder = new ForwardMessageBuilder(friend);
            List<String> modList = typeset.getModList();
            int count = 0;
            for (int i = 0; i < modList.size(); i++) {
                count++;
                builder.add(friend.getBot().getId(), "Mod" + (i + 1), new PlainText(modList.get(i)));
                //等于100才发送,超过100发不了
                if (count == 100) {
                    count = 0;
                    friend.sendMessage(builder.build());
                    builder = new ForwardMessageBuilder(friend);
                }
                if (count > 100) {
                    friend.sendMessage("程序遇到量子异常,程序无法定位此异常,请联系外星人");
                    return;
                }
                //大于500停止发送,防止刷屏
                if (i > 500) {
                    friend.sendMessage("Mod数量大于500,出于安全策略,无法查看更多(什么量子服会装500个Mod?)");
                    return;
                }
            }
            //即使小于100,最后也会发送
            if (count > 0) friend.sendMessage(builder.build());
            status = 1;
        }
    }
}