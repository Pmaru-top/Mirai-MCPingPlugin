package top.mrxiaom.miraiutils;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.MessageSource;

public class CommandSenderFriend extends CommandSender {
	private final Friend friend;
	private final MessageSource source;

	public CommandSenderFriend(Bot bot, Friend friend, MessageSource source, int time) {
		super(bot, friend.getId(), friend.getNick(), time);
		this.friend = friend;
		this.source = source;
	}

	public Friend getFriend() {
		return this.friend;
	}

	public MessageSource getMessageSource() {
		return this.source;
	}
}
