package poly.mailboxe;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MailBoxe {

    private String mailBoxePath;
    private String generalMailBoxePath;
    private String userName;

    private List<Mail> mails;

    public List<Mail> getMails(){
        return this.mails;
    }

    public MailBoxe(String mailBoxPath){
        this.mailBoxePath = mailBoxPath;
        this.init();
    }

    public MailBoxe(String generalMailBoxePath, String userName){
        this(generalMailBoxePath + '/' + userName);
        this.generalMailBoxePath = generalMailBoxePath;
        this.userName = userName;
        this.init();
    }

    private void init(){
        this.mails = MailBoxe.loadMailBoxe(this.generalMailBoxePath);
        System.out.println(this.mails.toString());
    }

    public static List<Mail> loadMailBoxe(String path) {
        List<Mail> mails = new ArrayList<>();
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            mails.add(new Mail(MailBoxe.readMail(file.getPath())));
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