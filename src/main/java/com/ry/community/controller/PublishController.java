package com.ry.community.controller;

import com.ry.community.category.TagCategory;
import com.ry.community.dto.QuestionDTO;
import com.ry.community.dto.ResultDTO;
import com.ry.community.exception.CustomizeErrorCodeImpl;
import com.ry.community.exception.CustomizeSucceedCodeImpl;
import com.ry.community.model.Question;
import com.ry.community.model.User;
import com.ry.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: rongyao
 * @Description: 提问Controller
 * @Date: Create in 12:05 2019/8/18
 * @Version 1.0
 */
@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;

    /**
     * 通过点击问题进入其页面
     */
    @GetMapping("/publish/{id}")
    public String edit(
            @PathVariable(name = "id") Long id,
            Model model
    ) {
        QuestionDTO questuin = questionService.findQuestionDTOById(id);
        model.addAttribute("title", questuin.getTitle());
        model.addAttribute("description", questuin.getDescription());
        model.addAttribute("tags", questuin.getTags());
        model.addAttribute("id", questuin.getId());
        model.addAttribute("tagList", TagCategory.get());
        return "publish";
    }


    @GetMapping("/publish")
    public String publish(Model model) {
        model.addAttribute("tagList", TagCategory.get());
        return "publish";
    }

    /**
     * 进入发布问题页面
     */
    @ResponseBody
    @PostMapping("/publish")
    public ResultDTO doPublish(
            @RequestBody QuestionDTO questionDTO,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("tagList", TagCategory.get());
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {

            return ResultDTO.errorOf(CustomizeErrorCodeImpl.USER_NOT_FIND);
        }
        if (questionDTO.getTitle() == null || questionDTO.getTitle() == "") {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.QUESTION_TITLE_IS_EMPTY);
        }
        if (questionDTO.getDescription() == null || questionDTO.getDescription() == "") {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.QUESTION_DESCRIPTION_IS_EMPTY);
        }
        if (questionDTO.getTags() == null || questionDTO.getTags() == "") {
            return ResultDTO.errorOf(CustomizeErrorCodeImpl.QUESTION_TAGS_IS_EMPTY);
        }

        Question question = new Question();
        question.setTitle(questionDTO.getTitle());
        question.setDescription(questionDTO.getDescription());
        question.setTags(questionDTO.getTags());
        question.setCreator(user.getId());
        question.setId(questionDTO.getId());
        questionService.createOrUpdate(question);
        return ResultDTO.okOf(CustomizeSucceedCodeImpl.PUBLISHED_SUCCESSFULLY);
    }
}
