package com.worldtreestd.finder.ui.release.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;

import com.example.legend.wheel.widget.PlaceSelectorDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.net.BaseObserve;
import com.worldtreestd.finder.common.net.NetworkService;
import com.worldtreestd.finder.common.net.ProgressTransformer;
import com.worldtreestd.finder.common.net.UploadHelper;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.utils.LogUtils;
import com.worldtreestd.finder.common.widget.Glide4Engine;
import com.worldtreestd.finder.contract.release.ReleaseContract;
import com.worldtreestd.finder.event.RefreshEvent;
import com.worldtreestd.finder.event.RxBus;
import com.worldtreestd.finder.presenter.release.ReleasePresenter;
import com.worldtreestd.finder.ui.release.ReleaseActivity;
import com.worldtreestd.finder.ui.release.adapter.GridViewAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;
import top.zibin.luban.Luban;

import static com.yalantis.ucrop.UCrop.REQUEST_CROP;
import static com.zhihu.matisse.internal.ui.BasePreviewActivity.CROP_SUCCESS;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class PictureTextFragment extends BaseFragment<ReleaseContract.Presenter>
    implements ReleaseContract.View, GridViewAdapter.AddClickListener {

    @BindView(R.id.tv_select_address)
    AppCompatTextView mSelectAddress;
    @BindView(R.id.edt_release_text)
    AppCompatEditText mEditText;
    @BindView(R.id.grid_view)
    GridView mGridView;

    private int curPosition = 0;
    GridViewAdapter mAdapter;
    RxPermissions rxPermissions;
    public static final int REQUEST_CODE_CHOOSE = 100;

    @Override
    protected ReleaseContract.Presenter initPresenter() {
        return new ReleasePresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_release_picture_text;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        PlaceSelectorDialog selectorDialog = new PlaceSelectorDialog(getContext(), mSelectAddress);
        mSelectAddress.setOnClickListener(v -> selectorDialog.show());
        rxPermissions = new RxPermissions(this);
        mAdapter = new GridViewAdapter(getContext());
        mGridView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position) -> {
            if (mAdapter.getData().size()>0) {
                // 设置源uri及目标uri
                curPosition = position;
                Matisse.from(this).choose(MimeType.ofImage())
                        .imageEngine(new Glide4Engine())
                        .forPreView(mAdapter.getData(), position);
            }
        });
        mAdapter.setAddClickListener(this);
        ((ReleaseActivity)_mActivity).getSendTv().setOnClickListener(v -> requestRelease());
    }

    private void operationMedia(int count) {
        Matisse.from(this).choose(MimeType.ofImage())
                .countable(true).maxSelectable(9)
                .thumbnailScale(0.85f)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.worldtreestd.finder.fileprovider","test"))
                .imageEngine(new Glide4Engine())
                .showSingleMediaType(true)
                .selectedCount(count)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mAdapter.addData(Matisse.obtainItemResult(data));
        } else if (resultCode == CROP_SUCCESS) {
            Item item = Matisse.obtainCurItem(data);
            if (requestCode==REQUEST_CODE_CHOOSE) {
                mAdapter.addData(Arrays.asList(item));
            } else if (requestCode == REQUEST_CROP) {
                this.curPosition = Matisse.obtainCurPosition(data);
                mAdapter.updateData(curPosition, item);
            }
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onAddClickListener(View view) {
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        operationMedia(mAdapter.getCount()-1);
                    } else {
                        DialogUtils.showToast(getContext(), "请先开启相关的权限!");
                    }
                });
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
        if (mAdapter.getData() == null || mAdapter.getData().size()==0) {
            DialogUtils.showToast(_mActivity, "发布的照片不能为空!");
        } else if (TextUtils.isEmpty(content)){
            DialogUtils.showToast(_mActivity, "写点什么吧!");
        } else if (getString(R.string.select_address).equals(address)) {
            DialogUtils.showToast(_mActivity, "请选择地址!");
        } else {
            prepare();
        }
    }

    private void prepare() {
        Observable.just(mAdapter.getPaths())
                .compose(new NetworkService.ThreadTransformer<>())
                .compose(ProgressTransformer.applyProgressBar(getContext(),"正在发布..."))
                .map(strings -> Luban.with(getContext()).filter(path -> !path.endsWith( ".gif")).load(strings).get())
                .subscribe(new BaseObserve<List<File>>() {
                    @Override
                    public void onSuccess(List<File> data) {
                        LogUtils.logD(this, mEditText.getText().toString());
                        UploadHelper helper = new UploadHelper();
                        helper.addParameter("title", "test")
                                .addParameter("content", mEditText.getText().toString())
                                .addParameter("address", mSelectAddress.getText().toString())
                                .addParameter("type", "0")
                                .addParameter("files", data);
                        mPresenter.addDynamic(helper.builder());
                    }
                });
    }
}