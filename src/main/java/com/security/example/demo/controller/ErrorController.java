package com.security.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class ErrorController {


    @RequestMapping("/403")
    public String paeg403() {
        return "/error/403";
    }

    @RequestMapping("/404")
    public String paeg404() {
        return "/error/404";
    }

    @RequestMapping("/500")
    public String paeg500() {
        return "/error/500";
    }
}