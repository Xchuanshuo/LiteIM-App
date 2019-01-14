package com.worldtreestd.finder.ui.release;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.WindowManager;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.ui.release.fragment.PictureTextFragment;
import com.worldtreestd.finder.ui.release.fragment.VideoTextFragment;

import butterknife.BindView;

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
    @BindView(R.id.tv_send)
    AppCompatTextView mSendTv;

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
        String type = getIntent().getStringExtra("TYPE");
        if (!TextUtils.isEmpty(type)) {
            open(Integer.parseInt(type));
        }
    }

    private void open(int id) {
        switch (id) {
            case DYNAMIC_ITEM_WORD_PICTURE:
                setToolbarTitle("发布图文...");
                loadRootFragment(R.id.container, mPictureTextFragment);
                break;
            case DYNAMIC_ITEM_WORD_VIDEO:
                setToolbarTitle("发布视频...");
                loadRootFragment(R.id.container, mVideoTextFragment);
                break;
            default: break;
        }
    }

    public AppCompatTextView getSendTv() {
        return mSendTv;
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }
}
