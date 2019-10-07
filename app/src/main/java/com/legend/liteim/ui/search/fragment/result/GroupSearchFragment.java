package com.legend.liteim.ui.search.fragment.result;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.R;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.contract.search.SearchResultContract;
import com.legend.liteim.presenter.search.GroupSearchPresenter;
import com.legend.liteim.ui.search.adapter.GroupSearchItemAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.legend.liteim.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 19-9-18.
 * @description
 */
public class GroupSearchFragment extends BaseSearchFragment<ChatGroup>
    implements SearchResultContract.GroupSearchView {

    private List<ChatGroup> chatGroupList = new ArrayList<>();

    @Override
    protected SearchResultContract.Presenter initPresenter() {
        return new GroupSearchPresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new GroupSearchItemAdapter(R.layout.item_search_user, chatGroupList, getContext());
    }

    @Override
    protected void initWidget() {
        setEnableLoaderMore(false);
        super.initWidget();
        getEmptyTextView().setText(getString(R.string.empty_group_list));
    }

    @Override
    public void showSearchResult(List<ChatGroup> chatGroups) {
        super.showSearchResult(chatGroups);
        chatGroupList.clear();
        chatGroupList.addAll(chatGroups);
        setLoadDataResult(chatGroupList, REFRESH_SUCCESS);
    }
}
