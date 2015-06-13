package co.voat.android.dialogs;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.api.SubmissionBody;
import co.voat.android.api.SubmissionResponse;
import co.voat.android.api.VoatClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Jawn on 6/12/2015.
 */
public class SubmissionDialog extends AppCompatDialog {

    @InjectView(R.id.subverse)
    EditText subverseText;
    @InjectView(R.id.title)
    EditText titleText;
    @InjectView(R.id.text)
    EditText textText;

    private Callback<SubmissionResponse> postSubmissionCallback = new Callback<SubmissionResponse>() {
        @Override
        public void success(SubmissionResponse submissionResponse, Response response) {
            if (submissionResponse.success) {
                Timber.d("Post was successful");
                dismiss();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
        }
    };

    @OnClick(R.id.submit)
    void onSubmitClick(View v) {
        final String subverse = subverseText.getText().toString();
        String title = titleText.getText().toString();
        String text = textText.getText().toString();
        boolean hasError = false;
        if (TextUtils.isEmpty(subverse)) {
            subverseText.setError(getContext().getString(R.string.required_field));
            hasError = true;
        }
        if (TextUtils.isEmpty(title)) {
            titleText.setError(getContext().getString(R.string.required_field));
            hasError = true;
        }
        if (title.length() < 5 ) {
            titleText.setError(getContext().getString(R.string.too_short));
            hasError = true;
        }
        if (!hasError) {
            SubmissionBody request = new SubmissionBody.Builder(title)
                    .setContent(text)
                    .build();
            VoatClient.instance().postSubmission(subverse, request, postSubmissionCallback);
        }
    }

    public SubmissionDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_submission);
        ButterKnife.inject(this);
    }
}
