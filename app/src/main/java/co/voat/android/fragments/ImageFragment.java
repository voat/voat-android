package co.voat.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import co.voat.android.R;
import co.voat.android.data.Submission;
import co.voat.android.util.IntentUtils;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Jawn on 6/13/2015.
 */
public class ImageFragment extends BaseFragment {
    private static final String EXTRA_SUBMISSION = "extra_submission";

    public static ImageFragment newInstance(Submission submission) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SUBMISSION, submission);
        fragment.setArguments(args);
        return fragment;
    }

    @InjectView(R.id.root)
    View root;
    @InjectView(R.id.image)
    PhotoView image;

    @OnClick(R.id.upvote)
    void onUpVote(View v) {

    }
    @OnClick(R.id.downvote)
    void onDownVote(View v) {

    }
    @OnClick(R.id.share)
    void onClickShare(View v) {
        if (!IntentUtils.share(getActivity(), submission.getUrl())) {
            Snackbar.make(root, getString(R.string.no_share), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
    @OnClick(R.id.download)
    void onClickDownload(View v) {

    }
    @OnClick(R.id.browser)
    void onClickBrowser(View v) {
        if (!IntentUtils.openBrowser(getActivity(), submission.getUrl())) {
            Snackbar.make(root, getString(R.string.no_browser), Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    Submission submission;

    private final View.OnClickListener onImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        submission = (Submission) getArguments().getSerializable(EXTRA_SUBMISSION);
        Glide.with(this)
                .load(submission.getUrl())
                .into(image);
        image.setOnClickListener(onImageClickListener);
    }
}
