package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MailBox{

    private String mailBoxPath;
    private String personalMailBoxPath;
    private String userName;

    private List<Mail> mails;

    public List<Mail> getMails(){
        return this.mails;
    }

    public MailBox(String mailBoxPath, String userName){
        this.personalMailBoxPath = mailBoxPath + '/' + userName;
        this.mailBoxPath = mailBoxPath;
        this.userName = userName;
        this.init();
    }

    private void init(){
        this.mails = MailBox.loadMailBox(this.personalMailBoxPath);
        System.out.println(this.mails.toString());
    }

    public static List<Mail> loadMailBox(String path) {
        List<Mail> mails = new ArrayList<>();
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            mails.add(new Mail(MailBox.readMail(file.getPath())));
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