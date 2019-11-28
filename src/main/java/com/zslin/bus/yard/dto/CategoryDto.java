package com.zslin.bus.yard.dto;

import com.zslin.bus.yard.model.ClassCategory;

import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
public class CategoryDto {

    private ClassCategory category;

    private List<ClassCategory> children;

    public CategoryDto() {
    }

    public CategoryDto(ClassCategory category, List<ClassCategory> children) {
        this.category = category;
        this.children = children;
    }

    public ClassCategory getCategory() {
        return category;
    }

    public void setCategory(ClassCategory category) {
        this.category = category;
    }

    public List<ClassCategory> getChildren() {
        return children;
    }

    public void setChildren(List<ClassCategory> children) {
        this.children = children;
    }
}
