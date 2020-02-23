package poly;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

class Command {

    public final static String USER = "USER";
    public final static String PASS = "PASS";
    public final static String APOP = "APOP";
    public final static String STAT = "STAT";
    public final static String LIST = "LIST";
    public final static String RETR = "RETR";
    public final static String QUIT = "QUIT";


    private String command;
    private List<String> params;

    public String getCommand() {
        return command.toUpperCase();
    }

    public String getParam(int index) {
        if (index < params.size())
            return params.get(index);
        return null;
    }

    public List<String> getParams() {
        return params;
    }

    public int numberParams() {
        return params.size();
    }

    public Command(String raw) {
        this.params = new ArrayList<>();
        explode(raw);
    }

    private void explode(String raw) {
        StringTokenizer stringTokenizer = new StringTokenizer(raw, " ");
        // Get the command
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