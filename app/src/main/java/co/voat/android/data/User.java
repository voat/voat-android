package co.voat.android.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jawn on 6/11/2015.
 */
public class User implements Serializable {

    static User currentUser;
    public static User getCurrentUser() {
        return currentUser;
    }
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    String userName;
    String registrationDate;
    String bio;
    String profilePicture;
    Points commentPoints;
    Points submissionPoints;
    Points commentVoting;
    Points submissionVoting;
    List<Badge> badges;
    //Custom
    String authToken;

    public String getUserName() {
        return userName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public Points getCommentPoints() {
        return commentPoints;
    }

    public Points getSubmissionPoints() {
        return submissionPoints;
    }

    public Points getCommentVoting() {
        return commentVoting;
    }

    public Points getSubmissionVoting() {
        return submissionVoting;
    }

    public List<Badge> getBadges() {
        return badges;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public static class Preferences {
        String userName;
        boolean disableCustomCSS;
        boolean enableNightMode;
        String language;
        boolean openLinkNewWindow;
        boolean enableAdultContent;
        boolean publiclyDisplayVotes;
        boolean publiclyDisplaySubscriptions;

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setDisableCustomCSS(boolean disableCustomCSS) {
            this.disableCustomCSS = disableCustomCSS;
        }

        public void setEnableNightMode(boolean enableNightMode) {
            this.enableNightMode = enableNightMode;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public void setOpenLinkNewWindow(boolean openLinkNewWindow) {
            this.openLinkNewWindow = openLinkNewWindow;
        }

        public void setEnableAdultContent(boolean enableAdultContent) {
            this.enableAdultContent = enableAdultContent;
        }

        public void setPubliclyDisplayVotes(boolean publiclyDisplayVotes) {
            this.publiclyDisplayVotes = publiclyDisplayVotes;
        }

        public void setPubliclyDisplaySubscriptions(boolean publiclyDisplaySubscriptions) {
            this.publiclyDisplaySubscriptions = publiclyDisplaySubscriptions;
        }

        public String getUserName() {
            return userName;
        }

        public boolean isDisableCustomCSS() {
            return disableCustomCSS;
        }

        public boolean isEnableNightMode() {
            return enableNightMode;
        }

        public String getLanguage() {
            return language;
        }

        public boolean isOpenLinkNewWindow() {
            return openLinkNewWindow;
        }

        public boolean isEnableAdultContent() {
            return enableAdultContent;
        }

        public boolean isPubliclyDisplayVotes() {
            return publiclyDisplayVotes;
        }

        public boolean isPubliclyDisplaySubscriptions() {
            return publiclyDisplaySubscriptions;
        }
    }
}
