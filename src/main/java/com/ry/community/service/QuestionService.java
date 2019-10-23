package com.ry.community.service;

import com.ry.community.dto.PaginationDTO;
import com.ry.community.dto.QuestionDTO;
import com.ry.community.dto.QuestionQueryDto;
import com.ry.community.exception.CustomizeErrorCodeImpl;
import com.ry.community.exception.CustomizeException;
import com.ry.community.mapper.QuestionMaperCustom;
import com.ry.community.mapper.QuestionMapper;
import com.ry.community.mapper.UserMapper;
import com.ry.community.model.Question;
import com.ry.community.model.QuestionExample;
import com.ry.community.model.User;
import com.ry.community.util.PageUtil;
import org.apache.commons.lang3.StringUtils;
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
    private QuestionMaperCustom questionMaperCustom;

    private List<QuestionDTO> questionDTOList = new ArrayList<>();
    private List<QuestionDTO> questionByUserDTOList = new ArrayList<>();

    public PaginationDTO<QuestionDTO> list(String search, Integer currentPage, Integer size) {
        questionDTOList.clear();
        if (StringUtils.isNotBlank(search)) {
            search = StringUtils.replace(search, " ", "|");
        }
        QuestionQueryDto questionQueryDto = new QuestionQueryDto();
        questionQueryDto.setSearch(search);
        int totalCount = questionMaperCustom.countBySearch(questionQueryDto);
        PageUtil pageUtil = new PageUtil(totalCount, size, currentPage);
        questionQueryDto.setSize(size);
        questionQueryDto.setPage(pageUtil.getOffset());
        List<Question> questionList = questionMaperCustom.selectBySearch(questionQueryDto);
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setTList(questionDTOList);
        paginationDTO.setPagination(pageUtil.getTotalPage(), pageUtil.getCurrentPage());
        return paginationDTO;
    }

    public PaginationDTO<QuestionDTO> list(Integer currentPage, Integer size, User user) {
        questionByUserDTOList.clear();
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(user.getId());
        questionExample.setOrderByClause("gmt_create DESC");
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        PageUtil pageUtil = new PageUtil(totalCount, size, currentPage);
        questionExample.setOrderByClause("gmt_create DESC");
        RowBounds rowBounds = new RowBounds(pageUtil.getOffset(), size);
        List<Question> questionForUserList = questionMapper.selectByExampleWithRowbounds(questionExample, rowBounds);
        for (Question question : questionForUserList) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionByUserDTOList.add(questionDTO);
        }
        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        paginationDTO.setTList(questionByUserDTOList);
        paginationDTO.setPagination(pageUtil.getTotalPage(), pageUtil.getCurrentPage());
        return paginationDTO;
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

    /**
     * 通过 tags 查询 当前问题的相关问题，多个标签之间用 "|" 分割开来，"|"在正则表达式中表示"或"
     */
    public List<Question> selectQuestionListByTagsWithRegexp(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setId(questionDTO.getId());
        String tags = questionDTO.getTags();
        //如果tags为空则当前问题不存在相关问题
        if (StringUtils.isBlank(tags)) {
            return new ArrayList<>();
        }
        String regexpTags = StringUtils.replace(tags, ",", "|");
        question.setTags(regexpTags);
        return questionMaperCustom.selectQuestionListByTagsWithRegexp(question);
    }
}
