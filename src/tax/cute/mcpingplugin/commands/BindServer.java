package tax.cute.mcpingplugin.commands;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.ForwardMessageBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import tax.cute.mcpingplugin.Plugin;
import tax.cute.mcpingplugin.Server;
import tax.cute.mcpingplugin.util.Util;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderFriend;
import top.mrxiaom.miraiutils.CommandSenderGroup;

import java.io.IOException;
import java.util.List;

public class BindServer extends CommandModel {
    Plugin plugin;

    public BindServer(Plugin plugin) {
        super("bindServer");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if (!plugin.config.isEnable()) return;
        if (!plugin.config.isOwner(sender.getSenderID())) return;

        if (sender instanceof CommandSenderGroup) {
            CommandSenderGroup senderGroup = (CommandSenderGroup) sender;
            Group group = senderGroup.getGroup();
            try {
                if (args[0].contentToString().equalsIgnoreCase("/bindServer")) {
                    group.sendMessage(
                            "# 用法" +
                                    "\n# /bindServer add [群号] [命令] [地址] 添加一个绑定" +
                                    "\n# /bindServer remove [群号] 移除一个绑定" +
                                    "\n# /bindServer list 查看绑定列表" +
                                    "\n# ps:可用\"this\"代指本群 移除时可用all代指所有群"
                    );
                    return;
                }
                if (args[0].contentToString().equalsIgnoreCase("add")) addBind(args, group);
                if (args[0].contentToString().equalsIgnoreCase("remove")) removeBind(args, group);
                if (args[0].contentToString().equalsIgnoreCase("list")) getBind(group);
            } catch (IOException e) {
                group.sendMessage("读写配置文件时出现了异常\n" + e);
            }
        }

        if (sender instanceof CommandSenderFriend) {
            CommandSenderFriend senderFriend = (CommandSenderFriend) sender;
            Friend friend = senderFriend.getFriend();
            try {
                if (args[0].contentToString().equalsIgnoreCase("/bindServer")) {
                    friend.sendMessage(
                            "# 用法" +
                                    "\n# /bindServer add [群号] [命令] [地址] 添加一个绑定" +
                                    "\n# /bindServer remove [群号] 移除一个绑定" +
                                    "\n# /bindServer list 查看绑定列表" +
                                    "\n# ps:可用\"this\"代指本群 移除时可用all代指所有群"
                    );
                    return;
                }
                if (args[0].contentToString().equalsIgnoreCase("add")) addBind(args, friend);
                if (args[0].contentToString().equalsIgnoreCase("remove")) removeBind(args, friend);
                if (args[0].contentToString().equalsIgnoreCase("list")) getBind(friend);
            } catch (IOException e) {
                friend.sendMessage("读写配置文件时出现了异常\n" + e);
            }
        }
    }

    private void addBind(SingleMessage[] args, Object sendObject) throws IOException {
        if (sendObject instanceof Group) {
            Group group = (Group) sendObject;
            if (args.length != 4) {
                group.sendMessage("参数输入有误");
                return;
            }
            long num = 0;
            if (args[1].contentToString().equalsIgnoreCase("this"))
                num = group.getId();
            else if (Util.isNum(args[1].contentToString()))
                num = Long.parseLong(args[1].contentToString());
            else
                group.sendMessage("意外的数据类型(需要整数)");

            String cmd = args[2].contentToString();
            String host = args[3].contentToString();
            if (plugin.config.addBindServer(new Server(num, cmd, host)))
                group.sendMessage("绑定成功 可在该群发送 " + cmd + " 获取" + host + "的信息");
            else group.sendMessage("绑定失败 此群已绑定");
        } else if (sendObject instanceof Friend) {
            Friend friend = (Friend) sendObject;
            if (args.length != 4) {
                friend.sendMessage("参数输入有误");
                return;
            }
            long num;
            if (Util.isNum(args[1].contentToString()))
                num = Long.parseLong(args[1].contentToString());
            else {
                friend.sendMessage("意外的数据类型(需要整数)");
                return;
            }

            String cmd = args[2].contentToString();
            String host = args[3].contentToString();
            if (plugin.config.addBindServer(new Server(num, cmd, host)))
                friend.sendMessage("绑定成功 可在该群发送 " + cmd + " 获取" + host + "的信息");
            else friend.sendMessage("绑定失败 此群已绑定");
        }
    }

