package co.voat.android.api;

/**
 * Start your submissions!
 * Created by Jawn on 6/12/2015.
 */
public class SubmissionBody {
    String title;
    boolean nsfw;
    boolean anon;
    String url;
    String content;

    private SubmissionBody() {
        //Nope, use the builder
    }

    public static class Builder {
        SubmissionBody body;
        public Builder(String title) {
            body = new SubmissionBody();
            body.title = title;
        }
        public Builder setNsfw(boolean nsfw) {
            body.nsfw = nsfw;
            return this;
        }
        public Builder setAnon(boolean anon) {
            body.anon = anon;
            return this;
        }
        public Builder setContent(String content) {
            body.content = content;
            return this;
        }
        public Builder setUrl(String url) {
            body.url = url;
            return this;
        }
        public SubmissionBody build() {
            return body;
        }
    }
}
