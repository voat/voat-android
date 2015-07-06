package co.voat.android.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import co.voat.android.utils.CommonColors;

/**
 * Just so that we do not have to keep setting the colors everywhere
 * Created by Jawn on 7/5/2015.
 */
public class VoatSwipeRefreshLayout extends SwipeRefreshLayout {

    public VoatSwipeRefreshLayout(Context context) {
        super(context);
        init();
    }

    public VoatSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setColorSchemeColors(CommonColors.colorAccent(getContext()));
    }
}
