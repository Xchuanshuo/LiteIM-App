package com.legend.liteim.ui.contacts.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.contacts.GroupUserAddContract;
import com.legend.liteim.event.RefreshEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.presenter.contacts.GroupUserAddPresenter;
import com.legend.liteim.ui.contacts.adapter.GroupUserAddItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public class GroupUserAddFragment extends BottomSheetDialogFragment
 implements GroupUserAddContract.View {

    public static String KEY_GROUP_ID = "key_group_id";
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private GroupUserAddItemAdapter mAdapter;
    private List<User> userList = new ArrayList<>();
    private GroupUserAddContract.Presenter mPresenter;
    private Long groupId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate( R.layout.fragment_group_user_add, container);
        // 控件绑定
        ButterKnife.bind(this, root);
        initWidget();
        initEventData();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 初始化Presenter
        Bundle bundle = getArguments();
        if (bundle != null) {
            groupId = bundle.getLong(KEY_GROUP_ID);
        }
        initPresenter();
    }

    private void initWidget() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GroupUserAddItemAdapter(R.layout.item_group_user_add, userList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initPresenter() {
        new GroupUserAddPresenter(this, groupId);
    }

    private void initEventData() {
        mPresenter.pullUnJoinedUserList();
    }

    @OnClick(R.id.tv_submit)
    void onSubmitOnClick() {
        String ids = mAdapter.getSelectedIdsStr();
        if (!TextUtils.isEmpty(ids)) {
            mPresenter.submitGroupUser(ids);
        } else {
            DialogUtils.showToast(getContext(), getString(R.string.error_group_ids));
        }
    }

    @Override
    public void setPresenter(GroupUserAddContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showUserList(List<User> users) {
        userList.clear();
        userList.addAll(users);
        mAdapter.setNewData(userList);
    }

    @Override
    public void showSubmitSuccess() {
        RxBus.getDefault().post(new RefreshEvent());
        dismiss();
    }
}
