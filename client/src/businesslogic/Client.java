package businesslogic;

import model.Mail;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedReader bufferedReader;
    private model.User user;

    private final String[] RESERVED_WORDS = { "Date:", "Subject:", "From:", "To:", "MIME-Version:"};

    public Client()
    {

    }

    public Client(String adresseIP, int numeroPort) throws IOException, SocketTimeoutException
    {
        InetAddress inetAddressServer = InetAddress.getByName(adresseIP);
        socket = new Socket();
        //4s de timeout
        this.socket.connect(new InetSocketAddress(adresseIP, numeroPort), 4*1000);
        connexion();
    }


    private void connexion() throws IOException { //todo : à finir

        bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String reponse = bufferedReader.readLine();

        String [] tabConnexion = reponse.split(" ");

        if (tabConnexion[0].equals("-ERR"))
        {
            System.out.println("La connexion a échouée");
        }


    }

    private boolean apop() throws IOException, NoSuchAlgorithmException {  // Méthode d'authentification :
        // renvoie vrai si authentifié, faux sinon

        String hashPassword = Security.getMd5String(user.getPassword()); // récupération du nom et du mdp grâce au front

        String commande = "APOP " +  user.getName() + " " + hashPassword + "\r\n";
        System.out.println(commande);

        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("Evenement : " + reponse);

        String tabReponse[] = reponse.split(" ");
        System.out.println("Etat (+OK ou -ERR) : " + tabReponse[0]);

        if (tabReponse[0].equals("+OK")) { // si la réponse du serveur commence par un +OK : la méthode retourne vrai
            // = authentifié
            return true;
        }
        else { // si la réponse du serveur commence par un -ERR : la méthode retourne faux
            return false;
        }

    }


    private String stat() throws IOException {

        String commande = "STAT" + "\r\n";
        System.out.println(commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("Evenement : " + reponse);

        return reponse;

    }

    private Mail retr(int numeroMessage) throws IOException {

        Mail mail = new Mail();
        StringBuilder mime = new StringBuilder();
        StringBuilder date = new StringBuilder();
        StringBuilder sujet = new StringBuilder();
        StringBuilder destinataire = new StringBuilder();
        StringBuilder emetteur = new StringBuilder();
        StringBuilder corps = new StringBuilder();

        String line = "";

        String commande = "RETR " + numeroMessage + "\r\n";
        System.out.println(commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("Evenement : " + reponse);


        Scanner scanner = new Scanner(reponse);
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            //System.out.println(line);
            // process the line
            String lineSplit[] = line.split(" ");
            int sizeLineSplit = lineSplit.length;
            for (int i = 0; i < sizeLineSplit; i++) {

                if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[0])) {
                    date.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[0], "")).append(" ");
                }
                if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[1])) {

                    sujet.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[1], "")).append(" ");
                }
                if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[2])) {
                    emetteur.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[2], "")).append(" ");
                }
                if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[3])) {
                    destinataire.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[3], "")).append(" ");
                }
                if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[4])) {
                    mime.append(lineSplit[i].replaceAll(this.RESERVED_WORDS[4], "")).append(" ");
                }

                if(!Arrays.stream(this.RESERVED_WORDS).anyMatch(lineSplit[0]::equalsIgnoreCase))
                {
                    if(scanner.hasNextLine())
                    {
                        corps.append(lineSplit[i]).append(" ");
                    }
                }
            }
            corps.append("\r\n");

            mail.setMime(mime.toString().trim());
            mail.setSujet(sujet.toString().trim());
            mail.setDate(date.toString().trim());
            mail.setDestinataire(destinataire.toString().trim());
            mail.setEmetteur(emetteur.toString().trim());
            mail.setCorps(corps.toString().trim());
            
        }
        return mail;
    }

    private void quit() throws IOException {

        String commande = "QUIT" + "\r\n";
        System.out.println(commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();

        System.out.println("Evenement : " + reponse);

        String tabReponse[] = reponse.split(" ");
        if (tabReponse[0] == "+OK") {

            bufferedOutputStream.close(); // fermeture des flux
            bufferedOutputStream.close();
            socket.close(); // fermeture du socket

            System.out.println("Fermeture de la connexion");
        }
        else {
            System.out.println("Erreur lors de la fermeture de la connexion");
        }

    }



}

