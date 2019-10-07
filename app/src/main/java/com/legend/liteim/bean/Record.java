package com.legend.liteim.bean;

import java.util.List;

/**
 * @author Legend
 * @data by on 19-1-15.
 * @description
 */
public class Record<T> {


    /**
     * records : [{"id":1,"userId":14,"title":null,"content":"坚持坚持再坚持","type":0,"urls":"[\"/14/images/d7a315caa9464a50940b1605e082b837.png\",\"/14/images/0b2db12ac32a46ac96011b7d40de389d.jpg\",\"/14/images/34ab5103987f46c897b7da8e3e4c11c2.jpg\",\"/14/images/f9f90ea9d7e94c5ea6e89231de7a94d6.jpg\",\"/14/images/d7b332e26a03420c987264a8911aec08.jpg\"]","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:05:10","collected":false},{"id":2,"userId":14,"title":null,"content":"你就说你想你的呢","type":1,"urls":"{\"coverPath\":\"/14/images/28eca260ca254d1a8a64a4abfe2b1ec6.jpg\",\"url\":\"/14/videos/a7644bcef0064988b88ef846c8c72370.png\"}","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:05:31","collected":false},{"id":3,"userId":14,"title":null,"content":"觉得呢想你","type":1,"urls":"{\"coverPath\":\"/14/images/466440629d4e48a087eb9be23df8920f.jpg\",\"url\":\"/14/videos/66a7f395a1714d67b188652f9023b738.png\"}","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:06:44","collected":false},{"id":4,"userId":14,"title":null,"content":"明显吗吃米线","type":1,"urls":"{\"coverPath\":\"/14/images/696644c22898456bb8f7fa4ae1fa155d.jpg\",\"url\":\"/14/videos/5c32cb0701cc4881be521bb0b1076b5b.mp4\"}","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:18:34","collected":false},{"id":5,"userId":14,"title":null,"content":"看得出吗","type":0,"urls":"[\"/14/images/e4cb1afaf1ff468ebf2a006d8ca323f3.png\",\"/14/images/7cd56380eebb4620842c9a313bee52d7.png\",\"/14/images/84dbc03d51eb4de2a67ae3ed25786d21.png\",\"/14/images/a06db2e31df04eaeb08ed1cef9c84da1.png\",\"/14/images/a2711f97abe644728a0a343c8b6102c2.mp4\",\"/14/images/942ffd9088c946d697ef87281ff801a8.png\"]","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:19:11","collected":false},{"id":6,"userId":14,"title":null,"content":"，就差你吃嫩草","type":1,"urls":"{\"coverPath\":\"/14/images/7fb587e2d2cc498791472070909274d4.jpg\",\"url\":\"/14/videos/7ce20f8f6c3f4f4f897d85b8cb2e6258.png\"}","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:19:32","collected":false},{"id":7,"userId":14,"title":null,"content":"可笑吗下","type":0,"urls":"[\"/14/images/db4f0695d4fb4aabb677029fc797fdea.jpg\",\"/14/images/972759d77b2c46e7975376cae5765e73.jpg\"]","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:21:19","collected":false},{"id":8,"userId":14,"title":null,"content":"江西南昌呢","type":1,"urls":"{\"coverPath\":\"/14/images/78bdb319073a4681b9a2855463d629bd.jpg\",\"url\":\"/14/videos/d707209e88554902a517d861faaf9cc9.jpg\"}","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:21:35","collected":false},{"id":9,"userId":14,"title":null,"content":"垃圾bug","type":0,"urls":"[\"/14/images/1dc0672926004b7abb120ff2ce2cde8f.jpg\",\"/14/images/d68d8e0daab1457c8ddd86917e1b757a.jpg\",\"/14/images/356883ef6dfb4a4e985aeaf66f3b3612.jpg\",\"/14/images/98fd17fc4c0d4370adee57640add5de3.jpg\",\"/14/images/461f1c7378ef4058aa8a8c9ee3ee6904.jpg\"]","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:23:28","collected":false},{"id":10,"userId":14,"title":null,"content":"辣鸡bug","type":1,"urls":"{\"coverPath\":\"/14/images/45a528e718a347aeb56095c78255466f.jpg\",\"url\":\"/14/videos/08a07db2f1b5489591365d3c79d087d4.mp4\"}","watchNum":0,"commentNum":0,"collectNum":0,"username":"丶legend","portrait":"http://qzapp.qlogo.cn/qzapp/1106570475/15E7BD092C885D5A434F12A28D87486F/50","status":1,"createTime":"2019-01-14T21:23:46","collected":false}]
     * total : 13
     * size : 10
     * current : 1
     * searchCount : true
     * pages : 2
     */

    private int total;
    private int size;
    private int current;
    private boolean searchCount;
    private int pages;
    private List<T> records;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public boolean isSearchCount() {
        return searchCount;
    }

    public void setSearchCount(boolean searchCount) {
        this.searchCount = searchCount;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

}
