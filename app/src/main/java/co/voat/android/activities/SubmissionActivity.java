package co.voat.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.VoatApp;
import co.voat.android.data.Submission;
import co.voat.android.events.ContextualCommentEvent;
import co.voat.android.events.ContextualDownvoteEvent;
import co.voat.android.events.ContextualProfileEvent;
import co.voat.android.events.ContextualUpvoteEvent;
import co.voat.android.events.ShowContextualMenuEvent;
import co.voat.android.fragments.CommentFragment;
import co.voat.android.fragments.ImageFragment;
import co.voat.android.fragments.WebFragment;
import co.voat.android.utils.IntentUtils;
import co.voat.android.utils.UrlUtils;

/**
 * All the stuff
 * Created by Jawn on 6/13/2015.
 */
public class SubmissionActivity extends BaseActivity {

    private static final String EXTRA_SUBMISSION = "extra_submission";
    private static final String EXTRA_STRAIGHT_TO_COMMENTS = "extra_straight_to_comments";

    public static Intent newInstance(Context context, Submission submission) {
        return newInstance(context, submission, false);
    }

    public static Intent newInstance(Context context, Submission submission, boolean straightToComments) {
        Intent intent = new Intent(context, SubmissionActivity.class);
        intent.putExtra(EXTRA_SUBMISSION, submission);
        intent.putExtra(EXTRA_STRAIGHT_TO_COMMENTS, straightToComments);
        return intent;
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.contextual_toolbar)
    Toolbar contextualToolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.submission_bar_root)
    ViewGroup submissionBar;

    @OnClick(R.id.upvote)
    void onUpVote(View v) {

    }
    @OnClick(R.id.downvote)
    void onDownVote(View v) {

    }
    @OnClick(R.id.share)
    void onClickShare(View v) {
        if (!IntentUtils.share(this, submission.getUrl())) {
            Snackbar.make(root, getString(R.string.no_share), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
    @OnClick(R.id.download)
    void onClickDownload(View v) {

    }
    @OnClick(R.id.browser)
    void onClickBrowser(View v) {
        if (!IntentUtils.openBrowser(this, submission.getUrl())) {
            Snackbar.make(root, getString(R.string.no_browser), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    Submission submission;
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
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.messages));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        contextualToolbar.setNavigationIcon(R.drawable.ic_back);
        contextualToolbar.setNavigationOnClickListener(contextualNavigationClickListener);
        contextualToolbar.inflateMenu(R.menu.menu_comments);
        contextualToolbar.setOnMenuItemClickListener(contextualMenuItemClickListener);
        submission = (Submission) getIntent().getSerializableExtra(EXTRA_SUBMISSION);
        boolean straightToComments = getIntent().getBooleanExtra(EXTRA_STRAIGHT_TO_COMMENTS, false);
        viewPager.setAdapter(new PostPagerAdapter(SubmissionActivity.this, getSupportFragmentManager(), submission));
        if (straightToComments && submission.getType() == Submission.TYPE_LINK) {
            viewPager.setCurrentItem(1);
        }
        tabLayout.setupWithViewPager(viewPager);
        eventReceiver = new EventReceiver();
        actionBarSize = getResources().getDimensionPixelSize(R.dimen.actionBarSize);
        setupSubmissionBar();
    }

    private void setupSubmissionBar() {
        if (TextUtils.isEmpty(submission.getUrl())) {
            submissionBar.findViewById(R.id.browser).setVisibility(View.GONE);
            submissionBar.findViewById(R.id.download).setVisibility(View.GONE);
            submissionBar.findViewById(R.id.share).setVisibility(View.GONE);
            //TODO allow sharing if API eventually returns the original url of the post
        } else {
            if (!UrlUtils.isImageLink(submission.getUrl())) {
                submissionBar.findViewById(R.id.download).setVisibility(View.GONE);
            }
        }
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
        String comments;
        String web;
        String image;
        PostPagerAdapter(Context context, FragmentManager fm, Submission submission) {
            super(fm);
            this.submission = submission;
            comments = context.getString(R.string.comments);
            web = context.getString(R.string.web);
            image = context.getString(R.string.image);
        }

        /**
         * Return the fragment page to be shown
         */
        @Override
        public Fragment getItem(int position) {
            switch (submission.getType()) {
                case Submission.TYPE_LINK:
                    if (position == 0) {
                        if (UrlUtils.isImageLink(submission.getUrl())) {
                            return ImageFragment.newInstance(submission);
                        } else {
                            return WebFragment.newInstance(submission.getUrl());
                        }
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
                        if (UrlUtils.isImageLink(submission.getUrl())) {
                            return image;
                        }
                        return web;
                    } else {
                        return comments;
                    }
                case Submission.TYPE_SELF:
                    return comments;
                default:
                    return null;
            }
        }

    }
}
