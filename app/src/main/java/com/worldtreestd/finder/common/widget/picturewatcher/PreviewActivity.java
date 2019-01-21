package com.worldtreestd.finder.common.widget.picturewatcher;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.utils.DialogUtils;
import com.worldtreestd.finder.data.StorageData;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

import static com.worldtreestd.finder.common.utils.Constant.POSITION;

/**
 * @author legend
 */
public class PreviewActivity extends SwipeBackActivity
        implements ViewPager.OnPageChangeListener, View.OnClickListener {

    public static final String URL_LIST = "url_list";
    private PreviewPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private TextView mSaveTv;
    private TextView mNumTv;
    private List<String> urlList;

    public static void come(Context context, List<String> urlList, int position) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(URL_LIST, (ArrayList<String>) urlList);
        intent.putExtra(POSITION, position);
        intent.setClass(context, PreviewActivity.class);
        context.startActivity(intent);
    }

    public static void come(Context context, List<String> urlList) {
        come(context, urlList, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        urlList = getIntent().getStringArrayListExtra(URL_LIST);
        if (urlList==null || urlList.size() == 0) {
            finish();
        }
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Integer position = getIntent().getIntExtra(POSITION, 0);
        mNumTv = findViewById(R.id.tv_num);
        mNumTv.setText(position+1+"/"+urlList.size());
        mSaveTv = findViewById(R.id.tv_save);
        mViewPager = findViewById(R.id.mViewPager);
        mPagerAdapter = new PreviewPagerAdapter(getSupportFragmentManager(), urlList);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setCurrentItem(position);
        mSaveTv.setOnClickListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mNumTv.setText(position+1+"/"+urlList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        StorageData data = StorageData.getInstance();
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        data.downloadFile(urlList.get(mViewPager.getCurrentItem()));
                    } else {
                        DialogUtils.showToast(this, "未获取相关权限!");
                    }
                }).dispose();
    }
}
