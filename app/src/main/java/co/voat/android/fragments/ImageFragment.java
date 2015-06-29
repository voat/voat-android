package co.voat.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.data.Submission;
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

    @Bind(R.id.root)
    View root;
    @Bind(R.id.image)
    PhotoView image;

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
        ButterKnife.bind(this, view);
        submission = (Submission) getArguments().getSerializable(EXTRA_SUBMISSION);
        Glide.with(this)
                .load(submission.getUrl())
                .into(image);
        image.setOnClickListener(onImageClickListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
