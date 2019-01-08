package com.worldtreestd.finder.common.utils;

import android.support.v4.util.ArrayMap;

import com.worldtreestd.finder.common.bean.BannerBean;
import com.worldtreestd.finder.common.bean.CommentBean;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.bean.DetailBean;
import com.worldtreestd.finder.common.bean.DynamicBean;
import com.worldtreestd.finder.common.bean.ItemBean;
import com.worldtreestd.finder.common.bean.SearchKeywordBean;

import java.util.ArrayList;
import java.util.List;

import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_PICTURE;
import static com.worldtreestd.finder.common.utils.Constant.DYNAMIC_ITEM_WORD_VIDEO;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_CENTER;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_HEAD;
import static com.worldtreestd.finder.common.utils.Constant.HOME_ITEM_TAIL;

/**
 * @author Legend
 * @data by on 18-7-16.
 * @description
 */
public class TestDataUtils {

    private static final String[] week = {"一", "二", "三", "四", "五", "六", "日"};
    public static final ArrayMap<String, Integer> weekMap = new ArrayMap<>();
    public static final ArrayMap<String, Integer> weekTHMap = new ArrayMap<>();

    static {
        for (int i=0;i<week.length;i++) {
            weekMap.put(week[i], i);
        }
        for (int i=0;i<19;i++) {
            weekTHMap.put("第"+i+"周", i);
        }
    }

    public static List<BannerBean> getBannerData() {
        List<BannerBean> result = new ArrayList<>();
        for (int i=0;i<4;i++) {
            result.add(new BannerBean("the "+i, "https://i.loli.net/2017/11/09/5a046546a2a1f.jpg"));
        }
        return result;
    }

    public static List<CommonMultiBean<DetailBean>> getHomePageData() {
        List<CommonMultiBean<DetailBean>> result = new ArrayList<>();
        for (EnumUtil total: EnumUtil.values()) {
            DetailBean headDetail = new DetailBean(total.text);
            result.add(new CommonMultiBean<>(headDetail, HOME_ITEM_HEAD));
            for (int j=0;j<6;j++) {
                DetailBean detailBean = new DetailBean("item: "+j);
                result.add(new CommonMultiBean<>(detailBean, HOME_ITEM_CENTER, j<2?3:j!=5?2:6));
            }
            result.add(new CommonMultiBean<>(headDetail, HOME_ITEM_TAIL));
        }
        return result;
    }

    public static List<SearchKeywordBean> getSearchData() {
        List<SearchKeywordBean> result = new ArrayList<>();
        int length = 15;
        for (int i=0;i<10;i++) {
            SearchKeywordBean bean = new SearchKeywordBean(DateUtils.getRandomString((int)(Math.random()*length)));
            result.add(bean);
        }
        return result;
    }

    public static List<CommonMultiBean<DynamicBean>> getDynamicData() {
        List<CommonMultiBean<DynamicBean>> result = new ArrayList<>();
        for (int i=0;i<40;i++) {
            DynamicBean dynamicBean = new DynamicBean("、legend", "");
            dynamicBean.setContent(CONTENT[(int) (Math.random() * 10)]);
            if (i%3 == 0) {
                dynamicBean.setVideoUrl(VideoConstant.videoUrlList[(int) (Math.random() * 7)]);
                dynamicBean.setVideoImageUrl(IMAGE_URL[(int) (Math.random() * 30)]);
                result.add(new CommonMultiBean<>(dynamicBean, DYNAMIC_ITEM_WORD_VIDEO));
            } else {
                dynamicBean.setImageUrlList(makeImages());
                result.add(new CommonMultiBean<>(dynamicBean, DYNAMIC_ITEM_WORD_PICTURE));
            }
        }
        return result;
    }

    public static List<CommentBean> getCommentList() {
        List<CommentBean> commentBeanList = new ArrayList<>();
        for (int i=0;i<40;i++) {
            CommentBean commentBean = new CommentBean("、legend", ""+i);
            commentBean.setContent(CONTENT[(int) (Math.random() * 10)]);
            commentBeanList.add(commentBean);
        }
        return commentBeanList;
    }

    public static List<ItemBean> getItemBeanList() {
        List<ItemBean> itemBeanList = new ArrayList<>();
        ItemBean courseTable =
                new ItemBean(EnumUtil.COURSE_QUERY.code, EnumUtil.COURSE_QUERY.text);
        List<ItemBean.Type> weekList = new ArrayList<>();
        for (int i=0;i<7;i++) {
            weekList.add(new ItemBean.Type(i, "星期"+week[i]));
        }
        courseTable.setTypes(weekList);
        itemBeanList.add(courseTable);
        for (int i=0;i<20;i++) {
            ItemBean itemBean = new ItemBean(i, "Menu: "+i);
            List<ItemBean.Type> list = new ArrayList<>();
            for (int j=0;j<10;j++) {
                list.add(new ItemBean.Type(j, "Type: "+j));
            }
            itemBean.setTypes(list);
            itemBeanList.add(itemBean);
        }

        return itemBeanList;
    }

    public static List<String> getWeekNumberList() {
        List<String> list = new ArrayList<>();
        for (int i=1;i<21;i++) {
            list.add("第"+i+"周");
        }
        return list;
    }

    private static List<String> makeImages() {
        List<String> imageBeans = new ArrayList<>();
        int randomCount = (int) (Math.random() * 12);
        if (randomCount == 0) {
            randomCount = randomCount + 1;
        } else if (randomCount == 8) {
            randomCount = randomCount + 1;
        }
        for (int i = 0; i < randomCount; i++) {
            imageBeans.add(IMAGE_URL[(int) (Math.random() * 50)]);
        }
        return imageBeans;
    }

