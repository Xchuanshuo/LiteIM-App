package com.legend.liteim.ui.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.common.net.BaseObserver;
import com.legend.liteim.common.net.FormHelper;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.common.widget.Glide4Engine;
import com.legend.liteim.contract.contacts.GroupCreateContract;
import com.legend.liteim.event.RefreshEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.presenter.contacts.GroupCreatePresenter;
import com.legend.liteim.ui.contacts.adapter.GroupUserAddItemAdapter;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;

import net.qiujuer.genius.ui.widget.EditText;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;

import static com.legend.liteim.ui.release.fragment.PictureTextFragment.REQUEST_CODE_CHOOSE;
import static com.zhihu.matisse.internal.ui.BasePreviewActivity.CROP_SUCCESS;

public class GroupCreateActivity extends BaseActivity<GroupCreateContract.Presenter>
    implements GroupCreateContract.View {

    @BindView(R.id.tv_submit)
    AppCompatTextView mSubmitTv;
    @BindView(R.id.edt_name)
    EditText mNameEdt;
    @BindView(R.id.edt_desc)
    EditText mDescEdt;
    @BindView(R.id.im_portrait)
    CircleImageView mPortrait;
    private String mPortraitPath;
    RxPermissions rxPermissions;
    private List<User> userList = new ArrayList<>();

    public static void show(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, GroupCreateActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    @Override
    protected GroupCreateContract.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new GroupUserAddItemAdapter(R.layout.item_group_user_add, userList);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        this.rxPermissions = new RxPermissions(this);
        // 拉取好友列表
        mPresenter.pullFriendList();
    }

    @OnClick(R.id.im_portrait)
    void portraitOnClick() {
        mPresenter.addDisposable(rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA)
                .subscribeWith(new BaseObserver<Boolean>() {
                    @Override
                    public void onSuccess(Boolean isAccepted) {
                        if (isAccepted) {
                            operationMedia();
                        } else {
                            DialogUtils.showToast(getContext(), getString(R.string.permissions_error));
                        }
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mPortraitPath = Matisse.obtainItemResult(data).get(0).getPath();
        } else if (resultCode == CROP_SUCCESS) {
            Item item = Matisse.obtainCurItem(data);
            mPortraitPath = item.getPath();
        }
        LogUtils.logD(this.getClass(), "selected path---------------" + mPortraitPath);
        if (!TextUtils.isEmpty(mPortraitPath)) {
            GlideUtil.loadImage(getContext(), mPortraitPath, mPortrait);
        }
    }

    private void operationMedia() {
        Matisse.from(this).choose(MimeType.ofImage())
                .countable(true).maxSelectable(1)
                .thumbnailScale(0.85f)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.legend.liteim.fileprovider","test"))
                .imageEngine(new Glide4Engine())
                .showSingleMediaType(true)
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @OnClick(R.id.tv_submit)
    void submitOnClick() {
        String name = mNameEdt.getText().toString().trim();
        String desc = mDescEdt.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)
                || TextUtils.isEmpty(mPortraitPath)) {
            DialogUtils.showToast(getContext(), getString(R.string.group_create_error));
            return;
        }

        // 构建参数后执行创建操作
        FormHelper helper = new FormHelper();
        try {
            String ids = ((GroupUserAddItemAdapter)mAdapter).getSelectedIdsStr();
            DialogUtils.showToast(getContext(), ids + "");
            helper.addParameter("name", name)
                  .addParameter("description", desc)
                  .addParameter("ids", ids)
                  .addParameter("file", Luban.with(getContext()).load(mPortraitPath).get());
            mPresenter.createGroup(helper.builder());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showCreateSuccess(String msg) {
        DialogUtils.showToast(getContext(), msg);
        // 通知主页进行刷新
        RxBus.getDefault().post(new RefreshEvent());
        finish();
    }

    @Override
    public void showFriendList(List<User> users) {
        userList.clear();
        userList.addAll(users);
        mAdapter.setNewData(userList);
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }

}
