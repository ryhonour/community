package com.ry.community.controller;

import com.ry.community.dto.QuestionDTO;
import com.ry.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: rongyao
 * @Description: 修改问题Controller
 * @Date: Create in 20:43 2019/8/21
 * @Version 1.0
 */
@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/question/{id}")
    public String question(
            @PathVariable(name = "id") Integer id,
            Model model
    ) {
        QuestionDTO questionDTO = questionService.findQuestionDTOById(id);
        model.addAttribute("questionDTO", questionDTO);
        return "question";
    }
}
