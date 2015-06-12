package co.voat.android.data;

import java.io.Serializable;
import java.util.List;

import co.voat.android.api.AuthResponse;

/**
 * User error
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
    AuthResponse authResponse;

    public User(String username, AuthResponse authResponse) {
        this.userName = username;
        this.authResponse = authResponse;
    }

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

    public AuthResponse getAuthToken() {
        return authResponse;
    }

    public void setAuthToken(AuthResponse authToken) {
        this.authResponse = authToken;
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
    }
}
