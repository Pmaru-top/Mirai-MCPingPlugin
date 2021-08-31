package tax.cute.mcpingplugin;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import tax.cute.mcpingplugin.commands.*;
import tax.cute.mcpingplugin.util.Util;
import top.mrxiaom.miraiutils.CommandListener;

import java.io.IOException;

public class Plugin extends JavaPlugin{
    public Config config;
    public String JETypesetText;
    public String BETypesetText;
    public String McPingCmd;

    public String configFilePath;
    public String jeTypesetFilePath;
    public String beTypesetFilePath;

    CommandListener cmd;
    BindServer bindServer;
    Enable enable;
    MCPing mcPing;
    OtherMcPingSet otherMcPingSet;
    Owner owner;
    Reload reload;

    SendBindServer sendBindServer;
    OtherMcPing otherMcPing;

    Console console;

    public Plugin() {
        super(new JvmPluginDescriptionBuilder(
                        "tax.cute.mcpingplugins", // id
                        "2.1.0" // version
                )
                        .name("MCPing")
                        .author("CuteStar")
                        // .info("...")
                        .build()
        );
    }

    @Override
    public void onLoad(PluginComponentStorage pcs) {
        String directory;
        this.getLogger().info("配置文件路径: " + (directory = this.getDataFolder().getAbsoluteFile().getPath()));
        configFilePath = directory + "\\Config.yml";
        jeTypesetFilePath = directory + "\\typeset.txt";
        beTypesetFilePath = directory + "\\Betypeset.txt";
    }

    private void init() {
        this.bindServer = new BindServer(this);
        this.enable = new Enable(this);
        this.mcPing = new MCPing(this);
        this.otherMcPingSet = new OtherMcPingSet(this);
        this.owner = new Owner(this);
        this.reload = new Reload(this);
        this.console = new Console(this);
    }

    private void register() {
        cmd = new CommandListener("/");
        cmd.registerCommand(bindServer);
        cmd.registerCommand(enable);
        cmd.registerCommand(mcPing);
        cmd.registerCommand(otherMcPingSet);
        cmd.registerCommand(owner);
        cmd.registerCommand(reload);
        GlobalEventChannel.INSTANCE.registerListenerHost(cmd);
    }

    @Override
    public void onEnable() {
        init();
        try {
            JETypeset.createTypesetFile(this.jeTypesetFilePath);
            BETypeset.createTypesetFile(this.beTypesetFilePath);
            this.config = Config.getConfig(this.configFilePath);
            this.JETypesetText = Util.readText(this.jeTypesetFilePath, "GBK");
            this.BETypesetText = Util.readText(this.beTypesetFilePath, "GBK");
            this.McPingCmd = config.getCmd();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendBindServer = new SendBindServer(this);
        otherMcPing = new OtherMcPing(this);
        register();

        GlobalEventChannel.INSTANCE.registerListenerHost(sendBindServer);
        GlobalEventChannel.INSTANCE.registerListenerHost(otherMcPing);
        CommandManager.INSTANCE.registerCommand(console,true);

        this.getLogger().info("github URL: https://github.com/MX233/Mirai-MCPingPlugin");
    }

    @Override
    public void onDisable() {
        this.getLogger().info("插件已卸载");
    }
}

//这么乱的代码也许只有作者本人才能看懂罢