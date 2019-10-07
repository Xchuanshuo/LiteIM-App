package com.legend.liteim.presenter.userinfo;

import android.app.Dialog;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.FinderApiService;
import com.legend.liteim.common.net.FormHelper;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.DataUtils;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.contract.userinfo.UserInfoModifyContract;
import com.legend.liteim.data.Blocker;
import com.legend.liteim.db.UserHelper;

import net.qiujuer.genius.kit.handler.Run;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-10-4.
 * @description
 */
public class UserInfoModifyPresenter extends BasePresenter<UserInfoModifyContract.View>
 implements UserInfoModifyContract.Presenter {

    public UserInfoModifyPresenter(UserInfoModifyContract.View view) {
        super(view);
    }

    @Override
    public void updateUserInfo(User user) {
        Dialog dialog = DialogUtils.showLoadingDialog(getView().getContext(), "正在保存已修改的信息~");
        dialog.show();
        Run.onBackground(() -> {
            user.setPortrait(getUrl(user.getPortrait()));
            user.setBackground(getUrl(user.getBackground()));
            Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {
                @Override
                public boolean shouldSkipField(FieldAttributes f) {
                    return f.getName().contains("friend");
                }

                @Override
                public boolean shouldSkipClass(Class<?> clazz) {
                    return false;
                }
            }).create();
            String userStr = gson.toJson(user);
            // 使用注解后序列化出问题
            userStr = userStr.replace(",\"baseObjId\":0","");
            LogUtils.logD(this, userStr);
            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json;charset=utf-8"), userStr);
            addDisposable(NetworkService.getInstance().updateUser(globalData.getJWT(), body)
                    .compose(new NetworkService.ThreadTransformer<>())
                    .subscribeWith(new BaseObserver<ResultVo<String>>() {
                        @Override
                        public void onSuccess(ResultVo<String> data) {
                            if (SUCCESS.equals(data.getCode())) {
                                UserHelper.getInstance().saveOrUpdate(user);
                                mView.updateSuccess();
                            }
                            dialog.dismiss();
                            DialogUtils.showToast(data.getMsg());
                        }

                        @Override
                        public void onFail(String errorMsg) {
                            super.onFail(errorMsg);
                            dialog.dismiss();
                            DialogUtils.showToast(errorMsg);
                        }
                    }));
        });
    }

    private String getUrl(String path) {
        String flag = DataUtils.getUUID();
        Blocker.put(flag);
        final String[] url = {path};
        if (!path.startsWith("http")) {
            try {
                FormHelper formHelper = new FormHelper();
                formHelper.addParameter("file", Luban.with(getView().getContext()).load(path).get());
                NetworkService.getInstance().uploadUserFile(globalData.getJWT(),  formHelper.builder())
                        .subscribe(new BaseObserver<ResultVo<String>>() {
                            @Override
                            public void onSuccess(ResultVo<String> data) {
                                if (SUCCESS.equals(data.getCode())) {
                                    url[0] = FinderApiService.BASE_URL + data.getData();
                                }
                                Blocker.count(flag);
                            }

                            @Override
                            public void onFail(String errorMsg) {
                                Blocker.count(flag);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
                Blocker.count(flag);
            }
        } else {
            Blocker.count(flag);
        }
        Blocker.await(flag);
        LogUtils.logD(this, "Uploaded path---------------: " + url[0]);
        return url[0];
    }
}
