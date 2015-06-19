package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.data.Submission;
import co.voat.android.events.ContextualCommentEvent;
import co.voat.android.events.ContextualDownvoteEvent;
import co.voat.android.events.ContextualProfileEvent;
import co.voat.android.events.ContextualUpvoteEvent;
import co.voat.android.events.ShowContextualMenuEvent;
import co.voat.android.fragments.CommentFragment;
import co.voat.android.fragments.WebFragment;

/**
 * All the stuff
 * Created by Jawn on 6/13/2015.
 */
public class SubmissionActivity extends BaseActivity {

    private static final String EXTRA_SUBMISSION = "extra_submission";

    public static Intent newInstance(Context context, Submission submission) {
        Intent intent = new Intent(context, SubmissionActivity.class);
        intent.putExtra(EXTRA_SUBMISSION, submission);
        return intent;
    }

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.contextual_toolbar)
    Toolbar contextualToolbar;
    @InjectView(R.id.tabs)
    TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    EventReceiver eventReceiver;
    int actionBarSize;

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    private final View.OnClickListener contextualNavigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggleContextualToolbar();
        }
    };
    private final Toolbar.OnMenuItemClickListener contextualMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_upvote:
                    VoatApp.bus().post(new ContextualUpvoteEvent());
                    return true;
                case R.id.action_downvote:
                    VoatApp.bus().post(new ContextualDownvoteEvent());
                    return true;
                case R.id.action_profile:
                    VoatApp.bus().post(new ContextualProfileEvent());
                    return true;
                case R.id.action_comment:
                    VoatApp.bus().post(new ContextualCommentEvent());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        ButterKnife.inject(this);
        toolbar.setTitle(getString(R.string.messages));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        contextualToolbar.setNavigationIcon(R.drawable.ic_back);
        contextualToolbar.setNavigationOnClickListener(contextualNavigationClickListener);
        contextualToolbar.inflateMenu(R.menu.menu_comments);
        contextualToolbar.setOnMenuItemClickListener(contextualMenuItemClickListener);
        Submission submission = (Submission) getIntent().getSerializableExtra(EXTRA_SUBMISSION);
        viewPager.setAdapter(new PostPagerAdapter(getSupportFragmentManager(), submission));
        tabLayout.setupWithViewPager(viewPager);
        eventReceiver = new EventReceiver();
        actionBarSize = getResources().getDimensionPixelSize(R.dimen.actionBarSize);
    }

    @Override
    protected void onResume() {
        super.onResume();
        VoatApp.bus().register(eventReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        VoatApp.bus().unregister(eventReceiver);
    }

    private void toggleContextualToolbar() {
        if (contextualToolbar.getTranslationY() == 0) {
            contextualToolbar.animate().translationY(-actionBarSize);
        } else {
            contextualToolbar.animate().translationY(0);
        }
    }

    private class EventReceiver {

        @Subscribe
        public void onContextualMenu(ShowContextualMenuEvent event) {
            toggleContextualToolbar();
        }

    }

    public static class PostPagerAdapter extends FragmentStatePagerAdapter {

        Submission submission;
        PostPagerAdapter(FragmentManager fm, Submission submission) {
            super(fm);
            this.submission = submission;
        }

        /**
         * Return the fragment page to be shown
         */
        @Override
        public Fragment getItem(int position) {
            switch (submission.getType()) {
                case Submission.TYPE_LINK:
                    if (position == 0) {
                        return WebFragment.newInstance(submission.getUrl());
                    } else {
                        return CommentFragment.newInstance(submission);
                    }
                case Submission.TYPE_SELF:
                    return CommentFragment.newInstance(submission);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            switch (submission.getType()) {
                case Submission.TYPE_LINK:
                    return 2;
                case Submission.TYPE_SELF:
                    return 1;
                default:
                    return 1;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (submission.getType()) {
                case Submission.TYPE_LINK:
                    if (position == 0) {
                        return "Web";
                    } else {
                        return "Comments";
                    }
                case Submission.TYPE_SELF:
                    return "Comments";
                default:
                    return null;
            }
        }

    }
}
