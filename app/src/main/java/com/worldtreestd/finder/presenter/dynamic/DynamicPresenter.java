package com.worldtreestd.finder.presenter.dynamic;

import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.bean.Record;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.contract.dynamic.DynamicContract;
import com.worldtreestd.finder.event.RefreshEvent;
import com.worldtreestd.finder.event.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.worldtreestd.finder.common.utils.Code.SUCCESS;

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
                .getDynamicList(page, 5)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<Record<Dynamic>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<Dynamic>> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            Record<Dynamic> record = data.getData();
                            totalPage = record.getPages();
                            deal(record.getRecords());
                        }
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
