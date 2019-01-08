package com.worldtreestd.finder.ui.userinfo.fragment;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.contract.base.BaseContract;

/**
 * @author Legend
 * @data by on 18-7-22.
 * @description 用户相关的信息
 */
public class UserRelationFragment extends BaseFragment {

    @Override
    protected BaseContract.Presenter initPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info_relation;
    }

    @Override
    public void refreshData() {

    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }
}
