package com.legend.liteim.common.base.mvp.fragment;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.legend.liteim.contract.base.BaseContract;

/**
 * @author Legend
 * @data by on 19-10-7.
 * @description 不需要适配器的Fragment继承该类
 */
public abstract class BaseNoAdapterFragment<T extends BaseContract.Presenter>
        extends BaseFragment<T, BaseQuickAdapter> {
}
