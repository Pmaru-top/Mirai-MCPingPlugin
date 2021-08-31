package tax.cute.mcpingplugin;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import tax.cute.mcpingplugin.util.Util;

import java.io.*;
import java.util.*;

public class Config {
    private final List<Long> owner;
    private final List<Server> bindServers;
    private boolean enable;
    private String cmd;
    private final String path;

    public Config(
            List<Long> owner,
            List<Server> bindServers,
            boolean enable,
            String cmd,
            String path
    ) {
        this.owner = owner;
        this.bindServers = bindServers;
        this.enable = enable;
        this.cmd = cmd;
        this.path = path;
    }

    public static Config getConfig(String path) throws IOException {
        if(!new File(path).isFile()) createConfig(path);

        InputStream in = new FileInputStream(path);

        Map<String, Object> map = new Yaml().load(in);
        in.close();

        //初始化是防止非这个类型的实例而空指针
        List<Long> owner = new ArrayList<>();
        if (map.get("Owner") instanceof List) {
            List<Object> list = (List<Object>) map.get("Owner");
            for (Object obj : list) {
                if (obj instanceof Long) {
                    owner.add((long) obj);
                }
            }
        }

        List<Server> bindServers = new ArrayList<>();
        if (map.get("bindServers") instanceof Map) {
            Map<String, Map> bindServersMap = (Map) map.get("bindServers");
            Set<String> set = bindServersMap.keySet();
            if (set.size() > 0) {
                for (String key : set) {
                    Map<String, String> server = bindServersMap.get(key);
                    if (Util.isNum(key) && server.get("Cmd") != null && server.get("Host") != null) {
                        bindServers.add(new Server(Long.parseLong(key), server.get("Cmd"), server.get("Host")));
                    }
                }
            }
        }

        boolean enable = false;
        if (map.get("Enable") instanceof Boolean) {
            enable = (Boolean) map.get("Enable");
        }

        String cmd = "/mcmotd";
        if (map.get("Enable") instanceof String) {
            cmd = (String) map.get("Enable");
        }

        return new Config(owner, bindServers, enable, cmd, path);
    }

    static void createConfig(String path) throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("Owner", new ArrayList<>());
        map.put("bindServers", new HashMap<>());
        map.put("Enable", true);
        map.put("Cmd", "/mcmotd");

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        FileWriter writer = new FileWriter(path);
        new Yaml().dump(map, writer);

        writer.flush();
        writer.close();
    }

    void saveConfig() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("Owner", owner);

        Map<String, Map> bindServersMap = new HashMap<>();
        for (Server server : bindServers) {
            Map<String, String> data = new HashMap<>();
            data.put("Cmd", server.getCmd());
            data.put("Host", server.getHost());
            bindServersMap.put(String.valueOf(server.getGroup()), data);
        }
        map.put("bindServers", bindServersMap);

        map.put("Enable", enable);
        map.put("Cmd", cmd);

        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        FileWriter writer = new FileWriter(path);
        new Yaml(options).dump(map, writer);

        writer.flush();
        writer.close();
    }

    public String getCmd() {
        return cmd;
    }

    public List<Long> getOwner() {
        return owner;
    }

    public List<Server> getBindServers() {
        return bindServers;
    }

    public Server getServer(long group) {
        if(!isBindServer(group))return null;
        for (Server server : bindServers) {
            if (server.getGroup() == group) {
                return server;
            }
        }
        return null;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setCmd(String cmd) throws IOException {
        this.cmd = cmd;
        saveConfig();
    }

    public void setEnable(boolean enable) throws IOException {
        this.enable = enable;
        saveConfig();
    }

    public void addOwner(long num) throws IOException {
        if (this.owner.contains(num)) return;
        this.owner.add(num);
        saveConfig();
    }

    public void removeOwner(long num) throws IOException {
        if (!this.owner.contains(num)) return;
        this.owner.remove(num);
        saveConfig();
    }

    public boolean addBindServer(Server server) throws IOException {
        if(isBindServer(server.getGroup())) return false;
        this.bindServers.add(server);
        saveConfig();
        return true;
    }

    public boolean removeBindServer(long group) throws IOException {
        if(!isBindServer(group)) return false;
        for (Server server : bindServers) {
            if (server.getGroup() == group) {
                bindServers.remove(server);
                saveConfig();
                return true;
            }
        }
        return false;
    }

    public int clearBindServer() throws IOException{
        int count = bindServers.size();
        bindServers.clear();
        saveConfig();
        return count;
    }

    public boolean isBindServer(long group) {
        for (Server server : bindServers) {
            if (server.getGroup() == group) {
                return true;
            }
        }
        return false;
    }

    public boolean isOwner(long num) {
        for (Long i : owner) {
            if (i == num) {
                return true;
            }
        }
        return false;
    }

    public String getPath() {
        return path;
    }
}