package com.shiyanlou.shinelon.Domain;

public class WeekCnt {
    private Integer week;
    private Integer cnt;
    public Integer getWeek() {
        return week;
    }
    public void setWeek(Integer week) {
        this.week = week;
    }
    public Integer getCnt() {
        return cnt;
    }
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
    @Override
    public String toString() {
        return "WeekCnt [week=" + week + ", cnt=" + cnt + "]";
    }


}
