package com.company.config;

import java.util.HashMap;
import com.company.error.NullConfigPathException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
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
            Config.singleton = new Config();
            Config.init();
        }
    }

    public static void setConfigPath(String path){
        Config.configPath = path;
    }

    public static void init() throws NullConfigPathException {
        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
        if (Config.configPath == null)
            throw new NullConfigPathException();

        try {
            FileReader reader = new FileReader(Config.configPath);
            //Read JSON file
            JSONObject obj = (JSONObject) jsonParser.parse(reader);
            obj.keySet().forEach(k -> {
                System.out.println(k);
            });


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasParam(String key) throws NullConfigPathException {
        Config.checkInstance();
        return Config.singleton.params.containsKey(key);
    }

    public static String getParams(String key) throws NullConfigPathException {
        if (Config.hasParam(key)) {
            return Config.singleton.params.get(key);
        }
        return null;
    }
}