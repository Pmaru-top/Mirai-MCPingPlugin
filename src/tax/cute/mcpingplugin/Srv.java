package tax.cute.mcpingplugin;

import javax.naming.directory.Attribute;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

public class Srv {
    private String SrvHost;
    private int SrvPort;
    private boolean exist = false;

    public Srv(boolean exist,String SrvHost,int SrvPort) {
        this.SrvHost = SrvHost;
        this.SrvPort = SrvPort;
        this.exist = exist;
    }

    public static Srv getSrv(String host,String Srv) {
        String SrvHost;
        int SrvPort;
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
        hashtable.put("java.naming.provider.url", "dns:");
        try {
            Attribute qwq = (new InitialDirContext(hashtable)).getAttributes((new StringBuilder()).append(Srv).append(host).toString(), new String[]{"SRV"}).get("srv");
            if (qwq != null) {
                String[] re = qwq.get().toString().split(" ", 4);
                SrvHost = re[3].substring(0, re[3].length() - 1);
                SrvPort = Integer.parseInt(re[2]);
                return new Srv(true,SrvHost, SrvPort);
            }
        } catch (Exception ignored) {
        }
        return new Srv(false,"null",0);
    }

    public String getSrvHost() {
        return SrvHost;
    }

    public int getSrvPort() {
        return SrvPort;
    }

    public boolean isExist() {
        return exist;
    }
}