package co.voat.android.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jawnnypoo.physicslayout.Physics;
import com.jawnnypoo.physicslayout.PhysicsConfig;
import com.jawnnypoo.physicslayout.PhysicsFrameLayout;

import org.jbox2d.common.Vec2;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.BuildConfig;
import co.voat.android.R;
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

    private static final String REPO_USER = "Jawnnypoo";
    private static final String REPO_NAME = "voat-android";

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @Bind(R.id.physics_layout)
    PhysicsFrameLayout physicsLayout;
    @Bind(R.id.version)
    TextView versionText;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    SensorManager sensorManager;
    Sensor accelerometer;

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
                physicsLayout.getPhysics().getWorld().setGravity(new Vec2(- event.values[0], event.values[1]));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) { }
    };

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
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.about));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        versionText.setText(BuildConfig.VERSION_NAME);
        physicsLayout.getPhysics().enableFling();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        GithubClient.instance().contributors(REPO_USER, REPO_NAME, contributorResponseCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }

    private void addContributors(List<Contributor> contributors) {
        PhysicsConfig config = new PhysicsConfig.Builder()
                .setShapeType(PhysicsConfig.ShapeType.CIRCLE)
                .setDensity(1.0f)
                .setFriction(0.0f)
                .setRestitution(0.0f)
                .build();

        int borderSize = getResources().getDimensionPixelSize(R.dimen.border_size);
        int x = 0;
        int y = 0;
        int imageSize = getResources().getDimensionPixelSize(R.dimen.image_size);
        for (int i=0; i<contributors.size(); i++) {
            Contributor contributor = contributors.get(i);
            CircleImageView imageView = new CircleImageView(this);
            FrameLayout.LayoutParams llp = new FrameLayout.LayoutParams(
                    getResources().getDimensionPixelSize(R.dimen.image_size),
                    getResources().getDimensionPixelSize(R.dimen.image_size));
            imageView.setLayoutParams(llp);
            imageView.setBorderWidth(borderSize);
            imageView.setBorderColor(Color.BLACK);
            Physics.setPhysicsConfig(imageView, config);
            physicsLayout.addView(imageView);
            imageView.setX(x);
            imageView.setY(y);
            x = (x + imageSize);
            if (x > physicsLayout.getWidth()) {
                x = 0;
                y = (y + imageSize) % physicsLayout.getHeight();
            }

            Glide.with(this)
                    .load(contributor.avatarUrl)
                    .into(imageView);
        }
    }
}
