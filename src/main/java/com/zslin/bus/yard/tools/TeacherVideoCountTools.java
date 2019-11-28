package com.zslin.bus.yard.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.dto.TeacherVideoCountDto;
import com.zslin.bus.yard.dto.TeacherVideoTreeDto;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.Teacher;
import com.zslin.bus.yard.model.TeacherVideoCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2019/2/11.
 */
@Component
public class TeacherVideoCountTools {

    @Autowired
    private ITeacherVideoCountDao teacherVideoCountDao;

    @Autowired
    private IVideoRecordDao videoRecordDao;

    @Autowired
    private ISchoolDao schoolDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    private static Integer VIEW_COUNT = 10; //每个视频每个用户可以播放次数

    /** 获取剩余次数 */
    public Integer getSurplusCount(String username, Integer courseId) {
        TeacherVideoCount tvc = teacherVideoCountDao.findByUsernameAndCourseId(username, courseId);
        Integer usedCount = videoRecordDao.findCount(username, courseId); //已经使用次数
        usedCount = usedCount==null?0:usedCount;
        Integer totalCount = VIEW_COUNT + (tvc==null?0:tvc.getCount());
        return totalCount - usedCount;
    }

    /** 生成教师对应的课程次数 */
    public List<TeacherVideoTreeDto> buildTree(String username) {
        Integer rootSystemId = schoolDao.findSystemId(username); //获取对应的课程体系
        Teacher tea = teacherDao.findByPhone(username);
        Sort sort = SimpleSortBuilder.generateSort("orderNo");
        List<ClassSystem> systemList = classSystemDao.findByParent(rootSystemId, sort);
        List<TeacherVideoTreeDto> result = new ArrayList<>();
        for(ClassSystem cs : systemList) {
            List<ClassCourse> courseList = classCourseDao.findByCid(cs.getId());
            List<TeacherVideoCountDto> tvcd = new ArrayList<>();
            for(ClassCourse cc : courseList) {
                tvcd.add(new TeacherVideoCountDto(username, tea.getName(), tea.getId(), getSurplusCount(username, cc.getId()), 0, cc.getId(), cc.getTitle()));
            }
            result.add(new TeacherVideoTreeDto(cs.getId(), cs.getName(), tvcd));
        }
        return result;
    }
}
