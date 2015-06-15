package co.voat.android.events;

import co.voat.android.data.Submission;

/**
 * Alert, alert! User has posted comment!
 * Created by Jawn on 6/15/2015.
 */
public class PostedCommentEvent {

    public Submission submission;
    public PostedCommentEvent(Submission submission) {
        this.submission = submission;
    }
}
