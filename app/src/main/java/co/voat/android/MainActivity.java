package co.voat.android;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.voat.android.api.SubmissionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Submission;
import co.voat.android.data.User;
import co.voat.android.dialogs.SubmissionDialog;
import co.voat.android.viewHolders.SubmissionViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.nav_view)
    NavigationView navigationView;
    @InjectView(R.id.nav_header_image)
    ImageView headerImage;
    @InjectView(R.id.nav_header_username)
    TextView headerUsername;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.subverses_spinner)
    Spinner subversesSpinner;
    ArrayAdapter<String> subversesSpinnerAdapter;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.list)
    RecyclerView list;

    @OnClick(R.id.fab)
    void onClickAdd(View v) {
        new SubmissionDialog(this).show();
    }

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
                case R.id.nav_my_subverse:
                    gotoMySubscriptions();
                    break;
                case R.id.nav_messages:
                    gotoMessages();
                    break;
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

    private final View.OnClickListener onHeaderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO login if not, user profile if so
            gotoUser(null);
            drawerLayout.closeDrawers();
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            loadSubverse(subversesSpinnerAdapter.getItem(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadSubverse(subversesSpinnerAdapter.getItem(0));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        ButterKnife.inject(this);
        setupToolbar();
        setupDrawer();
        subversesSpinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, VoatClient.getDefaultSubverses());
        subversesSpinner.setAdapter(subversesSpinnerAdapter);
        subversesSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));
        list.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setNavigationOnClickListener(navigationClickListener);
        toolbar.setOnMenuItemClickListener(menuItemClickListener);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
    }

    private void setupDrawer() {
        //TODO set the proper tab as selected
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        headerImage.setOnClickListener(onHeaderClickListener);
        Glide.with(this)
                .load("http://i.imgur.com/wt4NRqA.jpg")
                .into(headerImage);
        User user = User.getCurrentUser();
        if (user != null) {
            headerUsername.setText(user.getUserName());
            if (!TextUtils.isEmpty(user.getProfilePicture())) {
                Glide.with(this)
                        .load(user.getProfilePicture())
                        .into(headerImage);
            }
        }
    }

    private void loadSubverse(String subverse) {
        swipeRefreshLayout.setRefreshing(true);
        VoatClient.instance().getSubmissions(subverse, new Callback<SubmissionsResponse>() {
            @Override
            public void success(SubmissionsResponse submissionsResponse, Response response) {
                swipeRefreshLayout.setRefreshing(false);
                if (submissionsResponse.success) {
                    list.setAdapter(new PostAdapter(submissionsResponse.data));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);
                Timber.e(error.toString());
            }
        });
    }

    //TODO make this static?
    public class PostAdapter extends RecyclerView.Adapter<SubmissionViewHolder> {

        private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.list_position);
                gotoDetail(mValues.get(position));
            }
        };

        private List<Submission> mValues;

        public Submission getValueAt(int position) {
            return mValues.get(position);
        }

        public PostAdapter(List<Submission> items) {
            mValues = items;
        }

        @Override
        public SubmissionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            SubmissionViewHolder holder = SubmissionViewHolder.create(parent);
            holder.itemView.setOnClickListener(onItemClickListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SubmissionViewHolder holder, int position) {
            Submission submission = getValueAt(position);
            holder.bind(submission);
            holder.itemView.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
