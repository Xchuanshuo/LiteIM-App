package com.worldtreestd.finder.presenter.dynamic;

import android.text.TextUtils;

import com.worldtreestd.finder.LoginActivity;
import com.worldtreestd.finder.bean.Comment;
import com.worldtreestd.finder.bean.Record;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.contract.dynamic.DynamicDetailContract;
import com.worldtreestd.finder.event.CollectEvent;
import com.worldtreestd.finder.event.RxBus;

import static com.worldtreestd.finder.common.utils.Code.SUCCESS;

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
        addDisposable(RxBus.getDefault().toFlowable(CollectEvent.class)
                .subscribe(collectEvent -> {
                    if (collectEvent.isCollected()) {
                        mView.showCollectedSuccess();
                    } else {
                        mView.showUnCollectedSuccess();
                    }
                }));
    }

    @Override
    public void collectDynamic(Integer dynamicId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().collectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(true));
                        }
                    }
                }));
    }

    @Override
    public void unCollectDynamic(Integer dynamicId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().unCollectDynamic(jwt, dynamicId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            RxBus.getDefault().post(new CollectEvent(false));
                        }
                    }
                }));

    }

    @Override
    public void commentList(Integer dynamicId, Integer page) {
        if (totalPage!=0 && page>totalPage) {
            mView.showNoMoreData();
            return;
        }
        addDisposable(NetworkService.getInstance().dynamicCommentList(sharedData.getJWT(), dynamicId, page, 10)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<Record<Comment>>>() {
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
    public void releaseComment(Integer dynamicId, String content) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().addDynamicComment(jwt, dynamicId, content)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
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
    public void deleteComment(Integer commentId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().deleteDynamicComment(jwt, commentId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.deleteCommentSuccess(data.getData());
                        }
                    }
                }));
    }

    @Override
    public void praiseComment(Integer commentId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().praiseDynamicComment(jwt, commentId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.praiseCommentSuccess(data.getData());
                        }
                    }
                }));
    }

    @Override
    public void unPraiseComment(Integer commentId) {
        String jwt = sharedData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.come(mView.getContext());
            return;
        }
        addDisposable(NetworkService.getInstance().unPraiseDynamicComment(jwt, commentId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (data.getCode().equals(SUCCESS)) {
                            mView.unPraiseCommentSuccess();
                        }
                    }
                }));
    }
}
