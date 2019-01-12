package com.worldtreestd.finder.ui.release.fragment;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.GridView;

import com.example.legend.wheel.widget.PlaceSelectorDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.utils.ImageDisposeUtils;
import com.worldtreestd.finder.common.widget.Glide4Engine;
import com.worldtreestd.finder.contract.release.PictureTextContract;
import com.worldtreestd.finder.presenter.release.PictureTextPresenter;
import com.worldtreestd.finder.ui.release.adapter.GridViewAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import butterknife.BindView;
import byc.imagewatcher.ImageWatcher;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class PictureTextFragment extends BaseFragment<PictureTextContract.Presenter>
    implements PictureTextContract.View, GridViewAdapter.AddClickListener {

    @BindView(R.id.tv_select_address)
    AppCompatTextView mSelectAddress;
    @BindView(R.id.edt_release_text)
    AppCompatEditText mEditText;
    @BindView(R.id.grid_view)
    GridView mGridView;
    GridViewAdapter mAdapter;
    ImageWatcher mImageWatcher;
    RxPermissions rxPermissions;
    private static final int REQUEST_CODE_CHOOSE = 100;

    @Override
    protected PictureTextContract.Presenter initPresenter() {
        return new PictureTextPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_release_picture_text;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mImageWatcher = ImageDisposeUtils.getWatcher(_mActivity);
        PlaceSelectorDialog selectorDialog = new PlaceSelectorDialog(getContext(), mSelectAddress);
        mSelectAddress.setOnClickListener(v -> selectorDialog.show());
        rxPermissions = new RxPermissions(this);
        mAdapter = new GridViewAdapter(getContext());
        mGridView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, view) -> {
            if (mAdapter.getData().size()>0) {
                Matisse.from(_mActivity).choose(MimeType.ofImage())
                        .imageEngine(new Glide4Engine())
                        .forPreView(mAdapter.getData(), position);
            }
        });
        mAdapter.setAddClickListener(this);
    }

    private void operationMedia(int count) {
        Matisse.from(this).choose(MimeType.ofImage())
                .countable(true).maxSelectable(9)
                .thumbnailScale(0.85f)
                .imageEngine(new Glide4Engine())
                .selectedCount(count)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mAdapter.addData(Matisse.obtainItemResult(data));
        }
    }

    @Override
    public void onAddClickListener(View view) {
        rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
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
        if (mImageWatcher.handleBackPressed()) {
            return true;
        }
        return false;
    }

}