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

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @OnClick(R.id.setting_confirm_exit_root)
    void onConfirmExitClick() {
        checkConfirmExit.setChecked(!checkConfirmExit.isChecked());
        VoatPrefs.setConfirmExit(this, checkConfirmExit.isChecked());
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
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.settings));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(navigationClickListener);

        setUI();
    }

    private void setUI() {
        checkConfirmExit.setChecked(VoatPrefs.isConfirmExit(this));
    }
}