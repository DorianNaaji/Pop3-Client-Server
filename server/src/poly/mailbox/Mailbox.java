package poly.mailbox;

import poly.services.ConfigHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Mailbox {

    // TODO personalMailbox
    private String mailboxPath;
    private String generalMailboxPath = ConfigHandler.getParams("generalMailBoxes");
    private String userName;

    private List<Mail> mails;

    public List<Mail> getMails(){
        return this.mails;
    }

    /*public Mailbox(String mailboxPath){
        this.mailboxPath = mailboxPath;
        this.init();
    }*/

    public Mailbox(String userName){
        this.mailboxPath = (generalMailboxPath + '/' + userName);
       // this.generalMailboxPath = generalMailboxPath;
        this.userName = userName;
        this.init();
    }

    private void init(){
        this.mails = Mailbox.loadMailbox(this.mailboxPath);
        System.out.println(this.mails.toString());
    }

    public static List<Mail> loadMailbox(String path) {
        List<Mail> mails = new ArrayList<>();
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            mails.add(new Mail(Mailbox.readMail(file.getPath())));
        }
        return mails;
    }

    public static String readMail(String path) {
        StringBuilder contentFile = new StringBuilder();
        BufferedReader buffer = null;
        try {
            InputStream flux = new FileInputStream(path);
            InputStreamReader reader = new InputStreamReader(flux);
            buffer = new BufferedReader(reader);
            String line;
            while ((line = buffer.readLine()) != null) {
                contentFile.append(line).append("\n");
            }
            buffer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentFile.toString();
    }
}