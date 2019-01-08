package com.worldtreestd.finder.common.net;

import com.worldtreestd.finder.common.bean.CourseBean;
import com.worldtreestd.finder.common.bean.HomeCircleBean;
import com.worldtreestd.finder.common.bean.MessageBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Legend
 * @data by on 18-5-16.
 * @description Finder Api集合
 */
public interface FinderApiService {

    String BASE_URL = "http://api.cspojie.cn/";

    @GET("message/{id}")
    Observable<BaseResponse<List<MessageBean>>> getMessageList(@Path("id") String id);

    @GET("circles/")
    Observable<BaseResponse<List<HomeCircleBean>>> getCircleList();

    @GET("circles/{id}")
    Observable<HomeCircleBean> getCircleDetail(@Path("id") String id);

    @GET("courses/")
    Observable<List<CourseBean>> getCourseData(@Query("search") String grade);

    /**
     * 得到班级列表
     * @return
     */
    @GET("media/class_grade.json")
    Observable<List<String>> getClassGrade();
}
