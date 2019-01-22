package com.worldtreestd.finder.common.net;

import com.worldtreestd.finder.bean.Comment;
import com.worldtreestd.finder.bean.Dynamic;
import com.worldtreestd.finder.bean.LoginReturn;
import com.worldtreestd.finder.bean.Record;
import com.worldtreestd.finder.bean.User;
import com.worldtreestd.finder.common.bean.CourseBean;
import com.worldtreestd.finder.common.bean.HomeCircleBean;
import com.worldtreestd.finder.common.bean.MessageBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Observable<ResultVo<LoginReturn>> login(@Query("openId") String username,
                                            @Query("password") String password);
    /**
     * 用户注册
     * @param requestBody
     * @return
     */
    @POST("/user/")
    Observable<ResultVo<User>> register(@Body RequestBody requestBody);

    /**
     * 是否已经关注该用户
     * @param jwt
     * @param targetId
     * @return
     */
    @GET("/user-fans/is-follow/{targetId}")
    Observable<ResultVo<Boolean>> isAlreadyFollow(@Header("Authorization") String jwt, @Path("targetId") Integer targetId);

    /**
     * 关注用户
     * @param jwt
     * @param targetId
     * @return
     */
    @POST("/user-fans/{targetId}")
    Observable<ResultVo<String>> followUser(@Header("Authorization") String jwt, @Path("targetId") Integer targetId);

    /**
     * 取消关注用户
     * @param jwt
     * @param targetId
     * @return
     */
    @DELETE("/user-fans/{targetId}")
    Observable<ResultVo<String>> unFollowUser(@Header("Authorization") String jwt, @Path("targetId") Integer targetId);

    /**
     * 用户关注人列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET("/user-fans/user-like/{userId}/{page}/{size}")
    Observable<ResultVo<Record<User>>> userFollowList(@Path("userId") Integer userId, @Path("page") Integer page,
                                                          @Path("size") Integer size);

    /**
     * 用户粉丝列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET("/user-fans/user-fans/{userId}/{page}/{size}")
    Observable<ResultVo<Record<User>>> userFansList(@Path("userId") Integer userId, @Path("page") Integer page,
                                                        @Path("size") Integer size);
    /**
     * 添加动态
     * @param jwt
     * @Param params
     * @return
     */
    @Multipart
    @POST("/dynamic/")
    Observable<ResultVo<String>> addDynamic(@Header("Authorization") String jwt, @PartMap Map<String, RequestBody> params);

    /**
     * 动态信息分页展示
     * @param page
     * @param size
     * @return
     */
    @GET("/dynamic/{page}/{size}")
    Observable<ResultVo<Record<Dynamic>>> getDynamicList(@Header("Authorization") String jwt, @Path("page") Integer page, @Path("size") Integer size);

    /**
     * 个人发布的动态
     * @param jwt
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET("/dynamic/{userId}/{page}/{size}")
    Observable<ResultVo<Record<Dynamic>>> getPersonalDynamic(@Header("Authorization") String jwt, @Path("userId") Integer userId,
                                                             @Path("page") Integer page, @Path("size") Integer size);
    /**
     * 删除发布的动态
     * @param jwt
     * @param dynamicId
     * @return
     */
    @DELETE("/dynamic/{dynamicId}")
    Observable<ResultVo<String>> deleteDynamic(@Header("Authorization") String jwt, @Path("dynamicId") Integer dynamicId);

    /**
     * 收藏动态
     * @param jwt
     * @param dynamicId
     * @return
     */
    @POST("/dynamic-collect/{dynamicId}")
    Observable<ResultVo<String>> collectDynamic(@Header("Authorization") String jwt, @Path("dynamicId") Integer dynamicId);

    /**
     * 取消收藏动态
     * @param jwt
     * @param dynamicId
     * @return
     */
    @DELETE("/dynamic-collect/{dynamicId}")
    Observable<ResultVo<String>> unCollectDynamic(@Header("Authorization") String jwt, @Path("dynamicId") Integer dynamicId);

    /**
     * 动态评论列表
     * @param jwt
     * @param dynamicId
     * @param page
     * @param size
     * @return
     */
    @GET("/dynamic-comment/{dynamicId}/{page}/{size}")
    Observable<ResultVo<Record<Comment>>> dynamicCommentList(@Header("Authorization") String jwt, @Path("dynamicId") Integer dynamicId,
                                                             @Path("page") Integer page, @Path("size") Integer size);
    /**
     * 增加一条评论
     * @param jwt
     * @param dynamicId
     * @param content
     * @return
     */
    @POST("/dynamic-comment/{dynamicId}")
    Observable<ResultVo<String>> addDynamicComment(@Header("Authorization") String jwt, @Path("dynamicId") Integer dynamicId,
                                                   @Query("content") String content);
    @DELETE("/dynamic-comment/{commentId}")
    Observable<ResultVo<String>> deleteDynamicComent(@Header("Authorization") String jwt, @Path("commentId") Integer commentId);

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
