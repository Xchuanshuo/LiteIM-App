package com.legend.liteim.ui.chat.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.im.common.MsgType;
import com.legend.liteim.R;
import com.legend.liteim.bean.Message;
import com.legend.liteim.common.bean.CommonMultiBean;
import com.legend.liteim.common.utils.DateUtils;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.widget.picturewatcher.PreviewActivity;
import com.legend.liteim.data.Blocker;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.data.StorageData;
import com.legend.liteim.db.MessageHelper;
import com.legend.liteim.ui.chat.Face;
import com.legend.liteim.ui.chat.audio.AudioPlayHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.legend.im.common.MsgType.CONTENT_AUDIO;
import static com.legend.im.common.MsgType.CONTENT_PICTURE;
import static com.legend.im.common.MsgType.CONTENT_TEXT;

/**
 * @author Legend
 * @data by on 19-9-13.
 * @description 聊天消息适配器
 */
public class ChatMsgAdapter extends BaseMultiItemQuickAdapter<CommonMultiBean<Message>, BaseViewHolder> {

    // 保存RecyclerView的AdapterPosition到图片url位置的映射
    private ArrayMap<Integer, Integer> posMap = new ArrayMap<>();
    // 预料的图片url列表
    private List<String> imgUrlList = new ArrayList<>();
    // 记录当前已经添加到列表的图片数量
    private AtomicInteger counter = new AtomicInteger(-1);
    private String lastMsgTime = "";
    private MessageHelper messageHelper = MessageHelper.getInstance();
    private OnClickListener clickListener;
    private ArrayMap<String, Integer> statusMap = messageHelper.getFlagToPosMap();

    public ChatMsgAdapter(@Nullable List<CommonMultiBean<Message>> data) {
        super(data);
        addItemType(CONTENT_TEXT, R.layout.item_msg_text);
        addItemType(CONTENT_PICTURE, R.layout.item_msg_picture);
        addItemType(CONTENT_AUDIO, R.layout.item_msg_audio);
    }

