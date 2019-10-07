package com.legend.liteim.presenter.userinfo;

import com.legend.liteim.bean.Record;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.contract.userinfo.UserFansContract;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserFansPresenter extends BasePresenter<UserFansContract.View>
    implements UserFansContract.Presenter {

    private int totalPage = 0;

    public UserFansPresenter(UserFansContract.View view) {
        super(view);
    }

    @Override
    public void fansList(Long userId, Integer page) {
        if (totalPage != 0 && page > totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance().userFansList(userId, page, 10)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<User>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<User>> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            Record<User> record = data.getData();
                            totalPage = record.getPages();
                            mView.showFansList(record.getRecords());
                        }
                    }
                }));
    }
}
