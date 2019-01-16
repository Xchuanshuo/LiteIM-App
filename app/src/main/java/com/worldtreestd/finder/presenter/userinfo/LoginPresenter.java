package com.worldtreestd.finder.presenter.userinfo;

import com.google.gson.Gson;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ResultVo;
import com.worldtreestd.finder.common.utils.Code;
import com.worldtreestd.finder.common.utils.LogUtils;
import com.worldtreestd.finder.common.utils.MD5Util;
import com.worldtreestd.finder.contract.userinfo.LoginContract;
import com.worldtreestd.finder.data.SharedData;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Legend
 * @data by on 19-1-9.
 * @description
 */
public class LoginPresenter extends BasePresenter<LoginContract.View>
    implements LoginContract.Presenter {

    private SharedData sharedData = SharedData.getInstance();

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(JSONObject o, User user) {
        addDisposable(NetworkService.getInstance().login(user.getOpenId(), user.getPassword())
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<String>>() {
                    @Override
                    public void onSuccess(ResultVo<String> data) {
                        // 登录成功后保存jwt信息
                        if (data.getCode().equals(Code.SUCCESS)) {
                            sharedData.saveJWT(data.getData());
                            if (mView != null) {
                                mView.loginAfter();
                            }
                        } else if (data.getCode().equals(Code.USER_NOT_EXIST)) {
                            // 用户不存在则去进行注册
                            register(o, user.getOpenId());
                        }
                    }
                }));
    }

    @Override
    public void register(JSONObject o, String openId) {
        User user = buildUser(o, openId);
        Gson gson = new Gson();
        String userStr = gson.toJson(user);
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json;charset=utf-8"), userStr);
        addDisposable(NetworkService.getInstance().register(body)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserve<ResultVo<User>>() {
                    @Override
                    public void onSuccess(ResultVo<User> data) {
                        // 注册成功 后进行登录
                        if (data.getCode().equals(Code.SUCCESS)) {
                            login(o, user);
                        }
                    }
                }));
    }

    private User buildUser(JSONObject o, String openId) {
        User user = new User();
        user.setOpenId(openId);
        LogUtils.logD(this, openId);
        user.setPassword(MD5Util.encryptAddSalt(openId));
        try {
            if ("男".equals(o.getString("gender"))) {
                user.setSex(1);
            } else {
                user.setSex(0);
            }
            user.setUsername(o.getString("nickname"));
            user.setPortrait(o.getString("figureurl_1"));
            user.setBackground(o.getString("figureurl_2"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
