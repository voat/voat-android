package co.voat.android.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.design.widget.NavigationView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.R;
import co.voat.android.VoatApp;
import co.voat.android.data.User;
import co.voat.android.events.LoginEvent;
import co.voat.android.events.LogoffEvent;
import co.voat.android.utils.CommonColors;
import co.voat.android.utils.CommonDrawables;

/**
 * So that we dont have navigation specific stuff all over our activities
 * Created by Jawn on 6/14/2015.
 */
public class VoatNavigationView extends NavigationView {

    @InjectView(R.id.nav_header_image)
    ImageView headerImage;
    @InjectView(R.id.nav_header_user_image)
    ImageView headerUserImage;
    @InjectView(R.id.nav_header_username)
    TextView headerUsername;

    EventReceiver eventReceiver;

    public VoatNavigationView(Context context) {
        super(context);
        init();
    }

    public VoatNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VoatNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.inflateHeaderView(R.layout.nav_header);
        ButterKnife.inject(this);
        eventReceiver = new EventReceiver();
        bindUser();
    }

    public void bindUser() {
        Glide.with(getContext())
                .load(CommonDrawables.getRandomHeader())
                .into(headerImage);
        headerImage.setColorFilter(CommonColors.colorPrimary(getContext()), PorterDuff.Mode.MULTIPLY);
        User user = User.getCurrentUser();
        if (user != null) {
            headerUsername.setText(user.getUserName());
            if (!TextUtils.isEmpty(user.getProfilePicture())) {
                Glide.with(getContext())
                        .load(user.getProfilePicture())
                        .into(headerUserImage);
            } else {
                Glide.with(getContext())
                        .load(R.drawable.goat)
                        .into(headerUserImage);
            }
        } else {
            headerUserImage.setImageDrawable(null);
            headerUsername.setText(getContext().getString(R.string.log_in));
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        VoatApp.bus().register(eventReceiver);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        VoatApp.bus().unregister(eventReceiver);
    }

    private class EventReceiver {

        @Subscribe
        public void onLogoff(LogoffEvent event) {
            bindUser();
        }

        @Subscribe
        public void onLogin(LoginEvent event) {
            bindUser();
        }

    }
}
