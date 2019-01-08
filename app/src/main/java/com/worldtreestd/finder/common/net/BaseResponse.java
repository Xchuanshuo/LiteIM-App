package com.worldtreestd.finder.common.net;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-5-16.
 * @description 响应模板类
 */
public class BaseResponse<T> {

    //　未知错误
    public static final int STATE_UNKNOW_ERROR = 100001;
    // 响应成功
    public static int STATE_OK = 200;

    /**
     * count : 11
     * next : http://api.cspojie.cn/circles/?format=json&page=2
     * previous : null
     * results : [{"id":100063,"user":"a          执手","plan_list":[{"id":38,"from_circle_name":"一叶障目","from_circle":100063,"user":"♚回不去De唯成年","end_time":"2018-04-07T23:33:33","content":"。。。","address":"湖南长沙望城县 湖南信息职业技术学院","users_num":2,"is_finished":true,"add_time":"2018-04-08T07:35:41"}],"name":"一叶障目","address":"湖南长沙望城县 湖南信息职业技术学院","desc":"我们的活动","image":"http://api.cspojie.cn/media/circleimage/1520939407145new.jpg","watch_num":228,"add_time":"2018-03-13T19:11:28.621217"},{"id":100062,"user":"丶legend","plan_list":[],"name":"扭扭捏捏呢不不不","address":"甘肃兰州区(县) l你你你","desc":"慢慢看扣扣密码木马木马摸摸奶奶你爸爸vvv那拜拜拜拜v好爸爸","image":"http://api.cspojie.cn/media/circleimage/1520934785094new.jpg","watch_num":185,"add_time":"2018-03-13T17:54:34.144942"},{"id":100061,"user":"丶legend","plan_list":[],"name":"MM呆萌呆萌的","address":"内蒙古呼和浩特新城区 轮子i死的打底裤","desc":"快点快点快发快发康复科课程名称没出车参谋参谋米饭米饭没车没房摩西摩西明显吗","image":"http://api.cspojie.cn/media/circleimage/1520934605360new.jpg","watch_num":36,"add_time":"2018-03-13T17:50:57.013463"},{"id":100060,"user":"丶legend","plan_list":[],"name":"开心吗谢谢妈小妹","address":"宁夏银川兴庆区 快下课付款开裆裤付","desc":"短裤毛线帽参谋参谋炒米粉全美超模参谋参谋的","image":"http://api.cspojie.cn/media/circleimage/1520934555308new.jpg","watch_num":41,"add_time":"2018-03-13T17:49:52.016116"},{"id":100059,"user":"丶legend","plan_list":[],"name":"全啊楠楠啊","address":"海南海口秀英区 摩西摩西明显吗","desc":"i大口大口饭卡付款看我妈下面吃没吃明显吗参谋参谋出门馍馍菜米饭","image":"http://api.cspojie.cn/media/circleimage/1520934520461new.jpg","watch_num":16,"add_time":"2018-03-13T17:49:10.100931"},{"id":100058,"user":"丶legend","plan_list":[{"id":35,"from_circle_name":"摩西摩西快下课","from_circle":100058,"user":"丶legend","end_time":"2018-04-21T23:33:33","content":"京东客服免费的吗我开车没车没房麻麻烦烦麻烦你开心农场那你吃恼羞成怒女的","address":"江苏常州新北区 闪客快打的快点快点","users_num":1,"is_finished":true,"add_time":"2018-03-13T05:52:59"},{"id":36,"from_circle_name":"摩西摩西快下课","from_circle":100058,"user":"丶legend","end_time":"2018-04-22T23:33:33","content":"快下课方面打客服客服没错没错你妹MX难兄难弟那些那些女的你想你到哪","address":"江苏常州新北区 闪客快打的快点快点","users_num":1,"is_finished":true,"add_time":"2018-03-13T05:52:30"},{"id":37,"from_circle_name":"摩西摩西快下课","from_circle":100058,"user":"a          执手","end_time":"2018-04-20T23:33:33","content":"我要拿到一百万。","address":"江苏常州新北区 闪客快打的快点快点","users_num":3,"is_finished":false,"add_time":"2018-03-13T19:09:47.275505"}],"name":"摩西摩西快下课","address":"江苏常州新北区 闪客快打的快点快点","desc":"男的女的男的女的呢腐男腐女腐男腐女等你洗洗就行可么多么羡慕羡慕下","image":"http://api.cspojie.cn/media/circleimage/1520934482217new.jpg","watch_num":23,"add_time":"2018-03-13T17:48:36.525419"},{"id":100057,"user":"丶legend","plan_list":[],"name":"狂拽酷炫看","address":"山西太原小店区 没妹子没得","desc":"摩西摩西开车MX没车没房面对面父母妇女法芙娜MSN电脑的看到麻烦","image":"http://api.cspojie.cn/media/circleimage/1520934425272new.jpg","watch_num":32,"add_time":"2018-03-13T17:47:51.763982"},{"id":100048,"user":"admin","plan_list":[],"name":"Go->Docker","address":"望城县","desc":"为可移植性和并发性而生，强大的容器","image":"http://api.cspojie.cn/media/circleimage/%E5%B1%8F%E5%B9%95%E6%88%AA%E5%9B%BE42.png","watch_num":47,"add_time":"2018-03-03T21:06:45.648553"}]
     */

    private int count;
    private String next;
    private Object previous;
    private T results;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * id : 100063
         * user : a          执手
         * plan_list : [{"id":38,"from_circle_name":"一叶障目","from_circle":100063,"user":"♚回不去De唯成年","end_time":"2018-04-07T23:33:33","content":"。。。","address":"湖南长沙望城县 湖南信息职业技术学院","users_num":2,"is_finished":true,"add_time":"2018-04-08T07:35:41"}]
         * name : 一叶障目
         * address : 湖南长沙望城县 湖南信息职业技术学院
         * desc : 我们的活动
         * image : http://api.cspojie.cn/media/circleimage/1520939407145new.jpg
         * watch_num : 228
         * add_time : 2018-03-13T19:11:28.621217
         */

        private int id;
        private String user;
        private String name;
        private String address;
        private String desc;
        private String image;
        private int watch_num;
        private String add_time;
        private List<PlanListBean> plan_list;

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

        public int getWatch_num() {
            return watch_num;
        }

        public void setWatch_num(int watch_num) {
            this.watch_num = watch_num;
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

        public static class PlanListBean {
            /**
             * id : 38
             * from_circle_name : 一叶障目
             * from_circle : 100063
             * user : ♚回不去De唯成年
             * end_time : 2018-04-07T23:33:33
             * content : 。。。
             * address : 湖南长沙望城县 湖南信息职业技术学院
             * users_num : 2
             * is_finished : true
             * add_time : 2018-04-08T07:35:41
             */

            private int id;
            private String from_circle_name;
            private int from_circle;
            private String user;
            private String end_time;
            private String content;
            private String address;
            private int users_num;
            private boolean is_finished;
            private String add_time;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getFrom_circle_name() {
                return from_circle_name;
            }

            public void setFrom_circle_name(String from_circle_name) {
                this.from_circle_name = from_circle_name;
            }

            public int getFrom_circle() {
                return from_circle;
            }

            public void setFrom_circle(int from_circle) {
                this.from_circle = from_circle;
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

            public boolean isIs_finished() {
                return is_finished;
            }

            public void setIs_finished(boolean is_finished) {
                this.is_finished = is_finished;
            }

            public String getAdd_time() {
                return add_time;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }
        }
    }
}
