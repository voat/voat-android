package co.voat.android.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import co.voat.android.utils.CommonColors;

/**
 * Automagically colors actionbar icons according to the foreground color of the theme
 * Created by Jawn on 7/7/2015.
 */
public class VoatToolbar extends Toolbar {

    public VoatToolbar(Context context) {
        super(context);
    }

    public VoatToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VoatToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void inflateMenu(int resId) {
        super.inflateMenu(resId);
        for (int i=0; i<getMenu().size(); i++) {
            Drawable icon = getMenu().getItem(i).getIcon();
            if (icon != null) {
                icon.setColorFilter(CommonColors.colorForground(getContext()), PorterDuff.Mode.MULTIPLY);
            }
        }
    }

    @Override
    public void setNavigationIcon(Drawable icon) {
        super.setNavigationIcon(icon);
        if (getNavigationIcon() != null) {
            getNavigationIcon().setColorFilter(CommonColors.colorForground(getContext()), PorterDuff.Mode.MULTIPLY);
        }
    }
}
