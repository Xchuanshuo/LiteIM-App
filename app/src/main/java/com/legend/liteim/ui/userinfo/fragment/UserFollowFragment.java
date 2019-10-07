package com.legend.liteim.ui.userinfo.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.common.widget.ItemDecoration;
import com.legend.liteim.contract.userinfo.UserFollowContract;
import com.legend.liteim.presenter.userinfo.UserFollowPresenter;
import com.legend.liteim.ui.userinfo.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.ui.userinfo.UserInfoActivity.LOOK_USER;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserFollowFragment extends BaseFragment<UserFollowContract.Presenter>
    implements UserFollowContract.View {

    private int currentPage = -1;
    private List<User> userList = new ArrayList<>();
    private User lookUser;

    public static UserFollowFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOOK_USER, user);
        UserFollowFragment fragment = new UserFollowFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected UserFollowContract.Presenter initPresenter() {
        return new UserFollowPresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new UserAdapter(R.layout.item_user_follow, userList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info_relation;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        this.lookUser = (User) bundle.getSerializable(LOOK_USER);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mRecyclerView.addItemDecoration(new ItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void refreshData() {
        this.currentPage = 1;
        userList.clear();
        mPresenter.followerList(lookUser.getId(), currentPage);
    }

    @Override
    public void loadMoreData() {
        currentPage++;
        mPresenter.followerList(lookUser.getId(), currentPage);
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    @Override
    public void showFollowerList(List<User> userBeanList) {
        userList.addAll(userBeanList);
        if (userList.size() == 0) {
            getEmptyTextView().setText("当前还未关注任何用户");
            return;
        }
        mAdapter.setNewData(userList);
    }
}
