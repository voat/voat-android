package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jawnnypoo.physicslayout.Physics;
import com.jawnnypoo.physicslayout.PhysicsConfig;
import com.jawnnypoo.physicslayout.PhysicsFrameLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.github.Contributor;
import co.voat.android.github.GithubClient;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Whatchu know about dat
 * Created by Jawn on 6/14/2015.
 */
public class AboutActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @InjectView(R.id.physics_layout)
    PhysicsFrameLayout physicsLayout;
    @InjectView(R.id.version)
    TextView versionText;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private final Callback<List<Contributor>> contributorResponseCallback = new Callback<List<Contributor>>() {
        @Override
        public void success(List<Contributor> contributorList, Response response) {
            addContributors(contributorList);
        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);
        toolbar.setTitle(getString(R.string.about));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        versionText.setText(BuildConfig.VERSION_NAME);
        physicsLayout.getPhysics().enableFling();
        GithubClient.instance().contributors("Jawnnypoo", "voat-android", contributorResponseCallback);

    }

    private void addContributors(List<Contributor> contributors) {
        PhysicsConfig config = new PhysicsConfig.Builder()
                .setShapeType(PhysicsConfig.ShapeType.CIRCLE)
                .setDensity(1.0f)
                .setFriction(0.0f)
                .setRestitution(0.0f)
                .build();
        for (Contributor contributor : contributors) {
            CircleImageView imageView = new CircleImageView(this);
            FrameLayout.LayoutParams llp = new FrameLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_size),
                    getResources().getDimensionPixelSize(R.dimen.image_size));
            imageView.setLayoutParams(llp);
            Physics.setPhysicsConfig(imageView, config);
            physicsLayout.addView(imageView);

            Glide.with(this)
                    .load(contributor.avatarUrl)
                    .into(imageView);
        }
        physicsLayout.getPhysics().onLayout(true);
    }
}
