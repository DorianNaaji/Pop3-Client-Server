package businesslogic;

import customexceptions.ClosingConnexionException;
import customexceptions.OpeningConnexionException;

import javax.jws.soap.SOAPBinding;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;

public class Client {

    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedReader bufferedReader;
    private model.User user;



    public Client(String adresseIP, int numeroPort) throws IOException, SocketTimeoutException, OpeningConnexionException
    {
        InetAddress inetAddressServer = InetAddress.getByName(adresseIP);
        socket = new Socket();
        //4s de timeout
        this.socket.connect(new InetSocketAddress(adresseIP, numeroPort), 4*1000);
        Connexion();
    }

    private void Connexion() throws IOException, OpeningConnexionException
    {

        bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String reponse = bufferedReader.readLine();
        System.out.println("État : En attente de connexion");
        System.out.println("Événement : " + reponse);
        String [] tabConnexion = reponse.split(" ");

        if (tabConnexion[0].equals("-ERR"))
        {
            throw new OpeningConnexionException("La connexion avec le serveur a échoué.");
        }

    }

    public boolean Apop() throws IOException, NoSuchAlgorithmException {  // Méthode d'authentification :
        // renvoie vrai si authentifié, faux sinon

        String hashPassword = Security.getMd5String(user.getPassword()); // récupération du nom et du mdp grâce au front

        String commande = "APOP " +  user.getName() + " " + hashPassword + "\r\n";
        System.out.println("Envoi d'une commande : " + commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("État : en attente d'autorisation");
        System.out.println("Réponse : " + reponse);

        String tabReponse[] = reponse.split(" ");
        System.out.println("Evenement : " + tabReponse[0]);

        if (tabReponse[0] == "+OK") { // si la réponse du serveur commence par un +OK : la méthode retourne vrai
            // = authentifié
            return true;
        }
        else { // si la réponse du serveur commence par un -ERR : la méthode retourne faux
            return false;
        }

    }


    private String Stat() throws IOException {

        String commande = "STAT" + "\r\n";
        System.out.println("Envoi d'une commande : " + commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("Réponse : " + reponse);

        return reponse;

    }

    private String Retr(int numeroMessage) throws IOException {

        String commande = "RETR " + numeroMessage + "\r\n";
        System.out.println(commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("Réponse : " + reponse);

        String tabReponse[] = reponse.split(" ");

        return reponse;

    }

    public void Quit() throws IOException, ClosingConnexionException {

        String commande = "QUIT" + "\r\n";
        System.out.println("Envoi d'une commande : " + commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();

        System.out.println("Réponse : " + reponse);

        String tabReponse[] = reponse.split(" ");
        if (tabReponse[0].equals("+OK")) {

            bufferedOutputStream.close(); // fermeture des flux
            bufferedOutputStream.close();
            socket.close(); // fermeture du socket

            System.out.println("Fermeture de la connexion");
        }
        else {
            throw new ClosingConnexionException("Une erreur est survenue lors de la" +
                    " fermeture de la connexion côté serveur.");
        }

    }



}

