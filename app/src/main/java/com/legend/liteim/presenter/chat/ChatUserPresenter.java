package com.legend.liteim.presenter.chat;

import com.legend.im.common.MsgType;
import com.legend.liteim.bean.Message;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.contract.chat.ChatContract;

import net.qiujuer.genius.kit.handler.Run;

/**
 * @author Legend
 * @data by on 19-9-13.
 * @description
 */
public class ChatUserPresenter extends ChatPresenter {

    public ChatUserPresenter(ChatContract.View view, Long mReceiverId) {
        super(view, mReceiverId, MsgType.TO_USER);
    }

    @Override
    public void receiveUserMessage(Message message) {
        super.receiveUserMessage(message);
        CommonMultiBean<Message> bean = new CommonMultiBean<>(message, message.getContentType());
        Run.onUiSync(() -> mView.showNewMessage(bean));
    }
}
