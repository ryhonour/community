package com.ry.community.service;

import com.ry.community.dto.QuestionDTO;
import com.ry.community.mapper.QuestionMapper;
import com.ry.community.mapper.UserMapper;
import com.ry.community.model.Question;
import com.ry.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 20:08 2019/8/19
 * @Version 1.0
 */
@Service
public class QuestionService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionMapper questionMapper;

    private List<QuestionDTO> questionDTOList = new ArrayList<>();

    public List<QuestionDTO> list(){
        List<Question> questionList = questionMapper.list();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
