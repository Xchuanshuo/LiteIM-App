package com.legend.liteim.model;

import android.content.Context;
import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.event.GroupJoinEvent;
import com.legend.liteim.event.RxBus;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public class GroupAddModel {

    private GlobalData globalData = GlobalData.getInstance();
    private Context mContext;

    public GroupAddModel(Context context) {
        this.mContext = context;
    }

    public void joinGroup(Long groupId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mContext);
            return;
        }
        NetworkService.getInstance().joinGroup(jwt, groupId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribe(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            RxBus.getDefault().post(new GroupJoinEvent(true));
                        } else {
                            RxBus.getDefault().post(new GroupJoinEvent(false));
                        }
                        DialogUtils.showToast(mContext, data.getMsg());
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        super.onFail(errorMsg);
                        RxBus.getDefault().post(new GroupJoinEvent(false));
                    }
                });

    }
}
