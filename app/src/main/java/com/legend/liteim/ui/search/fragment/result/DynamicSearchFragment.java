package com.legend.liteim.ui.search.fragment.result;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.contract.search.SearchResultContract;
import com.legend.liteim.presenter.search.DynamicSearchPresenter;
import com.legend.liteim.ui.dynamic.adapter.DynamicItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class DynamicSearchFragment extends BaseSearchFragment<CommonMultiBean<Dynamic>>
    implements SearchResultContract.DynamicSearchView  {

    private List<CommonMultiBean<Dynamic>> beanList = new ArrayList<>();

    @Override
    protected SearchResultContract.Presenter initPresenter() {
        return new DynamicSearchPresenter(this);
    }

    @Override
    protected void initWidget() {
        setEnableLoaderMore(false);
        super.initWidget();
        getEmptyTextView().setText(getString(R.string.empty_dynamic_list));
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new DynamicItemAdapter(beanList, getContext());
    }

    @Override
    public void showSearchResult(List<CommonMultiBean<Dynamic>> data) {
        beanList.clear();
        beanList.addAll(data);
        setLoadDataResult(beanList, REFRESH_SUCCESS);
    }
}
