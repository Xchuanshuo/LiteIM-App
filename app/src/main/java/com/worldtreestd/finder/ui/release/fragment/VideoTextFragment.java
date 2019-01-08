package com.worldtreestd.finder.ui.release.fragment;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;

import com.example.legend.wheel.widget.PlaceSelectorDialog;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.base.mvp.fragment.BaseFragment;
import com.worldtreestd.finder.contract.release.VideoTextContract;
import com.worldtreestd.finder.presenter.release.VideoTextPresenter;

import butterknife.BindView;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class VideoTextFragment extends BaseFragment<VideoTextContract.Presenter>
    implements VideoTextContract.View {

    @BindView(R.id.tv_select_address)
    AppCompatTextView mSelectAddress;
    @BindView(R.id.edt_release_text)
    AppCompatEditText mEditText;

    @Override
    protected VideoTextContract.Presenter initPresenter() {
        return new VideoTextPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_release_video_text;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        PlaceSelectorDialog selectorDialog = new PlaceSelectorDialog(getContext(), mSelectAddress);
        mSelectAddress.setOnClickListener(v -> selectorDialog.show());
    }

    @Override
    public void refreshData() {

    }

    @Override
    public boolean onBackPressedSupport() {
        return false;
    }

}
