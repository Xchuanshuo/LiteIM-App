package com.legend.liteim.presenter.dynamic;

import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.bean.Comment;
import com.legend.liteim.bean.Record;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.dynamic.DynamicDetailContract;
import com.legend.liteim.event.CollectEvent;
import com.legend.liteim.event.RxBus;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public class DynamicDetailPresenter extends BasePresenter<DynamicDetailContract.View>
    implements DynamicDetailContract.Presenter {

    private int totalPage = 0;

    public DynamicDetailPresenter(DynamicDetailContract.View view) {
        super(view);
        registerEvent();
    }

    private void registerEvent() {
        addDisposable(RxBus.getDefault().toObservable(CollectEvent.class)
                .subscribe(collectEvent -> {
                    if (collectEvent.isCollected()) {
                        mView.showCollectedSuccess();
                    } else {
                        mView.showUnCollectedSuccess();
                    }
                }));
    }

    @Override
    public void collectDynamic(Long dynamicId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().collectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(true));
                        }
                    }
                }));
    }

    @Override
    public void unCollectDynamic(Long dynamicId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().unCollectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(false));
                        }
                    }
                }));

    }

    @Override
    public void commentList(Long dynamicId, Integer page) {
        if (totalPage!=0 && page>totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance().dynamicCommentList(globalData.getJWT(), dynamicId, page, 10)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<Comment>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<Comment>> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            Record<Comment> record = data.getData();
                            totalPage = record.getPages();
                            mView.showCommentData(record.getRecords());
                        }
                    }
                }));
    }

    @Override
    public void releaseComment(Long dynamicId, String content) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().addDynamicComment(jwt, dynamicId, content)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.showCommentSuccess(data.getData());
                        } else {
                            DialogUtils.showToast(mView.getContext(), "评论失败");
                        }
                    }
                }));
    }

    @Override
    public void deleteComment(Long commentId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().deleteDynamicComment(jwt, commentId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.deleteCommentSuccess(data.getData());
                        }
                    }
                }));
    }

    @Override
    public void praiseComment(Long commentId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().praiseDynamicComment(jwt, commentId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.praiseCommentSuccess(data.getData());
                        }
                    }
                }));
    }

    @Override
    public void unPraiseComment(Long commentId) {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().unPraiseDynamicComment(jwt, commentId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.unPraiseCommentSuccess();
                        }
                    }
                }));
    }
}
