package com.legend.liteim.ui.userinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.legend.liteim.MainActivity;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.common.utils.ActivityManager;
import com.legend.liteim.common.utils.DialogUtils;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.common.utils.LogUtils;
import com.legend.liteim.common.widget.CircleImageView;
import com.legend.liteim.common.widget.Glide4Engine;
import com.legend.liteim.contract.userinfo.UserInfoModifyContract;
import com.legend.liteim.data.GlobalData;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.presenter.userinfo.UserInfoModifyPresenter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhihu.matisse.internal.entity.Item;

import net.qiujuer.genius.ui.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;

import static com.legend.liteim.ui.userinfo.UserInfoActivity.LOOK_USER;
import static com.zhihu.matisse.internal.ui.BasePreviewActivity.CROP_SUCCESS;

public class UserInfoModifyActivity extends BaseActivity<UserInfoModifyContract.Presenter>
    implements UserInfoModifyContract.View {

    @BindView(R.id.img_background)
    ImageView mBackgroundImg;
    @BindView(R.id.im_portrait)
    CircleImageView mPortrait;
    @BindView(R.id.edt_name)
    EditText mNameEdt;
    @BindView(R.id.edt_signature)
    EditText mSignatureEdt;
    @BindView(R.id.tv_submit)
    AppCompatTextView mSubmitTv;
    private static final int SELECT_BACKGROUND = 10;
    private static final int SELECT_PORTRAIT = 11;
    private User curUser;
    private String mPortraitPath = "";
    private String mBackgroundPath = "";

    public static void show(Context context, User user) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOOK_USER, user);
        intent.putExtras(bundle);
        intent.setClass(context, UserInfoModifyActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_info_modify;
    }

    @Override
    protected UserInfoModifyContract.Presenter initPresenter() {
        return new UserInfoModifyPresenter(this);
    }

    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    @Override
    protected void initWindows() {
        super.initWindows();
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setSwipeBackEnable(false);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        this.curUser = (User) bundle.getSerializable(LOOK_USER);
        if (curUser != null) {
            this.mPortraitPath = curUser.getPortrait();
            this.mBackgroundPath = curUser.getBackground();
        }
        return super.initArgs(bundle);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        GlideUtil.loadImage(getContext(), mPortraitPath, mPortrait);
        GlideUtil.loadImageByBlur(getContext(), mBackgroundPath, mBackgroundImg);
        mNameEdt.setText(curUser.getUsername());
        mSignatureEdt.setText(curUser.getSignature());
    }

    @OnClick({R.id.im_portrait, R.id.img_background_add, R.id.tv_submit})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.im_portrait:
                operationMedia(SELECT_PORTRAIT);
                break;
            case R.id.img_background_add:
                operationMedia(SELECT_BACKGROUND);
                break;
            case R.id.tv_submit:
                String name = mNameEdt.getText().toString();
                String signature = mSignatureEdt.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(signature)) {
                    DialogUtils.showToast(getString(R.string.empty_user_info));
                    return;
                }
                if (!isChanged()) {
                    DialogUtils.showToast("信息更改后才需要保存");
                    return;
                }
                User user = new User();
                user.setId(curUser.getId());
                user.setUsername(name);
                user.setSignature(signature);
                user.setPortrait(mPortraitPath);
                user.setBackground(mBackgroundPath);
                mPresenter.updateUserInfo(user);
                break;
            default: break;
        }
    }

    private void operationMedia(int code) {
        Matisse.from(this).choose(MimeType.ofImage())
                .countable(true).maxSelectable(1)
                .thumbnailScale(0.85f)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.legend.liteim.fileprovider","test"))
                .imageEngine(new Glide4Engine())
                .showSingleMediaType(true)
                .forResult(code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PORTRAIT:
                if (resultCode == RESULT_OK) {
                    mPortraitPath = Matisse.obtainItemResult(data).get(0).getPath();
                } else if (resultCode == CROP_SUCCESS) {
                    Item item = Matisse.obtainCurItem(data);
                    mPortraitPath = item.getPath();
                }
                LogUtils.logD(this.getClass(), "selected portrait path---------------"
                        + mPortraitPath);
                if (!TextUtils.isEmpty(mPortraitPath)) {
                    GlideUtil.loadImage(getContext(), mPortraitPath, mPortrait);
                }
                break;
            case SELECT_BACKGROUND:
                if (resultCode == RESULT_OK) {
                    mBackgroundPath = Matisse.obtainItemResult(data).get(0).getPath();
                } else if (resultCode == CROP_SUCCESS) {
                    Item item = Matisse.obtainCurItem(data);
                    mBackgroundPath = item.getPath();
                }
                LogUtils.logD(this.getClass(), "selected background path---------------"
                        + mBackgroundPath);
                if (!TextUtils.isEmpty(mBackgroundPath)) {
                    GlideUtil.loadImageByBlur(getContext(), mBackgroundPath, mBackgroundImg);
                }
                break;
            default: break;
        }
    }


    private boolean isChanged() {
        return !curUser.getUsername().equals(mNameEdt.getText().toString())
                || !curUser.getSignature().equals(mSignatureEdt.getText().toString())
                || !curUser.getPortrait().equals(mPortraitPath)
                || !curUser.getBackground().equals(mBackgroundPath);
    }

    @Override
    public void onBackPressedSupport() {
        if (isChanged()) {
            android.support.v7.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("您确认要丢弃已经修改的信息吗");
            builder.setPositiveButton("确定", (dialog, which) -> finish());
            builder.setNegativeButton("取消", null);
            builder.show();
        } else {
            finish();
        }
    }


    @Override
    public void updateSuccess() {
        UserHelper.getInstance().onChanged();
        MainActivity.show(this);
        // 更新信息时要当作是首次进入,防止连接被关闭
        GlobalData.getInstance().setFirstIn(true);
        ActivityManager.getInstance().finishAllActivity();
    }
}
