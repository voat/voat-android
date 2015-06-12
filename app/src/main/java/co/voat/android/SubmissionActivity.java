package co.voat.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.api.CommentsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Comment;
import co.voat.android.data.Submission;
import co.voat.android.viewHolders.CommentViewHolder;
import co.voat.android.viewHolders.SubmissionViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Jawn on 6/11/2015.
 */
public class SubmissionActivity extends BaseActivity {

    private static final String EXTRA_SUBMISSION = "extra_submission";

    public static Intent newInstance(Context context, Submission submission) {
        Intent intent = new Intent(context, SubmissionActivity.class);
        intent.putExtra(EXTRA_SUBMISSION, submission);
        return intent;
    }

    @InjectView(R.id.post_root)
    View postRoot;

    @InjectView(R.id.comment_list)
    RecyclerView commentList;

    Submission submission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        submission = (Submission) getIntent().getSerializableExtra(EXTRA_SUBMISSION);
        SubmissionViewHolder holder = new SubmissionViewHolder(postRoot);
        holder.bind(submission);
        commentList.setLayoutManager(new LinearLayoutManager(this));
        VoatClient.instance().getComments(submission.getSubverse(), submission.getId(), new Callback<CommentsResponse>() {
            @Override
            public void success(CommentsResponse commentsResponse, Response response) {
                if (commentsResponse.success) {
                    commentList.setAdapter(new CommentAdapter(commentsResponse.data));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.toString());
            }
        });
    }

    public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {

        private List<Comment> mValues;

        public Comment getValueAt(int position) {
            return mValues.get(position);
        }

        public CommentAdapter(List<Comment> items) {
            mValues = items;
        }

        @Override
        public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CommentViewHolder holder = CommentViewHolder.create(parent);
            return holder;
        }

        @Override
        public void onBindViewHolder(final CommentViewHolder holder, int position) {
            Comment submission = getValueAt(position);
            holder.bind(submission);
            holder.itemView.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }


}
