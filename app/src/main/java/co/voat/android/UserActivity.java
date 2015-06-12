package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.data.User;
import co.voat.android.dialogs.LoginDialog;

/**
 * Check out this user... so lame
 * Created by Jawn on 6/12/2015.
 */
public class UserActivity extends BaseActivity {

    private static final String EXTRA_USER = "extra_user";

    public static Intent newInstance(Context context, User user) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra(EXTRA_USER, user);
        return intent;
    }

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.backdrop)
    ImageView backdrop;
    @InjectView(R.id.user_image)
    ImageView userImage;

    User user;

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.inject(this);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        user = (User) getIntent().getSerializableExtra(EXTRA_USER);
        Glide.with(this)
                .load("http://i.imgur.com/wt4NRqA.jpg")
                .into(backdrop);
        Glide.with(this)
                .load("http://i.imgur.com/wt4NRqA.jpg")
                .into(userImage);

        new LoginDialog(this).show();
    }
}
