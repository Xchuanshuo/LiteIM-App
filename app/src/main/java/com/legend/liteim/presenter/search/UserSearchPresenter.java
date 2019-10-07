package com.legend.liteim.presenter.search;

import com.legend.liteim.bean.Record;
import com.legend.liteim.bean.User;
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
public class UserSearchPresenter extends
        BaseSearchPresenter<SearchResultContract.UserSearchView> {

    public UserSearchPresenter(SearchResultContract.UserSearchView view) {
        super(view);
    }

    @Override
    public void executeSearch(String keyword) {
        addDisposable(NetworkService.getInstance().searchUser(globalData.getJWT(), 1, 30, keyword)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<User>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<User>> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            Record<User> record = data.getData();
                            mView.showSearchResult(record.getRecords());
                        }
                    }
                }));

    }
}
