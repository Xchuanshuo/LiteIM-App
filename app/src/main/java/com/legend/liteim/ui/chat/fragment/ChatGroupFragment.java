package com.legend.liteim.ui.chat.fragment;

import com.legend.liteim.contract.chat.ChatContract;
import com.legend.liteim.presenter.chat.ChatGroupPresenter;

/**
 * @author Legend
 * @data by on 19-9-12.
 * @description
 */
public class ChatGroupFragment extends ChatFragment {

    @Override
    protected ChatContract.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }
}
