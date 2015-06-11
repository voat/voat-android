package co.voat.android.data;

/**
 * Post, ya know?
 * Created by John on 6/11/2015.
 */
public class Post {
    int score;
    String author;
    String title;
    String imageUrl;
    String link;

    public Post(int score, String author, String title, String imageUrl, String link) {
        this.score = score;
        this.author = author;
        this.title = title;
        this.imageUrl = imageUrl;
        this.link = link;
    }

    public int getScore() {
        return score;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLink() {
        return link;
    }
}
