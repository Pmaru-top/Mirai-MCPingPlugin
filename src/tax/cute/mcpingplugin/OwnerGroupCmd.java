package tax.cute.mcpingplugin;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.PlainText;

import java.io.IOException;

public class OwnerGroupCmd extends SimpleListenerHost {
    Plugin plugin;

    public OwnerGroupCmd(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private ListeningStatus onMonitor(GroupMessageEvent event) {
        long qqNum = event.getSender().getId();
        long groupNum = event.getGroup().getId();
        String msg;
        if (plugin.config.isOwner(qqNum)) {
            msg = event.getMessage().contentToString();
        } else {
            msg = "";
        }

        try {
            if (msg.toLowerCase().startsWith("/enable set ")) {
                String[] args = msg.split(" ");
                String bArgs = args[2];
                if (Util.isBoolean(bArgs)) {
                    plugin.config.setEnable(Boolean.parseBoolean(bArgs));
                    event.getSubject().sendMessage(plugin.name + "enable已修改为" + bArgs);
                } else {
                    event.getSubject().sendMessage(plugin.name + "类型有误(需要boolean)");
                }
            }
            //Get bindServer list function
            if(plugin.config.isEnable()) {
                if (msg.startsWith("/")) {
                    if (msg.equalsIgnoreCase("/bindServer list")) {
                        if (plugin.config.getBindServerList().size() > 0) {
                            ForwardMessageBuilder builder = new ForwardMessageBuilder(event.getGroup());
                            for (int i = 0; i < plugin.config.getBindServerList().size(); i++) {
                                JSONObject server = plugin.config.getBindServerList().getJSONObject(i);
                                StringBuilder sb = new StringBuilder();
                                sb
                                        .append(server.getString("GroupNum"))
                                        .append(":")
                                        .append(server.getString("CMD"))
                                        .append(":")
                                        .append(server.getString("Host"));
                                builder.add(event.getBot().getId(), String.valueOf(i), new PlainText(sb));
                            }
                            event.getSubject().sendMessage(builder.build());
                        } else {
                            event.getSubject().sendMessage(plugin.name + "绑定服务器列表为空");
                        }
                    }

                    if (msg.toLowerCase().startsWith("/bindserver add ")) {
                        String[] args = msg.split(" ");
                        long num;
                        //"this" to the current group
                        if (args[2].equalsIgnoreCase("this")) {
                            num = groupNum;
                        } else {
                            num = Long.parseLong(args[2]);
                        }

                        //1.group 2.cmd 3.host
                        if (plugin.config.addBindServer(num, args[3], args[4])) {
                            event.getSubject().sendMessage(plugin.name + "已将 " + args[4] + " 绑定到 " + num);
                        } else {
                            event.getSubject().sendMessage(plugin.name + num + "已绑定,无需重复绑定");
                        }
                    }

                    if (msg.equalsIgnoreCase("/bindserver remove all")) {
                        int count = plugin.config.removeAllBindServer();
                        event.getSubject().sendMessage(plugin.name + "已清空所有绑定服务器(" + count + "个)");
                    }else if (msg.toLowerCase().startsWith("/bindserver remove ")) {
                        long num;
                        String[] args = msg.split(" ");
                        if (args[2].equalsIgnoreCase("this")) {
                            num = groupNum;
                        } else {
                            num = Long.parseLong(args[2]);
                        }

                        if (plugin.config.removeBindServer(num)) {
                            event.getSubject().sendMessage(plugin.name + "已将绑定 " + num + " 的服务器移除");
                        } else {
                            event.getSubject().sendMessage(plugin.name + num + "未绑定服务器");
                        }
                    }

                    if (msg.toLowerCase().startsWith("/by owner add ")) {
                        String[] args = msg.split(" ");
                        long num = Long.parseLong(args[3]);
                        if (plugin.config.addOwner(num)) event.getSubject().sendMessage(plugin.name + "已将" + num + "添加为主人");
                        else event.getSubject().sendMessage(plugin.name + "添加失败:" + num + "已是主人,无需重复添加");
                    }

                    if (msg.toLowerCase().startsWith("/by owner remove ")) {
                        String[] args = msg.split(" ");
                        long num = Long.parseLong(args[3]);
                        if (plugin.config.removeOwner(num)) event.getSubject().sendMessage(plugin.name + "已将" + num + "移除主人");
                        else event.getSubject().sendMessage(plugin.name + "移除失败:" + num + "不是主人");
                    }

                    if (msg.equalsIgnoreCase("/by owner list")) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < plugin.config.getOwner().size(); i++) {
                            sb.append(plugin.config.getOwner().getString(i)).append("\n");
                        }
                        event.getSubject().sendMessage(plugin.name + "主人:\n" + sb);
                    }

                    if (msg.toLowerCase().startsWith("/cmd set ")) {
                        String[] args = msg.split(" ");
                        plugin.config.setMcpingCMD(args[2]);
                        event.getSubject().sendMessage(plugin.name + "mcping命令指令已更改为:" + args[2]);
                    }

                    if (msg.toLowerCase().startsWith("/reload")) {
                        plugin.config = Config.getConfig(plugin.configFilePath);
                        plugin.typesetText = Util.readText(plugin.typesetFilePath,"GBK");
                        event.getSubject().sendMessage("已完成重载");
                    }

                    if (msg.equalsIgnoreCase("/menu")) event.getSubject().sendMessage(Menu.menu());

                    if(msg.equalsIgnoreCase("/by")) event.getSubject().sendMessage(Menu.ownerMenu());

                    if(msg.equalsIgnoreCase("/bindServer")) event.getSubject().sendMessage(Menu.bindServerMenu());
                }
            }

        } catch (IOException e) {
            event.getSubject().sendMessage(plugin.name + "读写配置文件时遇到了错误" + e);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            event.getSubject().sendMessage(plugin.name + "参数输入错误");
        }

        return ListeningStatus.LISTENING;
    }

}
