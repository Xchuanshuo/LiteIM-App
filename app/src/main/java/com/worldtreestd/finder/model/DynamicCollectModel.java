package com.worldtreestd.finder.model;

import android.content.Context;
import android.text.TextUtils;

import com.worldtreestd.finder.LoginActivity;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.common.utils.Code;
import com.worldtreestd.finder.data.SharedData;
import com.worldtreestd.finder.event.CollectEvent;
import com.worldtreestd.finder.event.RxBus;

/**
 * @author Legend
 * @data by on 19-1-21.
 * @description
 */
public class DynamicCollectModel {

    private SharedData sharedData = SharedData.getInstance();
    private Context mContext;

    public DynamicCollectModel(Context context) {
        this.mContext = context;
    }

    public void collectDynamic(Integer dynamicId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mContext);
            return;
        }
        NetworkService.getInstance().collectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(Code.SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(true));
                        }
                    }
                });
    }

    public void unCollectDynamic(Integer dynamicId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mContext);
            return;
        }
        NetworkService.getInstance().unCollectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(Code.SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(false));
                        }
                    }
                });

    }
}
