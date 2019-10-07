package com.legend.liteim.presenter.contacts;

import android.text.TextUtils;

import com.legend.liteim.LoginActivity;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.Record;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ProgressTransformer;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.contacts.GroupCreateContract;
import com.legend.liteim.db.GroupHelper;

import java.util.Map;

import okhttp3.RequestBody;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-19.
 * @description
 */
public class GroupCreatePresenter extends BasePresenter<GroupCreateContract.View>
    implements GroupCreateContract.Presenter {

    public GroupCreatePresenter(GroupCreateContract.View view) {
        super(view);
    }

    @Override
    public void createGroup(Map<String, RequestBody> params) {
        addDisposable(NetworkService.getInstance().createGroup(globalData.getJWT(), params)
                .compose(new NetworkService.ThreadTransformer<>())
                .compose(ProgressTransformer.applyProgressBar(mView.getContext(), "正在创建..."))
                .subscribeWith(new BaseObserver<ResultVo<ChatGroup>>() {
                    @Override
                    public void onSuccess(ResultVo<ChatGroup> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            ChatGroup group = data.getData();
                            group.setJoined(true);
                            GroupHelper.getInstance().saveOrUpdate(group);
                            mView.showCreateSuccess("创建成功!");
                        } else {
                            DialogUtils.showToast("创建出错!");
                        }
                    }
                }));
    }

    public void pullFriendList() {
        String jwt = globalData.getJWT();
        if (TextUtils.isEmpty(jwt)) {
            LoginActivity.show(mView.getContext());
            DialogUtils.showToast(mView.getContext(), "请先登录再进行操作!");
            return;
        }
        addDisposable(NetworkService.getInstance().getFriendList(globalData.getJWT(), 1, 30)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<User>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<User>> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            Record<User> record = data.getData();
                            mView.showFriendList(record.getRecords());
                        } else {
                            DialogUtils.showToast(getView().getContext(), data.getMsg());
                        }
                    }
                }));
    }
}
