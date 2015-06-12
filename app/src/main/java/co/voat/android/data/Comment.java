package co.voat.android.data;

/**
 * Created by Jawn on 6/11/2015.
 */
public class Comment {
    int id;
    //TODO api returns null... ug
    int parentId;
    int submissionId;
    String subverse;
    String date;
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
        return parentId;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public String getSubverse() {
        return subverse;
    }

    public String getDate() {
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
