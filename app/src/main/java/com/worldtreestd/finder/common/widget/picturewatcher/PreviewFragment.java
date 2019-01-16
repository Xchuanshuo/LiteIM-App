package com.worldtreestd.finder.common.widget.picturewatcher;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.worldtreestd.finder.R;
import com.zhihu.matisse.internal.utils.PhotoMetadataUtils;

/**
 * @author Legend
 * @data by on 19-1-16.
 * @description
 */
public class PreviewFragment extends Fragment {

    private static final String ARGS_ITEM = "args_item";

    public static PreviewFragment newInstance(String url) {
        PreviewFragment fragment = new PreviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_ITEM, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final String url = getArguments().getString(ARGS_ITEM);
        if (url == null) {
            return;
        }
        PhotoView mPhotoView = view.findViewById(R.id.photo_view);
        mPhotoView.enable();
        Point point = PhotoMetadataUtils.getBitmapSize(url, getActivity());
        if (url.endsWith(".gif")) {
            Glide.with(getContext()).asGif().load(url)
                    .apply(new RequestOptions()
                            .override(point.x, point.y)
                            .priority(Priority.HIGH)
                            .fitCenter())
                    .into(mPhotoView);
        } else {
            Glide.with(getContext()).load(url)
                    .apply(new RequestOptions()
                            .override(point.x, point.y)
                            .priority(Priority.HIGH)
                            .fitCenter())
                    .into(mPhotoView);
        }
    }
}
