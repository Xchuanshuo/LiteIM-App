package com.legend.liteim.presenter.contacts;

import com.legend.liteim.bean.Record;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.contacts.GroupInfoContract;
import com.legend.liteim.db.GroupHelper;
import com.legend.liteim.db.SessionHelper;
import com.legend.liteim.event.RefreshEvent;
import com.legend.liteim.event.RxBus;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public class GroupInfoPresenter extends BasePresenter<GroupInfoContract.View>
    implements GroupInfoContract.Presenter {

    private Long groupId;

    public GroupInfoPresenter(GroupInfoContract.View view, Long groupId) {
        super(view);
        this.groupId = groupId;
        registerEvent();
    }

    private void registerEvent() {
        addDisposable(RxBus.getDefault().toObservable(RefreshEvent.class)
                .subscribe(refreshEvent -> pullGroupUserList()));
    }

    @Override
    public void joinGroup() {
        addDisposable(NetworkService.getInstance().joinGroup(globalData.getJWT(), groupId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            mView.showJoinState(true);
                        }
                        DialogUtils.showToast(getView().getContext(), data.getMsg());
                    }
                }));
    }

    @Override
    public void exitGroup() {
        addDisposable(NetworkService.getInstance().exitGroup(globalData.getJWT(), groupId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            mView.showJoinState(false);
                        }
                        DialogUtils.showToast(getView().getContext(), data.getMsg());
                    }
                }));
    }

    @Override
    public void deleteGroup() {
        addDisposable(NetworkService.getInstance().deleteGroup(globalData.getJWT(), groupId)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            SessionHelper.getInstance().deleteByGroupId(groupId);
                            GroupHelper.getInstance().deleteById(groupId);
                            mView.showDeleteSuccess();
                        }
                        DialogUtils.showToast(getView().getContext(), data.getMsg());
                    }
                }));
    }

    @Override
    public void pullGroupUserList() {
        addDisposable(NetworkService.getInstance().groupUserList(groupId, 1, 30)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<User>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<User>> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            Record<User> record = data.getData();
                            mView.showGroupUserList(record.getRecords());
                        }
                    }
                }));
    }
}
