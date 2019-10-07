package com.legend.liteim.presenter.search;

import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.Record;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.contract.search.SearchResultContract;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class GroupSearchPresenter extends BaseSearchPresenter<SearchResultContract.GroupSearchView>
 implements SearchResultContract.Presenter {

    public GroupSearchPresenter(SearchResultContract.GroupSearchView view) {
        super(view);
    }

    @Override
    public void executeSearch(String keyword) {
        addDisposable(NetworkService.getInstance().searchGroup(globalData.getJWT(), 1, 20, keyword)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<ChatGroup>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<ChatGroup>> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            Record<ChatGroup> record = data.getData();
                            mView.showSearchResult(record.getRecords());
                        }
                    }
                }));
    }
}
