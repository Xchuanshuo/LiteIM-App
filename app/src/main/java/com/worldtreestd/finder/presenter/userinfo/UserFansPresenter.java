package com.worldtreestd.finder.presenter.userinfo;

import com.worldtreestd.finder.bean.Record;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.contract.userinfo.UserFansContract;

import static com.worldtreestd.finder.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserFansPresenter extends BasePresenter<UserFansContract.View>
    implements UserFansContract.Presenter {

    private int totalPage = -1;

    public UserFansPresenter(UserFansContract.View view) {
        super(view);
    }

    @Override
    public void fansList(Integer userId, Integer page) {
        if (totalPage != -1 && page > totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance().userFansList(userId, page, 10)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<Record<User>>>() {
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
