package co.voat.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.BuildConfig;
import co.voat.android.R;
import co.voat.android.VoatApp;
import co.voat.android.VoatPrefs;
import co.voat.android.data.Badge;
import co.voat.android.data.User;
import co.voat.android.events.LogoffEvent;
import co.voat.android.fragments.SubmissionsFragment;
import co.voat.android.fragments.SubscriptionsFragment;
import co.voat.android.fragments.UserCommentsFragment;
import co.voat.android.utils.ColorUtils;
import co.voat.android.utils.CommonColors;
import co.voat.android.utils.CommonDrawables;
import co.voat.android.utils.CommonStrings;
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

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.backdrop)
    ImageView backdrop;
    @Bind(R.id.user_image)
    ImageView userImage;
    @Bind(R.id.username)
    TextView usernameText;
    @Bind(R.id.user_bio)
    TextView bioText;
    @Bind(R.id.user_member_time)
    TextView memberTimeText;
    @Bind(R.id.user_cp)
    TextView cpText;
    @Bind(R.id.badge_list)
    RecyclerView badgeList;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager messagesViewPager;

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
                    new AlertDialog.Builder(UserActivity.this)
                            .setMessage(getString(R.string.log_off_confirmation))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
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
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        boolean userProfile = false;
        if (user == null) {
            user = User.getCurrentUser();
            userProfile = true;
        }
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        if (userProfile) {
            toolbar.inflateMenu(R.menu.menu_user);
            toolbar.setOnMenuItemClickListener(menuItemClickListener);
        }
        Glide.with(this)
                .load(CommonDrawables.getRandomHeader())
                .into(backdrop);
        backdrop.setColorFilter(CommonColors.colorPrimary(this), PorterDuff.Mode.MULTIPLY);

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
        messagesViewPager.setAdapter(new UserPagerAdapter(this, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(messagesViewPager);
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

    public static class UserPagerAdapter extends FragmentStatePagerAdapter {

        String[] titles;
        UserPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            titles = context.getResources().getStringArray(R.array.user_sections);
        }

        /**
         * Return the fragment page to be shown
         */
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return UserCommentsFragment.newInstance();
                case 1:
                    return SubmissionsFragment.newInstance();
                case 2:
                    return SubscriptionsFragment.newInstance();
            }
            throw new RuntimeException("IDK WHAT YOU DID");
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

    }
}
