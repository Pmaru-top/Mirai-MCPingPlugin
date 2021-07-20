package tax.cute.mcpingplugin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;

public class Config {
    private static final String name = "[MCPing]";

    private String path;
    private JSONArray owner;
    private JSONArray bindServerList;
    private String enable;
    private String mcpingCMD;

    public Config(
            String path,
            JSONArray owner,
            JSONArray bindServerList,
            String enable,
            String mcpingCMD
    ) {
        this.path = path;
        this.owner = owner;
        this.bindServerList = bindServerList;
        this.enable = enable;
        this.mcpingCMD = mcpingCMD;
    }

    public static Config getConfig(String path) throws IOException {
        File file = new File(path);
        //Create a configuration file when it is judged that the configuration file does not exist
        if (!file.exists()) if (!createConfig(path)) System.err.println(name + "Create config failure");

        //initialization
        JSONArray owner = new JSONArray();
        String enable = "false";
        String mcpingCMD = null;
        JSONArray bindServer = new JSONArray();

        //Read config file text
        String jsonStr = Util.readText(path);
        JSONObject json = JSONObject.parseObject(jsonStr);

        //Determine whether the configuration file is json
        if (json == null) {
            createConfig(path);
            jsonStr = Util.readText(path);
            json = JSONObject.parseObject(jsonStr);
        }

        JSONObject config = new JSONObject();

        //Determine whether it is json, if not, it will be reset
        if (json.containsKey("Config")) {
            if (json.get("Config") instanceof JSONObject) {
                config = json.getJSONObject("Config");
            }
        }

        if (config.containsKey("Enable")) {
            if (config.get("Enable") instanceof String) {
                if (Util.isBoolean(config.getString("Enable"))) {
                    enable = config.getString("Enable");
                } else {
                    System.err.println(name + "\"Config\\Enable\"Unexpected type");
                }
            } else {
                System.err.println(name + "\"Config\\Enable\"Unexpected type");
            }
        } else {
            System.err.println(name + "\"Config\\Enable\"Missing");
        }

        if (config.containsKey("CMD")) {
            if (config.get("CMD") instanceof String) {
                mcpingCMD = config.getString("CMD");
            } else {
                System.err.println(name + "\"Config\\CMD\"Unexpected type");
            }
        } else {
            System.err.println(name + "\"Config\\CMD\"Missing");
        }

        if (json.containsKey("Owner")) {
            if (json.get("Owner") instanceof JSONArray) {
                owner = json.getJSONArray("Owner");
            } else {
                System.err.println(name + "\"Owner\"Unexpected type");
            }
        } else {
            System.err.println(name + "\"Owner\"Missing");
        }

        if (json.containsKey("BindServer")) {
            if (json.get("BindServer") instanceof JSONArray) {
                bindServer = json.getJSONArray("BindServer");
            } else {
                System.err.println(name + "\"BindServer\"Unexpected type");
            }
        } else {
            System.err.println(name + "\"BindServer\"Missing");
        }

        return new Config(path, owner, bindServer, enable, mcpingCMD);
    }

    public static boolean createConfig(String path) {
        try {
            JSONObject json = new JSONObject();

            JSONArray bindServer = new JSONArray();

            JSONArray owner = new JSONArray();

            JSONObject config = new JSONObject();
            config.put("Enable", "true");
            config.put("CMD", "/mcping");

            json.put("Owner", owner);
            json.put("BindServer", bindServer);
            json.put("Config", config);

            String jsonStr = JSON.toJSONString(json);

            OutputStream out = new FileOutputStream(path);
            out.write(jsonStr.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            File file = new File(path);
            file.delete();
            return false;
        }
        return true;
    }

    public void writeConfig() throws IOException {
        JSONObject json = new JSONObject();
        JSONObject config = new JSONObject();
        config.put("Enable", this.enable);
        config.put("CMD", this.mcpingCMD);

        json.put("Owner", this.owner);
        json.put("Config", config);
        json.put("BindServer", this.bindServerList);

        String jsonStr = JSON.toJSONString(json);

        OutputStream out = new FileOutputStream(path);
        out.write(jsonStr.getBytes());
        out.flush();
        out.close();
    }

    public boolean isEnable() {
        return Boolean.parseBoolean(this.enable);
    }

    public String getPath() {
        return this.path;
    }

    public String getMcpingCMD() {
        return this.mcpingCMD;
    }

    public JSONArray getOwner() {
        return this.owner;
    }

    public boolean isOwner(long qqNum) {
        return this.owner.contains(String.valueOf(qqNum));
    }

    public JSONArray getBindServerList() {
        return this.bindServerList;
    }

    public void setEnable(boolean args) throws IOException {
        this.enable = String.valueOf(args);
        writeConfig();
    }

    public void setMcpingCMD(String args) throws IOException {
        this.mcpingCMD = args;
        writeConfig();
    }

    public boolean addOwner(long qqNum) throws IOException {
        if (isOwner(qqNum)) return false;
        this.owner.add(String.valueOf(qqNum));
        writeConfig();
        return true;
    }

    public boolean removeOwner(long qqNum) throws IOException {
        if (!isOwner(qqNum)) return false;
        this.owner.remove(String.valueOf(qqNum));
        writeConfig();
        return true;
    }

    public JSONObject getServer(long groupNum) {
        for (int i = 0; i < this.bindServerList.size(); i++) {
            if (this.bindServerList.get(i) instanceof JSONObject) {
                JSONObject server = this.bindServerList.getJSONObject(i);
                if (server.containsKey("GroupNum")) {
                    if (server.get("GroupNum") instanceof String) {
                        if (Util.isNum(server.getString("GroupNum"))) {
                            if (server.getString("GroupNum").equals(String.valueOf(groupNum))) {
                                return this.bindServerList.getJSONObject(i);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public int getServerIndex(long groupNum) {
        for (int i = 0; i < this.bindServerList.size(); i++) {
            if (this.bindServerList.get(i) instanceof JSONObject) {
                JSONObject server = this.bindServerList.getJSONObject(i);
                if (server.containsKey("GroupNum")) {
                    if (server.get("GroupNum") instanceof String) {
                        if (Util.isNum(server.getString("GroupNum"))) {
                            if (server.getString("GroupNum").equals(String.valueOf(groupNum))) {
                                return i;
                            }
                        }
                    }
                }
            }
        }
        return -1;
    }

    public boolean addBindServer(long groupNum, String cmd, String host) throws IOException {
        if (getServer(groupNum) != null) {
            return false;
        }

        JSONObject server = new JSONObject();
        server.put("GroupNum", String.valueOf(groupNum));
        server.put("CMD", cmd);
        server.put("Host", host);
        this.bindServerList.add(server);
        writeConfig();
        return true;
    }

    public boolean removeBindServer(long groupNum) throws IOException {
        int index = getServerIndex(groupNum);
        if (index == -1) return false;
        this.bindServerList.remove(index);
        writeConfig();
        return true;
    }

    public int removeAllBindServer() throws IOException{
        int count = this.bindServerList.size();
        this.bindServerList.clear();
        writeConfig();
        return count;
    }

    public boolean isGroup(long groupNum) {
        JSONObject server = getServer(groupNum);
        String num;
        if (server == null) {
            return false;
        }
            num = server.getString("GroupNum");

        return Long.parseLong(num) == groupNum;

    }
}