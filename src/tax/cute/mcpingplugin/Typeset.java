package tax.cute.mcpingplugin;

import tax.cute.minecraftserverping.MCPing;
import java.io.IOException;
import java.util.Base64;

public class Typeset {
    private String motdText;
    private boolean sendFavicon;
    private byte[] favicon_bytes;

    public Typeset(String motdText, boolean sendFavicon,byte[] favicon_bytes) {
        this.motdText = motdText;
        this.sendFavicon = sendFavicon;
        this.favicon_bytes = favicon_bytes;
    }

    public static Typeset getTypeset(String host, int port, String typesetText) throws IOException{
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

        MCPing motd = MCPing.getMotd(host, port);

        boolean sendFavicon = typesetText.contains(favicon);
        byte[] favicon_bytes = null;
        if (sendFavicon) {
            if(motd.getFavicon().equals("null")) favicon_bytes = Base64.getDecoder().decode(Util.MCSERVERDEFAULTFAVICONBASE64);
            else favicon_bytes = Base64.getDecoder().decode(motd.getFavicon());
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
                .replace(mod_list, String.valueOf(motd.getModList()))
                .replace(favicon, "");
        return new Typeset(motdText,sendFavicon,favicon_bytes);
    }

    public String getMotdText() {
        return this.motdText;
    }

    public boolean isSendFavicon() {
        return this.sendFavicon;
    }

    public byte[] getFavicon_bytes() {
        return this.favicon_bytes;
    }
}
