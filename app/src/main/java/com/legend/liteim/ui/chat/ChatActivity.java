package com.legend.liteim.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;

import com.legend.liteim.R;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.data.Blocker;
import com.legend.liteim.event.JumpEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.ui.chat.fragment.ChatFragment;
import com.legend.liteim.ui.chat.fragment.ChatGroupFragment;
import com.legend.liteim.ui.chat.fragment.ChatUserFragment;
import com.legend.liteim.ui.contacts.GroupInfoActivity;
import com.legend.liteim.ui.userinfo.UserInfoActivity;

import butterknife.BindView;


/**
 * @author legend
 */
public class ChatActivity extends BaseActivity {

    /** 接收者Id，可以是群，也可以是人 */
    public static final String KEY_RECEIVER = "KEY_RECEIVER";
    public static final String KEY_RECEIVER_ID = "KEY_RECEIVER_ID";
    /** 是否是群 */
    private static final String KEY_RECEIVER_IS_GROUP = "KEY_RECEIVER_IS_GROUP";

    private boolean mIsGroup;

    @BindView(R.id.tv_info)
    AppCompatTextView mInfoTv;
    private Object object;

    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    /**
     * 显示人的聊天界面
     *
     * @param context 上下文
     * @param user  人的信息
     */
    public static void show(Context context, User user) {
        if (user == null || context == null || user.getId() == null) return;
//        Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra(KEY_RECEIVER, user);
//        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        context.startActivity(getUserIntent(context, user));
    }

    public static Intent getUserIntent(Context context, User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(KEY_RECEIVER, user);
        intent.putExtra(KEY_RECEIVER_IS_GROUP, false);
        return intent;
    }

    /**
     * 发起群聊天
     *
     * @param context 上下文
     * @param chatGroup   群的Model
     */
    public static void show(Context context, ChatGroup chatGroup) {
        if (chatGroup == null || context == null || chatGroup.getId() == null) return;
//        Intent intent = new Intent(context, ChatActivity.class);
//        intent.putExtra(KEY_RECEIVER, chatGroup);
//        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        context.startActivity(getGroupIntent(context, chatGroup));
    }

    public static Intent getGroupIntent(Context context, ChatGroup chatGroup) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(KEY_RECEIVER, chatGroup);
        intent.putExtra(KEY_RECEIVER_IS_GROUP, true);
        return intent;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        this.mIsGroup = bundle.getBoolean(KEY_RECEIVER_IS_GROUP);
        this.object = bundle.getSerializable(KEY_RECEIVER);
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        open(mIsGroup);
    }

    private void open(boolean isGroup) {
        ChatFragment fragment;
        Bundle bundle = new Bundle();
        if (isGroup) {
            ChatGroup chatGroup = getGroup();
            setToolbarTitle(chatGroup.getName() + " (" +
                    chatGroup.getTotalNumber() + "/" + chatGroup.getNumberLimit() + ")");
            fragment = new ChatGroupFragment();
            bundle.putLong(KEY_RECEIVER_ID, chatGroup.getId());
            chatGroup.setJoined(true);
            mInfoTv.setOnClickListener(v -> GroupInfoActivity.show(getContext(), chatGroup));
        } else {
            User user = getUser();
            setToolbarTitle(user.getUsername());
            fragment = new ChatUserFragment();
            bundle.putLong(KEY_RECEIVER_ID, user.getId());
            mInfoTv.setOnClickListener(v -> UserInfoActivity.show(getContext(), user));

        }
        fragment.setArguments(bundle);
        loadRootFragment(R.id.container, fragment);
    }

    public User getUser() { return (User) object; }

    public ChatGroup getGroup() { return (ChatGroup) object; }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Blocker.clear();
    }

    @Override
    public void onBackPressedSupport() {
        RxBus.getDefault().post(new JumpEvent(R.id.navigation_session));
        finish();
    }
}
