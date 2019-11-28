package com.zslin.bus.yard.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.dto.SystemDetailTreeDto;
import com.zslin.bus.yard.dto.SystemTreeDto;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassSystemDetail;
import com.zslin.bus.yard.model.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2018/12/19.
 */
@Component
public class ClassSystemTools {

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @Autowired
    private ISchoolDao schoolDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private ITeacherRoleDao teacherRoleDao;

    @Autowired
    private IGradeRoleSystemDao gradeRoleSystemDao;

    public List<SystemDetailTreeDto> buildUserSystemTreeByRole(String username) {
        Integer systemId = schoolDao.findSystemId(username);
        if(systemId!=null && systemId>0) {
            Teacher teacher = teacherDao.findByPhone(username); //获取教师
            List<Integer> roleIds = teacherRoleDao.findRoleId(teacher.getId());

            List<Integer> systemIds = new ArrayList<>();
            for(Integer rid : roleIds) {
                List<Integer> tempIds = gradeRoleSystemDao.findSystemId(rid);
                for(Integer tid : tempIds) {
                    if(!systemIds.contains(tid)) {systemIds.add(tid);} //如果不包含则添加
                }
            }
            if(systemIds==null || systemIds.size()<=0) {return null;}

            List<SystemDetailTreeDto> rootTree = new ArrayList<>();
            Sort sort = SimpleSortBuilder.generateSort("orderNo");
//            List<ClassSystem> children = classSystemDao.findByParent(systemId, sort);

            List<ClassSystem> children = classSystemDao.findByIds(systemIds, sort);
            List<SystemTreeDto> courseList = new ArrayList<>();
            for(ClassSystem c : children) {
                List<ClassSystemDetail> cList = classSystemDetailDao.findBySid(c.getId(), sort);
                courseList.add(new SystemTreeDto(c, cList));
            }
            rootTree.add(new SystemDetailTreeDto(classSystemDao.findOne(systemId), courseList));
            return rootTree;
        } else {
            return null;
        }
    }

    /**
     * 此功能只用于教师用户
     * @param username 用户名必须是手机号码
     * @return
     */
    public List<SystemDetailTreeDto> buildUserSystemTree(String username) {
        Integer systemId = schoolDao.findSystemId(username);
        if(systemId!=null && systemId>0) {
            List<SystemDetailTreeDto> rootTree = new ArrayList<>();
            Sort sort = SimpleSortBuilder.generateSort("orderNo");
            List<ClassSystem> children = classSystemDao.findByParent(systemId, sort);
            List<SystemTreeDto> courseList = new ArrayList<>();
            for(ClassSystem c : children) {
                List<ClassSystemDetail> cList = classSystemDetailDao.findBySid(c.getId(), sort);
                courseList.add(new SystemTreeDto(c, cList));
            }
            rootTree.add(new SystemDetailTreeDto(classSystemDao.findOne(systemId), courseList));
            return rootTree;
        } else {
            return null;
        }
    }

    public List<SystemDetailTreeDto> buildSystemTree() {
        Sort sort = SimpleSortBuilder.generateSort("orderNo");
        List<ClassSystem> rootList = classSystemDao.findRoot(sort);
        List<SystemDetailTreeDto> rootTree = new ArrayList<>();
        for(ClassSystem root : rootList) {

            List<ClassSystem> children = classSystemDao.findByParent(root.getId(), sort);
            List<SystemTreeDto> courseList = new ArrayList<>();
            for(ClassSystem c : children) {
                List<ClassSystemDetail> cList = classSystemDetailDao.findBySid(c.getId(), sort);
                courseList.add(new SystemTreeDto(c, cList));
            }
            rootTree.add(new SystemDetailTreeDto(root, courseList));
        }
        return rootTree;
    }

}
