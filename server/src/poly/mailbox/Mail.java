package poly.mailbox;

import java.util.StringTokenizer;

public class Mail {

    private static final String CARRIAGE_RETURN = "\r\n";
    private static final String NEW_LINE = "\n";

    private String header;
    private String body;
    private String content;

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String getContent() {
        return content;
    }

    public byte[] getBytes() {
        return content.getBytes();
    }

    public Mail(String content){
        this.content = content;
        init();
    }

    private void init(){
        // TODO explode mail to get header & body
        this.content = this.content.replace(NEW_LINE, CARRIAGE_RETURN);
        String[] array = this.content.split(CARRIAGE_RETURN, -1);

        int i = 0;
        boolean endHeader = false;
        StringBuilder headerBuilder = new StringBuilder();

        while (i < array.length) {
            headerBuilder.append(array[i]).append(CARRIAGE_RETURN);
            i++;
        }

;    }
}
