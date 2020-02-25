package poly;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * the Command class makes it possible to represent an "APOP" command received from the client,
 * it allows easily to access to either the keyword of the command or its parameters.
 */
class Command {

    /** USER command keyword */
    public final static String USER = "USER";
    /** PASS command keyword */
    public final static String PASS = "PASS";
    /** APOP command keyword */
    public final static String APOP = "APOP";
    /** STAT command keyword */
    public final static String STAT = "STAT";
    /** LIST command keyword */
    public final static String LIST = "LIST";
    /** RETR command keyword */
    public final static String RETR = "RETR";
    /** QUIT command keyword */
    public final static String QUIT = "QUIT";

    /** Keyword command */
    private String command;
    /** command's params */
    private List<String> params;

    /**
     * Get the command's keyword
     * @return, return a string
     */
    public String getCommand() {
        return command.toUpperCase();
    }

    /**
     * Get the i-th command's param
     * @param index, an int for i-th param
     * @return, return a string
     */
    public String getParam(int index) {
        if (index < params.size())
            return params.get(index);
        return null;
    }

    /**
     * Get all command's params
     * @return, return a String array
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * Get the number of params
     * @return, return an int
     */
    public int numberParams() {
        return params.size();
    }

    /**
     * Create a new Commande instance with the raw command from client
     * @param raw, a string within client's command
     */
    public Command(String raw) {
        this.params = new ArrayList<>();
        explode(raw);
    }

    /**
     * explodes the client's raw command to get the command's keyword and params
     * @param raw, a string within client's command
     */
    private void explode(String raw) {
        raw = raw.replaceAll("\r\n", "");
        StringTokenizer stringTokenizer = new StringTokenizer(raw, " ");
        // Get the command's keyword
        if (stringTokenizer.hasMoreElements())
            command = stringTokenizer.nextToken();
        // Get the params
        while (stringTokenizer.hasMoreElements())
            this.params.add(stringTokenizer.nextElement().toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(command).append(" ");
        params.forEach( p -> builder.append(p.toString()).append(" "));
        return builder.toString();
    }
}