package com.ry.community.controller;

import com.ry.community.mapper.QuestionMapper;
import com.ry.community.mapper.UserMapper;
import com.ry.community.model.Question;
import com.ry.community.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 12:05 2019/8/18
 * @Version 1.0
 */
@Controller
public class PublishController {
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @Param("title") String title,
            @Param("description") String description,
            @Param("tags") String tags,
            @CookieValue(value = "token",required = false) String token,
            Model model
    ){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tags",tags);
        User user = userMapper.findByToken(token);
        //判断用户是否登录，未登录返回错误信息
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        if(title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error","内容不能为空");
            return "publish";
        }
        if(tags == null || tags == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTags(tags);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        questionMapper.create(question);
        return "redirect:/";
    }
}
