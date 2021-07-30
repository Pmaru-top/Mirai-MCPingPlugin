package top.mrxiaom.miraiutils;

import net.mamoe.mirai.message.data.SingleMessage;

public abstract class CommandModel {

	private final String command;

	public CommandModel(String command) {
		this.command = command;
	}

	public String getCommand() {
		return this.command;
	}

	public abstract void onCommand(CommandSender sender, SingleMessage[] args);
}
