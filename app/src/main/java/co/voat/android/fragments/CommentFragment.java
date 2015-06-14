package co.voat.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.R;
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
 * Comments on comments on comments
 * Created by Jawn on 6/13/2015.
 */
public class CommentFragment extends BaseFragment {
    private static final String EXTRA_SUBMISSION = "extra_submission";

    public static CommentFragment newInstance(Submission submission) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SUBMISSION, submission);
        fragment.setArguments(args);
        return fragment;
    }

    @InjectView(R.id.post_root)
    View postRoot;
    @InjectView(R.id.submission_content)
    TextView submissionContentText;
    @InjectView(R.id.comment_list)
    RecyclerView commentList;

    Submission submission;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        submission = (Submission) getArguments().getSerializable(EXTRA_SUBMISSION);
        SubmissionViewHolder holder = new SubmissionViewHolder(postRoot);
        holder.bind(submission);
        if (TextUtils.isEmpty(submission.getFormattedContent())) {
            submissionContentText.setVisibility(View.GONE);
        } else {
            submissionContentText.setText(Html.fromHtml(submission.getFormattedContent()));
            submissionContentText.setMovementMethod(LinkMovementMethod.getInstance());
        }
        commentList.setLayoutManager(new LinearLayoutManager(getActivity()));
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
