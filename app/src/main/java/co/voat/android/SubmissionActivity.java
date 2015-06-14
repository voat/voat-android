package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.data.Submission;
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

    @InjectView(R.id.tabs)
    TabLayout tabLayout;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        ButterKnife.inject(this);
        Submission submission = (Submission) getIntent().getSerializableExtra(EXTRA_SUBMISSION);
        viewPager.setAdapter(new PostPagerAdapter(getSupportFragmentManager(), submission));
        tabLayout.setupWithViewPager(viewPager);
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
