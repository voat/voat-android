package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.Spinner;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.voat.android.api.SubmissionsResponse;
import co.voat.android.api.SubscriptionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Submission;
import co.voat.android.data.User;
import co.voat.android.dialogs.LoginDialog;
import co.voat.android.dialogs.SubmissionDialog;
import co.voat.android.viewHolders.SubmissionViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * The most main activity there ever were
 * Created by Jawn on 6/10/2015.
 */
public class MainActivity extends BaseActivity {

    private static final String EXTRA_SELECTED_SUBVERSE = "extra_selected_subverse";

    public static Intent newInstance(Context context, String selectedSubverse) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_SELECTED_SUBVERSE, selectedSubverse);
        return intent;
    }

    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.nav_header_root)
    View navigationViewHeader;
    @InjectView(R.id.nav_view)
    NavigationView navigationView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.subverses_spinner)
    Spinner subversesSpinner;
    ArrayAdapter<String> subversesSpinnerAdapter;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.list)
    RecyclerView list;
    @InjectView(R.id.fab)
    FloatingActionsMenu fab;

    @OnClick(R.id.nav_header_root)
    void onNavHeaderClick(View v) {
        if (User.getCurrentUser() != null) {
            gotoUser();
        } else {
            new LoginDialog(MainActivity.this).show();
        }
        drawerLayout.closeDrawers();
    }
    @OnClick(R.id.fab_submit_text)
    void onSubmitText(View v) {
        submissionDialog = new SubmissionDialog(this);
        submissionDialog.setMode(SubmissionDialog.MODE_TEXT);
        submissionDialog.setOnSubmissionListener(submissionListener);
        fab.collapse();
        submissionDialog.show();
    }
    @OnClick(R.id.fab_submit_link)
    void onSubmitLink(View v) {
        submissionDialog = new SubmissionDialog(this);
        submissionDialog.setMode(SubmissionDialog.MODE_LINK);
        submissionDialog.setOnSubmissionListener(submissionListener);
        fab.collapse();
        submissionDialog.show();
    }
    SubmissionDialog submissionDialog;

    String selectedSubverse;

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
                case R.id.nav_about:
                    //TODO delay this until the drawer is closed
                    gotoAbout();
                    break;
            }
            menuItem.setChecked(true);
            drawerLayout.closeDrawers();
            return true;
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

    private final Callback<SubscriptionsResponse> subscriptionsResponseCallback = new Callback<SubscriptionsResponse>() {
        @Override
        public void success(SubscriptionsResponse subscriptionsResponse, Response response) {
            if (subscriptionsResponse.success) {
                //TODO filter to subverse subscriptions and add them here
                subversesSpinnerAdapter = new ArrayAdapter<>(MainActivity.this,
                        R.layout.support_simple_spinner_dropdown_item,
                        VoatClient.getDefaultSubverses());
                subversesSpinner.setAdapter(subversesSpinnerAdapter);
                if (TextUtils.isEmpty(selectedSubverse)) {
                    loadSubverse(subversesSpinnerAdapter.getItem(0));
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
        }
    };

    private final SubmissionDialog.OnSubmissionListener submissionListener = new SubmissionDialog.OnSubmissionListener() {
        @Override
        public void onSubmitted() {
            Snackbar.make(root, getString(R.string.submission_successfully_posted), Snackbar.LENGTH_SHORT)
                    .show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_drawer);
        ButterKnife.inject(this);
        selectedSubverse = getIntent().getStringExtra(EXTRA_SELECTED_SUBVERSE);
        submissionDialog = new SubmissionDialog(this);
        submissionDialog.setOnSubmissionListener(submissionListener);
        setupToolbar();
        setupDrawer();
        setupSpinner();

        // This is a hack seen here:
        // http://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner
        subversesSpinner.post(new Runnable() {
            @Override
            public void run() {
                subversesSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.blue));
        list.setLayoutManager(new LinearLayoutManager(this));
        if (!TextUtils.isEmpty(selectedSubverse)) {
            loadSubverse(selectedSubverse);
        }
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
    }

    private void setupSpinner() {
        if (User.getCurrentUser() == null) {
            subversesSpinnerAdapter = new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item,
                    VoatClient.getDefaultSubverses());
            subversesSpinner.setAdapter(subversesSpinnerAdapter);
        } else {
            VoatClient.instance().getUserSubscriptions(User.getCurrentUser().getUserName(),
                    subscriptionsResponseCallback);
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
                gotoSubmission(mValues.get(position));
            }
        };

        private final View.OnClickListener onImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.list_position);
                gotoImage(mValues.get(position));
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
            holder.comments.setOnClickListener(onItemClickListener);
            holder.image.setOnClickListener(onImageClickListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SubmissionViewHolder holder, int position) {
            Submission submission = getValueAt(position);
            holder.bind(submission);
            holder.itemView.setTag(R.id.list_position, position);
            holder.comments.setTag(R.id.list_position, position);
            holder.image.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
