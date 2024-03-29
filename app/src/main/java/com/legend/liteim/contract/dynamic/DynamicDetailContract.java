package com.legend.liteim.contract.dynamic;

import com.legend.liteim.bean.Comment;
import com.legend.liteim.contract.base.BaseContract;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-8-21.
 * @description
 */
public interface DynamicDetailContract {

    interface View extends BaseContract.View<Presenter> {

        /**
         * 显示评论数据
         * @param commentList
         */
        void showCommentData(List<Comment> commentList);

        /**
         * 评论成功
         * @param msg
         */
        void showCommentSuccess(String msg);

        /**
         * 删除评论成功
         * @param msg
         */
        void deleteCommentSuccess(String msg);

        /**
         * 评论点赞成功
         * @param msg
         */
        void praiseCommentSuccess(String msg);

        /**
         * 评论取消点赞成功
         */
        void unPraiseCommentSuccess();

        /**
         * 显示收藏成功
         */
        void showCollectedSuccess();

        /**
         * 显示取消收藏成功
         */
        void showUnCollectedSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        /**
         * 收藏动态
         * @param dynamicId
         */
        void collectDynamic(Long dynamicId);

        /**
         * 取消收藏动态
         * @param dynamicId
         */
        void unCollectDynamic(Long dynamicId);

        /**
         * 评论列表
         * @param dynamicId
         * @param page
         */
        void commentList(Long dynamicId, Integer page);

        /**
         * 发布一条评论
         * @param dynamicId
         * @param content
         */
        void releaseComment(Long dynamicId, String content);

        /**
         * 删除自己发布的评论
         * @param commentId
         */
        void deleteComment(Long commentId);

        /**
         * 评论点赞
         * @param commentId
         */
        void praiseComment(Long commentId);

        /**
         * 评论取消点赞
         * @param commentId
         */
        void unPraiseComment(Long commentId);
    }
}
