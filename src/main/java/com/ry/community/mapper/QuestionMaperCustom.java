package com.ry.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

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
}
