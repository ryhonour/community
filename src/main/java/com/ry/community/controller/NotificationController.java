package com.ry.community.controller;

import com.ry.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 0:07 2019/10/1
 * @Version 1.0
 */
@Controller
public class NotificationController {
    @Autowired
    NotificationService notificationService;

    @GetMapping("notification/{id}")
    public String notification(@PathVariable(name = "id") Long id) {
        Long outerId = notificationService.getOuterId(id);
        String url = "redirect:/question/" + outerId;
        return url;
    }
}
