package tax.cute.mcpingplugin;

import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.IOException;

public class Plugin extends JavaPlugin{

    private static Plugin instance;
    private Group group;
    private OwnerGroupCmd ownerGroupCmd;
    private Friend friend;
    private OwnerFriendCmd ownerFriendCmd;
    public Config config;
    String name = "[MCPing]";
    String typesetText;
    final String configFilePath = "data\\MCPing\\Config.json";
    final String typesetFilePath = "data\\MCPing\\typeset.txt";

    public Plugin() {
        super(new JvmPluginDescriptionBuilder(
                        "tax.cute.mcpingplugins", // id
                        "1.0.0" // version
                )
                        .name("MCPing")
                        .author("CuteStar")
                        // .info("...")
                        .build()
        );
        instance = this;
    }
    public static Plugin getInstance() {
        return instance;
    }
    @Override
    public void onLoad(PluginComponentStorage pcs) {
        this.getLogger().info("ConfigPath: " + this.getDataFolder().getAbsoluteFile().getPath());

        try {
            Util.createTypesetFile(this.typesetFilePath);
            this.config = Config.getConfig(this.configFilePath);
            this.typesetText = Util.readText(this.typesetFilePath,"GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }

        GlobalEventChannel.INSTANCE.registerListenerHost(this.group = new Group(this));

        GlobalEventChannel.INSTANCE.registerListenerHost(this.ownerGroupCmd = new OwnerGroupCmd(this));

        GlobalEventChannel.INSTANCE.registerListenerHost(this.friend = new Friend(this));

        GlobalEventChannel.INSTANCE.registerListenerHost(this.ownerFriendCmd = new OwnerFriendCmd(this));
    }

    @Override
    public void onEnable() {
        this.getLogger().info(name + "github URL: https://github.com/MX233/Mirai-MCPingPlugin");
    }

    @Override
    public void onDisable() {
        this.getLogger().info(name + "Disable");
    }


}
