package com.worldtreestd.finder.bean;

import org.litepal.crud.LitePalSupport;

/**
 * @author legend
 */
public class Dynamic extends LitePalSupport {
        /**
         * id : 1
         * userId : 14
         * title : null
         * content : 坚持坚持再坚持
         * type : 0
         * urls : ["/14/images/d7a315caa9464a50940b1605e082b837.png","/14/images/0b2db12ac32a46ac96011b7d40de389d.jpg","/14/images/34ab5103987f46c897b7da8e3e4c11c2.jpg","/14/images/f9f90ea9d7e94c5ea6e89231de7a94d6.jpg","/14/images/d7b332e26a03420c987264a8911aec08.jpg"]
         * watchNum : 0
         * commentNum : 0
         * collectNum : 0
         * username : 丶legend
         * portrait : http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50
         * status : 1
         * createTime : 2019-01-14T21:05:10
         * collected : false
         */

        private int id;
        private int userId;
        private Object title;
        private String content;
        private int type;
        private String urls;
        private int watchNum;
        private int commentNum;
        private int collectNum;
        private String username;
        private String portrait;
        private int status;
        private String createTime;
        private boolean collected;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Object getTitle() {
            return title;
        }

        public void setTitle(Object title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrls() {
            return urls;
        }

        public void setUrls(String urls) {
            this.urls = urls;
        }

        public int getWatchNum() {
            return watchNum;
        }

        public void setWatchNum(int watchNum) {
            this.watchNum = watchNum;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public int getCollectNum() {
            return collectNum;
        }

        public void setCollectNum(int collectNum) {
            this.collectNum = collectNum;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPortrait() {
            return portrait;
        }

        public void setPortrait(String portrait) {
            this.portrait = portrait;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public boolean isCollected() {
            return collected;
        }

        public void setCollected(boolean collected) {
            this.collected = collected;
        }
    }
