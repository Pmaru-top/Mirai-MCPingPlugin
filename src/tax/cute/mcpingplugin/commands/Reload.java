package tax.cute.mcpingplugin.commands;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.SingleMessage;
import tax.cute.mcpingplugin.BETypeset;
import tax.cute.mcpingplugin.Config;
import tax.cute.mcpingplugin.JETypeset;
import tax.cute.mcpingplugin.Plugin;
import tax.cute.mcpingplugin.util.Util;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderFriend;
import top.mrxiaom.miraiutils.CommandSenderGroup;

import java.io.IOException;

public class Reload extends CommandModel {
    Plugin plugin;
    public Reload(Plugin plugin) {
        super("reload");
        this.plugin = plugin;
    }
    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if(!plugin.config.isEnable()) return;
        if(!plugin.config.isOwner(sender.getSenderID())) return;
        if (sender instanceof CommandSenderGroup) {
            CommandSenderGroup senderGroup = (CommandSenderGroup)sender;
            Group group = senderGroup.getGroup();
            try {
                plugin.config = Config.getConfig(plugin.config.getPath());
                BETypeset.createTypesetFile(plugin.beTypesetFilePath);
                JETypeset.createTypesetFile(plugin.jeTypesetFilePath);
                plugin.JETypesetText = Util.readText(plugin.jeTypesetFilePath,"GBK");
                plugin.BETypesetText = Util.readText(plugin.beTypesetFilePath,"GBK");
                group.sendMessage("已完成重载");
            } catch (IOException e) {
                group.sendMessage("重新加载配置文件时出现了异常\n" + e);
            }
        }else if (sender instanceof CommandSenderFriend) {
            CommandSenderFriend senderFriend = (CommandSenderFriend)sender;
            Friend friend = senderFriend.getFriend();

            try {
                plugin.config = Config.getConfig(plugin.config.getPath());
                plugin.JETypesetText = Util.readText(plugin.jeTypesetFilePath,"GBK");
                plugin.BETypesetText = Util.readText(plugin.beTypesetFilePath,"GBK");
                friend.sendMessage("已完成重载");
            } catch (IOException e) {
                friend.sendMessage("重新加载配置文件时出现了异常\n" + e);
            }
        }
    }
}
