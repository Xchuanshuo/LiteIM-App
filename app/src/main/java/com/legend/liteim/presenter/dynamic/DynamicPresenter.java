package com.legend.liteim.presenter.dynamic;

import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.Record;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.dynamic.DynamicContract;
import com.legend.liteim.event.RefreshEvent;
import com.legend.liteim.event.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 18-7-19.
 * @description
 */
public class DynamicPresenter extends BasePresenter<DynamicContract.View>
    implements DynamicContract.Presenter {

    private static int totalPage = -1;

    public DynamicPresenter(DynamicContract.View view) {
        super(view);
        registerEvent();
    }

    private void registerEvent() {
        addDisposable(RxBus.getDefault().toObservable(RefreshEvent.class)
                .subscribe(refreshEvent -> mView.refreshData()));

    }

    @Override
    public void getDynamicData(Integer page) {
        if (totalPage!=-1 && page > totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance()
                .getDynamicList(globalData.getJWT(), page, 10)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<Dynamic>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<Dynamic>> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            Record<Dynamic> record = data.getData();
                            totalPage = record.getPages();
                            deal(record.getRecords());
                        }
                    }

                    @Override
                    public void onFail(String errorMsg) {
                        super.onFail(errorMsg);
                        mView.refreshFailure();
                        DialogUtils.showToast(mView.getContext(), errorMsg);
                    }
                }));
    }

    private void deal(List<Dynamic> dynamicList) {
        List<CommonMultiBean<Dynamic>> commonMultiBeans = new ArrayList<>();
        addDisposable(Observable.fromArray(dynamicList)
                .flatMap((Function<List<Dynamic>, ObservableSource<Dynamic>>) Observable::fromIterable)
                .filter(d -> d.getStatus() == 1)
                .subscribe(d -> commonMultiBeans.add(new CommonMultiBean<Dynamic>(d, d.getType()))));
        mView.showData(commonMultiBeans);
    }
}
