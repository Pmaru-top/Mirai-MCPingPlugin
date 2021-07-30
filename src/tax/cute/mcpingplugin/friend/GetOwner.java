package tax.cute.mcpingplugin.friend;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.SingleMessage;
import tax.cute.mcpingplugin.Plugin;
import top.mrxiaom.miraiutils.CommandModel;
import top.mrxiaom.miraiutils.CommandSender;
import top.mrxiaom.miraiutils.CommandSenderFriend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class GetOwner extends CommandModel {
    Plugin plugin;
    public GetOwner(Plugin plugin) {
        super("getOwner");
        this.plugin = plugin;
    }

    @Override
    public void onCommand(CommandSender sender, SingleMessage[] args) {
        if (sender instanceof CommandSenderFriend) {
            CommandSenderFriend senderFriend = (CommandSenderFriend)sender;
            Friend friend = senderFriend.getFriend();
            if(plugin.config.getOwner().size() > 0) return;
            try {
                InputStream in = new FileInputStream("data\\MCPing\\Pin.txt");
                String pin = new String(in.readAllBytes());
                in.close();
                if (!args[0].contentToString().equals(pin)) return;
                plugin.config.addOwner(friend.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            friend.sendMessage("你已成为主人");
        }
    }
}
