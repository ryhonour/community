package com.ry.community.controller;

import com.ry.community.dto.QuestionDTO;
import com.ry.community.mapper.UserMapper;
import com.ry.community.model.User;
import com.ry.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 16:30 2019/8/14
 * @Version 1.0
 */
@Controller
public class IndexController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionService questionService;

    private List<QuestionDTO> questionDTOList = new ArrayList<>();
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        @CookieValue(value = "token",required = false) String token,
                        Model model
    ){
        //判断Cookie中是否存在token
        if(token != null && token != ""){
            //判断数据库中是否存在该token对应的user
            User user = userMapper.findByToken(token);
            if(user != null){
                //把该user保存在Session中
                request.getSession().setAttribute("user",user);
            }
        }

        questionDTOList.clear();
        questionDTOList = questionService.list();
        model.addAttribute("questionDTOList",questionDTOList);

        return "index";
    }
}
