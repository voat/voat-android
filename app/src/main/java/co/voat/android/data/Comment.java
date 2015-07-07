package co.voat.android.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Jawn on 6/11/2015.
 */
public class Comment implements Serializable{

    public static List<Comment> groupComments(List<Comment> originalComments) {

        for (int i=0; i<originalComments.size(); i++) {
            Comment comment = originalComments.get(i);
            if (comment.getParentId() > 0) {
                for (int j=0; j<originalComments.size(); j++) {
                    Comment parentComment = originalComments.get(j);
                    if (parentComment.getParentId() == comment.getParentId()) {
                        originalComments.remove(comment);
                        originalComments.add(originalComments.indexOf(parentComment)+1, comment);
                    }
                }
            }
        }
        return originalComments;
    }

    int id;
    int parentID;
    int submissionID;
    String subverse;
    Date date;
    String lastEditDate;
    int upVotes;
    int downVotes;
    String userName;
    int childCount;
    int level;
    String content;
    String formattedContent;

    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentID;
    }

    public int getSubmissionId() {
        return submissionID;
    }

    public String getSubverse() {
        return subverse;
    }

    public Date getDate() {
        return date;
    }

    public String getLastEditDate() {
        return lastEditDate;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public String getUserName() {
        return userName;
    }

    public int getChildCount() {
        return childCount;
    }

    public int getLevel() {
        return level;
    }

    public String getContent() {
        return content;
    }

    public String getFormattedContent() {
        return formattedContent;
    }
}
