package businesslogic;

import customexceptions.MailImproperlyFormedException;
import customexceptions.ServerSideConnectException;
import model.Mail;

import java.io.*;
import customexceptions.ClosingConnexionException;
import model.User;

import javax.jws.soap.SOAPBinding;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Client
{

    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;
    private BufferedReader bufferedReader;
    private model.User user;

    private final String[] RESERVED_WORDS = {"Date:", "Subject:", "From:", "To:", "MIME-Version:", "OK"};

    public Client()
    {
    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public Socket getSocket() { return this.socket; }

    public Client(String adresseIP, int numeroPort) throws IOException, SocketTimeoutException, ServerSideConnectException
    {
        InetAddress inetAddressServer = InetAddress.getByName(adresseIP);
        socket = new Socket();
        //4s de timeout
        this.socket.connect(new InetSocketAddress(adresseIP, numeroPort), 4*1000);
        this.connexion();
        System.out.println("Etat : En attente d'autorisation");
    }


    public void connexion() throws IOException, ServerSideConnectException
    {

        bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("Etat : en attente de connexion");
        String reponse = bufferedReader.readLine();
        System.out.println("Événement : " + reponse);
        String [] tabConnexion = reponse.split(" ");

        if (tabConnexion[0].equals("-ERR"))
        {
            throw new ServerSideConnectException("La connexion avec le serveur a échoué");
        }
    }

    public boolean apop() throws IOException, NoSuchAlgorithmException
    {  // Méthode d'authentification :
        // renvoie vrai si authentifié, faux sinon

        String hashPassword = Security.getMd5String(user.getPassword()); // récupération du nom et du mdp grâce au front

        String commande = "APOP " + user.getName() + " " + hashPassword + "\r\n";
        System.out.println("Envoi d'une commande : " + commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();

        System.out.println("Evenement : " + reponse);
        System.out.println("État : en attente d'autorisation");
        System.out.println("Réponse : " + reponse);

        String tabReponse[] = reponse.split(" ");
        System.out.println("Evenement : " + tabReponse[0]);

        if (tabReponse[0].equals("+OK"))
        { // si la réponse du serveur commence par un +OK : la méthode retourne vrai
            // = authentifié
            return true;
        }
        else
        { // si la réponse du serveur commence par un -ERR : la méthode retourne faux
            return false;
        }
    }


    public String stat() throws IOException
    {
        String commande = "STAT" + "\r\n";
        System.out.println("Envoi d'une commande : " + commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();
        System.out.println("Evenement : " + reponse);

        return reponse;
    }

    public Mail retr(int numeroMessage) throws IOException, MailImproperlyFormedException
    {
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

        String reponse = this.readEverythingFromBufferedReader(this.bufferedReader);
        System.out.println("Evenement : " + reponse);

        try
        {
            Scanner scanner = new Scanner(reponse);
            while (scanner.hasNextLine())
            {
                line = scanner.nextLine();
                //System.out.println(line);
                // process the line
                String lineSplit[] = line.split(" ");
                int sizeLineSplit = lineSplit.length;
                for (int i = 0; i < sizeLineSplit; i++)
                {

                    if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[0]))
                    {
                        date.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[0], "")).append(" ");
                    }
                    if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[1]))
                    {

                        sujet.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[1], "")).append(" ");
                    }
                    if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[2]))
                    {
                        emetteur.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[2], "")).append(" ");
                    }
                    if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[3]))
                    {
                        destinataire.append(lineSplit[i].replaceAll("(?i)" + this.RESERVED_WORDS[3], "")).append(" ");
                    }
                    if (lineSplit[0].equalsIgnoreCase(this.RESERVED_WORDS[4]))
                    {
                        mime.append(lineSplit[i].replaceAll(this.RESERVED_WORDS[4], "")).append(" ");
                    }


                    if (!Arrays.stream(this.RESERVED_WORDS).anyMatch(lineSplit[0]::equalsIgnoreCase) && !lineSplit[0].replace("+", "").equals(this.RESERVED_WORDS[5]))
                    {
                        if (scanner.hasNextLine())
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

                //String contenuMail = reponse.substring(0, reponse.length() - 7); //contient le mail sans les éléments de fin : \r\n . \r\n
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new MailImproperlyFormedException("Le mail reçu côté serveur est incorrectement formé : " + mail.toString() + "\nRapport complet : " + e.getMessage());
        }

        Path currentRelativePath = Paths.get("");
        new File(currentRelativePath.toAbsolutePath().toString() + "\\Mail").mkdir();
        File fichier = new File(currentRelativePath.toAbsolutePath().toString() + "\\Mail\\mail" + numeroMessage + ".mail");
        PrintWriter out = new PrintWriter(new FileWriter(fichier));
        out.write(mail.getStringToWriteOnDisk());
        out.close(); //Ferme le flux du fichier, sauvegardant ainsi les données.
        return mail;
    }

    public void quit() throws IOException, ClosingConnexionException
    {
        String commande = "QUIT" + "\r\n";
        System.out.println("Envoi d'une commande : " + commande);
        //écriture et envoi
        bufferedOutputStream.write(commande.getBytes());
        bufferedOutputStream.flush();

        String reponse = bufferedReader.readLine();

        System.out.println("Evenement : " + reponse);

        String tabReponse[] = reponse.split(" ");

        if (tabReponse[0].equals("+OK"))
        {
            bufferedOutputStream.close(); // fermeture des flux
            bufferedOutputStream.close();
            socket.close(); // fermeture du socket

            System.out.println("Fermeture de la connexion");

            // todo : ne semble pas fonctionner (pas de fichiers supprimés sur disque)
            Path currentRelativePath = Paths.get("");
            String folderToPurge = currentRelativePath.toAbsolutePath().toString() + "\\Mail\\";
            Arrays.stream(new File(folderToPurge).listFiles()).forEach(File::delete);
        }
        else
        {
            throw new ClosingConnexionException("Une erreur est survenue lors de la" +
                    " fermeture de la connexion côté serveur.");
        }
    }

    public List<Mail> synchronisation() throws IOException, MailImproperlyFormedException
    {
        Mail mail = new Mail();
        List<Mail> listMail = new ArrayList<Mail>();

        String reponseStatCommand = stat();

        String[] tabReponseStatCommand = reponseStatCommand.split(" ");

        if (tabReponseStatCommand[0].equals("+OK"))
        {
            String nombreMessagesString = tabReponseStatCommand[1];
            int nombreMessages = Integer.parseInt(nombreMessagesString);

            for (int i = 1; i <= nombreMessages; i++)
            { // le premier element =  à l'indice 1
                mail = retr(i);
                listMail.add(mail);
            }
        }

        return listMail;

    }

    private String readEverythingFromBufferedReader(BufferedReader buffIn) throws IOException
    {
        StringBuilder content = new StringBuilder();
        String line;
        // since RETR responses are ended by a single dot....
        while( !(line = buffIn.readLine()).equals("."))
        {
            content.append(line + "\r\n");
        }
        return content.toString();
    }

}

