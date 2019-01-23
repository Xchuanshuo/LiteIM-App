package com.worldtreestd.finder.presenter.userinfo;

import com.worldtreestd.finder.bean.Record;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.common.utils.Code;
import com.worldtreestd.finder.contract.userinfo.UserFollowContract;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserFollowPresenter extends BasePresenter<UserFollowContract.View>
    implements UserFollowContract.Presenter {

    private int totalPage = 0;

    public UserFollowPresenter(UserFollowContract.View view) {
        super(view);
    }

    @Override
    public void followerList(Integer userId, Integer page) {
        if (totalPage!=0 && page > totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance().userFollowList(userId, page, 10)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<Record<User>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<User>> data) {
                        if (data.getCode().equals(Code.SUCCESS)) {
                            Record<User> record = data.getData();
                            totalPage = record.getPages();
                            mView.showFollowerList(record.getRecords());
                        }
                    }
                }));
    }
}
