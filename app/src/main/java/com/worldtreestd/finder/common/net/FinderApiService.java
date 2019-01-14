package com.worldtreestd.finder.common.net;

import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.bean.CourseBean;
import com.worldtreestd.finder.common.bean.HomeCircleBean;
import com.worldtreestd.finder.common.bean.MessageBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Legend
 * @data by on 18-5-16.
 * @description Finder Api集合
 */
public interface FinderApiService {

//    String BASE_URL = "http://api.cspojie.cn/";
    String BASE_URL = "http://192.168.43.21:8080";

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @POST("/user/login")
    Observable<ResultVo<String>> login(@Query("openId") String username,
                                       @Query("password") String password);
    /**
     * 用户注册
     * @param requestBody
     * @return
     */
    @POST("/user/")
    Observable<ResultVo<User>> register(@Body RequestBody requestBody);

    /**
     * 添加动态
     * @param jwt
     * @Param params
     * @return
     */
    @Multipart
    @POST("/dynamic/")
    Observable<ResultVo<String>> addDynamic(@Header("Authorization") String jwt, @PartMap Map<String, RequestBody> params);

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
