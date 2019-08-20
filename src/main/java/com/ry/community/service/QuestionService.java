package com.ry.community.service;

import com.ry.community.dto.PaginationDTO;
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

    public PaginationDTO list(Integer currentPage, Integer size) {
        questionDTOList.clear();
        Integer totalCount = questionMapper.count();
        Integer totalPage;
        //计算总页数
        if (totalCount % size == 0) {
            totalPage = totalCount / size;
        } else {
            totalPage = totalCount / size + 1;
        }
        //设置当前页
        if (currentPage <= 0) {
            currentPage = 1;
        } else if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        //分页查询时数据的偏移量 = 每页查询数  * （页数 - 1）
        Integer offset = size * (currentPage - 1);
        PaginationDTO paginationDTO = new PaginationDTO();
        List<Question> questionList = questionMapper.list(offset, size);
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOList(questionDTOList);
        paginationDTO.setPagination(totalPage, currentPage);
        return paginationDTO;
    }
}
