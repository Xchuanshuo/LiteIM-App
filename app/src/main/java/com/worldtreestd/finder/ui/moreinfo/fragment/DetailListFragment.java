package com.worldtreestd.finder.ui.moreinfo.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.contract.moreinfo.DetailListContract;
import com.worldtreestd.finder.presenter.moreinfo.DetailListPresenter;
import com.worldtreestd.finder.ui.moreinfo.MoreInfoDetailActivity;
import com.worldtreestd.finder.ui.moreinfo.adapter.DetailItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_SUCCESS;
import static com.worldtreestd.finder.common.utils.Constant.PARAM1;
import static com.worldtreestd.finder.common.utils.Constant.PARAM2;

/**
 * @author Legend
 * @data by on 18-7-15
 * @description
 */
public class DetailListFragment extends BaseFragment<DetailListContract.Presenter>
        implements DetailListContract.View {

    private AppCompatSpinner mSpinner;
    private int type;
    private int id;
    private String name;

    @Override
    protected DetailListContract.Presenter initPresenter() {
        return new DetailListPresenter(this);
    }

    private List<CommonMultiBean> detailList = new ArrayList<>();

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new DetailItemAdapter(detailList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more_info_detail;
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        id = bundle.getInt(PARAM1);
        name = bundle.getString(PARAM2);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        type = ((MoreInfoDetailActivity)_mActivity).getTypeId();
        mSpinner = ((MoreInfoDetailActivity) _mActivity).getSpanner();
    }

    @Override
    public void refreshData() {
        switch (type) {
            case 102:
                mPresenter.requestData(type, ((MoreInfoDetailActivity)_mActivity).getClassGrade(), mSpinner.getSelectedItem().toString(), name);
                break;
            default: break;
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    @Override
    public void showData(List<CommonMultiBean> detailBeanList) {
        detailList.clear();
        switch (type) {
            case 102:
                if (detailBeanList.size() == 0) {
                    getEmptyTextView().setText(getString(R.string.course_is_empty));
                }
                detailList.addAll(detailBeanList);
                break;
            default: break;
        }
        setLoadDataResult(detailList, REFRESH_SUCCESS);
    }
}
