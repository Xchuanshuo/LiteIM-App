package com.legend.liteim.presenter.userinfo;

import android.content.Context;
import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.R;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.Record;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.userinfo.PersonalDynamicContract;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-1-19.
 * @description
 */
public class PersonalDynamicPresenter extends BasePresenter<PersonalDynamicContract.View>
    implements PersonalDynamicContract.Presenter {

    private static int totalPage = 0;

    public PersonalDynamicPresenter(PersonalDynamicContract.View view) {
        super(view);
    }

    @Override
    public void personalDynamic(Long userId, Integer page) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            Context mContext = mView.getContext();
            LoginActivity.show(mContext);
            return;
        }
        if (totalPage!=0 && page > totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance().getPersonalDynamic(globalData.getJWT(), userId, page, 5)
                .map(recordResultVo -> {
                    List<CommonMultiBean<Dynamic>> beanList = new ArrayList<>();
                    if (recordResultVo.getCode().equals(SUCCESS)) {
                        Record<Dynamic> record = recordResultVo.getData();
                        totalPage = record.getPages();
                        List<Dynamic> list = record.getRecords();
                        for (Dynamic d: list) {
                            beanList.add(new CommonMultiBean<>(d, d.getType()));
                        }
                    }
                    return beanList;
                })
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<List<CommonMultiBean<Dynamic>>>() {
                    @Override
                    public void onSuccess(List<CommonMultiBean<Dynamic>> data) {
                        mView.showData(data);
                    }
                }));
    }

    @Override
    public void deleteDynamic(Long dynamicId) {
        Context mContext = mView.getContext();
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            DialogUtils.showToast(mContext, mContext.getString(R.string.login_request));
            LoginActivity.show(mContext);
        }
        addDisposable(NetworkService.getInstance().deleteDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showDeleteSuccess(data.getData());
                        } else {
                            DialogUtils.showToast(mContext, "只能删除自己发布的动态!");
                        }
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        super.onFail(errorMsg);
                        DialogUtils.showToast(mContext, errorMsg);
                    }
                }));
    }
}
