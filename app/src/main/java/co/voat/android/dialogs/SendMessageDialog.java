package co.voat.android.dialogs;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.api.MessageBody;
import co.voat.android.api.SimpleResponse;
import co.voat.android.api.VoatClient;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Sendin you some PM's gurl
 * Created by Jawn on 6/13/2015.
 */
public class SendMessageDialog extends AppCompatDialog {

    @Bind(R.id.user_hint)
    TextInputLayout userHint;
    @Bind(R.id.user)
    EditText userText;
    @Bind(R.id.subject_hint)
    TextInputLayout subjectHint;
    @Bind(R.id.subject)
    EditText subjectText;
    @Bind(R.id.message_hint)
    TextInputLayout messageHint;
    @Bind(R.id.message)
    EditText messageText;

    @OnClick(R.id.send)
    void onSendClick(View v) {
        String user = userText.getText().toString();
        String subject = subjectText.getText().toString();
        String message = messageText.getText().toString();
        boolean hasError = false;
        if (TextUtils.isEmpty(user)) {
            userHint.setError(getContext().getString(R.string.required_field));
            hasError = true;
        }
        if (TextUtils.isEmpty(subject)) {
            subjectHint.setError(getContext().getString(R.string.required_field));
            hasError = true;
        }
        if (TextUtils.isEmpty(message)) {
            messageHint.setError(getContext().getString(R.string.required_field));
            hasError = true;
        }
        if (subject.length() > 50 ) {
            subjectHint.setError(getContext().getString(R.string.too_long));
            hasError = true;
        }
        if (!hasError) {
            MessageBody body = new MessageBody.Builder(user, subject, message)
                    .build();
            VoatClient.instance().postMessage(body, sendMessageCallback);
        }
    }

    private final Callback<SimpleResponse> sendMessageCallback = new Callback<SimpleResponse>() {
        @Override
        public void success(SimpleResponse simpleResponse, Response response) {
            if (simpleResponse.success) {
                dismiss();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
        }
    };

    public SendMessageDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_send_message);
        ButterKnife.bind(this);
        userHint.setErrorEnabled(true);
        subjectHint.setErrorEnabled(true);
        messageHint.setErrorEnabled(true);
    }
}
