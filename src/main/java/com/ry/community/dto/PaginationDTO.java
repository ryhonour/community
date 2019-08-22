package com.ry.community.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 15:09 2019/8/20
 * @Version 1.0
 */
@Data
@Component
public class PaginationDTO {
    private List<QuestionDTO> questionDTOList;
    private Boolean showPrevious;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;

    private Integer currentPage;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer totalPage, Integer currentPage) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
        //清空页面List
        pages.clear();

        Integer previousPage;
        Integer nextPage;
        //向页面List中加入所显示的页面
        pages.add(currentPage);
        for (int i = 1; i <= 3; i++) {
            previousPage = currentPage - i;
            nextPage = currentPage + i;
            if (previousPage > 0) {
                pages.add(previousPage);
            }
            if (nextPage <= totalPage) {
                pages.add(nextPage);
            }
            //对页面进行排序
            Collections.sort(pages);

            if (totalPage == 1) {
                showPrevious = false;
                showFirstPage = false;
                showNext = false;
                showEndPage = false;
            }
            //是否显示上一页
            if (currentPage == 1) {
                showPrevious = false;
            } else {
                showPrevious = true;
            }

            //是否显示下一页
            if (currentPage.equals(totalPage)) {
                showNext = false;
            } else {
                showNext = true;
            }

            //是否展示第一页
            if (pages.contains(1)) {
                showFirstPage = false;
            } else {
                showFirstPage = true;
            }

            //是否显示最后一页
            if (pages.contains(totalPage)) {
                showEndPage = false;
            } else {
                showEndPage = true;
            }
        }

    }
}
