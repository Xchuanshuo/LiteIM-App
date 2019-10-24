package com.legend.liteim.ui.chat.fragment;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.legend.liteim.R;
import com.legend.liteim.common.base.mvp.MyApplication;
import com.legend.liteim.common.base.mvp.fragment.BaseNoAdapterFragment;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.ScreenUtils;
import com.legend.liteim.common.utils.StreamUtil;
import com.legend.liteim.common.widget.AudioRecordView;
import com.legend.liteim.common.widget.Glide4Engine;
import com.legend.liteim.model.ExtraDisposableManager;
import com.legend.liteim.ui.chat.Face;
import com.legend.liteim.ui.chat.adapter.FaceAdapter;
import com.legend.liteim.ui.chat.audio.AudioRecordHelper;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.ui.Ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.OnClick;

import static com.legend.liteim.ui.release.fragment.PictureTextFragment.REQUEST_CODE_CHOOSE;
import static com.zhihu.matisse.internal.ui.BasePreviewActivity.CROP_SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-26.
 * @description 面板fragment
 */
public class PanelFragment extends BaseNoAdapterFragment {

    View mFacePanel, mAudioPanel, mMorePanel;
    private PanelCallback mCallback;
    private ExtraDisposableManager manager = ExtraDisposableManager.getInstance();
    private List<String> imagePathList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_panel;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        initFacePanel();
        initAudioPanel();
        initMorePanel();
    }

    private void initFacePanel() {
        mFacePanel = mView.findViewById(R.id.lay_panel_face);
        View backspace = mFacePanel.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(v -> {
            PanelCallback callback = mCallback;
            if (callback == null) return;
            // 模拟一个键盘删除点击
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL,
                    0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            callback.getInputEditText().dispatchKeyEvent(event);
        });
        TabLayout tabLayout = mFacePanel.findViewById(R.id.tab);
        ViewPager viewPager = mFacePanel.findViewById(R.id.pager);
        tabLayout.setupWithViewPager(viewPager);

        // 每一个表情显示48dp
        final int minFaceSize = (int) Ui.dipToPx(getResources(), 64);
        final int totalSize = ScreenUtils.getScreenWidth(Objects.requireNonNull(getActivity()));
        final int spanCount = totalSize / minFaceSize;

        viewPager.setAdapter(new FacePagerAdapter(spanCount));
    }

    private void initAudioPanel() {
        mAudioPanel = mView.findViewById(R.id.lay_panel_audio);
        final AudioRecordView audioRecordView = mView.findViewById(R.id.view_audio_record);
        TextView mRecordTimeTv = mView.findViewById(R.id.tv_record_time);
        // 录音的临时缓存文件
        File tempFile = MyApplication.getAudioTmpFile(true);
        long maxRecordTime = 60 * 1000;
        final AudioRecordHelper helper = new AudioRecordHelper(tempFile, new AudioRecordHelper.RecordCallback() {
            @Override
            public void onRecordStart() {
                // 录制开始
                Run.onUiAsync(() -> {
                    mRecordTimeTv.setText(getString(R.string.audio_record_time));
                    mRecordTimeTv.setVisibility(View.VISIBLE);
                });
            }

            @Override
            public void onProgress(long time) {
                // 更新已经录制时间 毫秒
                int secondTime = (int) (time / 1000);
                Run.onUiAsync(() -> {
                    if (secondTime >= maxRecordTime/1000) {
                        DialogUtils.showToast(getContext(),"达到语音最大时长!");
                    }
                    mRecordTimeTv.setText(String.format("0:%02d" , secondTime));
                });
            }

            @Override
            public void onRecordDone(File file, long time) {
                // 时间是毫秒, 小于1秒则不发送
                if (time < 1000) return;
                // 更改为一个发送的录音文件
                File audioFile = MyApplication.getAudioTmpFile(false);
                boolean isSuccess = StreamUtil.copy(file, audioFile);
                // 通知到聊天界面
                if (isSuccess && mCallback != null) {
                    Run.onUiAsync(() -> mRecordTimeTv.setVisibility(View.INVISIBLE));
                    mCallback.onRecordDone(audioFile.getAbsoluteFile(), time);
                }
            }
        });
        helper.setMaxRecordTime(maxRecordTime);
        // 初始化
        audioRecordView.setup(new AudioRecordView.Callback() {
            @Override
            public void requestStartRecord() {
                // 请求开始
                RxPermissions rxPermissions = new RxPermissions(_mActivity);
                manager.addDisposable(rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                helper.recordAsync();
                            } else {
                                DialogUtils.showToast(getContext(), getString(R.string.permissions_error));
                            }
                        }));
            }

            @Override
            public void requestStopRecord(int type) {
                mRecordTimeTv.setVisibility(View.INVISIBLE);
                // 请求结束
                switch (type) {
                    case AudioRecordView.END_TYPE_CANCEL:
                    case AudioRecordView.END_TYPE_DELETE:
                        // 删除和取消都代表想要取消
                        helper.stop(true);
                        break;
                    case AudioRecordView.END_TYPE_NONE:
                    case AudioRecordView.END_TYPE_PLAY:
                        // 播放暂时当中就是想要发送
                        helper.stop(false);
                        break;
                }
            }
        });
    }

    private void initMorePanel() {
        mMorePanel = mView.findViewById(R.id.lay_panel_more);
    }

    public void showFace() {
        mFacePanel.setVisibility(View.VISIBLE);
        mAudioPanel.setVisibility(View.GONE);
        mMorePanel.setVisibility(View.GONE);
    }

    public void showAudio() {
        mAudioPanel.setVisibility(View.VISIBLE);
        mFacePanel.setVisibility(View.GONE);
        mMorePanel.setVisibility(View.GONE);
    }

    public void showMore() {
        mMorePanel.setVisibility(View.VISIBLE);
        mAudioPanel.setVisibility(View.GONE);
        mFacePanel.setVisibility(View.GONE);
    }

    @OnClick({R.id.tv_send_picture, R.id.tv_send_video, R.id.tv_send_file, R.id.tv_send_location})
    public void onMoreClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_picture:
                DialogUtils.showToast(getContext(), "发送图片！");
                requestPermissions();
                break;
            case R.id.tv_send_video:
                DialogUtils.showToast(getContext(), "发送视频！");
                break;
            case R.id.tv_send_file:
                DialogUtils.showToast(getContext(), "发送文件！");
                break;
            case R.id.tv_send_location:
                DialogUtils.showToast(getContext(), "发送位置信息");
                break;
            default: break;
        }
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        manager.addDisposable(rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        operationMedia();
                    } else {
                        DialogUtils.showToast(getContext(), getString(R.string.permissions_error));
                    }
                }));
    }

    private void operationMedia() {
        Matisse.from(this).choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(3)
                .thumbnailScale(0.85f)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.legend.liteim.fileprovider","test"))
                .imageEngine(new Glide4Engine())
                .showSingleMediaType(true)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePathList.clear();
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            imagePathList.addAll(itemsToStrings(Matisse.obtainItemResult(data)));
        } else if (resultCode == CROP_SUCCESS) {
            Item item = Matisse.obtainCurItem(data);
            // 选择时剪裁
            if (requestCode == REQUEST_CODE_CHOOSE) {
                imagePathList.add(item.getPath());
            }
        }
        if (imagePathList.size() > 0) {
            if (mCallback == null) return;
            mCallback.onSendPictures(imagePathList.toArray(new String[0]));
        }
    }

    private List<String> itemsToStrings(List<Item> items) {
        List<String> result = new ArrayList<>();
        for (Item item: items) {
            result.add(item.getPath());
        }
        return result;
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

    class FacePagerAdapter extends PagerAdapter {

        private int spanCount;

        FacePagerAdapter(int spanCount) {
            this.spanCount = spanCount;
        }

        @Override
        public int getCount() {
            return Face.all(getContext()).size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            // 添加的
            LayoutInflater inflater = LayoutInflater.from(getContext());
            RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.lay_face_content, container, false);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

            // 设置Adapter
            List<Face.Bean> faces = Face.all(Objects.requireNonNull(getContext())).get(position).faces;
            FaceAdapter adapter = new FaceAdapter(R.layout.item_face, faces);
            adapter.setOnItemClickListener((adapter1, view, position1) -> {
                if (mCallback == null) return;
                // 表情添加到输入框
                EditText editText = mCallback.getInputEditText();
                Face.inputFace(getContext(), editText.getText(), (Face.Bean) Objects.requireNonNull(adapter1.getItem(position1)), (int)
                        (editText.getTextSize() + Ui.dipToPx(getResources(), 2)));
            });

            recyclerView.setAdapter(adapter);

            // 添加
            container.addView(recyclerView);

            return recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 移除的
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // 拿到表情盘的描述
            return Face.all(Objects.requireNonNull(getContext())).get(position).name;
        }

    }

    public void setCallback(PanelCallback callback) {
        this.mCallback = callback;
    }

    /** 回调聊天界面的Callback */
    public interface PanelCallback {

        /** 获取输入文本框 */
        EditText getInputEditText();

        /** 返回需要发送的图片*/
        void onSendPictures(String[] paths);

        /**
         * 录音完成时回调
         * @param file 音频文件
         * @param time 录音时长
         */
        void onRecordDone(File file, long time);
    }
}
