package co.voat.android.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.R;

/**
 * The best browser ever
 * Created by Jawn on 6/13/2015.
 */
public class WebFragment extends BaseFragment {

    private static final String EXTRA_URL = "extra_url";

    public static WebFragment newInstance(String url) {
        WebFragment frag = new WebFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_URL, url);
        frag.setArguments(args);
        return frag;
    }

    @InjectView(R.id.root)
    View root;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.webview)
    WebView webView;

    String url;

    private final SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            load();
        }
    };

    private final WebViewClient webViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            swipeRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        url = getArguments().getString(EXTRA_URL);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);
        //Fix so that the webview isnt zoomed in initially
        //http://stackoverflow.com/questions/3808532/how-to-set-the-initial-zoom-width-for-a-webview
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(url);
    }

    private void load() {
        swipeRefreshLayout.setRefreshing(true);
        webView.loadUrl(url);
    }
}
