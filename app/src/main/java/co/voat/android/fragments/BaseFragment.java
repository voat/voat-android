package co.voat.android.fragments;

import android.support.v4.app.Fragment;

import co.voat.android.BaseActivity;

/**
 * Created by Jawn on 6/12/2015.
 */
public class BaseFragment extends Fragment {

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
