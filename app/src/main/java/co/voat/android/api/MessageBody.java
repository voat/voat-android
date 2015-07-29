package co.voat.android.api;

/**
 * Dat message body
 * Created by Jawn on 6/13/2015.
 */
public class MessageBody {

    String recipient;
    String subject;
    String message;

    private MessageBody() {
        //Nope, use the builder
    }

    public static class Builder {
        MessageBody body;
        public Builder(String recipient, String subject, String message) {
            body = new MessageBody();
            body.recipient = recipient;
            body.subject = subject;
            body.message = message;
        }
        public MessageBody build() {
            return body;
        }
    }
}
