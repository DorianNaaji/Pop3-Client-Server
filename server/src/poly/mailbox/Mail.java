package poly.mailbox;

public class Mail {

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
    }

    public Mail(String header, String body){
        this.header = header;
        this.body = body;
        this.content = header + body;
    }

    private void explodeContent(){
        // TODO explode mail to get header & body
    }
}
