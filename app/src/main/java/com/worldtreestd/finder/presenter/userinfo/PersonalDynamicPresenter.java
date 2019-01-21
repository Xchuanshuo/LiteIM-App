package com.worldtreestd.finder.presenter.userinfo;

import android.content.Context;
import android.text.TextUtils;

import com.worldtreestd.finder.LoginActivity;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.bean.Record;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.contract.userinfo.PersonalDynamicContract;

import java.util.ArrayList;
import java.util.List;

import static com.worldtreestd.finder.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-1-19.
 * @description
 */
public class PersonalDynamicPresenter extends BasePresenter<PersonalDynamicContract.View>
    implements PersonalDynamicContract.Presenter {

    private static int totalPage = -1;

    public PersonalDynamicPresenter(PersonalDynamicContract.View view) {
        super(view);
    }

    @Override
    public void personalDynamic(Integer userId, Integer page) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            Context mContext = mView.getContext();
            LoginActivity.come(mContext);
            return;
        }
        if (totalPage!=-1 && page > totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance().getPersonalDynamic(sharedData.getJWT(), userId, page, 5)
                .map(recordResultVo -> {
                    List<CommonMultiBean<Dynamic>> beanList = new ArrayList<>();
                    if (recordResultVo.getCode().equals(SUCCESS)) {
                        Record record = recordResultVo.getData();
                        totalPage = record.getPages();
                        List<Dynamic> list = record.getRecords();
                        for (Dynamic d: list) {
                            beanList.add(new CommonMultiBean<>(d, d.getType()));
                        }
                    }
                    return beanList;
                })
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<List<CommonMultiBean<Dynamic>>>() {
                    @Override
                    public void onSuccess(List<CommonMultiBean<Dynamic>> data) {
                        if (data.size() > 0) {
                            mView.showData(data);
                        } else {
                            mView.refreshFailure();
                        }
                    }
                }));
    }

    @Override
    public void deleteDynamic(Integer dynamicId) {
        Context mContext = mView.getContext();
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            DialogUtils.showToast(mContext, mContext.getString(R.string.login_request));
            LoginActivity.come(mContext);
        }
        addDisposable(NetworkService.getInstance().deleteDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
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
