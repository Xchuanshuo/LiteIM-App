package com.legend.liteim.ui.contacts.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.legend.liteim.R;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.ui.chat.ChatActivity;
import com.legend.liteim.ui.userinfo.UserInfoActivity;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-10.
 * @description
 */
public class ContactsAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {

    private static final String TAG = ContactsAdapter.class.getSimpleName();

    public static final int TYPE_PARENT = 0;
    public static final int TYPE_USER = 2;
    public static final int TYPE_GROUP = 3;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public ContactsAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_PARENT, R.layout.item_contacts_parent);
        addItemType(TYPE_USER, R.layout.item_contacts_entry);
        addItemType(TYPE_GROUP, R.layout.item_contacts_entry);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case TYPE_PARENT:
                final ParentItem parentItem = (ParentItem) item;
                helper.setText(R.id.tv_title, parentItem.title)
                      .setText(R.id.tv_count, "数量: " + parentItem.count);
                helper.itemView.setOnClickListener(v -> {
                    int position = helper.getAdapterPosition();
                    Log.d(TAG, "Parent 0 item position: " + position);
                    if (parentItem.isExpanded()) {
                        helper.setImageResource(R.id.img_arrow, R.drawable.ic_expand_more_black_24dp);
                        collapse(position);
                    } else {
                        helper.setImageResource(R.id.img_arrow, R.drawable.ic_chevron_right_black_24dp);
                        expand(position);
                    }
                });
                break;
            case TYPE_USER:
                final User user = (User) item;
                String signature = user.getSignature();
                if (TextUtils.isEmpty(signature)) {
                    signature = "一切都是空空如也~";
                }
                helper.setText(R.id.tv_username, user.getUsername())
                      .setText(R.id.tv_signature, signature);
                GlideUtil.loadImage(helper.itemView.getContext(),
                        user.getPortrait(), helper.getView(R.id.portrait));
                LinearLayout mLayout = helper.getView(R.id.layout_main);
                mLayout.setOnClickListener(v -> {
                    DialogUtils.showToast(helper.itemView.getContext(), "进入好友信息页面");
                    UserInfoActivity.show(mContext, user);
                });
                break;
            case TYPE_GROUP:
                ChatGroup chatGroup = (ChatGroup) item;
                helper.setText(R.id.tv_username, chatGroup.getName());
                helper.setText(R.id.tv_signature, chatGroup.getDescription());
                GlideUtil.loadImage(helper.itemView.getContext(),
                        chatGroup.getPortrait(), helper.getView(R.id.portrait));
                LinearLayout layout = helper.getView(R.id.layout_main);
                layout.setOnClickListener(v -> {
                    DialogUtils.showToast(helper.itemView.getContext(), "进入群组聊天页面");
                    ChatActivity.show(mContext, chatGroup);
                });
                break;
            default: break;
        }

    }
}
