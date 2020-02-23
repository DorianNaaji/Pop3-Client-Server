package businesslogic;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedReader bufferedReader;

    public Client(String adresseIP, int numeroPort) throws IOException {
        InetAddress inetAddressServer = InetAddress.getByName(adresseIP);
        socket = new Socket(inetAddressServer, numeroPort);
    }

    private void Connexion() throws IOException { //todo : à finir

        bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String reponse = bufferedReader.readLine();

        String [] tabConnexion = reponse.split(" ");

        if (tabConnexion[0].equals("-ERR"))
        {
            System.out.println("La connexion a échouée");
        }


    }

    private String Apop() throws IOException {
        String nom = ""; // voir comment récupérer nom et password à partir de la connexion
        String hashPassword = "";

        String commande = "APOP " +  nom + " " + hashPassword + "\r\n";
        System.out.println(commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("Réponse : " + reponse);

        return reponse;

    }


    private String Stat() throws IOException {

        String commande = "STAT" + "\r\n";
        System.out.println(commande);
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

        return reponse;

    }

    private void Quit() throws IOException {

        String commande = "QUIT ";
        System.out.println(commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();

        System.out.println("Réponse : " + reponse);

        socket.close();
        System.out.println("Fermeture de la connexion");


    }





}

