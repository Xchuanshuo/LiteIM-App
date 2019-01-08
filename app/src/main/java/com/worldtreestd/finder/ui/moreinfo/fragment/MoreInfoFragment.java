package com.worldtreestd.finder.ui.moreinfo.fragment;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.ItemBean;
import com.worldtreestd.finder.contract.moreinfo.MoreInfoContract;
import com.worldtreestd.finder.presenter.moreinfo.DetailActivityPresenter;
import com.worldtreestd.finder.presenter.moreinfo.MoreInfoPresenter;
import com.worldtreestd.finder.ui.moreinfo.adapter.MoreInfoContentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;
import q.rorbin.verticaltablayout.widget.TabView;

import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_SUCCESS;

/**
 * @author Legend
 * @data by on 18-5-30.
 * @description
 */
public class MoreInfoFragment extends BaseFragment<MoreInfoContract.Presenter>
    implements MoreInfoContract.View {

    @BindView(R.id.more_info_tab_layout)
    VerticalTabLayout mTabLayout;
    private boolean needScroll;
    private int index;
    private boolean isClickTab;
    private LinearLayoutManager mManager;
    private List<ItemBean> itemBeanList = new ArrayList<>();

    @Override
    protected MoreInfoContract.Presenter initPresenter() {
        return new MoreInfoPresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new MoreInfoContentAdapter(R.layout.fragment_more_info_content, itemBeanList, _mActivity);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more_info;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        new DetailActivityPresenter(null).getClassGradeList();
        mManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        leftLinkageRight();
    }

    @Override
    public void refreshData() {
        itemBeanList.clear();
        mPresenter.getMoreInfoList();
        jumpTop();
    }

    @Override
    public void showMoreInfoList(List<ItemBean> moreInfoList) {
        itemBeanList = moreInfoList;
        setLoadDataResult(itemBeanList, REFRESH_SUCCESS);
        mTabLayout.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return itemBeanList == null ? 0 : itemBeanList.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int i) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int i) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int i) {
                return new TabView.TabTitle.Builder()
                        .setContent(itemBeanList.get(i).getName())
                        .setTextColor(ContextCompat.getColor(_mActivity, R.color.red),
                                ContextCompat.getColor(_mActivity, R.color.gray))
                        .build();
            }

            @Override
            public int getBackground(int i) {
                return 0;
            }
        });
    }

    private void leftLinkageRight() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (needScroll && (newState==RecyclerView.SCROLL_STATE_IDLE)) {
                    scrollRecyclerView();
                }
                rightLinkageLeft(newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (needScroll) {
                    scrollRecyclerView();
                }
            }
        });
        mTabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                int x = (int) tab.getTabView().getX();
                int y = (int) tab.getTabView().getY();
                int scrollY = (int) mTabLayout.getTabAt(position).getY();
                Log.d(this.getClass().getName(), "visibility: "+tab.getVisibility()
                        +" x: "+tab.getTabView().getX()+ ", y: "+tab.getTabView().getY());
                Log.d(this.getClass().getName(), "TabHeight: "+scrollY);
                mTabLayout.smoothScrollTo(x, y-700);
                isClickTab = true;
                selectTag(position);
            }

            @Override
            public void onTabReselected(TabView tab, int position) {

            }
        });
    }

    private void rightLinkageLeft(int newState) {
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (isClickTab) {
                isClickTab = false;
                return;
            }
            int firstPosition = mManager.findFirstVisibleItemPosition();
            if (index != firstPosition) {
                index = firstPosition;
                setChecked(index);
            }
        }
    }

    private void selectTag(int i) {
        index = i;
        mRecyclerView.stopScroll();
        smoothScrollToPosition(i);
    }

    private void setChecked(int position) {
        if (isClickTab) {
            isClickTab = false;
        } else {
            if (mTabLayout == null) { return; }
            mTabLayout.setTabSelected(index);
        }
        index = position;
    }

    private void scrollRecyclerView() {
        needScroll = false;
        int indexDistance = index - mManager.findFirstVisibleItemPosition();
        if (indexDistance>=0 && indexDistance<mRecyclerView.getChildCount()) {
            int top = mRecyclerView.getChildAt(indexDistance).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        }
    }

    private void smoothScrollToPosition(int currentPosition) {
        int firstPosition = mManager.findFirstVisibleItemPosition();
        int lastPosition = mManager.findLastVisibleItemPosition();
        if (currentPosition <= firstPosition) {
            mRecyclerView.smoothScrollToPosition(currentPosition);
        } else if (currentPosition <= lastPosition) {
            int top = mRecyclerView.getChildAt(currentPosition - firstPosition).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            mRecyclerView.smoothScrollToPosition(currentPosition);
            needScroll = true;
        }
    }

    @Override
    public void jumpTop() {
        mRecyclerView.smoothScrollToPosition(0);
        if (mTabLayout != null) {
            mTabLayout.setTabSelected(0);
        }
    }
}
