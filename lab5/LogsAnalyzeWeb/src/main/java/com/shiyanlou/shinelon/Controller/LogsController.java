package com.shiyanlou.shinelon.Controller;


import com.shiyanlou.shinelon.Dao.CityTimesDao;
import com.shiyanlou.shinelon.Dao.HourTimesDao;
import com.shiyanlou.shinelon.Domain.CityTimes;
import com.shiyanlou.shinelon.Domain.HourTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class LogsController {

    @Autowired
    public CityTimesDao cityTimesDao;
    @Autowired
    public HourTimesDao hourTimesDao;


    @RequestMapping("/cityTime")
    public ModelAndView cityTimes(){
        return new ModelAndView("cityTimes");
    }



    @RequestMapping("/hourTimes")
    public ModelAndView hourTimes(){
        return new ModelAndView("hourTimes");
    }


    @RequestMapping("/showCity")
    @ResponseBody
    public List<CityTimes> showCity() throws Exception{
        return cityTimesDao.selectCityTimes();
    }

    @RequestMapping("/showHour")
    @ResponseBody
    public List<HourTimes> showHour() throws Exception{
        return hourTimesDao.selectCityTimes();
    }


}
