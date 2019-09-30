package com.ry.community.controller;

import com.ry.community.category.TagCategory;
import com.ry.community.dto.QuestionDTO;
import com.ry.community.model.Question;
import com.ry.community.model.User;
import com.ry.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "id", required = false) Long id,
            HttpServletRequest request,
            Model model
    ) {
        model.addAttribute("title", title);
        model.addAttribute("description", description);
        model.addAttribute("tags", tags);
        model.addAttribute("tagList", TagCategory.get());
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        if (title == null || title == "") {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (description == null || description == "") {
            model.addAttribute("error", "内容不能为空");
            return "publish";
        }
        if (tags == null || tags == "") {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTags(tags);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
