package co.voat.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.VoatApp;
import co.voat.android.api.CommentsResponse;
import co.voat.android.api.UserResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.api.VoteResponse;
import co.voat.android.data.Comment;
import co.voat.android.data.Submission;
import co.voat.android.data.Vote;
import co.voat.android.dialogs.CommentDialog;
import co.voat.android.events.ContextualCommentEvent;
import co.voat.android.events.ContextualDownvoteEvent;
import co.voat.android.events.ContextualProfileEvent;
import co.voat.android.events.ContextualUpvoteEvent;
import co.voat.android.events.ShowContextualMenuEvent;
import co.voat.android.viewHolders.CommentViewHolder;
import co.voat.android.viewHolders.CommentsHeaderViewHolder;
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

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.comment_list)
    RecyclerView commentList;

    @OnClick(R.id.fab)
    void onFabClick(View v) {
        new CommentDialog(getActivity(), submission).show();
    }

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadComments();
        }
    };

    CommentAdapter commentAdapter;

    Submission submission;
    EventReceiver eventReceiver;

    private Callback<CommentsResponse> commentsResponseCallback = new Callback<CommentsResponse>() {
        @Override
        public void success(CommentsResponse commentsResponse, Response response) {
            if (commentsResponse.success) {
                commentAdapter.setData(commentsResponse.data);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Timber.e(error.toString());
            Snackbar.make(getActivity().getWindow().getDecorView(), getString(R.string.error), Snackbar.LENGTH_SHORT)
                    .show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        submission = (Submission) getArguments().getSerializable(EXTRA_SUBMISSION);
        commentList.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new CommentAdapter(submission);
        commentList.setAdapter(commentAdapter);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        eventReceiver = new EventReceiver();
        loadComments();
    }

    private void loadComments() {
        swipeRefreshLayout.setRefreshing(true);
        VoatClient.instance().getComments(submission.getSubverse(), submission.getId(), commentsResponseCallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        VoatApp.bus().register(eventReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        VoatApp.bus().unregister(eventReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class EventReceiver {

        @Subscribe
        public void onContextualComment(ContextualCommentEvent event) {
            VoatApp.bus().post(new ShowContextualMenuEvent());
        }

        @Subscribe
        public void onContextualUpvote(ContextualUpvoteEvent event) {
            VoatApp.bus().post(new ShowContextualMenuEvent());
            VoatClient.instance().postVote(Vote.VOTE_COMMENT,
                    commentAdapter.getSelectedComment().getId(),
                    Vote.VOTE_UP, "", new Callback<VoteResponse>() {
                        @Override
                        public void success(VoteResponse voteResponse, Response response) {
                            if (voteResponse.success && voteResponse.data.success) {
                                Toast.makeText(getActivity(), getString(R.string.vote_cast), Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(getActivity(), voteResponse.data.message, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Timber.e(error.toString());
                            Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

        }

        @Subscribe
        public void onContextualDownvote(ContextualDownvoteEvent event) {
            VoatApp.bus().post(new ShowContextualMenuEvent());
            VoatClient.instance().postVote(Vote.VOTE_COMMENT,
                    commentAdapter.getSelectedComment().getId(),
                    Vote.VOTE_UP, "", new Callback<VoteResponse>() {
                        @Override
                        public void success(VoteResponse voteResponse, Response response) {
                            if (voteResponse.success && voteResponse.data.success) {
                                Toast.makeText(getActivity(), getString(R.string.vote_cast), Toast.LENGTH_SHORT)
                                        .show();
                            } else {
                                Toast.makeText(getActivity(), voteResponse.data.message, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Timber.e(error.toString());
                            Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }

        @Subscribe
        public void onContextualProfile(ContextualProfileEvent event) {
            VoatApp.bus().post(new ShowContextualMenuEvent());
            VoatClient.instance().getUserInfo(
                    commentAdapter.getSelectedComment().getUserName(), new Callback<UserResponse>() {
                        @Override
                        public void success(UserResponse userResponse, Response response) {
                            if (userResponse.success) {
                                getBaseActivity().gotoUser(userResponse.data);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.could_not_retrieve_user), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Timber.e(error.toString());
                            Toast.makeText(getActivity(), getString(R.string.could_not_retrieve_user), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        }

    }

    public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 0;
        private static final int TYPE_COMMENT = 1;

        private Submission submission;
        private ArrayList<Comment> comments = new ArrayList<>();
        private Comment selectedComment;

        public Comment getValueAt(int position) {
            return comments.get(position-1);
        }

        public Comment getSelectedComment() {
            return selectedComment;
        }

        public CommentAdapter(Submission submission) {
            this.submission = submission;
        }

        public void setData(List<Comment> newComments) {
            comments.clear();
            comments.addAll(newComments);
            notifyDataSetChanged();
        }

        private final View.OnLongClickListener onCommentLongClick = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                VoatApp.bus().post(new ShowContextualMenuEvent());
                selectedComment = getValueAt((int) v.getTag(R.id.list_position));
                return true;
            }
        };

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                return CommentsHeaderViewHolder.create(parent);
            } else if (viewType == TYPE_COMMENT) {
                RecyclerView.ViewHolder holder = CommentViewHolder.newInstance(parent);
                holder.itemView.setOnLongClickListener(onCommentLongClick);
                ((CommentViewHolder)holder).contentText.setOnLongClickListener(onCommentLongClick);
                return holder;
            }
            throw new IllegalArgumentException("No view type matches");
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof CommentViewHolder) {
                Comment comment = getValueAt(position);
                ((CommentViewHolder) holder).bind(comment);
                holder.itemView.setTag(R.id.list_position, position);
                ((CommentViewHolder) holder).contentText.setTag(R.id.list_position, position);
            } else if (holder instanceof CommentsHeaderViewHolder) {
                ((CommentsHeaderViewHolder) holder).bind(submission);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            } else {
                return TYPE_COMMENT;
            }
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }
    }
}
