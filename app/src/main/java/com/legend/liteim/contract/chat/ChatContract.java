package com.legend.liteim.contract.chat;

import com.legend.liteim.bean.Message;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-12.
 * @description
 */
public interface ChatContract {

    interface Presenter extends BaseContract.Presenter {

        /**
         * 发送文本消息
         * @param content
         */
        void sendText(String content);

        /**
         * 发送图片消息
         * @param paths
         */
        void sendPictures(String[] paths);

        /**
         * 发送语音消息
         * @param path
         */
        void sendAudio(String path, long time);

        void onResend(Message message);

        void getMsgList(int offset);

    }

    interface View extends BaseContract.View<Presenter> {

        List<CommonMultiBean<Message>> getData();

        /**
         * 显示新到来的消息
         * @param bean
         */
        void showNewMessage(CommonMultiBean<Message> bean);

        /**
         * 显示消息列表
         * @param bean
         */
        void showMsgList(List<CommonMultiBean<Message>> bean);

        /**
         * 更新发送状态
         * @param msg 消息
         * @param position 布局的位置
         */
        void updateSendState(Message msg, int position);

        void showHistoryMsgList(List<CommonMultiBean<Message>> bean);
    }

    interface UserView extends View {

        void showUserMsgList(List<Message> msgList);
    }

    interface GroupView extends View {

        void showGroupMsgList(List<Message> msgList);
    }
}
