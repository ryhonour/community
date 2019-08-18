package com.ry.community.mapper;

import com.ry.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 14:21 2019/8/18
 * @Version 1.0
 */
@Mapper
@Component("questionMapper")
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tags) " +
            "values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tags})")
    void create(Question question);
}
