package com.ry.community.controller;

import com.ry.community.dto.PaginationDTO;
import com.ry.community.model.User;
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
@RequestMapping("/profiles")
public class ProfilesController {
    @Autowired
    QuestionService questionService;

    /**
     * 获取个人档案中我的question
     */
    @GetMapping("/{action}")
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

        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("selectName", "我的问题");
        } else if ("notification".equals(action)) {
            model.addAttribute("section", "notification");
            model.addAttribute("selectName", "我的通知");
        }

        PaginationDTO paginationForUserDTO = questionService.list(currentPage, size, user);
        model.addAttribute("paginationForUserDTO", paginationForUserDTO);
        return "profiles";
    }
}
