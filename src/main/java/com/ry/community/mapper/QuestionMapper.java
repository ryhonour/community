package com.ry.community.mapper;

import com.ry.community.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 14:21 2019/8/18
 * @Version 1.0
 */
@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tags) " +
            "values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tags})")
    void create(Question question);

    /**
     * offset 为偏移量从零开始，下面语句表示从offset开始查询，数量为size个，该查询用于分页。
     */
    @Select("select * from question limit #{offset},#{size}")
    List<Question> list(Integer offset, Integer size);

    @Select("select count(1) from question")
    Integer count();

}