    private void removeBind(SingleMessage[] args, Object sendObject) throws IOException {
        if (sendObject instanceof Group) {
            Group group = (Group) sendObject;
            if (args.length != 2) {
                group.sendMessage("参数输入错误");
                return;
            }
            long num = -1;
            if (args[1].contentToString().equalsIgnoreCase("all")) {
                group.sendMessage("已清空绑定数据(" + plugin.config.clearBindServer() + "个)");
            } else {
                if (args[1].contentToString().equalsIgnoreCase("this"))
                    num = group.getId();
                else if (Util.isNum(args[1].contentToString()))
                    num = Long.parseLong(args[1].contentToString());
                else
                    group.sendMessage("意外的数据类型(需要整数)");

                if (num != -1) {
                    if (plugin.config.removeBindServer(num))
                        group.sendMessage(num + "不再绑定服务器");
                    else
                        group.sendMessage("解绑失败:此群没有绑定服务器");
                }
            }
        } else if (sendObject instanceof Friend) {
            Friend friend = (Friend) sendObject;
            if (args.length != 2) {
                friend.sendMessage("参数输入错误");
                return;
            }
            long num = -1;
            if (args[1].contentToString().equalsIgnoreCase("all")) {
                friend.sendMessage("已清空绑定数据(" + plugin.config.clearBindServer() + "个)");
            } else {
                if (Util.isNum(args[1].contentToString()))
                    num = Long.parseLong(args[1].contentToString());
                else
                    friend.sendMessage("意外的数据类型(需要整数)");

                if (num != -1) {
                    if (plugin.config.removeBindServer(num))
                        friend.sendMessage(num + "不再绑定服务器");
                    else
                        friend.sendMessage("解绑失败:此群没有绑定服务器");
                }
            }
        }
    }

    private void getBind(Object sendObject) {
        List<Server> servers = plugin.config.getBindServers();
        if (sendObject instanceof Group) {
            Group group = (Group) sendObject;
            if (servers.size() < 1) {
                group.sendMessage("没有数据");
                return;
            }
            ForwardMessageBuilder builder = new ForwardMessageBuilder(group);
            int count = 0;
            for (int i = 0; i < servers.size(); i++) {
                count++;
                builder.add(group.getBot().getId(), "Server" + (i + 1), new PlainText(
                        "Group:" + servers.get(i).getGroup() +
                                "\nCmd:" + servers.get(i).getCmd() +
                                "\nHost:" + servers.get(i).getHost()
                ));
                //清零count 发送合并转发 清空ForwardMessageBuilder
                if (count == 100) {
                    count = 0;
                    group.sendMessage(builder.build());
                    builder = new ForwardMessageBuilder(group);
                }
                if (count > 100) {
                    group.sendMessage("程序遇到量子异常,程序无法定位此异常,请联系外星人");
                    return;
                }
            }
            //即使没上百,最后也会发送
            if (count > 0) group.sendMessage(builder.build());
        } else if (sendObject instanceof Friend) {
            Friend friend = (Friend) sendObject;
            if (servers.size() < 1) {
                friend.sendMessage("没有数据");
                return;
            }
            ForwardMessageBuilder builder = new ForwardMessageBuilder(friend);
            int count = 0;
            for (int i = 0; i < servers.size(); i++) {
                count++;
                builder.add(friend.getBot().getId(), "Server" + (i + 1), new PlainText(
                        "Group:" + servers.get(i).getGroup() +
                                "\nCmd:" + servers.get(i).getCmd() +
                                "\nHost:" + servers.get(i).getHost()
                ));
                //清零count 发送合并转发 清空ForwardMessageBuilder
                if (count == 100) {
                    count = 0;
                    friend.sendMessage(builder.build());
                    builder = new ForwardMessageBuilder(friend);
                }
                if (count > 100) {
                    friend.sendMessage("程序遇到量子异常,程序无法定位此异常,请联系外星人");
                    return;
                }
            }
            //即使没上百,最后也会发送
            if (count > 0) friend.sendMessage(builder.build());
        }
    }
}
