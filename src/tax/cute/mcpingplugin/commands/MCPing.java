package tax.cute.mcpingplugin.commands;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.SingleMessage;
import tax.cute.mcpingplugin.*;
import tax.cute.mcpingplugin.util.Util;
import tax.cute.minecraftserverping.Punycode;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderFriend;
import top.mrxiaom.miraiutils.CommandSenderGroup;

import java.util.Timer;
import java.util.TimerTask;

public class MCPing extends CommandModel {
    Plugin plugin;

    public MCPing(Plugin plugin) {
        super("mcping");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if (!plugin.config.isEnable()) return;

        if (sender instanceof CommandSenderGroup) {
            CommandSenderGroup senderGroup = (CommandSenderGroup) sender;
            Group group = senderGroup.getGroup();
            if (args[0].contentToString().equalsIgnoreCase("/mcping") || args.length != 1 || args[0].contentToString().isEmpty()) {
                group.sendMessage(Util.MENU);
                return;
            }

            String host = args[0].contentToString();

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

            sendMCPing(plugin, group, ip, port);
        }

        if (sender instanceof CommandSenderFriend) {
            CommandSenderFriend senderFriend = (CommandSenderFriend) sender;
            Friend friend = senderFriend.getFriend();
            if (args[0].contentToString().equalsIgnoreCase("/mcping") || args.length != 1 || args[0].contentToString().isEmpty()) {
                friend.sendMessage(Util.MENU);
                return;
            }

            String host = args[0].contentToString();

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

            sendMCPing(plugin, friend, ip, port);
        }
    }

    public static void sendMCPing(Plugin plugin, Object sendObject, String ip, int port) {
        if (sendObject instanceof Group) {
            Group group = (Group) sendObject;
            MCJEPingThread je = new MCJEPingThread(plugin, ip, port, group);
            MCBEPingThread be = new MCBEPingThread(plugin, ip, port, group);

            je.start();
            be.start();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (be.status == 0 && je.status == 0)
                        group.sendMessage("查询失败 请检查服务器是否开启");
                    je.stop();
                    be.stop();
                }
            }, 3000);
        } else if (sendObject instanceof Friend) {
            Friend friend = (Friend) sendObject;
            MCJEPingThread je = new MCJEPingThread(plugin, ip, port, friend);
            MCBEPingThread be = new MCBEPingThread(plugin, ip, port, friend);

            je.start();
            be.start();

            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (be.status == 0 && je.status == 0)
                        friend.sendMessage("查询失败 请检查服务器是否开启");
                    je.stop();
                    be.stop();
                }
            }, 3000);
        }
    }
}