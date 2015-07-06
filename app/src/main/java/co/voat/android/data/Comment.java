package co.voat.android.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Jawn on 6/11/2015.
 */
public class Comment implements Serializable{
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
