package co.voat.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.voat.android.R;

/**
 * Created by Jawn on 6/20/2015.
 */
public class UserCommentsFragment extends BaseFragment {

    public static UserCommentsFragment newInstance() {
        UserCommentsFragment fragment = new UserCommentsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subscriptions, container, false);
    }
}
