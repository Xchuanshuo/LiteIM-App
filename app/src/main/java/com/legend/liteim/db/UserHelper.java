package com.legend.liteim.db;

import android.text.TextUtils;

import com.legend.liteim.bean.User;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.data.Blocker;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.model.ExtraDisposableManager;

import net.qiujuer.genius.kit.handler.Run;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description 用户数据库操作辅助类
 */
public class UserHelper {

    private GlobalData globalData = GlobalData.getInstance();
    private User loginUser;

    public static UserHelper getInstance() {
        return Holder.INSTANCE;
    }
    private ExtraDisposableManager manager = ExtraDisposableManager.getInstance();

    private static class Holder {
        private static UserHelper INSTANCE = new UserHelper();
    }

    public User getCurrentUser() {
        // 获取用户信息
        if (loginUser == null) {
            String userId = globalData.getCurrentUserId();
            if (!TextUtils.isEmpty(userId)) {
                loginUser = LitePal.where("userid=?", userId)
                        .find(User.class).get(0);
            }
        }
        return loginUser;
    }

    public void onChanged() {
        this.loginUser = null;
    }

    public void saveOrUpdate(User user) {
        Run.onBackground(() -> user.saveOrUpdate("userId = ?", String.valueOf(user.getUserId())));
    }

    public void saveOrUpdateAll(List<User> userList) {
        Run.onBackground(() -> {
            for (User user : userList) {
                user.setFriend(true);
                user.saveOrUpdate("userId = ?", String.valueOf(user.getUserId()));
            }
        });
    }

    public User getUserById(Long id) {
        final User[] user = {LitePal.where("userId = ?", String.valueOf(id)).findFirst(User.class)};
        if (user[0] != null) {
            Blocker.count("0-"+id);
            return user[0];
        }
        String flag = "0-" + id;
        Blocker.put(flag);
        NetworkService.getInstance().getUserById(id)
                .observeOn(Schedulers.io())
                .subscribe(new BaseObserver<ResultVo<User>>() {
                    @Override
                    public void onSuccess(ResultVo<User> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            User u = data.getData();
                            user[0] = u;
                            Blocker.count(flag);
                            user[0].setFriend(true);
                            saveOrUpdate(u);
                        }
                    }
                });
        Blocker.await(flag);
        return user[0];
    }

    public List<User> getFriendList() {
        return LitePal.where("friend = 1").find(User.class);
    }
}
