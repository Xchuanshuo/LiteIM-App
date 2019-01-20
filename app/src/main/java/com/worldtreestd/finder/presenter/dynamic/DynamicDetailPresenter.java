package com.worldtreestd.finder.presenter.dynamic;

import android.text.TextUtils;

import com.worldtreestd.finder.LoginActivity;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.common.utils.Code;
import com.worldtreestd.finder.contract.dynamic.DynamicDetailContract;
import com.worldtreestd.finder.event.CollectEvent;
import com.worldtreestd.finder.event.RxBus;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class DynamicDetailPresenter extends BasePresenter<DynamicDetailContract.View>
    implements DynamicDetailContract.Presenter {

    public DynamicDetailPresenter(DynamicDetailContract.View view) {
        super(view);
        registerEvent();
    }

    private void registerEvent() {
        RxBus.getDefault().toObservable(CollectEvent.class)
                .subscribe(new BaseObserve<CollectEvent>() {
                    @Override
                    public void onSuccess(CollectEvent event) {
                        if (event.isCollected()) {
                            mView.showCollectedSuccess();
                        } else {
                            mView.showUnCollectedSuccess();
                        }
                    }
                });
    }

    @Override
    public void collectDynamic(Integer dynamicId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().collectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(Code.SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(true));
                        }
                    }
                }));
    }

    @Override
    public void unCollectDynamic(Integer dynamicId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().unCollectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(Code.SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(false));
                        }
                    }
                }));

    }
}
