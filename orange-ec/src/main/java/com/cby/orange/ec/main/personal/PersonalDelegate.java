package com.cby.orange.ec.main.personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cby.orange.delegate.bottom.BottomItemDelegate;
import com.cby.orange.ec.R;
import com.cby.orange.ec.R2;
import com.cby.orange.ec.main.personal.list.ListAdapter;
import com.cby.orange.ec.main.personal.list.ListBean;
import com.cby.orange.ec.main.personal.list.ListType;
import com.cby.orange.ec.main.personal.order.OrderListDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by baiyanfang on 2017/12/21.
 */

public class PersonalDelegate extends BottomItemDelegate {

    @BindView(R2.id.rv_personal_setting)
    RecyclerView mRecyclerView = null;

    private String type = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_personal;
    }

    @OnClick(R2.id.tv_all_order)
    void OnClickAllOrder() {
        type = "all";
        StartOrderListByType(type);
    }

    private void StartOrderListByType(String type) {
        final OrderListDelegate delegate = OrderListDelegate.newInstance(type);
        getParentDelegate().getSupportDelegate().start(delegate);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        final ListBean address = new ListBean.Builder()
                .setItemYype(ListType.ITEMNORMAL)
                .setId(1)
                .setText("收货地址")
                .build();
        final ListBean system = new ListBean.Builder()
                .setItemYype(ListType.ITEMNORMAL)
                .setId(2)
                .setText("系统设置")
                .build();

        List<ListBean> listBeans = new ArrayList<>();
        listBeans.add(address);
        listBeans.add(system);

        //设置recycleview
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        ListAdapter adapter = new ListAdapter(listBeans);
        mRecyclerView.setAdapter(adapter);
    }
}
