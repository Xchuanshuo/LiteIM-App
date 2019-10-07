package com.legend.liteim.ui.release.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.legend.wheel.widget.PlaceSelectorDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.legend.liteim.R;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.common.net.FormHelper;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.widget.Glide4Engine;
import com.legend.liteim.contract.release.ReleaseContract;
import com.legend.liteim.event.RefreshEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.presenter.release.ReleasePresenter;
import com.legend.liteim.ui.release.ReleaseActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayerStandard;

import static cn.jzvd.JZVideoPlayer.SCREEN_WINDOW_LIST;
import static com.legend.liteim.ui.release.fragment.PictureTextFragment.REQUEST_CODE_CHOOSE;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class VideoTextFragment extends BaseFragment<ReleaseContract.Presenter>
    implements ReleaseContract.View {

    @BindView(R.id.tv_select_address)
    AppCompatTextView mSelectAddress;
    @BindView(R.id.edt_release_text)
    AppCompatEditText mEditText;
    @BindView(R.id.video_player)
    JZVideoPlayerStandard mVideo;
    @BindView(R.id.image_placeholder)
    AppCompatImageView mChooseImage;
    /** 视频最大时长 单位s**/
    private static final Integer MAX_DURATION = 20;
    private File file = null;
    private RxPermissions rxPermissions;

    @Override
    protected ReleaseContract.Presenter initPresenter() {
        return new ReleasePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_release_video_text;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        rxPermissions = new RxPermissions(this);
        PlaceSelectorDialog selectorDialog = new PlaceSelectorDialog(getContext(), mSelectAddress);
        mSelectAddress.setOnClickListener(v -> selectorDialog.show());
        mChooseImage.setOnClickListener(v -> operationMedia());
        ((ReleaseActivity)_mActivity).getSendTv().setOnClickListener(v -> requestRelease());
    }

    @SuppressLint("CheckResult")
    private void operationMedia() {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                     .subscribe(aBoolean -> {
                         if (aBoolean) {
                             Matisse.from(this).choose(MimeType.ofVideo())
                                     .imageEngine(new Glide4Engine())
                                     .showSingleMediaType(true)
                                     .forResult(REQUEST_CODE_CHOOSE);
                         } else {
                             DialogUtils.showToast(getContext(), "请先开启相关的权限!");
                         }
                     });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_CHOOSE && resultCode==RESULT_OK) {
            String videoUrl = Matisse.obtainPathResult(data).get(0);
            MediaPlayer mediaPlayer = new MediaPlayer();
            int duration = 0;
            try {
                mediaPlayer.setDataSource(videoUrl);
                mediaPlayer.prepare();
                duration = mediaPlayer.getDuration()/1000;
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (duration > MAX_DURATION) {
                DialogUtils.showToast(_mActivity, "上传视频的最大时长为20秒!");
            } else {
                videoPlayerConfig(videoUrl, "");
                mChooseImage.setVisibility(View.GONE);
                mVideo.setVisibility(View.VISIBLE);
            }
        }
    }

    private void videoPlayerConfig(String videoUrl, String videoImageUrl) {
        mVideo.setUp(videoUrl, SCREEN_WINDOW_LIST);
        this.file = new File(videoUrl);
        Glide.with(this).
                load(videoUrl).into(mVideo.thumbImageView);
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    @Override
    public void releaseSuccess(String msg) {
        DialogUtils.showToast(_mActivity, msg);
        RxBus.getDefault().post(new RefreshEvent());
        _mActivity.finish();
    }

    @Override
    public void requestRelease() {
        String content = mEditText.getText().toString();
        String address = mSelectAddress.getText().toString();
        if (file == null) {
            DialogUtils.showToast(_mActivity, "上传的视频不能为空!");
        } else if (TextUtils.isEmpty(content)){
            DialogUtils.showToast(_mActivity, "写点什么吧!");
        } else if (getString(R.string.select_address).equals(address)) {
            DialogUtils.showToast(_mActivity, "请选择地址!");
        } else {
            FormHelper helper = new FormHelper();
            helper.addParameter("title", "test")
                    .addParameter("content", mEditText.getText().toString())
                    .addParameter("address", mSelectAddress.getText().toString())
                    .addParameter("type", "1")
                    .addParameter("files", Arrays.asList(file));
            mPresenter.addDynamic(helper.builder());
        }
    }
}
