package com.worldtreestd.finder.ui.userinfo.fragment;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.widget.ItemDecoration;
import com.worldtreestd.finder.contract.userinfo.UserFansContract;
import com.worldtreestd.finder.presenter.userinfo.UserFansPresenter;
import com.worldtreestd.finder.ui.userinfo.adapter.UserAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.worldtreestd.finder.common.utils.Constant.LOOK_USER;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserFansFragment extends BaseFragment<UserFansContract.Presenter>
    implements UserFansContract.View {

    private int currentPage = -1;
    private List<User> userList = new ArrayList<>();
    private User lookUser;

    public static UserFansFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOOK_USER, user);
        UserFansFragment fragment = new UserFansFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected UserFansContract.Presenter initPresenter() {
        return new UserFansPresenter(this);
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
        DialogUtils.showToast(getContext(), "执行了刷新");
        this.currentPage = 1;
        userList.clear();
        mPresenter.fansList(lookUser.getId(), currentPage);
    }

    @Override
    public void loadMoreData() {
        currentPage++;
        mPresenter.fansList(lookUser.getId(), currentPage);
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    @Override
    public void showFansList(List<User> userBeanList) {
        userList.addAll(userBeanList);
        if (userList.size() == 0) {
            getEmptyTextView().setText("当前还没有任何粉丝");
            return;
        }
        mAdapter.setNewData(userList);
    }
}
