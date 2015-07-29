package co.voat.android.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.VoatPrefs;
import co.voat.android.utils.CommonColors;

/**
 * Created by Jawn on 6/11/2015.
 */
public class SettingsActivity extends BaseActivity {

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, SettingsActivity.class);
        return intent;
    }

    @Bind(R.id.setting_confirm_exit)
    CompoundButton checkConfirmExit;
    @Bind(R.id.setting_dark_theme)
    CompoundButton checkDarkTheme;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.setting_confirm_exit_root)
    void onConfirmExitClick() {
        checkConfirmExit.setChecked(!checkConfirmExit.isChecked());
        VoatPrefs.setConfirmExit(this, checkConfirmExit.isChecked());
    }

    @OnClick(R.id.setting_dark_theme_root)
    void onDarkThemeClick() {
        checkDarkTheme.setChecked(!checkDarkTheme.isChecked());
        VoatPrefs.setDarkTheme(this, checkDarkTheme.isChecked());
        CommonColors.clear();
    }

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.settings));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);

        setUI();
    }

    @Override
    public void onBackPressed() {
        startActivity(MainActivity.newInstance(this));
        finish();
    }

    private void setUI() {
        checkConfirmExit.setChecked(VoatPrefs.isConfirmExit(this));
        checkDarkTheme.setChecked(VoatPrefs.isDarkTheme(this));
    }
}