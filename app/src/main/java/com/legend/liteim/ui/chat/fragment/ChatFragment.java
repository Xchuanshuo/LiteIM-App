package com.legend.liteim.ui.chat.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.bean.Message;
import com.legend.liteim.common.adapter.TextWatcherAdapter;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.contract.chat.ChatContract;
import com.legend.liteim.ui.chat.adapter.ChatMsgAdapter;

import net.qiujuer.widget.airpanel.AirPanel;
import net.qiujuer.widget.airpanel.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;
import static com.legend.liteim.ui.chat.ChatActivity.KEY_RECEIVER_ID;

/**
 * @author Legend
 * @data by on 19-9-13.
 * @description 聊天Fragment公共类
 */
public abstract class ChatFragment extends BaseFragment<ChatContract.Presenter>
    implements ChatContract.View, PanelFragment.PanelCallback {

    @BindView(R.id.lay_panel)
    LinearLayout mPanelLayout;
    @BindView(R.id.edt_content)
    EditText mContentEdt;
    @BindView(R.id.btn_submit)
    ImageView mSubmitBtn;
    @BindView(R.id.btn_face)
    ImageView mFaceImg;
    @BindView(R.id.btn_record)
    ImageView mAudioImg;
    private List<CommonMultiBean<Message>> msgList = new ArrayList<>();
    protected Long mReceiverId;
    AirPanel.Boss mPanelBoss;
    PanelFragment mPanelFragment;
    private int page = 1;

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new ChatMsgAdapter(msgList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat_common;
    }

    @Override
    protected void initWidget() {
        setEnableLoaderMore(false);
        super.initWidget();
        getEmptyTextView().setText(getString(R.string.empty_msg_list));
        mPanelBoss = mView.findViewById(R.id.lay_content);
        mPanelBoss.setup(() -> Util.hideKeyboard(mContentEdt));
        mPanelFragment = (PanelFragment) getChildFragmentManager().findFragmentById(R.id.frag_panel);
        // 设置面板的回调接口
        mPanelFragment.setCallback(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mContentEdt.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                mSubmitBtn.setActivated(needSendMsg);
            }
        });
        mRecyclerView.setOnTouchListener((v, event) -> {
            Util.hideKeyboard(mContentEdt);
            mPanelBoss.closePanel();
            return false;
        });
        mRecyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom < oldBottom) {
                mRecyclerView.post(() -> {
                    if (mAdapter.getItemCount() > 0) {
                        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                    }
                });
            }
        });
//        mContentEdt.getViewTreeObserver().addOnGlobalLayoutListener(() ->
//                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1));
        ((ChatMsgAdapter)mAdapter).setClickListener((message, position) -> {
            if (message.getStatus() == Message.STATUS_FAILURE) {
                message.setStatus(Message.STATUS_CREATED);
                mAdapter.notifyItemChanged(position, message);
                mPresenter.onResend(message);
            }
        });
    }

    @OnClick(R.id.btn_face)
    void onFaceClick() {
        mPanelBoss.openPanel();
        mPanelFragment.showFace();
    }

    @OnClick(R.id.btn_record)
    void onAudioClick() {
        mPanelBoss.openPanel();
        mPanelFragment.showAudio();
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        if (mSubmitBtn.isActivated()) {
            // 发送文本消息
            String content = mContentEdt.getText().toString();
            mContentEdt.setText("");
            mPresenter.sendText(content);
        } else {
            mPanelBoss.openPanel();
            mPanelFragment.showMore();
        }
    }

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        this.mReceiverId = bundle.getLong(KEY_RECEIVER_ID);
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mPanelBoss.isOpen()) {
            // 关闭面板并且返回true代表自己已经处理了消费了返回
            mPanelBoss.closePanel();
            return true;
        }
        return false;
    }

    @Override
    public void refreshData() {
        mPresenter.getMsgList(page++);
    }

    @Override
    public EditText getInputEditText() {
        return mContentEdt;
    }

    @Override
    public void onSendPictures(String[] paths) {
        for (String path: paths) {
            LogUtils.logD(this, "发送图片: " + path);
        }
        mPresenter.sendPictures(paths);
    }

    @Override
    public void onRecordDone(File file, long time) {
        LogUtils.logD(this, "Audio File: " + file.getAbsolutePath());
        mPresenter.sendAudio(file.getAbsolutePath(), time);
    }

    @Override
    public void showMsgList(List<CommonMultiBean<Message>> beanList) {
        msgList.clear();
        msgList.addAll(beanList);
        setLoadDataResult(msgList, REFRESH_SUCCESS);
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void showHistoryMsgList(List<CommonMultiBean<Message>> bean) {
        if (mSwipeRefreshLayout == null) return;
        mAdapter.addData(0, bean);
        mSwipeRefreshLayout.setRefreshing(false);
        if (bean.size() == 0) {
            mSwipeRefreshLayout.setEnabled(false);
        }
    }

    @Override
    public void updateSendState(Message msg, int position) {
        CommonMultiBean<Message> bean = (CommonMultiBean<Message>) mAdapter.getItem(position);
        if (bean != null) {
            Message data = bean.getData();
            // 更新状态和内容
            data.setStatus(msg.getStatus());
//            data.setMsg(msg.getMsg());
            mAdapter.notifyItemChanged(position , bean);
//            if (data.getStatus() == Message.STATUS_FAILURE) {
//                // 发送失败原位置不动
//            } else if (data.getStatus() == Message.STATUS_SUCCESS){
//                // 发送成功添加到消息列表末尾
//                mAdapter.remove(position);
//                mAdapter.addData(bean);
//            }
        }
    }

    @Override
    public void showNewMessage(CommonMultiBean<Message> bean) {
        mAdapter.addData(bean);
        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ChatMsgAdapter adapter = (ChatMsgAdapter) getAdapter();
        if (adapter != null && adapter.getPlayHelper() != null) {
            adapter.getPlayHelper().destroy();
        }
    }
}
