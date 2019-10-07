package com.legend.liteim.presenter.contacts;


import com.legend.liteim.bean.Record;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ProgressTransformer;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.contract.contacts.GroupUserAddContract;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-21.
 * @description
 */
public class GroupUserAddPresenter extends BasePresenter<GroupUserAddContract.View>
 implements GroupUserAddContract.Presenter {

    private Long groupId;

    public GroupUserAddPresenter(GroupUserAddContract.View view, Long groupId) {
        super(view);
        this.groupId = groupId;
        DialogUtils.showToast(getView().getContext(),  "群组id: " + groupId);
    }

    @Override
    public void pullUnJoinedUserList() {
        addDisposable(NetworkService.getInstance().unJoinedFriendList(globalData.getJWT(), groupId, 1, 30)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<Record<User>>>() {
                    @Override
                    public void onSuccess(ResultVo<Record<User>> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            Record<User> record = data.getData();
                            mView.showUserList(record.getRecords());
                        }
                    }
                }));
    }

    @Override
    public void submitGroupUser(String ids) {
        String encode;
        try {
            encode = URLEncoder.encode(ids, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        addDisposable(NetworkService.getInstance().joinGroupByIds(globalData.getJWT(), groupId, encode)
                .compose(new NetworkService.ThreadTransformer<>())
                .compose(ProgressTransformer.applyProgressBar(mView.getContext(), "正在拉取群成员~"))
                .subscribeWith(new BaseObserver<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            mView.showSubmitSuccess();
                        }
                        DialogUtils.showToast(getView().getContext(), data.getMsg());
                    }
                }));
    }
}
