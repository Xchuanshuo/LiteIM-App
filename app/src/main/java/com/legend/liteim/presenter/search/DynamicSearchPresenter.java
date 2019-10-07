package com.legend.liteim.presenter.search;

import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.Record;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.contract.search.SearchResultContract;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class DynamicSearchPresenter extends BaseSearchPresenter<SearchResultContract.DynamicSearchView>
        implements SearchResultContract.Presenter {

    public DynamicSearchPresenter(SearchResultContract.DynamicSearchView view) {
        super(view);
    }

    @Override
    public void executeSearch(String keyword) {
        addDisposable(NetworkService.getInstance().searchDynamic(globalData.getJWT(), 1, 20, keyword)
                .map(resultVo -> {
                    List<CommonMultiBean<Dynamic>> multiBeans = new ArrayList<>();
                    if (SUCCESS.equals(resultVo.getCode())) {
                        Record<Dynamic> record = resultVo.getData();
                        List<Dynamic> dynamics = record.getRecords();
                        for (Dynamic d : dynamics) {
                            CommonMultiBean<Dynamic> multiBean = new CommonMultiBean<>(d, d.getType());
                            multiBeans.add(multiBean);
                        }
                    }
                    return multiBeans;
                })
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<List<CommonMultiBean<Dynamic>>>() {

                    @Override
                    public void onSuccess(List<CommonMultiBean<Dynamic>> data) {
                        mView.showSearchResult(data);
                    }
                }));
    }
}
