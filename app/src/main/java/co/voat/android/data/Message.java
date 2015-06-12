package co.voat.android.data;

/**
 * Created by Jawn on 6/11/2015.
 */
public class Message {

    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;
    public static final int MESSAGE_TYPE_COMMENT = 4;
    public static final int MESSAGE_TYPE_SUBMISSION = 8;
    public static final int MESSAGE_TYPE_MENTION = 16;
    public static final int MESSAGE_TYPE_ALL = 31;

    public static final int MESSAGE_STATE_UNREAD = 1;
    public static final int MESSAGE_STATE_READ = 2;
    public static final int MESSAGE_STATE_ALL = 3;

    int id;
    int commentID;
    int submissionID;
    String subverse;
    String recipient;
    String sender;
    String subject;
    boolean unread;
    //TODO enum
    int type;
    String typeName;
    String sentDate;
    String content;
    String formattedContent;

    public int getId() {
        return id;
    }

    public int getCommentID() {
        return commentID;
    }

    public int getSubmissionID() {
        return submissionID;
    }

    public String getSubverse() {
        return subverse;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public boolean isUnread() {
        return unread;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSentDate() {
        return sentDate;
    }

    public String getContent() {
        return content;
    }

    public String getFormattedContent() {
        return formattedContent;
    }
}
