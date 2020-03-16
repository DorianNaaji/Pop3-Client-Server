package poly;

import poly.mailbox.Mailbox;
import poly.services.ConfigHandler;
import poly.error.NullConfigPathException;
import poly.services.UserHandler;
import poly.utils.PopSecurity;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringTokenizer;

import static junit.framework.Assert.assertEquals;

public class Server {

    public static final String CONFIG_PATH = "./src/poly/config.json";

    public static void init() {
        // INIT CONFIG CLASS
        ConfigHandler.setConfigPath(CONFIG_PATH);
        try {
            ConfigHandler.init();
        } catch (NullConfigPathException e) {
            e.printStackTrace();
        }
        UserHandler.init();
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        init();

        if (args != null && args.length > 0 && args[0].equalsIgnoreCase("--md5")) {
            System.out.println(PopSecurity.getMd5String(args[1]));
        } else {
            try {
                int port = Integer.parseInt(Objects.requireNonNull(ConfigHandler.getParams("port")));
               ServerSocket server = new ServerSocket(port);
                /*System.setProperty("jdk.tls.server.protocols", "TLSv1.2");
                SSLServerSocket sslServerSocket =
                        (SSLServerSocket)SSLServerSocketFactory.getDefault().createServerSocket(port);
                sslServerSocket.setEnabledCipherSuites(new String[] {
                        "SSL_DH_anon_WITH_RC4_128_MD5"
                });*/

                //sslServerSocket.setEnabledProtocols(SSLServerSocketFactory.getDefault().getDefaultCipherSuites());

                while (true) {
                    //Socket clientConnexion = sslServerSocket.accept();
                    Socket clientConnexion = server.accept();

                    if (clientConnexion != null) {
                        System.out.println("TCP connexion established");
                        Connexion connexion = new Connexion(clientConnexion);
                        if (connexion != null) {
                            Thread t = new Thread(connexion);
                            t.start();
                        } else {
                            System.out.println("connexion failed");
                        }
                    } else {
                        System.out.println("TC connexion failed");
                    }
                }
            } catch (IOException ioe) {
                System.err.println("[Cannot initialize Server]\n" + ioe);
                System.exit(1);
            }
        }
    }
}
