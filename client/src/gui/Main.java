package gui;

import customexceptions.MailImproperlyFormed;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Mail;
import model.MailRFC5322KeyValue;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/* HELP  :*/
/* "Could not find or load "Main" : https://stackoverflow.com/questions/10654120/error-could-not-find-or-load-main-class-in-intellij-ide */
/* "Create a JavaFX Project" : https://www.jetbrains.com/help/idea/javafx.html */

public class Main extends Application
{

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        this.test();
//        Client client = new Client();
//       Mail mail =  client.ess();

        /*ClientMainGui mainGui = new ClientMainGui(primaryStage);
        mainGui.show();

        Stage connexionDialog = new ClientConnexionDialog(mainGui);
        connexionDialog.show();*/
    }


    private final String[] RESERVED_WORDS = { "MIME-Version:", "Date:", "Subject:", "From:", "To:"};

    private Mail test() throws MailImproperlyFormed
    {
        String rawStringAsReceivedByServer =
                "MIME-Version: mano-mano\r\n" +
                        "Date: Mon, 14 Oct 2019 10:45:04 +0200\r\n" +
                        "Subject: Re: police !!\r\n" +
                        "From: dorian.naaji@coucou.com\r\n" +
                        "to: Delplanque Thibaut <thibaut.delplanque80@gmail.com>\r\n" +
                        "\r\n" +
                        "Un policier de la BAC de Toulon tue son ex-compagne avant de se suicider\r\n" +
                        "\r\n" +
                        "Il n'aurait pas accepté que sa conjointe veuille se séparer.\r\n" +
                        "Il s'est rendu à son domicile et lui a tiré dessus à plusieurs reprises avant de retourner l'arme contre lui.\r\n.\r\n";
        MailRFC5322KeyValue[] keyValues = new MailRFC5322KeyValue[6];
        Scanner scanner = new Scanner(rawStringAsReceivedByServer);
        // les 4 premières lignes sont forcément liées aux informations du mail (norme RFC 5322).
        try
        {
            for (int i = 0; i < 5; i++)
            {
                String line = scanner.nextLine();
                String[] lowerizedReservedWords = Arrays.stream(this.RESERVED_WORDS).map(s -> s.toLowerCase()).toArray(String[]::new);
                if (Arrays.stream(lowerizedReservedWords).parallel().anyMatch(line.toLowerCase()::contains))
                {
                    Optional<String> optReservedWord = Arrays.stream(this.RESERVED_WORDS).filter(line::contains).findAny();
                    // cause words are sometimes badly-typed.
                    Optional<String> optReservedWordLowerized = Arrays.stream(lowerizedReservedWords).filter(line.toLowerCase()::contains).findAny();
                    if (optReservedWord.isPresent())
                    {
                        keyValues[i] = new MailRFC5322KeyValue(optReservedWord.get(), line.substring(optReservedWord.get().length() + 1, line.length()));
                    }
                    //todo : voir si le nommage des clés (mots réservés) doit respecter une certaine norme (Majuscules, etc).
                    else
                    {
                        keyValues[i] = new MailRFC5322KeyValue(optReservedWordLowerized.get(), line.substring(optReservedWordLowerized.get().length() + 1, line.length()));
                    }
                }
            }
            scanner.close();
            // on passe désormais au corps du message
            String lastMetInfo = keyValues[4].toString();
            String body = rawStringAsReceivedByServer;
            // on enlève tout ce qu'il y a dans l'entête (indexOf(lastMetInfo) + lastMetInfo.length) ainsi que les retours chariots (qui sont TOUJOURS des \r\n donc au nombre de 4 ici).
            body = body.substring(body.indexOf(lastMetInfo) + lastMetInfo.length() + 4);
            // Il s'agit ensuite d'enlever les 5 derniers caractères (qui sont la fin du message, \r\n.\r\n)
            body = body.replace(body.substring(body.length() - 5), "");
            keyValues[5] = new MailRFC5322KeyValue(body);

            // on initialise ensuite un Mail.
            String mime = Arrays.stream(keyValues).filter(s -> s.getKey().toLowerCase().equals(this.RESERVED_WORDS[0])).findFirst().get().toString();
            String date = Arrays.stream(keyValues).filter(s -> s.getKey().toLowerCase().equals(this.RESERVED_WORDS[1])).findFirst().get().toString();
            String subject = Arrays.stream(keyValues).filter(s -> s.getKey().toLowerCase().equals(this.RESERVED_WORDS[2])).findFirst().get().toString();
            String from = Arrays.stream(keyValues).filter(s -> s.getKey().toLowerCase().equals(this.RESERVED_WORDS[3])).findFirst().get().toString();
            String to = Arrays.stream(keyValues).filter(s -> s.getKey().toLowerCase().equals(this.RESERVED_WORDS[4])).findFirst().get().toString();
            return new Mail(from, to, date, subject, keyValues[5].getValue(), mime);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            throw new MailImproperlyFormed("L'email ne respecte pas la norme RFC 5322.");
        }
    }

    public static boolean containsIgnoreCase(String str, String subString) {
        return str.toLowerCase().contains(subString.toLowerCase());
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}