package top.mrxiaom.miraiutils;

import net.mamoe.mirai.Bot;

public abstract class CommandSender {
	private final long senderId;
	private final String senderNick;
	private final int sendTime;
	private final Bot bot;

	protected CommandSender(Bot bot, long senderId, String senderNick, int sendTime) {
		this.bot = bot;
		this.senderId = senderId;
		this.senderNick = senderNick;
		this.sendTime = sendTime;
	}

	public Bot getBot() {
		return this.bot;
	}

	public long getSenderID() {
		return this.senderId;
	}

	public String getSenderNick() {
		return this.senderNick;
	}

	public int getSendTime() {
		return this.sendTime;
	}
}
