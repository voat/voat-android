package co.voat.android.data;

/**
 * Created by John on 6/11/2015.
 */
public class Subverse {
    String name;
    String title;
    String description;
    String creationDate;
    int subscriberCount;
    boolean ratedAdult;
    String sidebar;
    //TODO enum
    String type;

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public int getSubscriberCount() {
        return subscriberCount;
    }

    public boolean isRatedAdult() {
        return ratedAdult;
    }

    public String getSidebar() {
        return sidebar;
    }

    public String getType() {
        return type;
    }
}
