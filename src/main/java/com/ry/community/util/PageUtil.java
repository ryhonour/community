package com.ry.community.util;

/**
 * @Author: rongyao
 * @Description:
 * @Date: Create in 17:48 2019/9/30
 * @Version 1.0
 */
public class PageUtil {
    private Integer totalCount;
    private Integer size;
    private Integer currentPage;

    public PageUtil(Integer totalCount, Integer size, Integer currentPage) {
        this.totalCount = totalCount;
        this.size = size;
        this.currentPage = currentPage;
    }

    public Integer getCurrentPage() {
        Integer totalPage = getTotalPage();
        if (currentPage <= 0) {
            return 1;
        } else if (currentPage > totalPage) {
            return totalPage;
        } else {
            return currentPage;
        }
    }

    public Integer getTotalPage() {
        if (totalCount % size == 0) {
            return totalCount / size;
        } else {
            return totalCount / size + 1;
        }
    }

    public Integer getOffset() {
        Integer emp = getCurrentPage();
        return size * (emp - 1);
    }
}
