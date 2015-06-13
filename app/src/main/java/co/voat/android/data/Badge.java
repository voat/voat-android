package co.voat.android.data;

/**
 * Created by Jawn on 6/11/2015.
 */
public class Badge {

    public static Badge createFakeBadge() {
        Badge badge = new Badge();
        badge.name = "Developer";
        badge.awardDate =" bleh";
        badge.title = "Made significant contributions to Voat codebase";
        badge.badgeGraphic = "http://fakevout.azurewebsites.net//Graphics/Badges/developer.png";
        return badge;
    }

    String name;
    String awardDate;
    String title;
    String badgeGraphic;

    public String getName() {
        return name;
    }

    public String getAwardDate() {
        return awardDate;
    }

    public String getTitle() {
        return title;
    }

    public String getBadgeGraphic() {
        return badgeGraphic;
    }
}
