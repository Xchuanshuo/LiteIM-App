package com.worldtreestd.finder.ui.release;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.WindowManager;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.ui.release.fragment.PictureTextFragment;
import com.worldtreestd.finder.ui.release.fragment.VideoTextFragment;

import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_PICTURE;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_VIDEO;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description 发布图文或者视频
 */
public class ReleaseActivity extends BaseActivity {

    private final PictureTextFragment mPictureTextFragment = new PictureTextFragment();
    private final VideoTextFragment mVideoTextFragment = new VideoTextFragment();

    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_release;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        loadMultipleRootFragment(R.id.container, 0,
                mPictureTextFragment, mVideoTextFragment);
        String type = getIntent().getStringExtra("TYPE");
        if (!TextUtils.isEmpty(type)) {
            open(Integer.parseInt(type));
        }
    }

    private void open(int id) {
        switch (id) {
            case DYNAMIC_ITEM_WORD_PICTURE:
                setToolbarTitle("发布图文...");
                showHideFragment(mPictureTextFragment);
                break;
            case DYNAMIC_ITEM_WORD_VIDEO:
                setToolbarTitle("发布视频...");
                showHideFragment(mVideoTextFragment);
                break;
            default: break;
        }
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
