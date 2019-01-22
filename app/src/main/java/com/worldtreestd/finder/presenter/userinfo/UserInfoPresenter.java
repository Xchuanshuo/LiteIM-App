package com.worldtreestd.finder.presenter.userinfo;

import android.text.TextUtils;

import com.worldtreestd.finder.LoginActivity;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.contract.userinfo.UserInfoContract;

import static com.worldtreestd.finder.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-1-22.
 * @description
 */
public class UserInfoPresenter extends BasePresenter<UserInfoContract.View>
        implements UserInfoContract.Presenter {

    public UserInfoPresenter(UserInfoContract.View view) {
        super(view);
    }

    @Override
    public void followState(Integer targetId) {
        if (TextUtils.isEmpty(sharedData.getJWT())) {
            mView.showFollowState(false);
            return;
        }
        addDisposable(NetworkService.getInstance().isAlreadyFollow(sharedData.getJWT(), targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<Boolean>>() {
                    @Override
                    public void onSuccess(ResultVo<Boolean> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFollowState(data.getData());
                        }
                    }
                }));
    }

    @Override
    public void followUser(Integer targetId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().followUser(jwt, targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFollowState(true);
                        }
                    }
                }));
    }

    @Override
    public void unFollowUser(Integer targetId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().unFollowUser(jwt, targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFollowState(false);
                        }
                    }
                }));
    }
}
