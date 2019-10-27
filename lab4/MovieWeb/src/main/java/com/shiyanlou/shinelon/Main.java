package com.shiyanlou.shinelon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
@RestController
public class Main {
    @RequestMapping("/hello")
    public String hello() {
        return "hello ";
    }

    @RequestMapping("/frist")
    public ModelAndView firstDemo() {
        return new ModelAndView("test");
    }

    @RequestMapping("/cloud")
    public ModelAndView cloudDemo() {
        return new ModelAndView("index");
    }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
