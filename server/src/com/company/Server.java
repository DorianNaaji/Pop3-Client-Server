package com.company;

import com.company.security.PopSecurity;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Server {
    public HashMap<String, String> getLpasswd() {
        return lpasswd;
    }

    public void setLpasswd(HashMap<String, String> lpasswd) {
        this.lpasswd = lpasswd;
    }

    private HashMap<String, String> lpasswd = new HashMap<String, String>();

    private void initUser() {
        try {
            // TODO franglais !!
            InputStream flux = new FileInputStream("/home/cecile/pwd");
            InputStreamReader lecture = new InputStreamReader(flux);
            BufferedReader buff = new BufferedReader(lecture);
            String ligne;
            while ((ligne = buff.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(ligne, ":");
                String user = (String) st.nextElement();
                String pwd = (String) st.nextElement();
                lpasswd.put(user, pwd);
            }
            buff.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }


    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        if (args != null && args.length > 0 && args[0].equalsIgnoreCase("--md5")) {
            System.out.println(PopSecurity.getMd5String(args[1]));
        } else {
            try {
                ServerSocket server = new ServerSocket(1025);
                Server myserver = new Server();
                myserver.initUser();
                //server.setSoTimeout(1000);
                while (true) {
                    Socket clientConnexion = server.accept();
                    if (clientConnexion != null) {
                        System.out.println("Connexion TCP etablie");
                        Connexion con = new Connexion(clientConnexion, myserver.getLpasswd());
                        if (con != null) {
                            Thread t = new Thread(con);
                            t.start();
                        } else {
                            System.out.println("=======NO CONNEXIONJ");
                        }
                    } else {
                        System.out.println("=======NO CONNEXIONJ");
                    }
                }
            } catch (IOException ioe) {
                System.err.println("[Cannot initialize Server]\n" + ioe);
                System.exit(1);
            }
        }
    }
}
