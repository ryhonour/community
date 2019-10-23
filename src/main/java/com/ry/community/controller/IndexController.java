package com.ry.community.controller;

import com.ry.community.dto.PaginationDTO;
import com.ry.community.dto.QuestionDTO;
import com.ry.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: rongyao
 * @Description: 首页Controller
 * @Date: Create in 16:30 2019/8/14
 * @Version 1.0
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "currentPage", defaultValue = "1") Integer currentPage,
                        @RequestParam(name = "size", defaultValue = "11") Integer size,
                        @RequestParam(name = "search", required = false) String search
    ) {
        PaginationDTO<QuestionDTO> paginationDTO = questionService.list(search, currentPage, size);
        model.addAttribute("paginationDTO", paginationDTO);
        model.addAttribute("search", search);
        return "index";
    }
}
