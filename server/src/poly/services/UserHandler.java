package poly.services;

import poly.utils.PopSecurity;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.StringTokenizer;

public class UserHandler {


    private HashMap<String, String> users;

    private static UserHandler INSTANCE;

    private UserHandler() {
        this.users = new HashMap<>();
    }

    private boolean checkUserPassword(String user, String hash, String timbre) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return (this.users.containsKey(user) && PopSecurity.getMd5String(timbre + this.users.get(user)).equals(hash));
       // return (this.users.containsKey(user) && this.users.get(user).equals(hash));
    }

    private void putUser(String user, String password) {
        this.users.put(user, password);
    }

    public static void init() {
        if (INSTANCE == null)
            INSTANCE = new UserHandler();
        try {
            String globalPath = ConfigHandler.getParams("globalPath");
            String usersPath = ConfigHandler.getParams("usersPath");
            String usersFile = ConfigHandler.getParams("usersFile");
            String path = globalPath + usersPath + usersFile;

            InputStream flux = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(flux);
            BufferedReader buffer = new BufferedReader(reader);
            String line;
            while ((line = buffer.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, ":");
                String user = (String) st.nextElement();
                String password = (String) st.nextElement();
                INSTANCE.putUser(user, password);
            }
            buffer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static boolean checkAuth(String user, String hash ,String timbre) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        init();
        return INSTANCE.checkUserPassword(user, hash, timbre);
    }
}
