package poly;

import poly.mailbox.Mail;
import poly.mailbox.Mailbox;
import poly.services.ConfigHandler;
import poly.services.UserHandler;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class Connexion implements Runnable {

    // TODO Maybe replace by System.lineSeparator()
    private static final String CARRIAGE_RETURN = "\r\n";

    private static final String CODE_OK = "+OK";
    private static final String CODE_ERR = "-ERR";

    private static final String SERVER_READY_MSG = CODE_OK + " Server ready";
    private static final String DEFAULT_ERR_MSG = CODE_ERR + " Unknown command or authentication failed";

    private static final String SPECIFY_EMAIL_NUMBER = CODE_ERR + " You must specify the number of an email between 1 and n";
    private static final String SPECIFY_VALID_EMAIL_NUMBER = CODE_ERR + " You specify a valid email number";
    private static final String NOT_EMAIL_FOR_THIS_NUMBER = CODE_ERR + " You do not have an email corresponding to this number";




    /** boolean value, represents the state of the user, authenticated or not */
    private boolean authenticated = false;
    /** boolean value, represents the state of the connexion */
    private boolean closeConnexion = false; // TODO USELESS ??

    /** Socket instance for connexion */
    private Socket socket;
    private BufferedOutputStream writer;
    private BufferedInputStream reader;

    /**
     * user's mailbox, it is instantiated only if the user is authenticated and if he has a mailbox
     * if authentication fails, then server return -ERR to client
     * if user's mailbox doesn't exist, then create a new folder
     */
    private Mailbox mailBox;

    /**
     * create a new instance of connexion
     * @param socket used for the connexion
     * @throws IOException, only if the socket instance is null
     */
    public Connexion(Socket socket) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("instance of Socket can not be null");
        }
        this.socket = socket;
        writer = new BufferedOutputStream(socket.getOutputStream());
        reader = new BufferedInputStream(socket.getInputStream());
    }

    // TODO review the run method, if we can structure it better
    public void run() {
        System.err.println("Launch of the authentication process");
        Command command;
        String answer;
        try {
            writer.write((SERVER_READY_MSG + CARRIAGE_RETURN).getBytes());
            writer.flush();
            while (!socket.isClosed()) {
                try {
                    // waiting for Client action
                    System.out.println("WAIT");
                    command = new Command(read());
                    System.out.printf("AUTH: %s CMD: %s%n", authenticated, command);

                    if (!authenticated) {
                        answer = authentication(command);
                    } else answer = communication(command);

                    // We send the response back to the client
                    writer.write((answer + "\n").getBytes());
                    writer.flush();
                    if (closeConnexion) {
                        System.err.println("Disconnection");
                        socket.close();
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("No command entered");
                }
            }
        } catch (SocketException e) {
            System.err.println("Connection interrupted");
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // TODO Check whether the read method respects all message forms
    /**
     * The read method, reads incoming commands on the socket from the client
     * @return, returns a String containing the client's command
     * @throws IOException
     */
    private String read() throws IOException {
        StringBuilder builder = new StringBuilder();
        String buffer;
        int stream;
        do {
            byte[] b = new byte[4096];
            stream = reader.read(b);
            buffer = new String(b, 0, stream);
            builder.append(buffer);
        } while(!buffer.contains(CARRIAGE_RETURN));
        return builder.toString();
    }

    public String authentication(Command command) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String answer;
        switch (command.getCommand()) {
            case Command.APOP:
                String user = command.getParam(0);
                String hash = command.getParam(1);
                if (UserHandler.checkAuth(user, hash)) {
                    authenticated = true;
                    this.mailBox = new Mailbox(
                            ConfigHandler.getParams("globalPath") +
                            ConfigHandler.getParams("mailboxesPath") + "/", user);
                    answer = CODE_OK;
                } else {
                    authenticated = false;
                    answer = CODE_ERR;
                }
                break;
            case Command.QUIT:
                // AUTH QUIT Scenario
                closeConnexion = true;
                answer = CODE_OK + " POP3 server signing off";;
                break;
            default:
                // AUTH Default scenario
                answer = DEFAULT_ERR_MSG;
                break;
        }
        return answer;
    }

    public String communication(Command command) {
        String answer = "";
        switch (command.getCommand()) {
            case Command.STAT:
                this.mailBox.refresh();
                answer = CODE_OK + " " + stat();
                break;
            case Command.LIST:
                this.mailBox.refresh();
                // TODO LIST implementation to do
                break;
            case Command.RETR:
                answer = retreiveMail(command);
                break;
            case Command.QUIT:
                closeConnexion = true;
                // TODO answer not precise enough
                answer = CODE_OK + " POP3 server signing off";
                break;
            default:
                answer = DEFAULT_ERR_MSG;
                break;
        }
        return answer;
    }

    public String stat() {
        List<Mail> mails = mailBox.getMails();
        int mailNumber = 0;
        int mailSize = 0;
        String anwser;
        for (Mail mail : mails) {
            mailNumber++;
            mailSize = mail.getContent().getBytes().length;
        }
        anwser = mailNumber + " " + mailSize;
        return anwser;
    }
    private String retreiveMail(Command command) {
        if (command.getParams().isEmpty()) {
            return SPECIFY_EMAIL_NUMBER;
        }
        String param = command.getParam(0);
        int mailNumber;
        try {
            mailNumber = Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return SPECIFY_VALID_EMAIL_NUMBER;
        }

        if (mailNumber <= 0 || mailNumber > mailBox.getMails().size()) {
            return NOT_EMAIL_FOR_THIS_NUMBER;
        }

        Mail mail = mailBox.getMails().get(mailNumber - 1);
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(CODE_OK).append(" ")
                .append(mail.getBytes().length)
                .append(" octets")
                .append(CARRIAGE_RETURN)
                .append(mail.getContent())
                .append(CARRIAGE_RETURN)
                .append(".")
                .append(CARRIAGE_RETURN);

        return stringBuilder.toString();
    }

}




