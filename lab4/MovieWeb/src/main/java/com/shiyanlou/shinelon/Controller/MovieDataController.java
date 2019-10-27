package com.shiyanlou.shinelon.Controller;

import com.shiyanlou.shinelon.Dao.MovieRateDao;
import com.shiyanlou.shinelon.Dao.WeekCntDao;
import com.shiyanlou.shinelon.Domain.DayCnt;
import com.shiyanlou.shinelon.Domain.MovieRate;
import com.shiyanlou.shinelon.Domain.WeekCnt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MovieDataController {

    public static Map<Integer,String> map = new HashMap<Integer,String>();
    {
        map.put(1, "星期一");
        map.put(2, "星期二");
        map.put(3, "星期三");
        map.put(4, "星期四");
        map.put(5, "星期五");
        map.put(6, "星期六");
        map.put(7, "星期天");
    }


    @Autowired
    public MovieRateDao movieRateDao;

    @Autowired
    public WeekCntDao weekCntDao;

    @RequestMapping("/cnt")
    public ModelAndView cnt() {
        return new ModelAndView("week_cnt");
    }

    @RequestMapping("/rate")
    public ModelAndView rate() {
        return new ModelAndView("movie_rate");
    }

    @RequestMapping("/week_cnt.do")
    @ResponseBody
    public List<DayCnt> showCnt(){
//		System.out.println( weekCntDao.getWeekCnt().size());
        List<WeekCnt> list=new ArrayList<WeekCnt>();
        List<DayCnt> weekList=new ArrayList<DayCnt>();
        list=weekCntDao.getWeekCnt();
        for(WeekCnt w:list) {
            DayCnt dayCnt=new DayCnt();
            dayCnt.setDay(map.get(w.getWeek()));
            dayCnt.setCnt(w.getCnt());
            weekList.add(dayCnt);
        }
        return weekList;
    }

    @RequestMapping("/movie_rate.do")
    @ResponseBody
    public List<MovieRate> showRate(){
        List<MovieRate> list =new ArrayList<MovieRate>();
        list=movieRateDao.getMovieRate();
        return list;
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

}
