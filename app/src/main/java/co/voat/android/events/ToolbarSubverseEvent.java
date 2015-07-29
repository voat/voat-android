package co.voat.android.events;

import android.support.annotation.NonNull;

/**
 * Created by Jawn on 6/22/2015.
 */
public class ToolbarSubverseEvent {

    @NonNull
    public String subverse;

    public ToolbarSubverseEvent(@NonNull String subverse) {
        this.subverse = subverse;
    }
}
