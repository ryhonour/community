package com.ry.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:30 2019/8/14
 * @Version 1.0
 */
@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name") String name, Model model){
        model.addAttribute("name", name);
        return "greeting";
    }
}
