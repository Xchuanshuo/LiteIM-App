package com.legend.liteim.model;

import android.content.Context;
import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.event.UserAddEvent;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class UserAddModel {

    private GlobalData globalData = GlobalData.getInstance();
    private Context mContext;

    public UserAddModel(Context context) {
        this.mContext = context;
    }

    public void addFriend(Long targetId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mContext);
            return;
        }
        NetworkService.getInstance().addFriend(jwt, targetId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            RxBus.getDefault().post(new UserAddEvent(true));
                        } else {
                            RxBus.getDefault().post(new UserAddEvent(false));
                        }
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        super.onFail(errorMsg);
                        RxBus.getDefault().post(new UserAddEvent(false));
                    }
                });

    }
}
