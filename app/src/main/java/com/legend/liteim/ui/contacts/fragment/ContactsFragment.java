package com.legend.liteim.ui.contacts.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.legend.liteim.R;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.fragment.BaseFragment;
import com.legend.liteim.contract.contacts.ContactsContract;
import com.legend.liteim.presenter.contacts.ContactsPresenter;
import com.legend.liteim.ui.contacts.adapter.ContactsAdapter;
import com.legend.liteim.ui.contacts.adapter.ParentItem;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-7.
 * @description
 */
public class ContactsFragment extends BaseFragment<ContactsContract.Presenter>
        implements ContactsContract.View {

    private List<MultiItemEntity> entityList = new ArrayList<>();

    @Override
    protected ContactsContract.Presenter initPresenter() {
        return new ContactsPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new ContactsAdapter(entityList);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        setEnableLoaderMore(false);
    }

    @Override
    public void refreshData() {
        entityList.clear();
        mPresenter.friendList();
        mPresenter.groupList();
    }

    @Override
    public void showFriendList(List<User> userList) {
        ParentItem<User> parentItem = new ParentItem<>("我的好友", userList.size());
        parentItem.count = userList.size();
        parentItem.setSubItems(userList);
        entityList.add(parentItem);
        setLoadDataResult(entityList, REFRESH_SUCCESS);
        mAdapter.collapse(0);
    }

    @Override
    public void showGroupList(List<ChatGroup> chatGroupList) {
        ParentItem<ChatGroup> parentItem = new ParentItem<>("我的群组", chatGroupList.size());
        parentItem.count = chatGroupList.size();
        parentItem.setSubItems(chatGroupList);
        entityList.add(parentItem);
        setLoadDataResult(entityList, REFRESH_SUCCESS);
        mAdapter.collapse(1);
    }
}
