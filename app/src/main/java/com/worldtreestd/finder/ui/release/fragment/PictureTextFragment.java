package com.worldtreestd.finder.ui.release.fragment;

import android.Manifest;
import android.content.Intent;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.GridView;

import com.example.legend.wheel.widget.PlaceSelectorDialog;
import com.guoxiaoxing.phoenix.core.PhoenixOption;
import com.guoxiaoxing.phoenix.core.model.MediaEntity;
import com.guoxiaoxing.phoenix.core.model.MimeType;
import com.guoxiaoxing.phoenix.picker.Phoenix;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.contract.release.PictureTextContract;
import com.worldtreestd.finder.presenter.release.PictureTextPresenter;
import com.worldtreestd.finder.ui.release.adapter.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
    List<MediaEntity> mediaEntityList = new ArrayList<>();
    RxPermissions rxPermissions;
    private static final int REQUEST_CODE = 100;

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
        PlaceSelectorDialog selectorDialog = new PlaceSelectorDialog(getContext(), mSelectAddress);
        mSelectAddress.setOnClickListener(v -> selectorDialog.show());
        rxPermissions = new RxPermissions(this);
        mAdapter = new GridViewAdapter(getContext(), mediaEntityList);
        mGridView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, view) -> {
            if (mAdapter.getData().size()>0) {
                Phoenix.with()
                        .pickedMediaList(mAdapter.getData())
                        .start(getActivity(), PhoenixOption.TYPE_BROWSER_PICTURE, position);
            }
        });
        mAdapter.setAddClickListener(this);
    }

    private void operationMedia() {
        Phoenix.with()
                .theme(PhoenixOption.THEME_BLUE)
                .fileType(MimeType.ofImage())//显示的文件类型图片、视频、图片和视频
                .maxPickNumber(12)// 最大选择数量
                .minPickNumber(0)// 最小选择数量
                .spanCount(4)// 每行显示个数
                .enableCamera(true)// 是否开启拍照
                .enableAnimation(true)// 选择界面图片点击效果
                .enableCompress(true)// 是否开启压缩
                .pickedMediaList(mAdapter.getData())// 已选图片数据
                .videoFilterTime(30)//显示多少秒以内的视频
                .mediaFilterSize(10000)//显示多少kb以下的图片/视频，默认为0，表示不限制
                .start(this, PhoenixOption.TYPE_PICK_MEDIA, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            List<MediaEntity> entityList = Phoenix.result(data);
            mAdapter.setData(entityList);
        }
    }

    @Override
    public void onAddClickListener(View view) {
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                            operationMedia();
                    } else {
                        DialogUtils.showToast(getContext(), "请先开启相关的权限!");
                    }
                });
    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

}