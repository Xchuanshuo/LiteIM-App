package com.worldtreestd.finder.ui.moreinfo.adapter;

import android.content.Context;
import android.support.v4.util.ArrayMap;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.worldtreestd.finder.R;
import com.worldtreestd.finder.common.bean.CommonMultiBean;
import com.worldtreestd.finder.common.bean.CourseContentBean;
import com.worldtreestd.finder.common.utils.EnumUtil;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-8-30.
 * @description
 */
public class DetailItemAdapter extends BaseMultiItemQuickAdapter<CommonMultiBean, BaseViewHolder> {

    private final String[] courseNumber = {"1-2节", "3-4节", "5-6节", "7-8节", "9-10节", "11-12节"};
    private final String[] courseTime = {"8:30~10:00", "10:20~11:50", "14:00~15:30",
                                            "15:50~17:20", "19:00~20:10", "20:30~22:00"};
    private ArrayMap<String, String> arrayMap;

    public DetailItemAdapter(List<CommonMultiBean> data) {
        super(data);
        arrayMap = new ArrayMap<>();
        for (int i=0;i<courseNumber.length;i++) {
            arrayMap.put(courseNumber[i], courseTime[i]);
        }
        addItemType(EnumUtil.CONFESSION_WALL.getCode(), R.layout.item_detail);
        addItemType(EnumUtil.COURSE_QUERY.getCode(), R.layout.item_course_detail);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommonMultiBean item) {
        switch (helper.getItemViewType()) {
            case 100:
                break;
            case 101:
                break;
            case 102:
                Context context = helper.itemView.getContext();
                CourseContentBean.WeeksBean.ScheduleBean bean =
                        (CourseContentBean.WeeksBean.ScheduleBean) item.getData();
                helper.setText(R.id.tv_class_number, context.getString(R.string.class_number)+bean.getNumber()+"  "+arrayMap.get(bean.getNumber()));
                helper.setText(R.id.tv_total_hours, bean.getDetail().getTotal_hours());
                helper.setText(R.id.tv_course_name, bean.getDetail().getCourse()==null?"当前没有课哦~":context.getString(R.string.course_name)+bean.getDetail().getCourse());
                helper.setText(R.id.tv_classroom, bean.getDetail().getClassroom());
                helper.setText(R.id.tv_course_teacher, context.getString(R.string.course_teacher)+bean.getDetail().getTeacher());

                break;
            default: break;
        }
    }
}
