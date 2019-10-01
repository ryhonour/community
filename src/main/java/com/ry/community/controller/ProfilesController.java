package com.ry.community.controller;

import com.ry.community.dto.NotificationDTO;
import com.ry.community.dto.PaginationDTO;
import com.ry.community.dto.QuestionDTO;
import com.ry.community.enums.ProfilesTypeEnum;
import com.ry.community.model.User;
import com.ry.community.service.NotificationService;
import com.ry.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: rongyao
 * @Description: 个人档案Controller
 * @Date: Create in 14:22 2019/8/21
 * @Version 1.0
 */
@Controller
@RequestMapping
public class ProfilesController {
    @Autowired
    QuestionService questionService;
    @Autowired
    NotificationService notificationService;

    /**
     * 获取个人档案中我的question
     */
    @GetMapping("/profiles/{action}")
    public String Profiles(@RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                           @RequestParam(name = "size", defaultValue = "10") Integer size,
                           @PathVariable(name = "action") String action,
                           HttpServletRequest request,
                           Model model
    ) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        if (ProfilesTypeEnum.QUESTIONS.equals(action)) {
            //在个人页面显示 我的问题
            PaginationDTO<QuestionDTO> paginationDTO = questionService.list(currentPage, size, user);
            model.addAttribute("paginationDTO", paginationDTO);
            model.addAttribute("section", ProfilesTypeEnum.QUESTIONS.getType());
            model.addAttribute("selectName", "我的问题");
        } else if (ProfilesTypeEnum.NOTIFICATIONS.equals(action)) {
            //在个人页面显示 我的通知
            PaginationDTO<NotificationDTO> paginationDTO = notificationService.list(currentPage, size, user);
            model.addAttribute("paginationDTO", paginationDTO);
            model.addAttribute("section", ProfilesTypeEnum.NOTIFICATIONS.getType());
            model.addAttribute("selectName", "我的通知");
        }
        return "profiles";
    }
}
