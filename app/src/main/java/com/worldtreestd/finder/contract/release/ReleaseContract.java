package com.worldtreestd.finder.contract.release;

import com.worldtreestd.finder.contract.base.BaseContract;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * @author Legend
 * @data by on 19-1-14.
 * @description
 */
public interface ReleaseContract {

    interface View extends BaseContract.View<Presenter> {

        /**
         * 上传成功后进行的操作
         * @param msg
         */
        void releaseSuccess(String msg);

        /**
         * 进行发布
         */
        void requestRelease();
    }

    interface Presenter extends BaseContract.Presenter {

        void addDynamic(Map<String, RequestBody> params);
    }
}
