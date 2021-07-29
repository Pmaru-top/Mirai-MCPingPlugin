package top.mrxiaom.miraiutils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.mamoe.mirai.event.Event;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.event.events.StrangerMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;

public class CommandListener extends SimpleListenerHost {
	private String prefix;
	private final Set<CommandModel> commands = new HashSet<CommandModel>();

	/**
	 * <p>
	 * The command listener.
	 * </p>
	 * <p>
	 * You can register it by {@link net.mamoe.mirai.event.EventChannel}
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * <p>
	 * {@code GlobalEventChannel.INSTANCE.registerListenerHost(new CommandListener("/"));}
	 * </p>
	 * 
	 * @param prefix the prefix of commands
	 * @author MrXiaoM
	 */
	public CommandListener(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Use the default prefix "/"
	 * 
	 * @author MrXiaoM
	 */
	public CommandListener() {
		this("/");
	}

	public void setCommandPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getCommandPrefix() {
		return this.prefix;
	}

	public void registerToChannel(EventChannel<Event> channel) {
		channel.registerListenerHost(this);
	}

	public void registerCommand(CommandModel command) {
		this.commands.add(command);
	}

	private void processMessages(MessageEvent event) {
		if (event instanceof StrangerMessageEvent) {
			StrangerMessageEvent sEvent = (StrangerMessageEvent) event;
			this.dispitchCommand(
					new CommandSenderStranger(event.getBot(), sEvent.getSender(), sEvent.getSource(), sEvent.getTime()),
					event.getMessage());
		}
		if (event instanceof FriendMessageEvent) {
			FriendMessageEvent fEvent = (FriendMessageEvent) event;
			this.dispitchCommand(
					new CommandSenderFriend(event.getBot(), fEvent.getSender(), fEvent.getSource(), fEvent.getTime()),
					event.getMessage());
		}
		if (event instanceof GroupMessageEvent) {
			GroupMessageEvent gEvent = (GroupMessageEvent) event;
			this.dispitchCommand(new CommandSenderGroup(event.getBot(), gEvent.getGroup(), gEvent.getSender(),
					gEvent.getSource(), gEvent.getTime()), event.getMessage());
		}
	}

	public void dispitchCommand(CommandSender sender, MessageChain message) {
		String cmdRoot = null;
		List<SingleMessage> args = new ArrayList<SingleMessage>();
		// 遍历消息里所有分段
		int h = 0;
		for (int i = 0; i < message.size(); i++) {
			if (i >= message.size())
				return;
			SingleMessage s = message.get(i);
			// i=0 时必为 MessageSource，这个不能算进参数里面，直接跳过
			if (s instanceof MessageSource) {
				continue;
			}
			// 如果分段是普通文本
			if (s instanceof PlainText) {
				PlainText text = (PlainText) s;
				String str = text.getContent();

				if (h == 0) {
					cmdRoot = str.contains(" ") ? (str.split(" ").length > 0 ? str.split(" ")[0] : null) : str;
					if (cmdRoot == null)
						break;
					// 如果不是命令前缀开头的命令根直接结束
					if (!cmdRoot.startsWith(this.prefix)) {
						cmdRoot = null;
						break;
					}
					// 去除命令前缀
					cmdRoot = cmdRoot.substring(this.prefix.length());
				}
				// 如果找不到命令根直接结束
				if (cmdRoot == null)
					break;
				// 将所有带空格的消息分段作为参数
				if (str.contains(" ")) {
					String[] splitText = str.split(" ");
					// 如果是在最前面的分段，则要选择从1开始还是从0开始
					// 从1开始可舍弃命令根为参数，即让参数里没有命令根
					for (int j = (h == 0 ? 1 : 0); j < splitText.length; j++) {
						args.add(new PlainText(splitText[j]));
					}
				} else {
					// 没有带空格时非第一个分段才加入到参数里面
					// 在消息为出现“文字+@AT+文字”等情况的时候会被用到
					if (h != 1) {
						args.add(text);
					}
				}
				h++;
				// 懒得写 else
				continue;
			}

			// 如果是其他的消息分段类型

			// 不是头个的分段的时候判断命令根，如果找不到直接下一个命令
			if (h > 0 && cmdRoot == null)
				break;

			h++;

			// 不是普通文本的分段统一加入参数列表
			args.add(s);
		}
		
		if (cmdRoot != null) {
			for (CommandModel model : this.commands) {
				if (cmdRoot.equalsIgnoreCase(model.getCommand())) {
					model.onCommand(sender, args.toArray(new SingleMessage[0]));
				}
			}
		}
	}

	@EventHandler
	private ListeningStatus onGroupMessage(GroupMessageEvent event) {
		this.processMessages(event);
		return ListeningStatus.LISTENING;
	}

	@EventHandler
	private ListeningStatus onFriendMessage(FriendMessageEvent event) {
		this.processMessages(event);
		return ListeningStatus.LISTENING;
	}

	@EventHandler
	private ListeningStatus onStrangerMessage(StrangerMessageEvent event) {
		
		this.processMessages(event);
		return ListeningStatus.LISTENING;
	}
}
