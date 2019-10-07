package com.legend.liteim.db;

import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.NetworkService;
import com.legend.liteim.common.net.ResultVo;
import com.legend.liteim.data.Blocker;
import com.legend.liteim.model.ExtraDisposableManager;

import net.qiujuer.genius.kit.handler.Run;

import org.litepal.LitePal;

import java.util.List;

import io.reactivex.schedulers.Schedulers;

import static com.legend.liteim.common.utils.Code.SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description 群组数据库操作辅助类
 */
public class GroupHelper {

    public static GroupHelper getInstance() {
        return Holder.INSTANCE;
    }
    private ExtraDisposableManager manager = ExtraDisposableManager.getInstance();

    private static class Holder {
        private static GroupHelper INSTANCE = new GroupHelper();
    }

    public void saveOrUpdate(ChatGroup group) {
        Run.onBackground(() -> group.saveOrUpdate("groupId = ?", String.valueOf(group.getId())));
    }

    public void saveOrUpdateAll(List<ChatGroup> chatGroupList) {
        Run.onBackground(() -> {
            for (ChatGroup chatGroup : chatGroupList) {
                chatGroup.setJoined(true);
                chatGroup.saveOrUpdate("groupId = ?", String.valueOf(chatGroup.getId()));
            }
        });
    }

    public void deleteById(Long groupId) {
        ChatGroup group = getGroupById(groupId);
        if (group != null) {
            group.delete();
        }
    }

    public ChatGroup getGroupById(Long groupId) {
        final ChatGroup[] group = {LitePal.where("groupId = ?", String.valueOf(groupId)).findFirst(ChatGroup.class)};
        if (group[0] != null) {
            return group[0];
        }
        String flag = 1 + "-" + groupId;
        Blocker.put(flag);
        manager.addDisposable(NetworkService.getInstance().getGroupById(groupId)
                .observeOn(Schedulers.io())
                .subscribeWith(new BaseObserver<ResultVo<ChatGroup>>() {
                    @Override
                    public void onSuccess(ResultVo<ChatGroup> data) {
                        if (SUCCESS.equals(data.getCode())) {
                            group[0] = data.getData();
                            Blocker.count(flag);
                            group[0].setJoined(true);
                            saveOrUpdate(group[0]);
                        }
                    }
                }));
        Blocker.await(flag);
        return group[0];
    }

    public List<ChatGroup> getGroupList() {
        return LitePal.where("joined = 1").find(ChatGroup.class);
    }
}
