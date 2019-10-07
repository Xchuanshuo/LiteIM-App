package com.legend.liteim.common.net;

import com.legend.im.bean.Msg;
import com.legend.liteim.bean.ChatMsgVO;
import com.legend.liteim.bean.Comment;
import com.legend.liteim.bean.Dynamic;
import com.legend.liteim.bean.ChatGroup;
import com.legend.liteim.bean.LoginReturn;
import com.legend.liteim.bean.Record;
import com.legend.liteim.bean.User;

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
import retrofit2.http.PUT;
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
//    String BASE_URL = "http://10.10.130.207:8080";
//    String BASE_URL = "http://finder.cspojie.cn";

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
     * 用户信息更新
     * @param requestBody
     * @return
     */
    @PUT("/user/")
    Observable<ResultVo<String>> updateUser(@Header("Authorization") String jwt,
                                            @Body RequestBody requestBody);


    /**
     * 根据id获取用户信息
     * @param id
     * @return
     */
    @GET("/user/{id}")
    Observable<ResultVo<User>> getUserById(@Path("id") Long id);

    /**
     * 上传用户信息相关的图片
     * @param jwt
     * @param params
     * @return
     */
    @Multipart
    @POST("/user/upload-image")
    Observable<ResultVo<String>> uploadUserFile(@Header("Authorization") String jwt,
                                                @PartMap Map<String, RequestBody> params);

    /**
     * 上传消息中的图片
     * @param jwt
     * @param params
     * @return
     */
    @Multipart
    @POST("/user/upload-msg-file")
    Observable<ResultVo<String>> uploadMsgFile(@Header("Authorization") String jwt, @PartMap Map<String, RequestBody> params);

    /**
     * 是否已经关注该用户
     * @param jwt
     * @param targetId
     * @return
     */
    @GET("/user-fans/is-follow/{targetId}")
    Observable<ResultVo<Boolean>> isAlreadyFollow(@Header("Authorization") String jwt, @Path("targetId") Long targetId);

    /**
     * 关注用户
     * @param jwt
     * @param targetId
     * @return
     */
    @POST("/user-fans/{targetId}")
    Observable<ResultVo<String>> followUser(@Header("Authorization") String jwt, @Path("targetId") Long targetId);

    /**
     * 取消关注用户
     * @param jwt
     * @param targetId
     * @return
     */
    @DELETE("/user-fans/{targetId}")
    Observable<ResultVo<String>> unFollowUser(@Header("Authorization") String jwt, @Path("targetId") Long targetId);

    /**
     * 用户关注人列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET("/user-fans/like/{userId}/{page}/{size}")
    Observable<ResultVo<Record<User>>> userFollowList(@Path("userId") Long userId, @Path("page") Integer page,
                                                          @Path("size") Integer size);

    /**
     * 用户粉丝列表
     * @param userId
     * @param page
     * @param size
     * @return
     */
    @GET("/user-fans/fans/{userId}/{page}/{size}")
    Observable<ResultVo<Record<User>>> userFansList(@Path("userId") Long userId, @Path("page") Integer page,
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
    Observable<ResultVo<Record<Dynamic>>> getPersonalDynamic(@Header("Authorization") String jwt, @Path("userId") Long userId,
                                                             @Path("page") Integer page, @Path("size") Integer size);
    /**
     * 删除发布的动态
     * @param jwt
     * @param dynamicId
     * @return
     */
    @DELETE("/dynamic/{dynamicId}")
    Observable<ResultVo<String>> deleteDynamic(@Header("Authorization") String jwt, @Path("dynamicId") Long dynamicId);

    /**
     * 收藏动态
     * @param jwt
     * @param dynamicId
     * @return
     */
    @POST("/dynamic-collect/{dynamicId}")
    Observable<ResultVo<String>> collectDynamic(@Header("Authorization") String jwt, @Path("dynamicId") Long dynamicId);

    /**
     * 取消收藏动态
     * @param jwt
     * @param dynamicId
     * @return
     */
    @DELETE("/dynamic-collect/{dynamicId}")
    Observable<ResultVo<String>> unCollectDynamic(@Header("Authorization") String jwt, @Path("dynamicId") Long dynamicId);

    /**
     * 动态评论列表
     * @param jwt
     * @param dynamicId
     * @param page
     * @param size
     * @return
     */
    @GET("/dynamic-comment/{dynamicId}/{page}/{size}")
    Observable<ResultVo<Record<Comment>>> dynamicCommentList(@Header("Authorization") String jwt, @Path("dynamicId") Long dynamicId,
                                                             @Path("page") Integer page, @Path("size") Integer size);
    /**
     * 增加一条评论
     * @param jwt
     * @param dynamicId
     * @param content
     * @return
     */
    @POST("/dynamic-comment/{dynamicId}")
    Observable<ResultVo<String>> addDynamicComment(@Header("Authorization") String jwt, @Path("dynamicId") Long dynamicId,
                                                   @Query("content") String content);
    /**
     * 删除一条评论
     * @param jwt
     * @param commentId
     * @return
     */
    @DELETE("/dynamic-comment/{commentId}")
    Observable<ResultVo<String>> deleteDynamicComment(@Header("Authorization") String jwt, @Path("commentId") Long commentId);

    /**
     * 给动态的评论点赞
     * @param jwt
     * @param commentId
     * @return
     */
    @POST("/dynamic-comment-favor/{commentId}")
    Observable<ResultVo<String>> praiseDynamicComment(@Header("Authorization") String jwt, @Path("commentId") Long commentId);

    /**
     * 动态的评论取消点赞
     * @param jwt
     * @param commentId
     * @return
     */
    @DELETE("/dynamic-comment-favor/{commentId}")
    Observable<ResultVo<String>> unPraiseDynamicComment(@Header("Authorization") String jwt, @Path("commentId") Long commentId);

    /**
     * 当前与对方是否已经是好友
     * @param jwt
     * @param targetId
     * @return
     */
    @GET("/friend/is-friend/{targetId}")
    Observable<ResultVo<String>> isFriend(@Header("Authorization") String jwt, @Path("targetId") Long targetId);

    /**
     * 添加好友
     * @param jwt
     * @param targetId
     * @return
     */
    @POST("/friend/{targetId}")
    Observable<ResultVo<String>> addFriend(@Header("Authorization") String jwt, @Path("targetId") Long targetId);

    /**
     * 删除好友
     * @param jwt
     * @param friendId
     * @return
     */
    @DELETE("/friend/{friendId}")
    Observable<ResultVo<String>> deleteFriend(@Header("Authorization") String jwt, @Path("friendId") Long friendId);


    /**
     * 获取用户的好友列表
     * @param jwt
     * @param page
     * @param size
     * @return
     */
    @GET("/friend/{page}/{size}")
    Observable<ResultVo<Record<User>>> getFriendList(@Header("Authorization") String jwt,
                                                     @Path("page") Integer page, @Path("size") Integer size);

    /**
     * 获取与好友的消息列表(包含所以)
     * @param jwt
     * @param targetId
     * @param page
     * @param size
     * @return
     */
    @GET("/single-chat-msg/{targetId}/{page}/{size}")
    Observable<ResultVo<Record<Msg>>> getUserMsgList(@Header("Authorization") String jwt,
                                                     @Path("targetId") Long targetId,
                                                     @Path("page") Integer page, @Path("size") Integer size);

    /**
     * 获取与好友的离线消息列表(还未签收的)
     * @param jwt
     * @param targetId
     * @param page
     * @param size
     * @return
     */
    @GET("/single-chat-msg/offline/{targetId}/{page}/{size}")
    Observable<ResultVo<Record<Msg>>> getUserOfflineMsgList(@Header("Authorization") String jwt,
                                                     @Path("targetId") Long targetId,
                                                     @Path("page") Integer page, @Path("size") Integer size);

    /**
     * 获取用户所有的离线消息列表
     * @param jwt
     * @return
     */
    @GET("/single-chat-msg/offline/all")
    Observable<ResultVo<List<ChatMsgVO>>> getAllUserOfflineMsgList(@Header("Authorization") String jwt);

    /**
     * 是否存在用户离线消息
     * @param jwt
     * @return
     */
    @GET("/single-chat-msg/offline/is-exist")
    Observable<ResultVo<Boolean>> isExistUserOfflineMsg(@Header("Authorization") String jwt);

    /**
     * 获取与好友的离线消息列表(还未签收的)
     * @param jwt
     * @param targetId
     * @param page
     * @param size
     * @return
     */
    @GET("/group-chat-msg/offline/{targetId}/{page}/{size}")
    Observable<ResultVo<Record<Msg>>> getGroupOfflineMsgList(@Header("Authorization") String jwt,
                                                            @Path("targetId") Long targetId,
                                                            @Path("page") Integer page, @Path("size") Integer size);

    /**
     * 获取用户所有的离线消息列表
     * @param jwt
     * @return
     */
    @GET("/group-chat-msg/offline/all")
    Observable<ResultVo<List<ChatMsgVO>>> getAllGroupOfflineMsgList(@Header("Authorization") String jwt);

    /**
     * 是否存在群组离线消息
     * @param jwt
     * @return
     */
    @GET("/group-chat-msg/offline/is-exist")
    Observable<ResultVo<Boolean>> isExistGroupOfflineMsg(@Header("Authorization") String jwt);


    /**
     * 获取指定群组的聊天消息列表
     * @param jwt
     * @param targetId
     * @param page
     * @param size
     * @return
     */
    @GET("/group-chat-msg/{groupId}/{page}/{size}")
    Observable<ResultVo<Record<Msg>>> getGroupMsgList(@Header("Authorization") String jwt,
                                                      @Path("groupId") Long targetId,
                                                      @Path("page") Integer page, @Path("size") Integer size);

    /**
     * 根据好友的名字或签名进行模糊搜索
     * @param jwt
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GET("/user/search/{page}/{size}")
    Observable<ResultVo<Record<User>>> searchUser(@Header("Authorization") String jwt,
                                                  @Path("page") Integer page, @Path("size") Integer size,
                                                  @Query("keyword") String keyword);

    /**
     * 根据群组的名称或简介进行模糊搜索
     * @param jwt
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GET("/group/search/{page}/{size}")
    Observable<ResultVo<Record<ChatGroup>>> searchGroup(@Header("Authorization") String jwt,
                                                        @Path("page") Integer page, @Path("size") Integer size,
                                                        @Query("keyword") String keyword);
    /**
     * 根据动态的内容进行模糊搜索
     * @param jwt
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    @GET("/dynamic/search/{page}/{size}")
    Observable<ResultVo<Record<Dynamic>>> searchDynamic(@Header("Authorization") String jwt,
                                                        @Path("page") Integer page, @Path("size") Integer size,
                                                        @Query("keyword") String keyword);

    /**
     * 获取用户已经加入的群组列表
     * @param jwt
     * @param page
     * @param size
     * @return
     */
    @GET("/group/joined/{page}/{size}")
    Observable<ResultVo<Record<ChatGroup>>> getJoinedGroupList(@Header("Authorization") String jwt,
                                                               @Path("page") Integer page, @Path("size") Integer size);
    /**
     * 创建一个群组
     * @param jwt
     * @param params
     * @return
     */
    @Multipart
    @POST("/group/")
    Observable<ResultVo<ChatGroup>> createGroup(@Header("Authorization") String jwt, @PartMap Map<String, RequestBody> params);

    /**
     * 根据id获取群组信息
     * @param groupId
     * @return
     */
    @GET("/group/{groupId}")
    Observable<ResultVo<ChatGroup>> getGroupById(@Path("groupId") Long groupId);


    /**
     * 删除群组
     * @param jwt
     * @param groupId
     * @return
     */
    @DELETE("/group/{groupId}")
    Observable<ResultVo<String>> deleteGroup(@Header("Authorization") String jwt, @Path("groupId") Long groupId);


    /**
     * 获取群成员列表
     * @param groupId
     * @param page
     * @param size
     * @return
     */
    @GET("/group-user/{groupId}/{page}/{size}")
    Observable<ResultVo<Record<User>>> groupUserList(@Path("groupId") Long groupId,
                                             @Path("page") Integer page, @Path("size") Integer size);


    /**
     * 为加入该群组的朋友列表
     * @param jwt
     * @param groupId
     * @param page
     * @param size
     * @return
     */
    @GET("/group-user/un-joined/{groupId}/{page}/{size}")
    Observable<ResultVo<Record<User>>> unJoinedFriendList(@Header("Authorization") String jwt, @Path("groupId") Long groupId,
                                                          @Path("page") Integer page, @Path("size") Integer size);

    /**
     * 加入群组
     * @param jwt
     * @param groupId
     * @return
     */
    @POST("/group-user/{groupId}")
    Observable<ResultVo<String>> joinGroup(@Header("Authorization") String jwt, @Path("groupId") Long groupId);

    /**
     * 批量拉取用户加入群组
     * @param jwt
     * @param groupId
     * @param ids
     * @return
     */
    @POST("/group-user/joins/{groupId}")
    Observable<ResultVo<String>> joinGroupByIds(@Header("Authorization") String jwt, @Path("groupId") Long groupId,
                                                @Query(value = "ids", encoded = true) String ids);

    /**
     * 退出群组
     * @param jwt
     * @param groupId
     * @return
     */
    @DELETE("/group-user/{groupId}")
    Observable<ResultVo<String>> exitGroup(@Header("Authorization") String jwt, @Path("groupId") Long groupId);
}
