package co.voat.android.api;

import java.util.List;

import co.voat.android.data.Comment;

/**
 * Created by Jawn on 6/11/2015.
 */
public class CommentsResponse {
    public boolean success;
    public List<Comment> data;
    public String error;
}
