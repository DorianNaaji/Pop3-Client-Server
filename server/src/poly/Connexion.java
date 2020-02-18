package poly;

import poly.mailbox.Mailbox;
import poly.services.UserHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Connexion implements Runnable {
    private Socket socket;
    private BufferedOutputStream writer;
    private BufferedInputStream reader;
    private boolean authentified = false;
    private boolean closeConnexion = false;

    private Mailbox mailBox;


    public Connexion(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("instance of Socket can not be null");
        }
        this.socket = socket;
        writer = new BufferedOutputStream(socket.getOutputStream());
        reader = new BufferedInputStream(socket.getInputStream());

        // TODO get global path + userName
        // this.mailBox = new MailBox(this.l)

    }

    public void run() {
        System.err.println("Lancement du processus d'authentification");
        String command = "null";
        String response = "+OK: server ready";
        try {
            writer.write((response + "\n").getBytes());
            writer.flush();
            while (!socket.isClosed()) {

                //on attend une commande du client
                System.out.println("WAIT");
                try {
                    command = read();

                    System.out.println("READ " + command + " " + authentified);
                    List<String> lcommand = explode(command);
                    if (!authentified) {
                        response = authentification(lcommand, response);
                        if (response.equalsIgnoreCase("+OK")) {
                            authentified = true;
                        }

                    } else response = communication(lcommand, response);
                    //On envoie la réponse au client
                    writer.write((response + "\n").getBytes());
                    writer.flush();


                    if (closeConnexion) {
                        System.err.println("Deconnexion");
                        socket.close();
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Pas de commande saisie");
                }
            }
        } catch (SocketException e) {
            System.err.println("Connexion interrompue");
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private List<String> explode(String command) {
        List<String> lcommande = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(command, " ");
        while (st.hasMoreElements()) {
            lcommande.add((String) st.nextElement());
        }
        return lcommande;
    }

    private String read() throws IOException {
        String clientCommand = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        clientCommand = new String(b, 0, stream);
        clientCommand = clientCommand.replaceAll("[\r\n]+", "");
        return clientCommand;
    }

    public String authentification(List<String> lcommand, String response) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String command = lcommand.get(0);
        switch (command.toUpperCase()) {
            case "APOP":

                String user = lcommand.get(1);
                String passwd = lcommand.get(2);
                if (UserHandler.checkAuth(user, passwd)) {
                    authentified = true;
                    response = "+OK";
                } else {
                    authentified = false;
                    response = "-ERR";
                }

                break;
            case "QUIT":
                //traitement cas quit
                closeConnexion = true;
                response = "+OK";
                break;
            default:
                //traitement cas default
                response = "-ERR : Commande inconnue ou identification non effectuee";

                break;
        }
        return response;
    }

    public String communication(List<String> lcommand, String response) {
        String command = lcommand.get(0);

        switch (command.toUpperCase()) {
            case "STAT":
                //traitement stat
                break;
            case "LIST":
                //traitement list
                break;
            case "RETR":
                //traitment retr
                break;
            case "QUIT":
                closeConnexion = true;
                response = "+OK";
                break;
            default:
                response = "Commande inconnue ou identification non effectuee";
                break;


        }
        return response;
    }

}
