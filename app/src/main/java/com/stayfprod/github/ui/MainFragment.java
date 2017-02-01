package com.stayfprod.github.ui;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stayfprod.github.R;
import com.stayfprod.github.databinding.FragmentMainBinding;
import com.stayfprod.github.event.ErrorEvent;
import com.stayfprod.github.event.SearchEvent;
import com.stayfprod.github.event.StopProgressEvent;
import com.stayfprod.github.presenter.MainPresenter;
import com.stayfprod.github.ui.adapter.SearchAdapter;
import com.stayfprod.github.util.RecyclerViewLazyScrollListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainFragment extends Fragment {

    private static final String SEARCH_KEY = "search";

    private String mSearchString;
    private SearchView mSearchView;
    private MainPresenter mMainPresenter;
    private SearchAdapter mSearchAdapter;
    private FragmentMainBinding mBind;
    private Handler mHandler;
    private Runnable mSearchAction;

    public MainFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            mSearchString = savedInstanceState.getString(SEARCH_KEY);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mMainPresenter = new MainPresenter();
        mHandler = new Handler(Looper.getMainLooper());
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StopProgressEvent event) {
        stopProgress();
        EventBus.getDefault().removeStickyEvent(StopProgressEvent.class);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SearchEvent event) {
        stopProgress();

        if (event.page == 0)
            mSearchAdapter.cleanList();

        mSearchAdapter.updateList(event.items);
        mBind.emptyResult.setVisibility(mSearchAdapter.isEmptyList() ? View.VISIBLE : View.GONE);
        EventBus.getDefault().removeStickyEvent(SearchEvent.class);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ErrorEvent event) {
        stopProgress();
        if (mMainPresenter.isFirstPage())
            showSnackBar(mBind.getRoot(), event.errDesc);
        EventBus.getDefault().removeStickyEvent(ErrorEvent.class);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mBind == null) {
            mBind = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
            mSearchAdapter = new SearchAdapter(getContext());

            mBind.refresh.setEnabled(false);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            mBind.list.setLayoutManager(layoutManager);
            mBind.list.setAdapter(mSearchAdapter);
            mBind.list.addOnScrollListener(
                    new RecyclerViewLazyScrollListener(getContext(), layoutManager, this::performSearch) {
                        public boolean isNeedDownloadMore() {
                            return mMainPresenter.isNeedDownloadMore();
                        }
                    }
            );
        }

        return mBind.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mSearchView != null)
            mSearchString = mSearchView.getQuery().toString();
        outState.putString(SEARCH_KEY, mSearchString);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        if (mSearchString != null && !mSearchString.isEmpty()) {
            MenuItemCompat.expandActionView(searchItem);
            mSearchView.setQuery(mSearchString, false);
            mSearchView.clearFocus();
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchAdapter.cleanList();
                mMainPresenter.cleanPage();
                mSearchString = newText;
                mHandler.removeCallbacks(mSearchAction);
                mSearchAction = () -> performSearch(newText);
                mHandler.postDelayed(mSearchAction, 500);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void performSearch() {
        performSearch(mSearchString);
    }

    public void performSearch(String text) {
        if (text != null && !text.trim().isEmpty()) {
            if (mMainPresenter.isFirstPage())
                mBind.refresh.setRefreshing(true);
        }

        mMainPresenter.findRepositoriesAsync(text);
    }

    private void stopProgress() {
        mBind.refresh.setRefreshing(false);
    }

    public void showSnackBar(View root, String msg) {
        showSnackBar(root, msg, null, null, false);
    }

    public void showSnackBar(View root, String msg, View.OnClickListener action, String actionButton, boolean isIndefinite) {
        if (msg == null)
            msg = "";
        Snackbar snackbar = Snackbar.make(root, msg, isIndefinite ? Snackbar.LENGTH_INDEFINITE : Snackbar.LENGTH_LONG);

        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setMaxLines(3);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        snackbar.setAction(actionButton, action).show();
    }
}
