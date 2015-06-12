package co.voat.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.voat.android.R;

/**
 * Show dem messages
 * Created by Jawn on 6/12/2015.
 */
public class MessagesFragment extends BaseFragment {

    public static MessagesFragment newInstance() {
        MessagesFragment frag = new MessagesFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }
}
