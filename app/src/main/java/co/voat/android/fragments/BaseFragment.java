package co.voat.android.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.voat.android.activities.BaseActivity;

/**
 * Created by Jawn on 6/12/2015.
 */
public class BaseFragment extends Fragment {

    private boolean viewCreated;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewCreated = true;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        viewCreated = false;
        super.onDestroyView();
    }

    public boolean isViewCreated() {
        return viewCreated;
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
