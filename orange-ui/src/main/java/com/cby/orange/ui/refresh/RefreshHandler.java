package com.cby.orange.ui.refresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cby.orange.app.ConfigKeys;
import com.cby.orange.app.Orange;
import com.cby.orange.net.RestClient;
import com.cby.orange.net.callback.IError;
import com.cby.orange.net.callback.IFailure;
import com.cby.orange.net.callback.ISuccess;
import com.cby.orange.ui.recycler.DataConverter;
import com.cby.orange.ui.recycler.MultipleRecyclerAdapter;
import com.cby.orange.utils.log.OrangeLogger;
import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by baiyanfang on 2017/12/21.
 */

public class RefreshHandler implements
        SwipeRefreshLayout.OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener {

    private final SwipeRefreshLayout REFRESH_LAYOUT;
    private final PagingBean BEAN;
    private final RecyclerView RECYCLERVIEW;
    private MultipleRecyclerAdapter mAdapter = null;
    private final DataConverter CONVERTER;

    public RefreshHandler(SwipeRefreshLayout refresh_layout,
                          PagingBean bean,
                          RecyclerView recyclerview,
                          DataConverter converter) {
        this.REFRESH_LAYOUT = refresh_layout;
        this.BEAN = bean;
        this.RECYCLERVIEW = recyclerview;
        this.CONVERTER = converter;
        REFRESH_LAYOUT.setOnRefreshListener(this);
    }

    public static RefreshHandler create(SwipeRefreshLayout refresh_layout,
                                        RecyclerView recyclerview,
                                        DataConverter converter) {
        return new RefreshHandler(refresh_layout, new PagingBean(), recyclerview, converter);
    }

    private void refresh() {
        REFRESH_LAYOUT.setRefreshing(true);
        RestClient.builder()
                .url("index.php")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        final JSONObject jsonObject = JSON.parseObject(response);
                        BEAN.setTotal(jsonObject.getInteger("total"))
                                .setPageSize(jsonObject.getInteger("page_size"))
                                .setPageIndex(0)
                                .setCurrentCount(0);

                        //设置adapter
                        mAdapter.getData().clear();
                        mAdapter.setNewData(CONVERTER.setJsonData(response).convert());
                        BEAN.addIndex();
                        REFRESH_LAYOUT.setRefreshing(false);

                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build()
                .get();
    }

    public void firstPage(String url) {
        BEAN.setDelayed(1000);
        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        final JSONObject jsonObject = JSON.parseObject(response);
                        BEAN.setTotal(jsonObject.getInteger("total"))
                                .setPageSize(jsonObject.getInteger("page_size"));
                        //设置adapter
                        mAdapter = MultipleRecyclerAdapter.create(CONVERTER.setJsonData(response));
                        mAdapter.setOnLoadMoreListener(RefreshHandler.this, RECYCLERVIEW);
                        RECYCLERVIEW.setAdapter(mAdapter);
                        BEAN.addIndex();

                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build()
                .get();

    }

    private void paging(final String url) {
        final int pageSize = BEAN.getPageSize();
        final int currentCount = BEAN.getCurrentCount();
        final int total = BEAN.getTotal();
        final int index = BEAN.getPageIndex();

        if (mAdapter.getData().size() < pageSize || currentCount >= pageSize) {
            mAdapter.loadMoreEnd();
        } else {
            Orange.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RestClient.builder()
                            .url(url + index)
                            .success(new ISuccess() {
                                @Override
                                public void onSuccess(String response) {
                                    mAdapter.addData(CONVERTER.setJsonData(response).convert());
                                    //累加数量
                                    BEAN.setCurrentCount(mAdapter.getData().size());
                                    mAdapter.loadMoreComplete();
                                    BEAN.addIndex();
                                }
                            })
                            .build()
                            .get();
                }
            }, 1000);
        }
    }

    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onLoadMoreRequested() {
        paging("refresh.php?index=");
    }
}
