package poly.services;

import java.util.HashMap;
import poly.error.NullConfigPathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConfigHandler {

    // STATIC ATTRIBUTES
    private static ConfigHandler singleton;
    private static String configPath;

    // ATTRIBUTES
    private HashMap<String, String> params;

    // CTOR
    private ConfigHandler() {
        this.params = new HashMap<String, String>();
    }

    private static void checkInstance() throws NullConfigPathException {
        if (ConfigHandler.singleton == null) {
            ConfigHandler.init();
        }
    }

    public static void setConfigPath(String path){
        ConfigHandler.configPath = path;
    }

    public static void init() throws NullConfigPathException {
        ConfigHandler.singleton = new ConfigHandler();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        if (ConfigHandler.configPath == null)
            throw new NullConfigPathException();

        try {
            FileReader reader = new FileReader(ConfigHandler.configPath);
            //Read JSON file
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            for (Object key : obj.keySet()) {
                ConfigHandler.singleton.params.put(key.toString(), obj.get(key).toString());
            }
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasParam(String key){
        try {
            ConfigHandler.checkInstance();
            return ConfigHandler.singleton.params.containsKey(key);
        } catch (NullConfigPathException e) {
            return false;
        }
    }

    public static String getParams(String key){
        if (ConfigHandler.hasParam(key)) {
            return ConfigHandler.singleton.params.get(key);
        }
        return null;
    }
}