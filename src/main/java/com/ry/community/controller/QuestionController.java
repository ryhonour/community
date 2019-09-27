package com.ry.community.controller;

import com.ry.community.dto.CommentDTO;
import com.ry.community.dto.QuestionDTO;
import com.ry.community.enums.CommentTypeEnum;
import com.ry.community.model.Question;
import com.ry.community.service.CommentService;
import com.ry.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Author: rongyao
 * @Description: 修改问题Controller
 * @Date: Create in 20:43 2019/8/21
 * @Version 1.0
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id") Long id,
                           Model model) {
        //获取当前问题
        QuestionDTO questionDTO = questionService.findQuestionDTOById(id);
        //获取当前问题的子评论
        List<CommentDTO> commentDTOList = commentService.listByTarget(id, CommentTypeEnum.QUESTION);
        //获取当前问题的相关问题（根据tags的查询）
        List<Question> relatedQuestionList = questionService.selectQuestionListByTagsWithRegexp(questionDTO);
        //增加阅读数
        questionService.incview(id);

        model.addAttribute("questionDTO", questionDTO);
        model.addAttribute("commentDTOList", commentDTOList);
        model.addAttribute("relatedQuestionList", relatedQuestionList);
        return "question";
    }
}
