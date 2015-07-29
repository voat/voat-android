package co.voat.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.data.Message;
import co.voat.android.dialogs.SendMessageDialog;
import co.voat.android.fragments.MessagesFragment;

/**
 * Check out that inbox!
 * Created by Jawn on 6/12/2015.
 */
public class MessagesActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MessagesActivity.class);
        return intent;
    }

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager messagesViewPager;

    @OnClick(R.id.fab)
    void onFabClick(View v) {
        new SendMessageDialog(this).show();
    }

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.messages));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        messagesViewPager.setAdapter(new MessagesPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(messagesViewPager);
    }

    public static class MessagesPagerAdapter extends FragmentStatePagerAdapter {

        MessagesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Return the fragment page to be shown
         */
        @Override
        public Fragment getItem(int position) {
            return MessagesFragment.newInstance(Message.MESSAGE_TYPES[position]);
        }

        @Override
        public int getCount() {
            return Message.MESSAGE_TYPES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Message.MESSAGE_TYPES[position];
        }

    }

}
