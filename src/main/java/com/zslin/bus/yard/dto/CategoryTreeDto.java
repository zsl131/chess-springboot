package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;

import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
public class CategoryTreeDto {

    private List<CategoryDto> treeList;

    private List<ClassCategory> categoryList;

    public CategoryTreeDto(List<CategoryDto> treeList, List<ClassCategory> categoryList) {
        this.treeList = treeList;
        this.categoryList = categoryList;
    }

    public List<CategoryDto> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<CategoryDto> treeList) {
        this.treeList = treeList;
    }

    public List<ClassCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ClassCategory> categoryList) {
        this.categoryList = categoryList;
    }
}
