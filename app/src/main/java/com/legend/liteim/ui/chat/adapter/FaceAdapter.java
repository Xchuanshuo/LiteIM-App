package com.legend.liteim.ui.chat.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.legend.liteim.R;
import com.legend.liteim.ui.chat.Face;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-27.
 * @description
 */
public class FaceAdapter extends BaseQuickAdapter<Face.Bean, BaseViewHolder> {

    public FaceAdapter(int layoutResId, @Nullable List<Face.Bean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Face.Bean bean) {
        if (bean != null
                // drawable 资源 id
                && ((bean.preview instanceof Integer)
                // face zip 包资源路径
                || bean.preview instanceof String)) {
            ImageView mFaceImg = helper.getView(R.id.im_face);
            RequestOptions options = new RequestOptions()
                    .formatOf(DecodeFormat.PREFER_ARGB_8888)
                    .placeholder(R.drawable.default_face);
            //设置解码格式8888，保证清晰度
            Glide.with(helper.itemView.getContext())
                    .asBitmap()
                    .load(bean.preview)
                    .apply(options)
                    .into(mFaceImg);
        }
    }
}
