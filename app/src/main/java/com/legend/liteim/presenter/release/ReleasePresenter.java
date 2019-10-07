package com.legend.liteim.presenter.release;

import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ProgressTransformer;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.release.ReleaseContract;

import java.util.Map;

import okhttp3.RequestBody;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class ReleasePresenter extends BasePresenter<ReleaseContract.View>
    implements ReleaseContract.Presenter {

    public ReleasePresenter(ReleaseContract.View view) {
        super(view);
    }

    @Override
    public void addDynamic(Map<String, RequestBody> params) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            DialogUtils.showToast(mView.getContext(), "请先登录再进行操作!");
            return;
        }
        addDisposable(NetworkService.getInstance().addDynamic(jwt, params)
                .compose(new NetworkService.ThreadTransformer<>())
                .compose(ProgressTransformer.applyProgressBar(mView.getContext(), "正在发布..."))
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            params.clear();
                            mView.releaseSuccess(data.getData());
                        } else {
                            DialogUtils.showToast(mView.getContext(), "上传出错!");
                        }
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        super.onFail(errorMsg);
                        DialogUtils.showToast(mView.getContext(), errorMsg);
                    }

                }));

    }
}
