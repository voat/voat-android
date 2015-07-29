package co.voat.android.data;

import java.io.Serializable;

/**
 * Please like, comment, and subscribe for more.
 * Created by Jawn on 6/11/2015.
 */
public class Subscription implements Serializable {

    public static final int TYPE_SUBVERSE = 1;
    public static final int TYPE_SET = 2;

    int type;
    String typeName;
    String name;

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getName() {
        return name;
    }
}
