package poly.mailbox;

import java.util.StringTokenizer;

public class Mail {

    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String NEW_LINE = "\n";

    private String header;
    private String body;

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String getContent() {
        return this.header + CARRIAGE_RETURN + this.body;
    }

    public byte[] getBytes() {
        return new String(this.header + CARRIAGE_RETURN + this.body).getBytes();
    }

    public Mail(String content){
        init(content);
    }

    private void init(String content){
        content = content.replace(NEW_LINE, CARRIAGE_RETURN);
        String[] array = content.split(CARRIAGE_RETURN, -1);

        int i = 0;
        boolean endHeader = false;
        StringBuilder headerBuilder = new StringBuilder();
        StringBuilder bodyBuilder = new StringBuilder();

        while (i < array.length) {
            if(array[i].equals("")) {
                endHeader = true;
            } else {
                if (endHeader) {
                    bodyBuilder.append(array[i]).append(CARRIAGE_RETURN);
                } else {
                    headerBuilder.append(array[i]).append(CARRIAGE_RETURN);
                }
            }
            i++;
        }
        this.body = bodyBuilder.toString();
        this.header = headerBuilder.toString();
;    }
}
