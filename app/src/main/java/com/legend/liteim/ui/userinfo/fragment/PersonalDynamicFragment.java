package com.legend.liteim.ui.userinfo.fragment;

import android.os.Bundle;

import com.legend.liteim.R;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.widget.CommonPopupWindow;
import com.legend.liteim.contract.userinfo.PersonalDynamicContract;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.presenter.userinfo.PersonalDynamicPresenter;
import com.legend.liteim.ui.dynamic.adapter.DynamicItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.ui.userinfo.UserInfoActivity.LOOK_USER;

/**
 * @author Legend
 * @data by on 18-7-22.
 * @description 用户相关的信息
 */
public class PersonalDynamicFragment extends BaseFragment<PersonalDynamicContract.Presenter, DynamicItemAdapter>
    implements PersonalDynamicContract.View {

    private int curPosition = -1;
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
    protected DynamicItemAdapter getAdapter() {
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
        mPresenter.personalDynamic(lookUser.getId(), currentPage);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mAdapter.setSelectorListener((v, position) -> {
            CommonPopupWindow mPopupWindow = CommonPopupWindow.getInstance()
                    .buildPopupWindow(v, -30, -27).onlyHideReport();
            curPosition = position;
            Dynamic dynamic = beanList.get(curPosition).getData();
            User operateUser = UserHelper.getInstance().getCurrentUser();
            if (operateUser==null || !operateUser.getId().equals(dynamic.getUserId())) {
                mPopupWindow.hideDelete();
            }
            mPopupWindow.setDeleteListener(view -> {
                mPresenter.deleteDynamic(dynamic.getId());
            });
            mPopupWindow.setShareListener(view -> {
                DialogUtils.showToast(view.getContext(), "分享了动态"+dynamic.getId());
            });
            mPopupWindow.show();
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    @Override
    public void showData(List<CommonMultiBean<Dynamic>> multiBeanList) {
        beanList.addAll(multiBeanList);
        if (beanList.size() == 0) {
            getEmptyTextView().setText("当前还未发布任何动态");
            return;
        }
        mAdapter.setNewData(beanList);
    }

    @Override
    public void showDeleteSuccess(String msg) {
        if (curPosition != -1) {
            beanList.remove(curPosition);
            mAdapter.notifyItemRangeRemoved(curPosition, 1);
        }
        DialogUtils.showToast(getContext(), msg);
    }

    @Override
    public void loadMoreData() {
        currentPage ++;
        mPresenter.personalDynamic(lookUser.getId(), currentPage);
    }
}
