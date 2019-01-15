package com.worldtreestd.finder.presenter.moreinfo;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.worldtreestd.finder.common.base.mvp.MyApplication;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.bean.CourseBean;
import com.worldtreestd.finder.common.bean.CourseContentBean;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.utils.Constant;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.utils.EnumUtil;
import com.worldtreestd.finder.common.utils.SharedPreferenceUtils;
import com.worldtreestd.finder.contract.moreinfo.DetailListContract;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author Legend
 * @data by on 18-7-15.
 * @description
 */
public class DetailListPresenter extends BasePresenter<DetailListContract.View>
        implements DetailListContract.Presenter {

    private List<CommonMultiBean> detailBeanList = new ArrayList<>();
    private SharedPreferenceUtils sharedPreference;
    private List<CourseContentBean> beanList;
    private String lastClassGrade;

    public DetailListPresenter(DetailListContract.View view) {
        super(view);
    }

    @Override
    public void requestData(int type, String...args) {
        detailBeanList.clear();
        switch (type) {
            case 100:
                break;
            case 101:
                break;
            case 102:
                sharedPreference =
                        new SharedPreferenceUtils(MyApplication.getInstance(), Constant.COURSE_QUERY);
                String courseStr = sharedPreference.get(args[0]);
                if (!TextUtils.isEmpty(courseStr)) {
                    dealCourseData(courseStr, args[0], args[1], args[2]);
                } else {
                    addDisposable(NetworkService.getInstance().getCourseData(args[0])
                            .compose(new NetworkService.NetworkTransformer<>())
                            .subscribeWith(new BaseObserve<List<CourseBean>>() {
                                @Override
                                public void onSuccess(List<CourseBean> data) {
                                    String string = data.get(0).getDatas();
                                    if (!TextUtils.isEmpty(string)) {
                                        sharedPreference.save(args[0], string);
                                        dealCourseData(string, args[0], args[1], args[2]);
                                    }
                                }
                                @Override
                                public void onFail(String errorMsg) {
                                    super.onFail(errorMsg);
                                    if (errorMsg.length() != 0) {
//                                        mView.hideLoading();
                                        DialogUtils.showToast(mView.getContext(), errorMsg);
                                    }
                                }
                            }));
                }
                break;
            default: break;
        }
    }

    @Override
    public void dealCourseData(String json, String... args) {
        if (!args[0].equals(lastClassGrade) || beanList == null) {
            Gson gson = new Gson();
            beanList = gson.fromJson(json, new TypeToken<List<CourseContentBean>>(){}.getType());
        }
        List<CourseContentBean.WeeksBean.ScheduleBean> scheduleBeanList =
                beanList.get(SharedPreferenceUtils.weekTHMap.get(args[1])).getWeeks()
                        .get(SharedPreferenceUtils.weekMap.get(args[2])).getSchedule();

        addDisposable(Observable.fromArray(scheduleBeanList)
                .flatMap((Function<List<CourseContentBean.WeeksBean.ScheduleBean>, ObservableSource<?>>) scheduleBeans -> Observable.fromIterable(scheduleBeans))
                .filter(o -> ((CourseContentBean.WeeksBean.ScheduleBean)o).getDetail().getCourse() != null)
                .subscribe(o -> detailBeanList.add(new CommonMultiBean(o, EnumUtil.COURSE_QUERY.getCode()))));
        mView.showData(detailBeanList);
        lastClassGrade = args[0];
    }
}
