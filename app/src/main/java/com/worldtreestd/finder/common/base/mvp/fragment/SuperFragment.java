package com.worldtreestd.finder.common.base.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.utils.DialogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author Legend
 * @data by on 18-5-28.
 * @description Fragment公共父类
 */
public abstract class SuperFragment extends SupportFragment {

    private Unbinder mUnbinder;
    protected boolean isInnerView;
    private long clickTime;
    protected boolean mIsFirstInitData = true;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView == null) {
            int layId = getLayoutId();
            Log.d("TAGTAGTAG", layId+"");
            // 初始化当前的根布局，但是不在创建时就添加到container
            mView = inflater.inflate(layId, container, false);
        }

        mUnbinder = ButterKnife.bind(this, mView);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mIsFirstInitData) {
            mIsFirstInitData = false;
            onFirstInit();
        }
        initEventAndData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    /**
     * 处理回退事件
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        } else {
            if (isInnerView) {
                _mActivity.finish();
                return true;
            }
            long currentTime = System.currentTimeMillis();
            final long time = 2000;
            if (currentTime - clickTime > time) {
                DialogUtils.showSnackbar(_mActivity, getString(R.string.exit_string_notice));
                clickTime = System.currentTimeMillis();
            } else {
                _mActivity.finish();
            }
        }
        return true;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
//        initEventAndData();
    }

    /**
     *  得到View布局ID
     * @return
     */
    protected abstract int getLayoutId();

    /**
     *  加载事件和数据
     */
    protected abstract void initEventAndData();

    /**
     * 首次初始化时调用
     */
    protected void onFirstInit() {

    }
}
