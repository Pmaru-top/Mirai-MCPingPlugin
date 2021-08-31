package tax.cute.mcpingplugin;

import tax.cute.mcpingplugin.util.Util;
import tax.cute.minecraftserverpingbe.MCBePing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BETypeset {
    private final String motdText;

    public BETypeset(String motdText) {
        this.motdText = motdText;
    }

    public static BETypeset getTypeset(String host,int port,String typesetText) throws Exception{
        final String description = "%description";
        final String default_mode = "%default_mode";
        final String version = "%version";
        final String protocol_num = "%protocol_num";
        final String type = "%type";
        final String online_players = "%online_players";
        final String max_players = "%max_players";
        final String delay = "%delay";
        final String world_name = "%world_name";

        MCBePing motd = MCBePing.getMotd(host, port,2000);

        String motdText = typesetText
                .replace(description,String.valueOf(Util.clearColorCode(motd.getDescription())))
                .replace(default_mode,String.valueOf(motd.getDefault_mode()))
                .replace(version,String.valueOf(motd.getVersion()))
                .replace(world_name,String.valueOf(motd.getWorld_name()))
                .replace(protocol_num,String.valueOf(motd.getProtocol_num()))
                .replace(type,motd.getType())
                .replace(online_players,String.valueOf(motd.getOnline_players()))
                .replace(max_players,String.valueOf(motd.getMax_players()))
                .replace(delay,String.valueOf(motd.getDelay()));
        return new BETypeset(motdText);
    }

    public String getMotdText() {
        return this.motdText;
    }

    public static void createTypesetFile(String path) throws IOException{
        if(new File(path).isFile()) return;
        String text =
                "[ 描述 ] %description" +
                        "\n[ 版本 ] %version(%protocol_num)" +
                        "\n[ 人数 ] %online_players/%max_players" +
                        "\n[ 延迟 ] %delayms" +
                        "\n[ 类型 ] %type" +
                        "\n[ 默认模式 ] %default_mode" +
                        "\n[ 世界名称 ] %world_name"
                ;
        OutputStream out = new FileOutputStream(path);
        out.write(text.getBytes());
        out.flush();
        out.close();
    }
}