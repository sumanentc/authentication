package com.fractal.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by suman.das on 11/15/16.
 */
@Controller
public class TestController {

    @RequestMapping("/")
    public String entry(){
        return "home";
    }
    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/hello")
    public String hello(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username

        model.addAttribute("username", name);
        return "hello";
    }

    //@RequestMapping(value = "/logout",method = RequestMethod.POST)
    public String hello(HttpServletRequest request){
        return "redirect:" + "http://localhost:8080/cas/logout?service=http://localhost:9090";
    }
    //@RequestMapping("/login")
    public String login(){
        return "login";
    }


}
