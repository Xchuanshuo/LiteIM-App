package com.legend.liteim.ui.search.fragment.result;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.bean.User;
import com.legend.liteim.contract.search.SearchResultContract;
import com.legend.liteim.presenter.search.UserSearchPresenter;
import com.legend.liteim.ui.search.adapter.UserSearchItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class UserSearchFragment extends BaseSearchFragment<User>
 implements SearchResultContract.UserSearchView {

    private List<User> userList = new ArrayList<>();

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new UserSearchItemAdapter(R.layout.item_search_user, userList, getContext());
    }

    @Override
    protected SearchResultContract.Presenter initPresenter() {
        return new UserSearchPresenter(this);
    }

    @Override
    protected void initWidget() {
        setEnableLoaderMore(false);
        super.initWidget();
        getEmptyTextView().setText(getString(R.string.empty_user_list));
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
    }

    @Override
    public void showSearchResult(List<User> data) {
        super.showSearchResult(data);
        userList.clear();
        userList.addAll(data);
        setLoadDataResult(userList, REFRESH_SUCCESS);
    }
}
