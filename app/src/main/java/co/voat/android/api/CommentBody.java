package co.voat.android.api;

/**
 * Dat comment body. Its simple, I know
 * Created by Jawn on 6/13/2015.
 */
public class CommentBody {

    String value;

    private CommentBody() {
        //Nope, use the builder
    }

    public static class Builder {
        CommentBody body;
        public Builder(String comment) {
            body = new CommentBody();
            body.value = comment;
        }
        public CommentBody build() {
            return body;
        }
    }
}
