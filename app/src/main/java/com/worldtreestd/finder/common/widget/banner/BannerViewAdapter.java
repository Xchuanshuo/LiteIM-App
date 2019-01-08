package com.worldtreestd.finder.common.widget.banner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.worldtreestd.finder.MainActivity;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.IBannerBean;

import java.util.List;

/**
 * @author Legend
 * @data by on 2018/2/6.
 * @description
 */

public class BannerViewAdapter<T extends IBannerBean> extends BannerViewBaseAdapter {

    private List<T> mBeansList;
    private Context mContext;

    public BannerViewAdapter(List<T> circleBannerBeans) {
        this.mBeansList = circleBannerBeans;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        final AppCompatImageView imageView;
        TextView title;

        if (mContext == null) {
            mContext = container.getContext();
        }
        View mView = LayoutInflater.from(mContext).inflate(R.layout.banner_item_layout,null);

        T bean = mBeansList.get(position);
        imageView = mView.findViewById(R.id.image);
        title = mView.findViewById(R.id.banner_title);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.shadow_left)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(mContext).load(bean.getImageUrl())
                .apply(requestOptions)
                .into(imageView);
        title.setText(bean.getName());
        final Intent intent = createIntent(mContext, bean);
        imageView.setOnClickListener(v -> {
            Pair<View, String> imagePair = Pair.create(imageView, "circle_image");
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation((Activity) mContext, imagePair);
            mContext.startActivity(intent, options.toBundle());
        });
        return mView;
    }

    @Override
    public int getSize() {
        return mBeansList.size();
    }

    public Intent createIntent(Context context, T bean) {
        final Intent intent = new Intent(context, MainActivity.class);

        return intent;
    }
}
