package com.zslin.bus.yard.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.yard.dao.IClassCategoryDao;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.ISystemCourseDao;
import com.zslin.bus.yard.dto.CategoryDto;
import com.zslin.bus.yard.dto.CategorySystemCourseDto;
import com.zslin.bus.yard.dto.RootCategorySystemCourseDto;
import com.zslin.bus.yard.dto.SystemCourseDto;
import com.zslin.bus.yard.model.ClassCategory;
import com.zslin.bus.yard.model.ClassCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
@Component
public class CategoryTools {

    @Autowired
    private IClassCategoryDao classCategoryDao;

    @Autowired
    private ISystemCourseDao systemCourseDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    public List<CategoryDto> buildCategoryTree() {
        List<CategoryDto> result = new ArrayList<>();
        Sort sort = SimpleSortBuilder.generateSort("orderNo");
        List<ClassCategory> rootList = classCategoryDao.findRoot(sort);
        for(ClassCategory m : rootList) {
            List<ClassCategory> children = classCategoryDao.findByParent(m.getId(), sort);
            result.add(new CategoryDto(m, children));
        }
        return result;
    }

    //用于为课程体系设置对应课程
    public List<RootCategorySystemCourseDto> buildCourseTree(Integer systemId) {
        List<RootCategorySystemCourseDto> result = new ArrayList<>();
        Sort sort = SimpleSortBuilder.generateSort("orderNo");
        List<Integer> courseIds = systemCourseDao.findCourseId(systemId); // 获取ID
        List<ClassCategory> rootList = classCategoryDao.findRoot(sort);
        for(ClassCategory m : rootList) {
            List<CategorySystemCourseDto> treeList = new ArrayList<>();
            List<ClassCategory> children = classCategoryDao.findByParent(m.getId(), sort);
            for(ClassCategory cc : children) {
                treeList.add(new CategorySystemCourseDto(cc, buildCourseDto(classCourseDao.findByCid(cc.getId()), courseIds)));
            }
            result.add(new RootCategorySystemCourseDto(m, treeList));
        }
        return result;
    }

    private List<SystemCourseDto> buildCourseDto(List<ClassCourse> courseList, List<Integer> courseIds) {
        List<SystemCourseDto> result = new ArrayList<>();
        for(ClassCourse cc : courseList) {
//            if(courseIds.contains(cc.getId())) {result.add(new SystemCourseDto(true, cc));}//
            result.add(new SystemCourseDto((courseIds!=null && courseIds.contains(cc.getId())), cc));
        }
        return result;
    }
}
