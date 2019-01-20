package com.worldtreestd.finder.ui.mainpage.fragment;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.common.bean.BannerBean;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.bean.DetailBean;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.common.utils.TestDataUtils;
import com.worldtreestd.finder.common.widget.banner.BannerView;
import com.worldtreestd.finder.common.widget.banner.BannerViewAdapter;
import com.worldtreestd.finder.contract.mainpage.HomeContract;
import com.worldtreestd.finder.presenter.mainpage.HomePresenter;
import com.worldtreestd.finder.ui.mainpage.adapter.HomeAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.worldtreestd.finder.common.base.mvp.StatusType.LOAD_MORE_SUCCESS;
import static com.worldtreestd.finder.common.base.mvp.StatusType.REFRESH_SUCCESS;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_CENTER;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_HEAD;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_TAIL;
import static com.worldtreestd.finder.ui.mainpage.adapter.HomeAdapter.mPartMap;

/**
 * @author Legend
 * @data by on 18-5-30.
 * @description
 */
public class HomeFragment extends BaseFragment<HomeContract.Presenter>
        implements HomeContract.View {

    public static final String TAG = "HomeFragment: ";
    private List<BannerBean> bannerBeanList = new ArrayList<>();
    private BannerView mBannerView;
    private List<CommonMultiBean<DetailBean>> itemBeanList = new ArrayList<>();
    private ProgressBar mProgressBar;
    private boolean isFirst = true;

    @Override
    protected HomeContract.Presenter initPresenter() {
        return new HomePresenter(this);
    }

    @Override
    protected BaseQuickAdapter getAdapter() {
        return new HomeAdapter(itemBeanList);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 6);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mBannerView = (BannerView) LayoutInflater.from(getContext()).inflate(R.layout.banner_view_layout, null);
        bannerBeanList = TestDataUtils.getBannerData();
        BannerViewAdapter<BannerBean> bannerViewAdapter = new BannerViewAdapter<>(bannerBeanList);
        mBannerView.setAdapter(bannerViewAdapter);
        mAdapter.addHeaderView(mBannerView);
        View mHomeNavigatorView = LayoutInflater.from(getContext()).inflate(R.layout.item_home_navigator, null);
        mAdapter.addHeaderView(mHomeNavigatorView);
        mAdapter.setSpanSizeLookup((gridLayoutManager, position) -> itemBeanList.get(position).getSpanSize());
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            DialogUtils.showToast(getContext(), "OnItemClick: "+position);
        });
        mAdapter.setOnLoadMoreListener(() -> refreshData(), mRecyclerView);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            int actualPosition = position+1;
            switch (adapter.getItemViewType(actualPosition)) {
                case HOME_ITEM_HEAD:
                    AppCompatTextView mTextView = getChildView(actualPosition).findViewById(R.id.mTitle);
                    DialogUtils.showToast(getContext(), "OnItemClickChildHead: "+actualPosition+mTextView.getText());
                    break;
                case HOME_ITEM_CENTER:
                    DialogUtils.showToast(getContext(), "OnItemClickChildCenter: "+actualPosition);
                    break;
                case HOME_ITEM_TAIL:
                    mProgressBar = getChildView(actualPosition).findViewById(R.id.mProgressBar);
                    mProgressBar.setVisibility(View.VISIBLE);
                    String str = mPartMap.get(actualPosition);
                    DialogUtils.showToast(getContext(), "OnItemClickChildTail: "+actualPosition+str);
                    break;
                default: break;
            }
        });
        Log.d(TAG, mProgressBar+"");
    }

    @Override
    public void refreshData() {
        if (isFirst) {
            isFirst = false;
        } else {
            mBannerView.startScroll();
        }
        itemBeanList.addAll(TestDataUtils.getHomePageData());
        setLoadDataResult(itemBeanList, REFRESH_SUCCESS);
        if (itemBeanList.size()<100) {
            setLoadDataResult(itemBeanList, LOAD_MORE_SUCCESS);
        } else {
            mAdapter.loadMoreEnd();
        }
    }
}
