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

public class Connexion implements Runnable {

    // TODO Maybe replace by System.lineSeparator()
    private static final String CARRIAGE_RETURN = "\r\n";

    private static final String CODE_OK = "+OK";
    private static final String CODE_ERR = "-ERR";

    private static final String SERVER_READY_MSG = CODE_OK + " Server ready";
    private static final String DEFAULT_ERR_MSG = CODE_ERR + " Unknown command or authentication failed";


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
                hash = hash.substring(0,hash.length() - 2);
                //replaceAll("[\r\n]+", "")
                if (UserHandler.checkAuth(user, hash)) {
                    authenticated = true;
                    this.mailBox = new Mailbox(user);
                    answer = CODE_OK;
                } else {
                    authenticated = false;
                    answer = CODE_ERR;
                }
                break;
            case Command.QUIT:
                // AUTH QUIT Scenario
                closeConnexion = true;
                answer = CODE_OK;
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
                // TODO STAT implementation to do
                break;
            case Command.LIST:
                // TODO LIST implementation to do
                break;
            case Command.RETR:
                // TODO RETR implementation to do
                break;
            case Command.QUIT:
                closeConnexion = true;
                // TODO answer not precise enough
                answer = CODE_OK;
                break;
            default:
                answer = DEFAULT_ERR_MSG;
                break;
        }
        return answer;
    }

}
