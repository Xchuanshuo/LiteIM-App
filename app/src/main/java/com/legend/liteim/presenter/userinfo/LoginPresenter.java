package com.legend.liteim.presenter.userinfo;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.legend.im.bean.MsgModel;
import com.legend.im.protoctol.command.Command;
import com.legend.liteim.bean.LoginReturn;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.common.utils.Code;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.common.utils.MD5Util;
import com.legend.liteim.contract.userinfo.LoginContract;
import com.legend.liteim.data.MsgProcessor;

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

    public LoginPresenter(LoginContract.View view) {
        super(view);
    }

    @Override
    public void login(JSONObject o, User user) {
        addDisposable(NetworkService.getInstance().login(user.getOpenId(), user.getPassword())
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<LoginReturn>>() {
                    @Override
                    public void onSuccess(ResultVo<LoginReturn> data) {
                        // 登录成功后保存jwt信息
                        if (data.getCode().equals(Code.SUCCESS)) {
                            // 储存JWT
                            String jwt = data.getData().getJwt();
                            globalData.saveJWT(jwt);
                            // 拿到用户信息并进行存储
                            User u = data.getData().getUser();
                            u.saveOrUpdate();
                            globalData.saveLoginUserId(u.getId()+"");
                            // 第一次登录时手动发起一条登录消息, 进行自动登录
                            MsgModel model = MsgModel.builder()
                                    .content(jwt).fromId(u.getId())
                                    .command(Command.LOGIN_REQUEST).build();
                            MsgProcessor.getInstance().sendMsg(model);
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
        addDisposable(NetworkService.getInstance().register(body)
                .compose(new NetworkService.ThreadTransformer<>())
                .subscribeWith(new BaseObserver<ResultVo<User>>() {
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
            user.setPortrait(o.getString("figureurl_2"));
            user.setBackground(o.getString("figureurl_2"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}
