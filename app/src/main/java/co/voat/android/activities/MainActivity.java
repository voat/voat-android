package co.voat.android.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.VoatApp;
import co.voat.android.VoatPrefs;
import co.voat.android.api.SubscriptionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Subscription;
import co.voat.android.data.Subverse;
import co.voat.android.data.User;
import co.voat.android.dialogs.LoginDialog;
import co.voat.android.dialogs.SubmissionDialog;
import co.voat.android.events.LoginEvent;
import co.voat.android.events.LogoffEvent;
import co.voat.android.events.ToolbarSubverseEvent;
import co.voat.android.fragments.SubmissionsFragment;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * The most main activity there ever were
 * Created by Jawn on 6/10/2015.
 */
public class MainActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_header_root)
    View navigationViewHeader;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.subverses_spinner)
    Spinner subversesSpinner;
    ArrayAdapter<String> subversesSpinnerAdapter;
    @Bind(R.id.fab)
    FloatingActionsMenu fab;
    @Bind(R.id.fragment_root)
    View fragmentRoot;

    EventReceiver eventReceiver;

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
                    if (User.getCurrentUser() == null) {
                        Toast.makeText(MainActivity.this, getString(R.string.must_be_logged_in), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        gotoMySubscriptions();
                    }
                    break;
                case R.id.nav_messages:
                    if (User.getCurrentUser() == null) {
                        Toast.makeText(MainActivity.this, getString(R.string.must_be_logged_in), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        gotoMessages();
                    }
                    break;
                case R.id.nav_settings:
                    //TODO delay this until the drawer is closed
                    gotoSettings();
                    finish();
                    break;
                case R.id.nav_about:
                    //TODO delay this until the drawer is closed
                    gotoAbout();
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    };

    private final AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Timber.d("onItemSelected " + subversesSpinnerAdapter.getItem(position));
            VoatApp.bus().post(new ToolbarSubverseEvent(subversesSpinnerAdapter.getItem(position)));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    private final Callback<SubscriptionsResponse> subscriptionsResponseCallback = new Callback<SubscriptionsResponse>() {
        @Override
        public void success(SubscriptionsResponse subscriptionsResponse, Response response) {
            if (subscriptionsResponse.success) {
                for (Subscription subscription : subscriptionsResponse.data) {
                    if (subscription.getType() == Subscription.TYPE_SUBVERSE) {
                        subversesSpinnerAdapter.add(subscription.getName());
                    }
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
        ButterKnife.bind(this);
        eventReceiver = new EventReceiver();
        VoatApp.bus().register(eventReceiver);
        submissionDialog = new SubmissionDialog(this);
        submissionDialog.setOnSubmissionListener(submissionListener);
        setupToolbar();
        setupDrawer();
        setupSpinner();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_root, SubmissionsFragment.newInstance())
                .commit();
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
        subversesSpinnerAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item);
        subversesSpinnerAdapter.add(Subverse.SUBVERSE_FRONT);
        subversesSpinnerAdapter.add(Subverse.SUBVERSE_ALL);
        subversesSpinner.setAdapter(subversesSpinnerAdapter);
        //This will cause the first item to load right away. Its weird.
        // http://stackoverflow.com/questions/2562248/how-to-keep-onitemselected-from-firing-off-on-a-newly-instantiated-spinner
        subversesSpinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        if (User.getCurrentUser() != null) {
            VoatClient.instance().getUserSubscriptions(User.getCurrentUser().getUserName(),
                    subscriptionsResponseCallback);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }
        if (VoatPrefs.isConfirmExit(this)) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.exit)
                    .setMessage(R.string.exit_description)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).show();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        VoatApp.bus().unregister(eventReceiver);
        super.onDestroy();
    }

    private class EventReceiver {

        @Subscribe
        public void onLogin(LoginEvent event) {
            Timber.d("onLogin MainActivity");
            setupSpinner();
        }

        @Subscribe
        public void onLogoff(LogoffEvent event) {
            Timber.d("onLogoff MainActivity");
            setupSpinner();
        }

    }
}
