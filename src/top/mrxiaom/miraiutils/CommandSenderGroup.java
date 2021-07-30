package top.mrxiaom.miraiutils;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.MessageSource;

public class CommandSenderGroup extends CommandSender {
	private final Group group;
	private final Member member;
	private final MessageSource source;

	public CommandSenderGroup(Bot bot, Group group, Member member, MessageSource source, int time) {
		super(bot, member.getId(), member.getNick(), time);
		this.group = group;
		this.member = member;
		this.source = source;
	}

	public Group getGroup() {
		return this.group;
	}

	public Member getMember() {
		return this.member;
	}

	public MessageSource getMessageSource() {
		return this.source;
	}
}
