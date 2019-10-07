package com.legend.liteim.presenter.userinfo;

import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.userinfo.UserInfoContract;

import static com.legend.liteim.common.utils.Code.SUCCESS;

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
    public void followState(Long targetId) {
        if (TextUtils.isEmpty(globalData.getJWT())) {
            mView.showFollowState(false);
            return;
        }
        addDisposable(NetworkService.getInstance().isAlreadyFollow(globalData.getJWT(), targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Boolean>>() {
                    @Override
                    public void onSuccess(ResultVo<Boolean> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFollowState(data.getData());
                        }
                    }
                }));
    }

    @Override
    public void followUser(Long targetId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().followUser(jwt, targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFollowState(true);
                        }
                    }
                }));
    }

    @Override
    public void unFollowUser(Long targetId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().unFollowUser(jwt, targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFollowState(false);
                        }
                    }
                }));
    }

    @Override
    public void friendState(Long targetId) {
        if (TextUtils.isEmpty(globalData.getJWT())) {
            mView.showFriendState(false);
            return;
        }
        addDisposable(NetworkService.getInstance().isFriend(globalData.getJWT(), targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFriendState(true);
                        }
                    }
                }));
    }

    @Override
    public void addFriend(Long targetId) {
        if (TextUtils.isEmpty(globalData.getJWT())) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().addFriend(globalData.getJWT(), targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFriendState(true);
                            DialogUtils.showToast(mView.getContext(), "添加好友成功");
                        }
                    }
                }));
    }

    @Override
    public void deleteFriend(Long targetId) {
        if (TextUtils.isEmpty(globalData.getJWT())) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().deleteFriend(globalData.getJWT(), targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showFriendState(false);
                            DialogUtils.showToast(mView.getContext(), "删除好友成功!");
                        }
                    }
                }));
    }
}
