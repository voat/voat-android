package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.data.Badge;
import co.voat.android.data.User;
import co.voat.android.events.LogoffEvent;
import co.voat.android.util.ColorUtils;
import co.voat.android.util.CommonColors;
import co.voat.android.util.CommonStrings;
import co.voat.android.viewHolders.BadgeViewHolder;

/**
 * Check out this user... so lame
 * Created by Jawn on 6/12/2015.
 */
public class UserActivity extends BaseActivity {

    private static final String EXTRA_USER = "extra_user";

    public static Intent newInstance(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        if (user != null) {
            intent.putExtra(EXTRA_USER, user);
        }
        return intent;
    }

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.backdrop)
    ImageView backdrop;
    @InjectView(R.id.user_image)
    ImageView userImage;
    @InjectView(R.id.username)
    TextView usernameText;
    @InjectView(R.id.user_bio)
    TextView bioText;
    @InjectView(R.id.user_member_time)
    TextView memberTimeText;
    @InjectView(R.id.user_cp)
    TextView cpText;
    @InjectView(R.id.badge_list)
    RecyclerView badgeList;

    User user;

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private final Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_log_off:
                    User.setCurrentUser(null);
                    VoatPrefs.clearUser(UserActivity.this);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VoatApp.bus().post(new LogoffEvent());
                        }
                    }, 450);
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.inject(this);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        toolbar.inflateMenu(R.menu.menu_user);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        if (user == null) {
            user = User.getCurrentUser();
        }
        bind(user);
    }

    private void bind(User user) {
        if (!TextUtils.isEmpty(user.getProfilePicture())) {
            Glide.with(this)
                    .load(user.getProfilePicture())
                    .into(userImage);
        } else {
            Glide.with(this)
                    .load(R.drawable.goat)
                    .into(userImage);
        }
        usernameText.setText(user.getUserName());
        if (!TextUtils.isEmpty(user.getBio())) {
            bioText.setText(user.getBio());
        } else {
            bioText.setText(getString(R.string.error_no_bio));
        }
        memberTimeText.setText(R.string.member_for);
        memberTimeText.append(" " + user.getRegistrationDate());

        cpText.setText(getString(R.string.cp) + " ");
        cpText.append(ColorUtils.colorWords("" + user.getSubmissionPoints().getSum(), CommonColors.colorPrimary(UserActivity.this)));
        cpText.append(" " + CommonStrings.dot(UserActivity.this) + " ");
        cpText.append(ColorUtils.colorWords("" + user.getCommentPoints().getSum(), CommonColors.colorPrimary(UserActivity.this)));
        //For testing...
        if (BuildConfig.DEBUG) {
            badgeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            ArrayList<Badge> badges = new ArrayList<>();
            badges.add(Badge.createFakeBadge());
            badgeList.setAdapter(new BadgeAdapter(badges));
        } else {
            if (user.getBadges() != null && !user.getBadges().isEmpty()) {
                badgeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                badgeList.setAdapter(new BadgeAdapter(user.getBadges()));
            } else {
                badgeList.setVisibility(View.GONE);
            }
        }

    }

    private void addFakeBadge() {
        user.getBadges().add(Badge.createFakeBadge());
    }

    //TODO view pager instead?
    public class BadgeAdapter extends RecyclerView.Adapter<BadgeViewHolder> {

        private List<Badge> mValues;

        public Badge getValueAt(int position) {
            return mValues.get(position);
        }

        public BadgeAdapter(List<Badge> items) {
            mValues = items;
        }

        @Override
        public BadgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            BadgeViewHolder holder = BadgeViewHolder.create(parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(final BadgeViewHolder holder, int position) {
            Badge badge = getValueAt(position);
            holder.bind(badge);
            holder.itemView.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
