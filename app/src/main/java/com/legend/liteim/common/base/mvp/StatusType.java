package com.legend.liteim.common.base.mvp;

/**
 * @author Legend
 * @data by on 18-6-1.
 * @description 状态类型
 */
final public class StatusType {

    /**
     *  请求成功
     */
    public static final int RESULT_OK = 200;

    /**
     *  请求页面不存在
     */
    public static final int RESULT_NOT_FOUND = 404;


    /**
     *  服务器响应出错
     */
    public static final int RESULT_SERVER_ERROR = 500;

    /**
     *  刷新成功
     */
    public static final int REFRESH_SUCCESS = 0;

    /**
     * 　刷新失败
     */
    public static final int REFRESH_FAILURE = 1;

    /**
     *  加载更多成功
     */
    public static final int LOAD_MORE_SUCCESS = 3;

    /**
     *  加载更多失败
     */
    public static final int LOAD_MORE_FAILURE = 4;


}
