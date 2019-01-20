package com.worldtreestd.finder.ui.userinfo.fragment;

import android.os.Bundle;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.contract.userinfo.PersonalDynamicContract;
import com.worldtreestd.finder.presenter.userinfo.PersonalDynamicPresenter;
import com.worldtreestd.finder.ui.dynamic.adapter.DynamicItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.worldtreestd.finder.common.utils.Constant.LOOK_USER;

/**
 * @author Legend
 * @data by on 18-7-22.
 * @description 用户相关的信息
 */
public class PersonalDynamicFragment extends BaseFragment<PersonalDynamicContract.Presenter>
    implements PersonalDynamicContract.View {

    private int currentPage = 1;
    private List<CommonMultiBean<Dynamic>> beanList = new ArrayList<>();
    private User lookUser;

    @Override
    protected PersonalDynamicContract.Presenter initPresenter() {
        return new PersonalDynamicPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_user_info_relation;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new DynamicItemAdapter(beanList, getContext());
    }

    public static PersonalDynamicFragment newInstance(User user) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOOK_USER, user);
        PersonalDynamicFragment fragment = new PersonalDynamicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        this.lookUser = (User) bundle.getSerializable(LOOK_USER);
    }

    @Override
    public void refreshData() {
        beanList.clear();
        this.currentPage = 1;
        if (getUserVisibleHint()) {
            mPresenter.personalDynamic(lookUser.getId(), currentPage);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    @Override
    public void showData(List<CommonMultiBean<Dynamic>> multiBeanList) {
        beanList.addAll(multiBeanList);
        mAdapter.setNewData(beanList);
    }

    @Override
    public void showNoMoreData() {
        mAdapter.loadMoreEnd();
    }

    @Override
    public void loadMoreData() {
        currentPage ++;
        mPresenter.personalDynamic(lookUser.getId(), currentPage);
    }
}
