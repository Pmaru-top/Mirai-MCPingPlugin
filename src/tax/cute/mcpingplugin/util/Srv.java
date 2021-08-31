package tax.cute.mcpingplugin.util;

import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class Srv{
    private String srvHost;
    private int srvPort;
    public Srv(String srvHost,int srvPort) {
        this.srvHost = srvHost;
        this.srvPort = srvPort;
    }

    public static Srv getSrv(String host, String srvName) {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        hashtable.put("java.naming.provider.url", "dns:");
        try {
            Attribute qwq = (new InitialDirContext(hashtable)).getAttributes(srvName + host, new String[]{"SRV"}).get("srv");
            if (qwq != null) {
                String[] re = qwq.get().toString().split(" ", 4);
                String srvHost = re[3].substring(0, re[3].length() - 1);
                int srvPort = Integer.parseInt(re[2]);
                return new Srv(srvHost,srvPort);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public String getSrvHost() {
        return srvHost;
    }

    public int getSrvPort() {
        return srvPort;
    }
}