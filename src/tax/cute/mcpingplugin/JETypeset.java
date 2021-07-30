package tax.cute.mcpingplugin;

import tax.cute.mcpingplugin.Util.Util;
import tax.cute.minecraftserverping.MCPing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.List;

public class JETypeset {
    private String motdText;
    private byte[] favicon_bytes;
    private List<String> modList;

    public JETypeset(String motdText,byte[] favicon_bytes, List<String> modList) {
        this.motdText = motdText;
        this.favicon_bytes = favicon_bytes;
        this.modList = modList;
    }

    public static JETypeset getTypeset(String host, int port, String typesetText) throws IOException{
        final String favicon = "%favicon";
        final String description = "%description";
        final String version_name = "%version_name";
        final String version_protocol = "%version_protocol";
        final String online_players = "%online_players";
        final String max_players = "%max_players";
        final String delay = "%delay";
        final String mod_count = "%mod_count";
        final String type = "%type";
        final String mod_list = "%mod_list";

        MCPing motd = MCPing.getMotd(host, port,2500);

        boolean sendFavicon = typesetText.contains(favicon);
        List<String> modList = null;
        if (typesetText.contains(mod_list) || motd.getModList().size() > 0) {
            modList = motd.getModList();
        }
        byte[] favicon_bytes = null;
        try {
            if (sendFavicon) {
                if (motd.getFavicon().equals("null"))
                    favicon_bytes = Base64.getDecoder().decode(Util.MC_SERVER_DEFAULT_FAVICON_BASE64);
                else {
                    favicon_bytes = Base64.getDecoder().decode(motd.getFavicon().replace("\n",""));
                }
            }
        } catch (Exception e) {
            favicon_bytes = Base64.getDecoder().decode(Util.MC_SERVER_DEFAULT_FAVICON_BASE64);
        }

        String motdText = typesetText
                .replace(description, motd.getDescription())
                .replace(version_name, motd.getVersion_name())
                .replace(version_protocol, motd.getVersion_protocol())
                .replace(online_players, String.valueOf(motd.getOnline_players()))
                .replace(max_players, String.valueOf(motd.getMax_players()))
                .replace(delay, String.valueOf(motd.getDelay()))
                .replace(mod_count, String.valueOf(motd.getMod_count()))
                .replace(type, motd.getType())
                .replace(mod_list, "")
                .replace(favicon, "");
        return new JETypeset(motdText,favicon_bytes,modList);
    }

    public String getMotdText() {
        return this.motdText;
    }

    public byte[] getFavicon_bytes() {
        return this.favicon_bytes;
    }

    public List<String> getModList() {
        return this.modList;
    }

    public static void createTypesetFile(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) return;
        String text =
                "\n%favicon" +
                        "\n[ 描述 ] %description" +
                        "\n[ 版本 ] %version_name(%version_protocol)" +
                        "\n[ 人数 ] %online_players/%max_players" +
                        "\n[ 延迟 ] %delayms" +
                        "\n[ 类型 ] %type" +
                        "\n[ Mod数量 ] %mod_count";
        OutputStream out = new FileOutputStream(path);
        out.write(text.getBytes());
        out.flush();
        out.close();
    }
}
