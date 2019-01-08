package com.worldtreestd.finder.common.bean;

import java.util.List;

/**
 * @author Legend
 * @data by on 2018/1/2.
 * @description
 */

public class HomeCircleBean {

    /**
     * id : 100001
     * user : admin
     * plan_list_item : [{"id":1,"user":"admin","end_time":"2017-12-30T09:51:00","content":"胜多负少爽肤水看出来是否深V看DVD没v啥都没出大V次幂多少v模式的方式递归漏电开关2数据访问","address":"望城县","users_num":3,"add_time":"2017-12-30T09:51:00","from_circle":100001},{"id":4,"user":"admin","end_time":"2017-12-30T09:51:00","content":"胜多负少爽肤水看出来是否深V看DVD没v啥都没出大V次幂多少v模式的方式递归漏电开关2数据访问","address":"望城县","users_num":3,"add_time":"2017-12-30T09:51:00","from_circle":100001},{"id":6,"user":"admin","end_time":"2018-01-02T09:19:00.904040","content":"","address":"","users_num":0,"add_time":"2018-01-02T09:19:00.904058","from_circle":100001},{"id":7,"user":"admin","end_time":"2018-01-02T09:58:12.353342","content":"","address":"","users_num":0,"add_time":"2018-01-02T09:58:12.353361","from_circle":100001},{"id":8,"user":"admin","end_time":"2018-01-02T10:11:42.702942","content":"","address":"","users_num":0,"add_time":"2018-01-02T10:11:42.702961","from_circle":100001},{"id":9,"user":"admin","end_time":"2018-01-02T12:48:18.408797","content":"","address":"","users_num":0,"add_time":"2018-01-02T12:48:18.408818","from_circle":100001}]
     * name : 世界树
     * address : 望城县
     * desc : 阿萨大师舒服撒发财树权威的十大。、实例：‘ 1’
     * image : null
     * add_time : 2017-12-30T09:23:00
     */

    private int id;
    private String user;
    private String name;
    private String address;
    private String desc;
    private String image;
    private String add_time;
    private int watch_num;
    private List<PlanListBean> plan_list;

    public HomeCircleBean(String imageurl, String name, String desc) {
        this.name = name;
        this.image = imageurl;
        this.desc = desc;
    }
    public HomeCircleBean(int id, String imageurl, String name, String desc, String created, String address, String add_time) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.image = imageurl;
        this.user = created;
        this.add_time = add_time;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public List<PlanListBean> getPlan_list() {
        return plan_list;
    }

    public void setPlan_list(List<PlanListBean> plan_list) {
        this.plan_list = plan_list;
    }

    public int getWatch_num() {
        return watch_num;
    }

    public void setWatch_num(int watch_num) {
        this.watch_num = watch_num;
    }

    public static class PlanListBean {
        /**
         * id : 1
         * user : admin
         * end_time : 2017-12-30T09:51:00
         * content : 胜多负少爽肤水看出来是否深V看DVD没v啥都没出大V次幂多少v模式的方式递归漏电开关2数据访问
         * address : 望城县
         * users_num : 3
         * add_time : 2017-12-30T09:51:00
         * from_circle : 100001
         */

        private int id;
        private String user;
        private String end_time;
        private String content;
        private String address;
        private int users_num;
        private String add_time;
        private int from_circle;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getUsers_num() {
            return users_num;
        }

        public void setUsers_num(int users_num) {
            this.users_num = users_num;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getFrom_circle() {
            return from_circle;
        }

        public void setFrom_circle(int from_circle) {
            this.from_circle = from_circle;
        }
    }
}
