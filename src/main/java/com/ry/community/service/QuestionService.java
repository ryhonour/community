package com.ry.community.service;

import com.ry.community.dto.PaginationDTO;
import com.ry.community.dto.QuestionDTO;
import com.ry.community.exception.CustomizeErrorCodeImpl;
import com.ry.community.exception.CustomizeException;
import com.ry.community.mapper.QuestionMaperCustom;
import com.ry.community.mapper.QuestionMapper;
import com.ry.community.mapper.UserMapper;
import com.ry.community.model.Question;
import com.ry.community.model.QuestionExample;
import com.ry.community.model.User;
import org.apache.ibatis.session.RowBounds;
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
    @Autowired
    QuestionMaperCustom questionMaperCustom;


    Integer totalPage;
    Integer offset;

    private List<QuestionDTO> questionDTOList = new ArrayList<>();
    private List<QuestionDTO> questionByUserDTOList = new ArrayList<>();

    public PaginationDTO list(Integer currentPage, Integer size) {
        questionDTOList.clear();
        QuestionExample questionExample = new QuestionExample();
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        calculationParam(totalCount, size, currentPage);
        questionExample.setOrderByClause("gmt_create DESC");
        RowBounds rowBounds = new RowBounds(offset, size);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(questionExample, rowBounds);
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setQuestionDTOList(questionDTOList);
        paginationDTO.setPagination(totalPage, currentPage);
        return paginationDTO;
    }

    public PaginationDTO list(Integer currentPage, Integer size, User user) {
        questionByUserDTOList.clear();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(user.getId());
        questionExample.setOrderByClause("gmt_create DESC");
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        calculationParam(totalCount, size, currentPage);
        RowBounds rowBounds = new RowBounds(offset, size);
        List<Question> questionForUserList = questionMapper.selectByExampleWithRowbounds(questionExample, rowBounds);
        for (Question question : questionForUserList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionByUserDTOList.add(questionDTO);
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        paginationDTO.setQuestionDTOList(questionByUserDTOList);
        paginationDTO.setPagination(totalPage, currentPage);
        return paginationDTO;
    }

    public void calculationParam(Integer totalCount, Integer size, Integer currentPage) {
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
        offset = size * (currentPage - 1);
    }

    public QuestionDTO findQuestionDTOById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FIND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            //更新
            question.setGmtModified(System.currentTimeMillis());
            int update = questionMapper.updateByPrimaryKeySelective(question);
            if (update != 1) {
                throw new CustomizeException(CustomizeErrorCodeImpl.QUESTION_NOT_FIND);
            }
        }
    }

    /**
     * 增加阅读数
     */
    public void incview(Long id) {
        questionMaperCustom.incView(id);
    }
}
