package co.voat.android.dialogs;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

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

    public static final int MODE_TEXT = 0;
    public static final int MODE_LINK = 1;

    public interface OnSubmissionListener {
        void onSubmitted();
    }
    OnSubmissionListener listener;

    @InjectView(R.id.subverse_hint)
    TextInputLayout subverseHint;
    @InjectView(R.id.subverse)
    EditText subverseText;
    @InjectView(R.id.title_hint)
    TextInputLayout titleHint;
    @InjectView(R.id.title)
    EditText titleText;
    @InjectView(R.id.text_hint)
    TextInputLayout textHint;
    @InjectView(R.id.text)
    EditText textText;

    private static Pattern urlPattern = Patterns.WEB_URL;
    private int mode;

    private Callback<SubmissionResponse> postSubmissionCallback = new Callback<SubmissionResponse>() {
        @Override
        public void success(SubmissionResponse submissionResponse, Response response) {
            if (submissionResponse.success) {
                Timber.d("Post was successful");
                if (listener != null) {
                    listener.onSubmitted();
                }
                dismiss();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
            Toast.makeText(getContext(), getContext().getString(R.string.submission_error), Toast.LENGTH_SHORT)
                    .show();
        }
    };

    @OnClick(R.id.submit)
    void onSubmitClick(View v) {
        final String subverse = subverseText.getText().toString();
        String title = titleText.getText().toString();
        String text = textText.getText().toString();
        boolean hasError = false;
        if (TextUtils.isEmpty(subverse)) {
            subverseHint.setError(getContext().getString(R.string.required_field));
            hasError = true;
        }
        if (TextUtils.isEmpty(title)) {
            titleHint.setError(getContext().getString(R.string.required_field));
            hasError = true;
        }
        if (title.length() < 5 ) {
            titleHint.setError(getContext().getString(R.string.too_short));
            hasError = true;
        }
        if (mode == MODE_LINK) {
            if (!urlPattern.matcher(text).matches() || !text.startsWith("http")
                || !text.startsWith("ftp")) {
                textHint.setError(getContext().getString(R.string.not_a_valid_url));
                hasError = true;
            }
        }
        if (!hasError) {
            SubmissionBody.Builder request = new SubmissionBody.Builder(title);
            if (mode == MODE_TEXT) {
                request.setContent(text);
            } else {
                request.setUrl(text);
            }
            VoatClient.instance().postSubmission(subverse, request.build(), postSubmissionCallback);
        }
    }

    public SubmissionDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_submission);
        ButterKnife.inject(this);
        setMode(MODE_TEXT);
    }

    public void setSubverse(String subverse) {
        subverseText.setText(subverse);
    }

    public void setMode(int mode) {
        String hint;
        switch (mode) {
            case MODE_LINK:
                hint = getContext().getString(R.string.url);
                textText.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
                break;
            case MODE_TEXT:
            default:
                hint = getContext().getString(R.string.text);
                textText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                        |InputType.TYPE_TEXT_FLAG_AUTO_CORRECT
                        |InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                break;
        }
        textHint.setHint(hint);
        this.mode = mode;
    }

    public void setOnSubmissionListener(OnSubmissionListener listener) {
        this.listener = listener;
    }
}
