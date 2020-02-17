package poly.utils;

import java.util.HashMap;
import poly.error.NullConfigPathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Config {

    // STATIC ATTRIBUTES
    private static Config singleton;
    private static String configPath;

    // ATTRIBUTES
    private HashMap<String, String> params;

    // CTOR
    private Config() {
        this.params = new HashMap<String, String>();
    }

    private static void checkInstance() throws NullConfigPathException {
        if (Config.singleton == null) {
            Config.init();
        }
    }

    public static void setConfigPath(String path){
        Config.configPath = path;
    }

    public static void init() throws NullConfigPathException {
        Config.singleton = new Config();
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        if (Config.configPath == null)
            throw new NullConfigPathException();

        try {
            FileReader reader = new FileReader(Config.configPath);
            //Read JSON file
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            for (Object key : obj.keySet()) {
                Config.singleton.params.put(key.toString(), obj.get(key).toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasParam(String key){
        try {
            Config.checkInstance();
            return Config.singleton.params.containsKey(key);
        } catch (NullConfigPathException e) {
            return false;
        }
    }

    public static String getParams(String key){
        if (Config.hasParam(key)) {
            return Config.singleton.params.get(key);
        }
        return null;
    }
}