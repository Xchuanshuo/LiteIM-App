package com.worldtreestd.finder.presenter.release;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.contract.release.VideoTextContract;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class VideoTextPresenter extends BasePresenter<VideoTextContract.View>
    implements VideoTextContract.Presenter {

    public VideoTextPresenter(VideoTextContract.View view) {
        super(view);
    }
}