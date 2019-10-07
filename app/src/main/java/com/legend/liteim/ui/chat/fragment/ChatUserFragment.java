package com.legend.liteim.ui.chat.fragment;

import com.legend.liteim.contract.chat.ChatContract;
import com.legend.liteim.presenter.chat.ChatUserPresenter;

/**
 * @author Legend
 * @data by on 19-9-12.
 * @description
 */
public class ChatUserFragment extends ChatFragment {

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatUserPresenter(this, mReceiverId);
    }
}
