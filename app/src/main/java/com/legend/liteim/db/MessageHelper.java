package com.legend.liteim.db;

import android.util.ArrayMap;

import com.legend.liteim.bean.Message;
import com.legend.liteim.common.utils.DateUtils;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.data.GlobalData;

import net.qiujuer.genius.kit.handler.Run;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-28.
 * @description 消息数据库操作辅助类
 */
public class MessageHelper {

    private static ArrayMap<String, Integer> flagToPosMap = new ArrayMap<>();

    public static MessageHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static MessageHelper INSTANCE = new MessageHelper();
    }

    public ArrayMap<String, Integer> getFlagToPosMap() {
        return flagToPosMap;
    }

    /**
     * 查询指定session的最新一条消息
     * @param sessionId
     * @return
     */
    public Message getLastMsgBySessionId(long sessionId) {
        List<Message> messages = LitePal.where("session_id = ?", sessionId+"")
                .order("createTime desc").limit(1).find(Message.class);
        if (messages.size() == 0) return null;
        return messages.get(0);
    }

    public void saveOrUpdate(Message message) {
        Run.onBackground(() -> message.saveOrUpdate("msgId = ? and type = ?",
                message.getMsgId(), message.getType()+""));
    }

    public void saveOrUpdateAll(List<Message> messages) {
        Run.onBackground(() -> {
            for (Message message : messages) {
                message.saveOrUpdate("msgId = ? and type = ?",
                        message.getMsgId(), message.getType()+"");
            }
        });
    }


    public List<Message> getUserMessageList(long toId) {
        return getUserMessageList(toId, 1);
    }

    public List<Message> getUserMessageList(long toId, int offset) {
        String curId = GlobalData.getInstance().getCurrentUserId();
        LogUtils.logD(this, "user message list curId: " + curId + " toId: " + toId);
        int limit = 100;
        List<Message> messages = LitePal.where("type = 0 and fromId = ? and toId = ?  " +
                        "or toId = ? and fromId = ? ",
                curId, toId+"", curId, toId+"").order("createTime desc")
                .offset((offset-1) * limit)
                .limit(limit).find(Message.class);
        Collections.sort(messages, (o1, o2) -> {
            if (DateUtils.compare(o1.getCreateTime(),
                    o2.getCreateTime())) {
                return 1;
            } else {
                return -1;
            }
        });
        return messages;
    }

    public List<Message> getGroupMessageList(long groupId) {
        return getGroupMessageList(groupId, 1);
    }

    public List<Message> getGroupMessageList(long groupId, int offset) {
        int limit = 100;
        List<Message> messages = LitePal.where("type = 1 and toId = ? ",
                groupId+"").order("createTime desc")
                .offset((offset-1) * limit)
                .limit(limit).find(Message.class);
        Collections.sort(messages, (o1, o2) -> {
            if (DateUtils.compare(o1.getCreateTime(),
                    o2.getCreateTime())) {
                return 1;
            } else {
                return -1;
            }
        });
        return messages;
    }
}
