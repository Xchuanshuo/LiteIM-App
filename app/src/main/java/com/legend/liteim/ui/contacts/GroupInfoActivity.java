package com.legend.liteim.ui.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.MainActivity;
import com.legend.liteim.R;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.activity.BaseActivity;
import com.legend.liteim.common.utils.GlideUtil;
import com.legend.liteim.contract.contacts.GroupInfoContract;
import com.legend.liteim.db.GroupHelper;
import com.legend.liteim.event.GroupJoinEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.presenter.contacts.GroupInfoPresenter;
import com.legend.liteim.ui.contacts.adapter.GroupUserItemAdapter;
import com.legend.liteim.ui.contacts.fragment.GroupUserAddFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.legend.liteim.ui.contacts.fragment.GroupUserAddFragment.KEY_GROUP_ID;

public class GroupInfoActivity extends BaseActivity<GroupInfoContract.Presenter>
    implements GroupInfoContract.View {

    public static final String KEY_GROUP = "key_group";
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.img_group_portrait)
    ImageView mPortraitImg;
    @BindView(R.id.tv_group_desc)
    TextView mDescTv;
    @BindView(R.id.btn_action)
    FloatingActionButton mActionBtn;
    private boolean isJoined = false;
    private List<User> userList = new ArrayList<>();
    private ChatGroup chatGroup;

    public static void show(Context context, ChatGroup chatGroup) {
        Intent intent = new Intent();
        intent.setClass(context, GroupInfoActivity.class);
        intent.putExtra(KEY_GROUP, chatGroup);
        context.startActivity(intent);
    }

    @Override
    public boolean showHomeAsUp() {
        return true;
    }

    @Override
    protected GroupInfoContract.Presenter initPresenter() {
        return new GroupInfoPresenter(this, chatGroup.getId());
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new GroupUserItemAdapter(R.layout.item_group_user_add, userList);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_group_info;
    }

    @Override
    protected void initWindows() {
        super.initWindows();
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        this.chatGroup = (ChatGroup) bundle.getSerializable(KEY_GROUP);
        return chatGroup != null;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        GlideUtil.loadImage(getContext(), chatGroup.getPortrait(), mPortraitImg);
        mCollapsingToolbar.setTitle(chatGroup.getName()+"");
        mDescTv.setText(chatGroup.getDescription());
        if (isGroupOwner()) {
            mActionBtn.setImageResource(R.drawable.ic_delete);
        } else {
            showJoinState(chatGroup.isJoined());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isGroupOwner()) {
            getMenuInflater().inflate(R.menu.group_menu,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.user_add:
                GroupUserAddFragment addFragment = new GroupUserAddFragment();
                Bundle bundle = new Bundle();
                bundle.putLong(KEY_GROUP_ID, chatGroup.getId());
                addFragment.setArguments(bundle);
                addFragment.show(getSupportFragmentManager(), getLocalClassName());
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        // 拉取群组用户列表
        mPresenter.pullGroupUserList();
    }

    @Override
    public void showGroupUserList(List<User> users) {
        userList.clear();
        userList.addAll(users);
        mAdapter.setNewData(userList);
    }

    @Override
    public void showJoinState(boolean state) {
        this.isJoined = state;
        chatGroup.setJoined(isJoined);
        mActionBtn.setImageResource(isJoined ? R.drawable.ic_done : R.drawable.ic_add);
        RxBus.getDefault().post(new GroupJoinEvent(isJoined));
        GroupHelper.getInstance().saveOrUpdate(chatGroup);
    }

    @Override
    public void showDeleteSuccess() {
        finish();
        MainActivity.show(this, 1);
    }

    @OnClick(R.id.btn_action)
    void actionOnClick() {
        if (isGroupOwner()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("您确定要解散当前群组吗?");
            builder.setMessage("此操作将会清除掉所有与该群相关的信息");
            builder.setPositiveButton("确定", (dialog, which) -> mPresenter.deleteGroup());
            builder.setNegativeButton("取消", null);
            builder.show();
        } else {
            if (isJoined) {
                // 退出群组
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("您确定要退出群组吗?");
                builder.setPositiveButton("确定", (dialog, which) -> mPresenter.exitGroup());
                builder.setNegativeButton("取消", null);
                builder.show();
            } else {
                // 加入群组
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("您确定要加入群组吗?");
                builder.setPositiveButton("确定", (dialog, which) -> mPresenter.joinGroup());
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        }
    }

    private boolean isGroupOwner() {
        return mPresenter.getCurUser() != null &&
               mPresenter.getCurUser().getId() == chatGroup.getOwnerId();
    }
}
