package com.zslin.bus.yard.service;

import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.common.tools.JsonTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.yard.dao.ITeacherDao;
import com.zslin.bus.yard.dao.IVideoRecordDao;
import com.zslin.bus.yard.dto.TeacherVideoTreeDto;
import com.zslin.bus.yard.model.Teacher;
import com.zslin.bus.yard.model.VideoRecord;
import com.zslin.bus.yard.tools.TeacherVideoCountTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zsl on 2019/2/8.
 */
@Service
public class VideoRecordService {

    @Autowired
    private IVideoRecordDao videoRecordDao;

    @Autowired
    private ITeacherDao teacherDao;

    @Autowired
    private TeacherVideoCountTools teacherVideoCountTools;

    /** 为教师配置视频播放次数而获取课程树 */
    public JsonResult queryCountTree(String params) {
        String username = JsonTools.getJsonParam(params, "username");
        List<TeacherVideoTreeDto> tree = teacherVideoCountTools.buildTree(username);
        return JsonResult.success().set("countTree", tree);
    }

    /**
     * 播放视频时记录
     *  - 播放到80%时记录
     * @param params
     * @return
     */
    public JsonResult addRecord(String params) {
//        System.out.println(params);
        try {
            String username = JsonTools.getHeaderParams(params, "username"); //获取登陆用户名，即教师手机号码
            String courseTitle = JsonTools.getJsonParam(params, "courseTitle");
            Integer courseId = Integer.parseInt(JsonTools.getJsonParam(params, "courseId"));
            Integer totalTime = Integer.parseInt(JsonTools.getJsonParam(params, "totalTime"));//视频总时长，单位秒
            Long now = System.currentTimeMillis() - totalTime * 1000; //毫秒
            List<VideoRecord> list = videoRecordDao.findByUserAndTime(username, courseId, now);
            if(list==null || list.size()<=0) {
                Teacher t = teacherDao.findByPhone(username); //通过手机号码获取教师信息
                VideoRecord vr = new VideoRecord();
                vr.setCreateDate(NormalTools.curDate());
                vr.setCreateTime(NormalTools.curDatetime());
                vr.setCreateLong(System.currentTimeMillis());
                vr.setCourseId(courseId);
                vr.setCourseTitle(courseTitle);
                vr.setSchId(t.getSchoolId());
                vr.setSchName(t.getSchoolName());
                vr.setTeaId(t.getId());
                vr.setUsername(username);
                vr.setUserNickname(t.getName());
                vr.setVideoTotalTime(totalTime);
                vr.setNeedCount("1");
                videoRecordDao.save(vr);
                return JsonResult.succ(vr).set("flag", "1");
            } else {
                return JsonResult.success("已经记录").set("flag", "0");
            }
        } catch (NumberFormatException e) {
            return JsonResult.error(e.getMessage());
        }
    }
}
