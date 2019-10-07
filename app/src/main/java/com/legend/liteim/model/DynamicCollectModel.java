package com.legend.liteim.model;

import android.content.Context;
import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.Code;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.event.CollectEvent;
import com.legend.liteim.event.RxBus;

/**
 * @author Legend
 * @data by on 19-1-21.
 * @description
 */
public class DynamicCollectModel {

    private GlobalData globalData = GlobalData.getInstance();
    private Context mContext;

    public DynamicCollectModel(Context context) {
        this.mContext = context;
    }

    public void collectDynamic(Long dynamicId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mContext);
            return;
        }
        NetworkService.getInstance().collectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(Code.SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(true));
                        }
                    }
                });
    }

    public void unCollectDynamic(Long dynamicId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mContext);
            return;
        }
        NetworkService.getInstance().unCollectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(Code.SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(false));
                        }
                    }
                });

    }
}
