package tax.cute.mcpingplugin.commands;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.SingleMessage;
import tax.cute.mcpingplugin.Plugin;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderFriend;
import top.mrxiaom.miraiutils.CommandSenderGroup;

import java.io.IOException;

public class OtherMcPingSet extends CommandModel {
    Plugin plugin;
    public OtherMcPingSet(Plugin plugin) {
        super("mcPingCmd");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if(!plugin.config.isOwner(sender.getSenderID())) return;
        if(args.length != 2) return;
        if(!args[0].contentToString().equalsIgnoreCase("set")) return;
        if (sender instanceof CommandSenderGroup) {
            CommandSenderGroup senderGroup = (CommandSenderGroup)sender;
            Group group = senderGroup.getGroup();
            String cmd = args[1].contentToString();
            try {
                plugin.config.setCmd(cmd);
                group.sendMessage("已更改命令为" + cmd);
            } catch (IOException e) {
                group.sendMessage("读写配置时出现异常" + e);
            }
        }else if (sender instanceof CommandSenderFriend) {
            CommandSenderFriend senderFriend = (CommandSenderFriend)sender;
            Friend friend = senderFriend.getFriend();
            String cmd = args[1].contentToString();
            try {
                plugin.config.setCmd(cmd);
                friend.sendMessage("已更改命令为" + cmd);
            } catch (IOException e) {
                friend.sendMessage("读写配置时出现异常" + e);
            }
        }
    }
}
