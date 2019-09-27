package com.ry.community.mapper;

import com.ry.community.model.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 14:52 2019/8/26
 * @Version 1.0
 */
@Mapper
public interface QuestionMaperCustom {
    /**
     * 浏览数自加一
     */
    @Update("update question set view_count = view_count + 1 where id = #{id}")
    void incView(@Param("id") Long id);

    /**
     * 评论数自加一
     */
    @Update("update question set comment_count = comment_count + 1 where id = #{id}")
    void incComment(@Param("id") Long id);

    /**
     * 通过 tags 查询 list<Question> ，多个标签之间用 "|" 分割开来，"|"在正则表达式中表示"或" 默认最多查找最新的15个相关问题
     */
    @Select("SELECT id,title,tags FROM question WHERE tags REGEXP #{question.tags} and id != #{question.id} order by gmt_create desc limit 15")
    List<Question> selectQuestionListByTagsWithRegexp(@Param("question") Question question);
}
