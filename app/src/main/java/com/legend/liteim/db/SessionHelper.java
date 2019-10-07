package com.legend.liteim.db;

import android.content.ContentValues;

import com.legend.im.common.MsgType;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.Message;
import com.legend.liteim.bean.Session;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.utils.DateUtils;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.event.MsgEvent;
import com.legend.liteim.event.RefreshSessionEvent;
import com.legend.liteim.event.RxBus;

import net.qiujuer.genius.kit.handler.Run;

import org.litepal.LitePal;

import java.util.List;

import static com.legend.im.common.MsgType.TO_GROUP;

/**
 * @author Legend
 * @data by on 19-9-23.
 * @description session数据库操作辅助类
 */
public class SessionHelper {

    private UserHelper userHelper = new UserHelper();
    private GroupHelper groupHelper = new GroupHelper();

    public static SessionHelper getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        private static SessionHelper INSTANCE = new SessionHelper();
    }

    public List<Session> getSessionList() {
        return LitePal.order("updateDate desc").find(Session.class, true);
    }

    public boolean isExistSession(Long fromId, Integer type) {
        return LitePal.where("fromId = ? and msgType = ?",
                String.valueOf(fromId), String.valueOf(type)).findFirst(Session.class) != null;
    }

    public Session getSession(Long fromId, Integer type) {
        Session session = LitePal.where("fromId = ? and msgType = ?",
                String.valueOf(fromId), String.valueOf(type)).findFirst(Session.class);
        if (session == null) {
            session = new Session();
            session.setFromId(fromId);
            session.setUnReadCount(0);
            session.setMsgType(type);
            if (type.equals(MsgType.TO_USER)) {
                User user = userHelper.getUserById(fromId);
                if (user != null) {
                    session.setName(user.getUsername());
                    session.setPortrait(user.getPortrait());
                }
            } else if (type.equals(TO_GROUP)) {
                ChatGroup group = groupHelper.getGroupById(fromId);
                if (group != null) {
                    session.setName(group.getName());
                    session.setPortrait(group.getPortrait());
                }
            }
        }
        return session;
    }

    public void saveOrUpdateAndNotify(Session session, MsgEvent event) {
        Message message = event.getMessage();
        message.setCreateTime(DateUtils.getDate());

        MessageHelper.getInstance().saveOrUpdate(message);
        session.setMessage(message);
        session.setUpdateDate(DateUtils.getDate());
        // 更新未读数量
        session.setUnReadCount(session.getUnReadCount() + event.getSize());
        session.saveOrUpdate("fromId = ? and  msgType = ?", session.getFromId()+""
                , String.valueOf(session.getMsgType()));
        LogUtils.logD(this, "Session ------------updateDate: " + session.getFromId());
        RxBus.getDefault().post(new RefreshSessionEvent());
    }

    public void updateUnReadCountAndNotify(Session session, int size) {
        ContentValues values = new ContentValues();
        values.put("unReadCount", String.valueOf(size));
        LitePal.update(Session.class, values, session.getId());
        RxBus.getDefault().post(new RefreshSessionEvent());
    }

    public void updateAllUnReadCountAndNotify() {
       Run.onBackground(() -> {
           List<Session> sessions = LitePal.findAll(Session.class);
           for (Session s : sessions) {
               ContentValues values = new ContentValues();
               values.put("unReadCount", String.valueOf(0));
               LitePal.update(Session.class, values, s.getId());
           }
           RxBus.getDefault().post(new RefreshSessionEvent());
       });
    }

    public int getTotalUnReadCount() {
        return LitePal.sum(Session.class, "unReadCount", int.class);
    }

    public void deleteByGroupId(Long groupId) {
        Session session = getSession(groupId, TO_GROUP);
        if (session != null) {
            session.delete();
        }
    }
}
