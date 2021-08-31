package tax.cute.mcpingplugin;

public class Server {
    private final long group;
    private final String cmd;
    private final String host;

    public Server(long group,String cmd,String host) {
        this.group = group;
        this.cmd = cmd;
        this.host = host;
    }

    public long getGroup() {
        return group;
    }

    public String getHost() {
        return host;
    }

    public String getCmd() {
        return cmd;
    }
}
