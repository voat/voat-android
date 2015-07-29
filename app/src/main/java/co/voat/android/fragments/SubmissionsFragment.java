package co.voat.android.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.VoatApp;
import co.voat.android.api.SubmissionsResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.api.VoteResponse;
import co.voat.android.data.Submission;
import co.voat.android.data.Vote;
import co.voat.android.events.ToolbarSubverseEvent;
import co.voat.android.viewHolders.SubmissionViewHolder;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Look at all these quality posts....
 * Created by Jawn on 6/20/2015.
 */
public class SubmissionsFragment extends BaseFragment{

    private static final String EXTRA_SUBVERSE = "extra_subverse";
    public static SubmissionsFragment newInstance() {
        return newInstance(null);
    }

    public static SubmissionsFragment newInstance(String subverse) {
        SubmissionsFragment fragment = new SubmissionsFragment();
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(subverse)) {
            args.putString(EXTRA_SUBVERSE, subverse);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.list)
    RecyclerView list;

    EventReceiver eventReceiver;
    String subverse;

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            loadSubverse();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subverse = getArguments().getString(EXTRA_SUBVERSE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_submissions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));
        eventReceiver = new EventReceiver();
        if (!TextUtils.isEmpty(subverse)) {
            loadSubverse();
        }
    }

    private void loadSubverse() {
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

    private class EventReceiver {

        @Subscribe
        public void onToolbarSubverse(ToolbarSubverseEvent event) {
            Timber.d("activity told us to load subverse: " + event.subverse);
            subverse = event.subverse;
            loadSubverse();
        }

    }

    //TODO make this static?
    public class PostAdapter extends RecyclerView.Adapter<SubmissionViewHolder> {

        private final View.OnClickListener onItemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.list_position);
                getBaseActivity().gotoSubmission(mValues.get(position));
            }
        };

        private final View.OnClickListener onCommentsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.list_position);
                getBaseActivity().gotoSubmission(mValues.get(position), true);
            }
        };

        private final Callback<VoteResponse> voteResponseCallback = new Callback<VoteResponse>() {
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
        };

        private final View.OnClickListener onUpvoteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.list_position);
                VoatClient.instance().postVote(Vote.VOTE_SUBMISSION,
                        mValues.get(position).getId(),
                        Vote.VOTE_UP, "", voteResponseCallback);
            }
        };

        private final View.OnClickListener onDownvoteClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag(R.id.list_position);
                VoatClient.instance().postVote(Vote.VOTE_SUBMISSION,
                        mValues.get(position).getId(),
                        Vote.VOTE_DOWN, "", voteResponseCallback);
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
            SubmissionViewHolder holder = SubmissionViewHolder.newInstance(parent);
            holder.itemView.setOnClickListener(onItemClickListener);
            holder.comments.setOnClickListener(onCommentsClickListener);
            holder.upVote.setOnClickListener(onUpvoteClickListener);
            holder.downVote.setOnClickListener(onDownvoteClickListener);
            return holder;
        }

        @Override
        public void onBindViewHolder(final SubmissionViewHolder holder, int position) {
            Submission submission = getValueAt(position);
            holder.bind(submission);
            holder.itemView.setTag(R.id.list_position, position);
            holder.comments.setTag(R.id.list_position, position);
            holder.image.setTag(R.id.list_position, position);
            holder.upVote.setTag(R.id.list_position, position);
            holder.downVote.setTag(R.id.list_position, position);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
