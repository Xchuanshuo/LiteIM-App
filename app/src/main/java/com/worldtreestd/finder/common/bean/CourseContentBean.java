package com.worldtreestd.finder.common.bean;

import java.util.List;

/**
 * @author Legend
 * @data by on 18-8-29.
 * @description
 */
public class CourseContentBean {


    /**
     * name : 第1周
     * weeks : [{"name":"星期一","schedule":[{"number":"1-2节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"3-4节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"5-6节","detail":{}},{"number":"7-8节","detail":{}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]},{"name":"星期二","schedule":[{"number":"1-2节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"3-4节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"5-6节","detail":{}},{"number":"7-8节","detail":{}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]},{"name":"星期三","schedule":[{"number":"1-2节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"3-4节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"5-6节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"7-8节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]},{"name":"星期四","schedule":[{"number":"1-2节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"3-4节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"5-6节","detail":{}},{"number":"7-8节","detail":{}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]},{"name":"星期五","schedule":[{"number":"1-2节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"3-4节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"5-6节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"7-8节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]},{"name":"星期六","schedule":[{"number":"1-2节","detail":{}},{"number":"3-4节","detail":{}},{"number":"5-6节","detail":{}},{"number":"7-8节","detail":{}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]},{"name":"星期日","schedule":[{"number":"1-2节","detail":{}},{"number":"3-4节","detail":{}},{"number":"5-6节","detail":{}},{"number":"7-8节","detail":{}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]}]
     */

    private String name;
    private List<WeeksBean> weeks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeeksBean> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<WeeksBean> weeks) {
        this.weeks = weeks;
    }

    public static class WeeksBean {
        /**
         * name : 星期一
         * schedule : [{"number":"1-2节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"3-4节","detail":{"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}},{"number":"5-6节","detail":{}},{"number":"7-8节","detail":{}},{"number":"9-10节","detail":{}},{"number":"11-12节","detail":{}}]
         */

        private String name;
        private List<ScheduleBean> schedule;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ScheduleBean> getSchedule() {
            return schedule;
        }

        public void setSchedule(List<ScheduleBean> schedule) {
            this.schedule = schedule;
        }

        public static class ScheduleBean {
            /**
             * number : 1-2节
             * detail : {"course":"专业技能训练（PLC)","total_hours":"总学时：28","teacher":"李颖","classroom":"13-107-PLC实验实训室"}
             */

            private String number;
            private DetailBean detail;

            public String getNumber() {
                return number;
            }

            public void setNumber(String number) {
                this.number = number;
            }

            public DetailBean getDetail() {
                return detail;
            }

            public void setDetail(DetailBean detail) {
                this.detail = detail;
            }

            public static class DetailBean {
                /**
                 * course : 专业技能训练（PLC)
                 * total_hours : 总学时：28
                 * teacher : 李颖
                 * classroom : 13-107-PLC实验实训室
                 */

                private String course;
                private String total_hours;
                private String teacher;
                private String classroom;

                public String getCourse() {
                    return course;
                }

                public void setCourse(String course) {
                    this.course = course;
                }

                public String getTotal_hours() {
                    return total_hours;
                }

                public void setTotal_hours(String total_hours) {
                    this.total_hours = total_hours;
                }

                public String getTeacher() {
                    return teacher;
                }

                public void setTeacher(String teacher) {
                    this.teacher = teacher;
                }

                public String getClassroom() {
                    return classroom;
                }

                public void setClassroom(String classroom) {
                    this.classroom = classroom;
                }
            }
        }
    }
}
