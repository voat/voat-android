package co.voat.android.data;

import java.io.Serializable;

/**
 * Created by Jawn on 6/11/2015.
 */
public class Message implements Serializable {

    public static final String MESSAGE_TYPE_INBOX = "Inbox";
    public static final String MESSAGE_TYPE_SENT = "Sent";
    public static final String MESSAGE_TYPE_COMMENT = "comment";
    public static final String MESSAGE_TYPE_SUBMISSION = "submission";
    public static final String MESSAGE_TYPE_MENTION = "mention";
    public static final String MESSAGE_TYPE_ALL = "all";
    public static final String[] MESSAGE_TYPES = {
            MESSAGE_TYPE_INBOX, MESSAGE_TYPE_SENT, MESSAGE_TYPE_COMMENT, MESSAGE_TYPE_SUBMISSION,
            MESSAGE_TYPE_MENTION, MESSAGE_TYPE_ALL
    };

    public static final String MESSAGE_STATE_UNREAD = "unread";
    public static final String MESSAGE_STATE_READ = "read";
    public static final String MESSAGE_STATE_ALL = "all";

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
