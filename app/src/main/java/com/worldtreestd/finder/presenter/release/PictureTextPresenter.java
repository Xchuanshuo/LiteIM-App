package com.worldtreestd.finder.presenter.release;

import com.worldtreestd.finder.common.base.mvp.presenter.BasePresenter;
import com.worldtreestd.finder.contract.release.PictureTextContract;

/**
 * @author Legend
 * @data by on 18-8-23.
 * @description
 */
public class PictureTextPresenter extends BasePresenter<PictureTextContract.View>
    implements PictureTextContract.Presenter {

    public PictureTextPresenter(PictureTextContract.View view) {
        super(view);
    }
}
