package com.worldtreestd.finder.ui.moreinfo;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.legend.wheel.WheelView;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.activity.BaseActivity;
import com.worldtreestd.finder.common.bean.ItemBean;
import com.worldtreestd.finder.common.utils.Constant;
import com.worldtreestd.finder.common.utils.EnumUtil;
import com.worldtreestd.finder.common.utils.SharedPreferenceUtils;
import com.worldtreestd.finder.common.utils.TestDataUtils;
import com.worldtreestd.finder.contract.moreinfo.DetailActivityContract;
import com.worldtreestd.finder.presenter.moreinfo.DetailActivityPresenter;
import com.worldtreestd.finder.ui.moreinfo.adapter.DetailPageAdapter;
import com.worldtreestd.finder.ui.moreinfo.adapter.SpinnerAdapter;
import com.worldtreestd.finder.ui.moreinfo.adapter.WheelAdapter;
import com.worldtreestd.finder.ui.moreinfo.fragment.DetailListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.worldtreestd.finder.common.utils.Constant.ITEM_TYPE;
import static com.worldtreestd.finder.common.utils.Constant.PARAM1;

/**
 * @author Legend
 * @data by on 18-7-15.
 * @description
 */
public class MoreInfoDetailActivity extends BaseActivity<DetailActivityContract.Presenter>
    implements DetailActivityContract.View {

    private static final String TAG = MoreInfoDetailActivity.class.getName();
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.mViewPager)
    ViewPager mViewPager;
    @BindView(R.id.mSpinner)
    AppCompatSpinner mSpanner;
    @BindView(R.id.float_button)
    FloatingActionButton mFloatButton;
    DetailPageAdapter mPageAdapter;
    private SharedPreferenceUtils shared;
    private static List<String> classGradeList = new ArrayList<>();
    private volatile String selectedClassGrade;
    private ItemBean itemBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_more_info_detail;
    }

    @Override
    protected DetailActivityContract.Presenter initPresenter() {
        return new DetailActivityPresenter(this);
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        itemBean = (ItemBean) getIntent().getSerializableExtra(ITEM_TYPE);
        List<ItemBean.Type> typeList = null != itemBean.getTypes()
                ?itemBean.getTypes():new ArrayList<>();
        mPageAdapter = new DetailPageAdapter(getSupportFragmentManager(), typeList);
        mViewPager.setAdapter(mPageAdapter);
        mTabLayout.setupWithViewPager(mViewPager,true);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        Log.d(TAG, "initData: "+getIntent().getStringExtra(PARAM1));
        int item = Integer.parseInt(getIntent().getStringExtra(PARAM1));
        mViewPager.setCurrentItem(item);
        byTypeLoadPage(itemBean.getId());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((DetailListFragment)mPageAdapter.getItem(mViewPager.getCurrentItem())).refreshData();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     *  EnumUtil
     * @param type
     */
    private void byTypeLoadPage(int type) {
        switch (type) {
            case 100:
                setToolbarTitle(EnumUtil.CONFESSION_WALL.getText());
                break;
            case 101:
                setToolbarTitle(EnumUtil.SCHOOL_NEWS.getText());
                break;
            case 102:
                shared = new SharedPreferenceUtils(this, Constant.SELECTED_NUMBER);
                loadWheelViewData();
                setToolbarTitle(EnumUtil.COURSE_QUERY.getText());
                mSearchButton.setVisibility(View.GONE);
                mWriteButton.setVisibility(View.GONE);
                mSpanner.setVisibility(View.VISIBLE);
                SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.item_spinner_select,
                        TestDataUtils.getWeekNumberList());
                adapter.setDropDownViewResource(R.layout.item_spinner_drop);
                mSpanner.setAdapter(adapter);
                String position = shared.get(Constant.POSITION);
                mSpanner.setSelection(position==null?0:Integer.parseInt(position));
                mSpanner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((DetailListFragment)mPageAdapter.getItem(mViewPager.getCurrentItem())).refreshData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                WheelAdapter wheelAdapter = new WheelAdapter(this, classGradeList);
                mFloatButton.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                mFloatButton.setOnClickListener(v -> {
                    final WheelView wheelView = new WheelView(v.getContext());
                    wheelView.setViewAdapter(wheelAdapter);
                    wheelView.setCurrentItem(Integer.parseInt(selectedClassGrade));
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("请选择班级")
                            .setView(wheelView)
                            .setPositiveButton("确定", (dialog, which) -> {
                                selectedClassGrade = wheelView.getCurrentItem()+"";
                                shared.save("selected", selectedClassGrade);
                                ((DetailListFragment)mPageAdapter
                                        .getItem(mViewPager.getCurrentItem())).refreshData();
                            }).show();
                });
                break;
            case 103:
                setToolbarTitle(EnumUtil.LOST_FOUND.getText());
                break;
            case 104:
                setToolbarTitle(EnumUtil.ASSOCIATION.getText());
                break;
            case 105:
                setToolbarTitle(EnumUtil.DYNAMIC_INFO.getText());
            default: break;

        }
    }

    @Override
    protected void onDestroy() {
        if (itemBean.getId()==EnumUtil.COURSE_QUERY.getCode()) {
            shared.save(Constant.POSITION, mSpanner.getSelectedItemPosition()+"");
        }
        super.onDestroy();
    }
    
    private void loadWheelViewData() {
        if (classGradeList.size() == 0) {
            mPresenter.getClassGradeList();
        }
        selectedClassGrade = shared.get(Constant.SELECTED)==null?"0":shared.get(Constant.SELECTED);
    }

    public String getClassGrade() {
        return classGradeList.get(Integer.parseInt(selectedClassGrade));
    }

    public AppCompatSpinner getSpanner() {
        return mSpanner;
    }

    public int getTypeId() {
        return itemBean.getId();
    }

    @Override
    public void onBackPressedSupport() {
        finish();
    }


    @Override
    public void showClassGradeList(List<String> gradeList) {
        classGradeList.clear();
        classGradeList.addAll(gradeList);
    }
}
