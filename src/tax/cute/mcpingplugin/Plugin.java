package tax.cute.mcpingplugin;

import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import tax.cute.mcpingplugin.Util.Util;
import tax.cute.mcpingplugin.commands.*;
import top.mrxiaom.miraiutils.CommandListener;

import java.io.IOException;

public class Plugin extends JavaPlugin{
    public Config config;
    public String name = "[MCPing]";
    public String JETypesetText;
    public String BETypesetText;
    public String McPingCmd;

    public final String configFilePath = "data\\MCPing\\Config.json";
    public final String JETypesetFilePath = "data\\MCPing\\typeset.txt";
    public final String BETypesetFilePath = "data\\MCPing\\Betypeset.txt";

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
                        "2.0.3" // version
                )
                        .name("MCPing")
                        .author("CuteStar")
                        // .info("...")
                        .build()
        );
    }
    @Override
    public void onLoad(PluginComponentStorage pcs) {
        this.getLogger().info("ConfigPath: " + this.getDataFolder().getAbsoluteFile().getPath());
    }

    private void register() {
        this.bindServer = new BindServer(this);
        this.enable = new Enable(this);
        this.mcPing = new MCPing(this);
        this.otherMcPingSet = new OtherMcPingSet(this);
        this.owner = new Owner(this);
        this.reload = new Reload(this);


        cmd = new CommandListener("/");
        cmd.registerCommand(bindServer);
        cmd.registerCommand(enable);
        cmd.registerCommand(mcPing);
        cmd.registerCommand(otherMcPingSet);
        cmd.registerCommand(owner);
        cmd.registerCommand(reload);

    }

    @Override
    public void onEnable() {
        try {
            JETypeset.createTypesetFile(this.JETypesetFilePath);
            BETypeset.createTypesetFile(this.BETypesetFilePath);
            this.config = Config.getConfig(this.configFilePath);
            this.JETypesetText = Util.readText(this.JETypesetFilePath, "GBK");
            this.BETypesetText = Util.readText(this.BETypesetFilePath, "GBK");
            this.McPingCmd = config.getMcPingCmd();
        } catch (IOException e) {
            e.printStackTrace();
        }

        console = new Console(this);
        CommandManager.INSTANCE.registerCommand(console,true);
        register();
        sendBindServer = new SendBindServer(this);
        otherMcPing = new OtherMcPing(this);

        GlobalEventChannel.INSTANCE.registerListenerHost(cmd);
        GlobalEventChannel.INSTANCE.registerListenerHost(sendBindServer);
        GlobalEventChannel.INSTANCE.registerListenerHost(otherMcPing);

        this.getLogger().info(name + "github URL: https://github.com/MX233/Mirai-MCPingPlugin");
    }

    @Override
    public void onDisable() {
        this.getLogger().info(name + "Disable");
    }
}