package com.worldtreestd.finder.presenter.moreinfo;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.utils.Constant;
import com.worldtreestd.finder.common.utils.DateUtils;
import com.worldtreestd.finder.common.utils.MD5Util;
import com.worldtreestd.finder.common.utils.SharedPreferenceUtils;
import com.worldtreestd.finder.contract.moreinfo.DetailActivityContract;

import java.util.Date;
import java.util.List;

/**
 * @author Legend
 * @data by on 18-9-3.
 * @description
 */
public class DetailActivityPresenter extends BasePresenter<DetailActivityContract.View>
    implements DetailActivityContract.Presenter {

    private SharedPreferenceUtils shared;

    public DetailActivityPresenter(DetailActivityContract.View view) {
        super(view);
        getClassGradeList();
    }

    @Override
    public void getClassGradeList() {
        shared = new SharedPreferenceUtils(MyApplication.getInstance(), Constant.CLASS_GRADE);
        List<String> gradeList = (List<String>) shared.get(Constant.LIST, List.class);
        Date oldDate = (Date) shared.get(Constant.SAVE_TIME, Date.class);
        if (mView != null && gradeList != null) {
            mView.showClassGradeList(gradeList);
        }
        if (oldDate!=null && DateUtils.getHourDiff(oldDate, new Date()) < 1) {
            return;
        }
        String oldEncrypt = shared.get(Constant.LIST_MD5);
        addDisposable(NetworkService.getInstance().getClassGrade()
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<List<String>>() {
                    @Override
                    public void onSuccess(List<String> data) {
                        String value = new Gson().toJson(data);
                        String newEncrypt = MD5Util.encrypt(value);
                        if (TextUtils.isEmpty(oldEncrypt) || !oldEncrypt.equals(newEncrypt)) {
                            if (mView != null) {
                                mView.showClassGradeList(data);
                            }
                            shared.save(Constant.LIST, data);
                            shared.save(Constant.LIST_MD5, newEncrypt);
                            shared.save(Constant.SAVE_TIME, new Date());
                        }
                    }
                }));
    }
}
