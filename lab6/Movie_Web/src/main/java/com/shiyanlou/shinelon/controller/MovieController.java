package com.shiyanlou.shinelon.controller;

import com.shiyanlou.shinelon.dao.WordCountDao;
import com.shiyanlou.shinelon.domain.GradeCount;
import com.shiyanlou.shinelon.domain.WordCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import java.util.ArrayList;
import java.util.List;


@RestController
public class MovieController {

    @Autowired
    public WordCountDao wordCountDao;

    @RequestMapping("/movie-cloud")
    public ModelAndView cloud() {
        return new ModelAndView("index");
    }

    @RequestMapping("/movie-grade")
    public ModelAndView grade() {
        return new ModelAndView("grade");
    }

    @RequestMapping("/movie_grade")
    @ResponseBody
    public List<GradeCount> movie_grade() throws Exception{
        List<GradeCount> list=new ArrayList<GradeCount>();
        list=wordCountDao.gradeList();
        return list;

    }



    @RequestMapping("/wordList")
    @ResponseBody
    public List<WordCount> wordList() throws Exception{
        List<WordCount> list=new ArrayList<WordCount>();
        list=wordCountDao.wordList();
        return list;

    }





}
