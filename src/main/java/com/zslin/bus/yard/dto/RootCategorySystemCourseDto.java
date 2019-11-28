package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;

import java.util.List;

/**
 * Created by zsl on 2018/12/24.
 */
public class RootCategorySystemCourseDto {

    private ClassCategory category;

    private List<CategorySystemCourseDto> treeList;

    public RootCategorySystemCourseDto() {
    }

    public RootCategorySystemCourseDto(ClassCategory category, List<CategorySystemCourseDto> treeList) {
        this.category = category;
        this.treeList = treeList;
    }

    public ClassCategory getCategory() {
        return category;
    }

    public void setCategory(ClassCategory category) {
        this.category = category;
    }

    public List<CategorySystemCourseDto> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<CategorySystemCourseDto> treeList) {
        this.treeList = treeList;
    }
}
