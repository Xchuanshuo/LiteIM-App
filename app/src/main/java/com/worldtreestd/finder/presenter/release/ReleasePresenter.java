package com.worldtreestd.finder.presenter.release;

import android.text.TextUtils;

import com.worldtreestd.finder.LoginActivity;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ProgressTransformer;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.contract.release.ReleaseContract;
import com.worldtreestd.finder.data.SharedData;

import java.util.Map;

import okhttp3.RequestBody;

import static com.worldtreestd.finder.common.utils.Code.SUCCESS;

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
        String jwt = SharedData.getInstance().getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            DialogUtils.showToast(mView.getContext(), "请先登录再进行操作!");
        }
        addDisposable(NetworkService.getInstance().addDynamic(jwt, params)
                .compose(new NetworkService.ThreadTransformer<>())
                .compose(ProgressTransformer.applyProgressBar(mView.getContext(), "正在发布..."))
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
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
                    public void onError(Throwable e) {
                        super.onError(e);
                        DialogUtils.showToast(mView.getContext(), e.getMessage());
                    }
                }));

    }
}
