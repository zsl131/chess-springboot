package com.zslin.bus.yard.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.yard.dao.IClassCategoryDao;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dto.CategoryCourseTreeDto;
import com.zslin.bus.yard.dto.CourseTreeDto;
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
public class CourseTools {

    @Autowired
    private IClassCategoryDao classCategoryDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    public List<CategoryCourseTreeDto> buildCourseTree() {
        Sort sort = SimpleSortBuilder.generateSort("orderNo");
        List<ClassCategory> rootList = classCategoryDao.findRoot(sort);
        List<CategoryCourseTreeDto> rootTree = new ArrayList<>();
        for(ClassCategory root : rootList) {

            List<ClassCategory> children = classCategoryDao.findByParent(root.getId(), sort);
            List<CourseTreeDto> courseList = new ArrayList<>();
            for(ClassCategory c : children) {
                List<ClassCourse> cList = classCourseDao.findByCid(c.getId());
                courseList.add(new CourseTreeDto(c, cList));
            }
            rootTree.add(new CategoryCourseTreeDto(root, courseList));
        }
        return rootTree;
    }

}