    public static String[] IMAGE_URL = new String[]{
            "http://pic.qiantucdn.com/58pic/22/06/55/57b2d98e109c6_1024.jpg",
            "http://pic.58pic.com/58pic/15/36/00/73b58PICgvY_1024.jpg",
            "http://pic.58pic.com/58pic/15/25/09/29658PICRyz_1024.jpg",
            "http://pic10.nipic.com/20101014/5123760_202617630000_2.jpg",
            "http://a0.att.hudong.com/31/35/300533991095135084358827466.jpg",
            "http://img.tupianzj.com/uploads/allimg/160822/9-160R2213608.jpg",
            "http://pic17.nipic.com/20111021/8289149_105725398120_2.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123912727.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123904521.jpg",
            "http://pic3.16pic.com/00/16/45/16pic_1645991_b.jpg",


            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123915795.jpg",
            "http://pic32.photophoto.cn/20140706/0013026472380593_b.jpg",
            "http://pic.58pic.com/58pic/15/35/99/44Q58PICZTw_1024.jpg",
            "http://pic.58pic.com/58pic/13/59/88/32W58PICQpk_1024.jpg",
            "http://pic10.nipic.com/20100926/2874022_122448852398_2.jpg",
            "http://pic.qiantucdn.com/58pic/22/72/01/57c6578859e1e_1024.jpg",
            "http://img1.3lian.com/2015/a1/105/d/40.jpg",
            "http://pic33.photophoto.cn/20141130/0036036806648015_b.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123906588.jpg",
            "http://pic12.photophoto.cn/20090925/0018032186087872_b.jpg",


            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123916285.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201210/20121006203300515.jpg",
            "http://pic.58pic.com/58pic/15/36/00/75j58PICFhA_1024.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201210/2012102917585160.jpg",
            "http://img06.tooopen.com/images/20161010/tooopen_sy_181159738255.jpg",
            "http://pic3.16pic.com/00/16/45/16pic_1645919_b.jpg",
            "http://img.tupianzj.com/uploads/allimg/160812/9-160Q2215I3.jpg",
            "http://pic1.16pic.com/00/16/46/16pic_1646589_b.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201210/20121006154614772.jpg",
            "http://img1.sc115.com/uploads/sc/jpg/HD/40/10850.jpg",


            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123920304.jpg",
            "http://pic21.nipic.com/20120528/7487615_092947555158_2.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120422005031366.JPG",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201210/20121006203301121.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120422013455474.JPG",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201303/2013030914472860.jpg",
            "http://img1.3lian.com/2015/w22/87/d/105.jpg",
            "http://pic4.nipic.com/20091210/525154_080329294192_2.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120422013452211.JPG",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201303/2013030914500355.jpg",

            "http://pic.qiantucdn.com/58pic/14/35/64/56t58PIC2wJ_1024.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123911931.jpg",
            "http://pic10.nipic.com/20101020/5588264_195446417000_2.jpg",
            "http://pic23.nipic.com/20120814/5914324_155903179106_2.jpg",
            "http://pic30.nipic.com/20130624/7447430_170550396000_2.jpg",
            "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201210/2012102917583789.jpg",
            "http://pic9.nipic.com/20100916/5521709_153351042692_2.jpg",
            "http://pic28.photophoto.cn/20130809/0036036888631254_b.jpg",
            "http://pic25.photophoto.cn/20121117/0036036800515046_b.jpg",
            "http://pic.58pic.com/58pic/14/32/69/14X58PICcg5_1024.jpg"
    };

    public final static String[] CONTENT = {
            "土地是以它的肥沃和收获而被估价的；才能也是土地，不过它生产的不是粮食，而是真理。如果只能滋生瞑想和幻想的话，即使再大的才能也只是砂地或盐池，那上面连小草也长不出来的。",
            "我需要三件东西：爱情友谊和图书。然而这三者之间何其相通！炽热的爱情可以充实图书的内容，图书又是人们最忠实的朋友。",
            "人生的磨难是很多的，所以我们不可对于每一件轻微的伤害都过于敏感。在生活磨难面前，精神上的坚强和无动于衷是我们抵抗罪恶和人生意外的最好武器",
            "爱情只有当它是自由自在时，才会叶茂花繁。认为爱情是某种义务的思想只能置爱情于死地。只消一句话：你应当爱某个人，就足以使你对这个人恨之入骨。",
            "温顺的青年人在图书馆里长大，他们相信他们的责任是应当接受西塞罗，洛克，培根发表的意见；他们忘了西塞罗，洛克与培根写这些书的时候，也不过是在图书馆里的青年人。",
            "较高级复杂的劳动，是这样一种劳动力的表现，这种劳动力比较普通的劳动力需要较高的教育费用，它的生产需要花费较多的劳动时间。因此，具有较高的价值。",
            "父亲子女兄弟姊妹等称谓，并不是简单的荣誉称号，而是一种负有完全确定的异常郑重的相互义务的称呼，这些义务的总和便构成这些民族的社会制度的实质部分。",
            "世界上没有才能的人是没有的。问题在于教育者要去发现每一位学生的禀赋、兴趣、爱好和特长，为他们的表现和发展提供充分的条件和正确引导。",
            "在人类历史的长河中，真理因为像黄金一样重，总是沉于河底而很难被人发现，相反地，那些牛粪一样轻的谬误倒漂浮在上面到处泛滥。",
            "要永远觉得祖国的土地是稳固地在你脚下，要与集体一起生活，要记住，是集体教育了你。那一天你若和集体脱离，那便是末路的开始。"
    };
}
