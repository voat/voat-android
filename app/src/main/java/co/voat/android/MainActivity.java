package co.voat.android;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.api.SubmissionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Submission;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.nav_view)
    NavigationView navigationView;
    @InjectView(R.id.nav_header_image)
    ImageView headerImage;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.list)
    RecyclerView list;

    private final Toolbar.OnMenuItemClickListener menuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case android.R.id.home:
                    drawerLayout.openDrawer(GravityCompat.START);
                    return true;
            }
            return false;
        }
    };

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    };

    private final NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_settings:
                    //TODO delay this until the drawer is closed
                    gotoSettings();
                    break;
            }
            menuItem.setChecked(true);
            drawerLayout.closeDrawers();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        ButterKnife.inject(this);
        setupToolbar();
        setupDrawer();
        list.setLayoutManager(new LinearLayoutManager(this));

        VoatClient.instance().getSubmissions("all", new Callback<SubmissionsResponse>() {
            @Override
            public void success(SubmissionsResponse submissionsResponse, Response response) {
                if (submissionsResponse.success) {
                    list.setAdapter(new PostAdapter(submissionsResponse.data));
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void setupToolbar() {
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
    }

    private void setupDrawer() {
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        Glide.with(this)
                .load("http://i.imgur.com/wt4NRqA.jpg")
                .into(headerImage);
    }

    public static class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

        private List<Submission> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            @InjectView(R.id.post_score)
            TextView scoreText;
            @InjectView(R.id.post_title)
            TextView titleText;
            @InjectView(R.id.post_author)
            TextView authorText;
            @InjectView(R.id.post_image)
            ImageView image;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
            }
        }

        public Submission getValueAt(int position) {
            return mValues.get(position);
        }

        public PostAdapter(List<Submission> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Submission submission = getValueAt(position);
            holder.scoreText.setText(submission.getUpVotes() + "");
            holder.titleText.setText(submission.getTitle());
            holder.authorText.setText(submission.getUserName());
            Glide.with(holder.image.getContext())
                    .load(submission.getThumbnail())
                    .into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "hello", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