    public void setClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommonMultiBean<Message> item) {
        Context mContext = helper.itemView.getContext();
        Message msg = item.getData();
        Long curId = Long.valueOf(GlobalData.getInstance().getCurrentUserId());
        boolean isSelf = curId.equals(msg.getFromId());
        // 公共元素的处理
        processCommon(helper, msg, isSelf, mContext);
        // 各个类型的消息单独逻辑的处理
        switch (helper.getItemViewType()) {
            case CONTENT_TEXT:
                processTextMsg(helper, msg, isSelf);
                break;
            case CONTENT_PICTURE:
                processPictureMsg(helper, msg, isSelf);
                break;
            case CONTENT_AUDIO:
                processAudioMsg(helper, msg, isSelf);
                break;
            default: break;
        }
    }

    private void processAudioMsg(BaseViewHolder helper, Message msg, boolean isSelf) {
        View.OnClickListener onClickListener = v -> data.downloadFile(new HelperHolder(isSelf, helper), msg.getMsg());
        if (isSelf) {
            helper.setText(R.id.tv_right_content, formatTime(msg.getAttach()));
            helper.getView(R.id.lay_right_bubble).setOnClickListener(onClickListener);
        } else {
            helper.setText(R.id.tv_left_content, formatTime(msg.getAttach()));
            helper.getView(R.id.lay_left_bubble).setOnClickListener(onClickListener);
        }
    }

    private AudioPlayHelper<HelperHolder> playHelper = new AudioPlayHelper<>(new AudioPlayHelper.RecordPlayListener<HelperHolder>() {
        @Override
        public void onPlayStart(HelperHolder holder) {
            holder.playStart();
        }

        @Override
        public void onPlayStop(HelperHolder holder) {
            holder.playStop();
        }

        @Override
        public void onPlayError(HelperHolder holder) {
            holder.playError();
        }
    });

    private StorageData<HelperHolder> data = new StorageData<>(StorageData.getCachePath() + "audio/cache",
            "mp3", new StorageData.DownloadCallback<HelperHolder>() {
        @Override
        public void downloadSuccess(HelperHolder holder, File file) {
            // 下载成功则直接播放文件
            playHelper.trigger(holder, file.getAbsolutePath());
        }

        @Override
        public void downloadFailure(HelperHolder holder) {
            DialogUtils.showToast("下载出错!");
        }
    });

    private String formatTime(String attach) {
        float time;
        try {
            // 毫秒转换为秒
            time = Float.parseFloat(attach) / 1000f;
        } catch (Exception e) {
            time = 0;
        }
        // 12000/1000f = 12.0000000
        // 取整一位小数点 1.234 -> 1.2 1.02 -> 1.0
        String shortTime = String.valueOf(Math.round(time * 10f) / 10f);
        // 1.0 -> 1     1.2000 -> 1.2
        shortTime = shortTime.replaceAll("[.]0+?$|0+?$", "");
        return String.format("%s″", shortTime);
    }

    // 处理图片消息
    private void processPictureMsg(BaseViewHolder helper, Message msg, boolean isSelf) {
        // 将图片的数量绑定到getAdapterPosition，防止重复添加url
        // 点击某张图片预览时根据这个值找到正确的位置
        final int position = helper.getAdapterPosition();
        if (!posMap.containsKey(position)) {
            counter.incrementAndGet();
            if (counter.get() == 0 || (!TextUtils.isEmpty(lastMsgTime) &&
                    DateUtils.compare(msg.getCreateTime(), lastMsgTime))) {
                // 如果本条消息的创建时间晚于前面的时间,说明这条消息是新到来的
                // 此时就要将图片url放到尾部
                imgUrlList.add(msg.getMsg());
                // 更新最后一条消息的时间
                lastMsgTime = msg.getCreateTime();
                // 重定位
                if (counter.get() != 0) refreshPosition();
                posMap.put(position, 0);
            } else {
                posMap.put(position, counter.get());
                imgUrlList.add(0, msg.getMsg());
            }
        }
        // 由于图片是最新的图片放到最后, 所以点击图片进行预览
        // 的时候需要计算一下反转后的位置
        View.OnClickListener onClickListener = v -> PreviewActivity.show(mContext, imgUrlList, posMap.containsKey(position) ?
                (counter.get() - posMap.get(position)) % posMap.size() :  0);

        String url = msg.getMsg();
        if (isSelf) {
            GlideUtil.loadImage(mContext, url, helper.getView(R.id.img_right_image));
            helper.getView(R.id.lay_right_bubble).setOnClickListener(onClickListener);
        } else {
            GlideUtil.loadImage(mContext, url, helper.getView(R.id.img_left_image));
            helper.getView(R.id.lay_left_bubble).setOnClickListener(onClickListener);
        }
    }

    // 处理文本消息
    private void processTextMsg(BaseViewHolder helper, Message msg, boolean isSelf) {
        Spannable spannable = new SpannableString(msg.getMsg());
        if (isSelf) {
            TextView mContent = helper.getView(R.id.tv_right_text);
            // 解析表情
            Face.decode(mContent, spannable,
                    (int) Ui.dipToPx(mContext.getResources(), mContent.getTextSize()));
            // 把内容设置到布局上
            mContent.setText(spannable);
        } else {
            TextView mContent = helper.getView(R.id.tv_left_text);
            Face.decode(mContent, spannable,
                    (int) Ui.dipToPx(mContext.getResources(), mContent.getTextSize()));
            // 把内容设置到布局上
            mContent.setText(spannable);
        }
    }

    // 公共元素的处理 如用户名头像等的显示与隐藏 以及消息发送状态的处理
    private void processCommon(BaseViewHolder helper, Message msg, boolean isSelf, Context context) {
        if (!TextUtils.isEmpty(msg.getMsgId())
                && (msg.getStatus() == Message.STATUS_FAILURE
                || msg.getStatus() == Message.STATUS_CREATED)
                && !statusMap.containsKey(msg.getMsgId())) {
            statusMap.put(msg.getMsgId(), helper.getAdapterPosition());
            Blocker.count(msg.getMsgId());
        }
        if (isSelf) {
            helper.setText(R.id.tv_right_username, msg.getUsername());
            GlideUtil.loadImage(helper.itemView.getContext(),
                    msg.getPortrait(), helper.getView(R.id.img_right_portrait));
            int status = msg.getStatus();
            // 处理消息的发送状态
            processMessageStatus(helper, context, status);
            // 只有发送失败情况下才可点击
            helper.getView(R.id.img_right_portrait).setEnabled(status == Message.STATUS_FAILURE);
            helper.getView(R.id.img_right_portrait).setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onResendClickListener(msg, helper.getAdapterPosition());
                }
            });
            helper.getView(R.id.right_layout).setVisibility(View.VISIBLE);
            helper.getView(R.id.left_layout).setVisibility(View.GONE);
        } else {
            helper.setText(R.id.tv_left_username, msg.getUsername());
            GlideUtil.loadImage(helper.itemView.getContext(),
                    msg.getPortrait(), helper.getView(R.id.img_left_portrait));
            helper.getView(R.id.left_layout).setVisibility(View.VISIBLE);
            helper.getView(R.id.right_layout).setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        if (MsgType.TO_USER == msg.getType()) {
            // 在用户聊天界面时去掉用户名称的显示
            if (isSelf) {
                params.addRule(RelativeLayout.START_OF, R.id.right_frame);
                params.setMarginStart((int) Ui.dipToPx(context.getResources(), 50.0f));
                params.setMarginEnd((int) Ui.dipToPx(context.getResources(), 3.0f));
                helper.getView(R.id.tv_right_username).setVisibility(View.GONE);
                helper.getView(R.id.lay_right_bubble).setLayoutParams(params);
            } else {
                params.addRule(RelativeLayout.END_OF, R.id.img_left_portrait);
                params.setMarginEnd((int) Ui.dipToPx(context.getResources(), 50.0f));
                params.setMarginStart((int) Ui.dipToPx(context.getResources(), 3.0f));
                helper.getView(R.id.tv_left_username).setVisibility(View.GONE);
                helper.getView(R.id.lay_left_bubble).setLayoutParams(params);
            }
        }
    }

    // 处理消息状态
    private void processMessageStatus(BaseViewHolder helper, Context context, int status) {
        Loading mLoading = helper.getView(R.id.loading);
        switch (status) {
            case Message.STATUS_CREATED:
                mLoading.setVisibility(View.VISIBLE);
                mLoading.setProgress(0);
                mLoading.setForegroundColor(UiCompat.getColor(context.getResources(), R.color.colorAccent));
                mLoading.start();
                break;
            case Message.STATUS_SUCCESS:
                mLoading.setVisibility(View.GONE);
                mLoading.stop();
                break;
            case Message.STATUS_FAILURE:
                mLoading.setVisibility(View.VISIBLE);
                mLoading.stop();
                mLoading.setProgress(1);
                mLoading.setForegroundColor(UiCompat.getColor(context.getResources(), R.color.alertImportant));
                break;
            default: break;
        }
    }

    /**
     *  当有新图片消息到达时,需要把之前的映射向前移动一位
     *  目的是始终保证最新接收到的图片是列表中的最后一位
     */
    private void refreshPosition() {
        for (int key : posMap.keySet()) {
            posMap.put(key, posMap.get(key) + 1);
        }
    }

    public interface OnClickListener {
        void onResendClickListener(Message message, int position);
    }

    private class HelperHolder {
        boolean isSelf;
        BaseViewHolder holder;

        HelperHolder(boolean isSelf, BaseViewHolder holder) {
            this.isSelf = isSelf;
            this.holder = holder;
        }

        void playStart() {
            if (isSelf) {
                holder.getView(R.id.im_right_audio_track).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.im_left_audio_track).setVisibility(View.VISIBLE);
            }
        }

        void playStop() {
            if (isSelf) {
                holder.getView(R.id.im_right_audio_track).setVisibility(View.INVISIBLE);
            } else {
                holder.getView(R.id.im_left_audio_track).setVisibility(View.INVISIBLE);
            }
        }

        void playError() {
            DialogUtils.showToast("播放出错！");
        }
    }

    public AudioPlayHelper<HelperHolder> getPlayHelper() {
        return playHelper;
    }
}
