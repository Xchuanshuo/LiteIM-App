package com.legend.liteim.ui.session.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.Message;
import com.legend.liteim.bean.Session;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.net.FinderApiService;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.db.GroupHelper;
import com.legend.liteim.db.SessionHelper;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.ui.chat.ChatActivity;
import com.legend.liteim.ui.chat.Face;

import net.qiujuer.genius.ui.Ui;

import java.util.List;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static com.legend.im.common.MsgType.CONTENT_AUDIO;
import static com.legend.im.common.MsgType.CONTENT_PICTURE;
import static com.legend.im.common.MsgType.CONTENT_TEXT;
import static com.legend.im.common.MsgType.TO_GROUP;
import static com.legend.im.common.MsgType.TO_USER;

/**
 * @author Legend
 * @data by on 19-9-22.
 * @description
 */
public class SessionItemAdapter extends BaseQuickAdapter<Session, BaseViewHolder> {

    private Long userId = Long.valueOf(GlobalData.getInstance().getCurrentUserId());

    public SessionItemAdapter(int layoutResId, @Nullable List<Session> data) {
        super(layoutResId, data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(BaseViewHolder helper, Session item) {
        Message message = item.getMessage();
        if (!TextUtils.isEmpty(item.getPortrait()) &&
                !item.getPortrait().startsWith("http")) {
            item.setPortrait(FinderApiService.BASE_URL + item.getPortrait());
        }
        GlideUtil.loadImage(helper.itemView.getContext(),
                item.getPortrait(), helper.getView(R.id.im_portrait));
        helper.setText(R.id.tv_name, item.getName());
        helper.setText(R.id.tv_time, item.getUpdateDate().substring(5, 16));
        if (message == null) return;
        TextView mContentTv = helper.getView(R.id.tv_content);
        switch (message.getContentType()) {
            case CONTENT_TEXT:
                // 进行表情解码
                if (message.getType() == TO_USER || message.getFromId().equals(userId)) {
                    Spannable s1 = new SpannableString(message.getMsg());
                    Face.decode(mContentTv, s1, (int) Ui.dipToPx(mContext.getResources(), mContentTv.getTextSize()));
                    mContentTv.setText(s1);
                } else if (message.getType() == TO_GROUP) {
                    Spannable s2 = new SpannableString(message.getUsername() + ": " + message.getMsg());
                    Face.decode(mContentTv, s2, (int) Ui.dipToPx(mContext.getResources(), mContentTv.getTextSize()));
                    mContentTv.setText(s2);
                }
                break;
            case CONTENT_PICTURE:
                if (message.getType() == TO_USER || message.getFromId().equals(userId)) {
                    mContentTv.setText("[图片]");
                } else if (message.getType() == TO_GROUP){
                    mContentTv.setText(message.getUsername() + ": " + "[图片]");
                }
                break;
            case CONTENT_AUDIO:
                if (message.getType() == TO_USER || message.getFromId().equals(userId)) {
                    mContentTv.setText("[语音]");
                } else if (message.getType() == TO_GROUP){
                    mContentTv.setText(message.getUsername() + ": " + "[语音]");
                }
                break;
            default: break;
        }

        // 设置点击事件
        if (message.getType() == TO_USER) {
            helper.itemView.setOnClickListener(v -> {
                User user = UserHelper.getInstance().getUserById(item.getFromId());
                ChatActivity.show(helper.itemView.getContext(), user);
            });
        } else if (message.getType() == TO_GROUP) {
            helper.itemView.setOnClickListener(v -> {
                ChatGroup group = GroupHelper.getInstance().getGroupById(item.getFromId());
                ChatActivity.show(helper.itemView.getContext(), group);
            });
        }
        Badge badge = (Badge) helper.getAssociatedObject();
        if (badge == null) {
            badge = new QBadgeView(mContext).bindTarget(helper.getView(R.id.tv_unread));
            badge.setBadgeGravity(Gravity.CENTER);
            helper.setAssociatedObject(badge);
        }
        badge.setBadgeNumber(item.getUnReadCount());
        badge.setOnDragStateChangedListener((dragState, badge1, targetView) -> {
            if (dragState == Badge.OnDragStateChangedListener.STATE_SUCCEED) {
                SessionHelper.getInstance().updateUnReadCountAndNotify(item, 0);
            }
        });
    }
}
