package com.legend.liteim.presenter.contacts;

import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.User;
import com.legend.liteim.common.base.mvp.presenter.BasePresenter;
import com.legend.liteim.contract.contacts.ContactsContract;
import com.legend.liteim.db.GroupHelper;
import com.legend.liteim.db.UserHelper;
import com.legend.liteim.event.GroupJoinEvent;
import com.legend.liteim.event.RefreshEvent;
import com.legend.liteim.event.RxBus;
import com.legend.liteim.event.UserAddEvent;

import net.qiujuer.genius.kit.handler.Run;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-9-7.
 * @description
 */
public class ContactsPresenter extends BasePresenter<ContactsContract.View>
    implements ContactsContract.Presenter {

    public ContactsPresenter(ContactsContract.View view) {
        super(view);
        registerRefreshEvent();
    }

    private void registerRefreshEvent() {
        addDisposable(RxBus.getDefault().toObservable(RefreshEvent.class)
                .subscribe(refreshEvent -> mView.refreshData()));
        addDisposable(RxBus.getDefault().toObservable(UserAddEvent.class)
                .subscribe(userAddEvent -> mView.refreshData()));
        addDisposable(RxBus.getDefault().toObservable(GroupJoinEvent.class)
                .subscribe(groupJoinEvent -> mView.refreshData()));
    }

    @Override
    public void friendList() {
        List<User> users = UserHelper.getInstance().getFriendList();
        Run.onUiAsync(() -> mView.showFriendList(users));
    }

    @Override
    public void groupList() {
        List<ChatGroup> groups = GroupHelper.getInstance().getGroupList();
        Run.onUiAsync(() -> mView.showGroupList(groups));
    }
}
