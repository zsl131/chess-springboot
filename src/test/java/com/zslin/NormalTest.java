package com.zslin;

import com.alibaba.fastjson.JSON;
import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.tools.NormalTools;
import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.basic.tools.SecurityUtil;
import com.zslin.bus.basic.dao.*;
import com.zslin.bus.basic.dto.ActivityRecordImageDto;
import com.zslin.bus.basic.dto.NormalDto;
import com.zslin.bus.basic.model.ActivityRecordImage;
import com.zslin.bus.basic.model.Department;
import com.zslin.bus.basic.model.Notice;
import com.zslin.bus.basic.model.Student;
import com.zslin.bus.common.dto.QueryListConditionDto;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.iservice.IApiTokenService;
import com.zslin.bus.common.model.ApiToken;
import com.zslin.bus.common.rabbit.RabbitMQConfig;
import com.zslin.bus.common.tools.*;
import com.zslin.bus.qiniu.dto.MyPutRet;
import com.zslin.bus.qiniu.tools.QiniuUploadTools;
import com.zslin.bus.tools.ExportStudentTools;
import com.zslin.bus.tools.JsonResult;
import com.zslin.bus.wx.dto.SendMessageDto;
import com.zslin.bus.wx.tools.AccessTokenTools;
import com.zslin.bus.wx.tools.InternetTools;
import com.zslin.bus.wx.tools.JSApiTools;
import com.zslin.bus.wx.tools.TemplateMessageTools;
import com.zslin.bus.yard.dao.*;
import com.zslin.bus.yard.model.ClassImage;
import com.zslin.bus.yard.model.ClassSystemDetail;
import com.zslin.bus.yard.model.Grade;
import com.zslin.bus.yard.model.TeacherClassroom;
import com.zslin.bus.yard.tools.MyFileTools;
import com.zslin.bus.yard.tools.TeachPlanTools;
import com.zslin.test.ImageHandleTools;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zsl on 2018/7/5.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class NormalTest {

    @Autowired
    private BuildApiCodeTools buildApiCodeTools;

    @Autowired
    private IApiTokenService apiTokenService;

    @Autowired
    private BuildAdminMenuTools buildAdminMenuTools;

    @Autowired
    private IDepUserDao depUserDao;

    @Autowired
    private IActivityApplyRecordDao activityApplyRecordDao;

    @Autowired
    private IDepartmentDao departmentDao;

    @Autowired
    private INoticeDao noticeDao;

    @Autowired
    private AccessTokenTools accessTokenTools;

    @Autowired
    private JSApiTools jsApiTools;

    @Autowired
    private ExportStudentTools exportStudentTools;

    @Autowired
    private QiniuUploadTools qiniuUploadTools;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @Autowired
    private ITeacherRoleDao teacherRoleDao;

    @Autowired
    public IGradeRoleSystemDao gradeRoleSystemDao;

    @Autowired
    private TemplateMessageTools templateMessageTools;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private IClassImageDao classImageDao;

    @Autowired
    private ImageHandleTools imageHandleTools;

    @Autowired
    private IActivityRecordImageDao activityRecordImageDao;

    @Autowired
    private ITeacherClassroomDao teacherClassroomDao;

    @Autowired
    private TeachPlanTools teachPlanTools;

    @Test
    public void test40() {
        teachPlanTools.buildIndex(38);
    }

    @Test
    public void test39() {
        List<TeacherClassroom> list = teacherClassroomDao.queryByCourseId(142, "2021", 38);
        for(TeacherClassroom c : list) {
            System.out.println(c);
        }
    }

    @Test
    public void test38() {
        /*Page<ActivityRecordImageDto> list = activityRecordImageDao.find4Page(SimplePageBuilder.generate(0));
        System.out.println(list.getTotalPages());
        System.out.println(list.getTotalElements());
        System.out.println(list.getContent());*/
    }

    @Test
    public void test37() throws Exception {
        imageHandleTools.process(500);
    }

    @Test
    public void test36() {
        List<ClassImage> list = classImageDao.findByTea("15925061256", "2020", 1, SimpleSortBuilder.generateSort("id"));
        System.out.println("-----------size::: "+list.size());
        for(ClassImage c : list) {
            System.out.println(c);
        }
    }

    @Test
    public void test35() {
        SendMessageDto smd = new SendMessageDto("活动审核结果通知", "o4Jhl0nkSmiRIqU9JkWIm2lj6qXE", "/wx/activityRecord/signUp?recordId=", "标题",
                TemplateMessageTools.field("活动主题", "活动主题"),
                TemplateMessageTools.field("活动时间", "活动时间"),
                TemplateMessageTools.field("审核结果", "驳回"),
                TemplateMessageTools.field("注意事项：测试注意事项"));
        rabbitTemplate.convertAndSend(RabbitMQConfig.DIRECT_EXCHANGE, RabbitMQConfig.DIRECT_ROUTING, smd);
    }

    @Test
    public void test34() {
        /*templateMessageTools.sendMessageByThread("活动审核结果通知", "o4Jhl0nkSmiRIqU9JkWIm2lj6qXE", "/wx/activityRecord/signUp?recordId=", "标题",
                TemplateMessageTools.field("活动主题", "活动主题"),
                TemplateMessageTools.field("活动时间", "活动时间"),
                TemplateMessageTools.field("审核结果", "驳回"),
                TemplateMessageTools.field("注意事项：测试注意事项"));*/
    }

    @Test
    public void test33() {
        String token = accessTokenTools.getAccessToken();
        System.out.println(token);
    }

    @Test
    public void test32() {
        String str = "\\publicFile\\upload\\20190923\\d48f3e7b-5025-4479-983e-9af252646631.jpg";
        System.out.println(str);
        System.out.println(str.replaceAll("\\\\", "/"));
    }

    @Test
    public void test31() {
        String phone = "15925061";
        List<Grade> list = teacherRoleDao.findRoleByPhone(phone);
        for(Grade g : list) {
            System.out.println(g.getName());
        }
        System.out.println("-----------------end-----------");
    }

    @Test
    public void test30() {
        String phone = "15925061256";
        String str = phone.substring(phone.length()-6);
        System.out.println("---->"+str);
    }

    @Test
    public void test29() {
        List<Integer> list = gradeRoleSystemDao.findSystemIdByPhone("15925061256");
        for(Integer id : list) {
            System.out.println("-------->"+id);
        }
    }

    @Test
    public void test28() {
        List<Integer> list = teacherRoleDao.findRoleIdByPhone("15925061256");
        System.out.println("==========size:::"+list.size());
        for(Integer id : list) {
            System.out.println("-------->"+id);
        }
    }

    @Test
    public void test27() throws Exception {
        FileOutputStream fos = new FileOutputStream(new File("D:/temp/ttt-111.xlsx"));
        List<ClassSystemDetail> data = classSystemDetailDao.findAll();
        exportStudentTools.export("测试标题", "工作表名称", data, new String[]{"体系","体系", "名称", "章节","课程"}, new String[]{"spname", "sname", "name", "sectionNo", "courseTitle"}, fos);

    }

    @Test
    public void test26() throws Exception {
        File f = new File("D:/temp/test.mp4");
        FileInputStream fis = new FileInputStream(f);
        String key = "myVideo.mp4";
        MyPutRet mpr = qiniuUploadTools.upload(fis, key);
        System.out.println(mpr);
        Long l = MyFileTools.getVideoTimeLength(f);
        System.out.println("======="+l);
    }

    @Test
    public void test25() throws Exception {
        FileOutputStream fos = new FileOutputStream(new File("D:/temp/ttt-new.xlsx"));
        exportStudentTools.exportStudent(null, fos);
    }

    @Test
    public void test24() throws Exception {
        /**
         * 注意这只是07版本以前的做法对应的excel文件的后缀名为.xls
         * 07版本和07版本以后的做法excel文件的后缀名为.xlsx
         */
        //创建新工作簿
        HSSFWorkbook workbook = new HSSFWorkbook();
        //新建工作表
        HSSFSheet sheet = workbook.createSheet("工作表");
        //创建行,行号作为参数传递给createRow()方法,第一行从0开始计算
        HSSFRow row = sheet.createRow(0);
        //创建单元格,row已经确定了行号,列号作为参数传递给createCell(),第一列从0开始计算
        HSSFCell cell = row.createCell(2);
        //设置单元格的值,即C1的值(第一行,第三列)
        cell.setCellValue("中文试试看");
        //输出到磁盘中
        FileOutputStream fos = new FileOutputStream(new File("D:/temp/ttt.xls"));
        workbook.write(fos);
        fos.close();
    }

    @Test
    public void test23() throws Exception {
        String str = "{'phone':'15925061256','mid':2,'con':'#code#=12345'}";
        System.out.println(URLEncoder.encode(str, "utf-8"));
    }

    @Test
    public void test22() {
        JsonResult jr = jsApiTools.buildJSTicket("http://www.baidu.com");
        System.out.println(jr.toString());
    }

    @Test
    public void test21() {
        String jsToken = accessTokenTools.getJsTicket();
        System.out.println(jsToken);
    }

    @Test
    public void test20() {
        String str = "张三（男）";
        String res = PinyinToolkit.cn2Spell(str, "_");
        System.out.println("========"+res);
    }

    @Test
    public void test19() {
        Map<String, Object> params = new HashMap<>();
        params.put("grant_type", "client_credential");
        params.put("appid", "wx37b74acce5f1cdef");
        params.put("secret", "6c16e634996db6738fa9809b79d053dd");
        String result = InternetTools.doGet("https://api.weixin.qq.com/cgi-bin/token", params);
        System.out.println("==="+result);
        String token = JsonTools.getJsonParam(result, "access_token");
        System.out.println(token);
    }

    @Test
    public void test18() {
        for(int i=0;i<100;i++) {
            Notice n = new Notice();
            n.setCreateDay(NormalTools.curDate("yyyy-MM-dd"));
            n.setCreateTime(NormalTools.curDatetime());
            n.setContent(i*25+"<p>通知公告标题部份111</p><p>通知公告标题部份111</p><p>通知公告标题部份通知公告标题部份通知公告标题部份通知公告标题部份</p><p>通知公告标题部份</p><p>通知公告标题部份</p><p>通知公告标题部份</p><p><br></p>");
            n.setGuide(i*25+"通知公告标题部份通知公告标题部份通知公告标题部份通知公告标题部份通知公告标题部份111");
            n.setPicPath("\\publicFile\\upload\\20180915\\d888728e-a1a5-4b7a-9b68-3a13ed925760.jpg");
            n.setStatus("1");
            n.setTitle(i+"通知公告标题部份2222");
            noticeDao.save(n);
        }
    }

    @Test
    public void test17() throws  Exception {
        String url = "http://zsl.nat300.top/weixin/root";
        System.out.println(URLEncoder.encode(url, "utf-8"));
    }

    @Test
    public void test16() {
        String fileName = "abc.mp4";
        System.out.println(MyFileTools.isVideoFile(fileName));
    }

    @Test
    public void test15() {
        File f = new File("F:\\视频教程\\小程序\\1.0 课程简介.mp4");
        System.out.println(MyFileTools.getVideoTimeLength(f));
    }

    @Test
    public void test14() {
        String query = "{\"headerParams\":{\"depids\":\"2,1\",\"isadminuser\":\"1\",\"userid\":\"1\",\"username\":\"root\"},\"page\":0,\"conditions\":\"{\\\"schoolName_like\\\":\\\"三\\\",\\\"name_like\\\":\\\"李\\\"}\"}";
        QueryListDto qld = QueryTools.buildQueryListDto(query);
        for(QueryListConditionDto dto : qld.getConditionDtoList()) {
            System.out.println(dto.getKey()+"="+dto.getMatch()+"="+dto.getValue());
        }
    }

    @Test
    public void test13() {
        Map<String, String> map = new HashMap<>();
        map.put("a", "aaaaa");
        map.put("b", "bbbbb");
        map.put("c", "ccccc");

        System.out.println("a::"+map.get("a"));
        System.out.println("d::"+map.get("d"));
    }

    @Test
    public void test12() {
        String str = "2,1";
        List<Department> list = departmentDao.findByIds(2,1);
        for(Department d : list) {
            System.out.println(d.getName() + "====" + d.getId());
        }
    }

    @Test
    public void test11() {
        Student stu = new Student();
        stu.setName("张三");
        String str = JSON.toJSONString(stu);
        System.out.println(str);
    }

    @Test
    public void test10() {
        Integer count = activityApplyRecordDao.applyCount(2);
        System.out.println("count:::"+count);
    }

    @Test
    public void test09() {
        JsonResult jsonResult = JsonResult.getInstance().fail("出错").set("message", "消息").set("size", 123);
        String str = JSON.toJSONString(jsonResult);
        System.out.println(str);
    }

    @Test
    public void test08() {
        NormalDto ndt = NormalDto.getInstance();
        ndt.set("message", "这里是Message");

        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        ndt.set("list", list);

        String str = JSON.toJSONString(ndt);
        System.out.println(str);

    }

    @Test
    public void test07() {
        String url = "http://zsl.nat300.top/wx/activity?a=1&b=3";
        String newUrl = rebuildUrl(url);
        System.out.println("url::"+url+"\nnewUrl::"+newUrl);
    }

    private String rebuildUrl(String url) {
        String tempStr = "_temp_str_";
        String tempUrl = url.replace("://", tempStr);
        if(tempUrl.indexOf("/")<0) {
            return tempUrl.replace(tempStr, "://") + "/wxRemote/queryAccount";
        } else {
            return tempUrl.substring(0, tempUrl.indexOf("/")).replace(tempStr, "://") + "/wxRemote/queryAccount";
        }
    }

    @Test
    public void test06() throws Exception {
        String psd = SecurityUtil.md5("root", "111111");
        System.out.println(psd);
    }

    @Test
    public void test05() {
        List<Department> list = depUserDao.findDepartmentByUserId(3);
        for(Department d : list) {
            System.out.println(d.getName());
        }
    }

    @Test
    public void test04() throws Exception {
        String str = "JTdCJTIyaWQlMjI6MSwlMjJuYW1lJTIyOiUyMiVFNiU5RCU4MyVFOSU5OSU5MCVFNyVBRSVBMSVFNyU5MCU4NiUyMiwlMjJvcmRlck51bSUyMjoxLCUyMmhyZWYlMjI6JTIyIyUyMiwlMjJpY29uJTIyOiUyMiUyMiU3RA==";
        System.out.println(getFromBase64(str));
    }

    public static String getFromBase64(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Test
    public void test02() {
        String str = "{\"uid\":1,\"rids\":[1,4,3]}";
        String uid = JsonTools.getJsonParam(str, "uid");
        System.out.println("uid:::"+uid);
        com.alibaba.fastjson.JSONArray array = JSON.parseArray(JsonTools.getJsonParam(str, "rids"));
        for(Integer i=0;i<array.size();i++) {
            Integer rid = array.getInteger(i);
            System.out.println("rid:::"+rid);
        }
    }

    @Test
    public void test01() {
        buildAdminMenuTools.buildAdminMenus();
    }

    @Test
    public void testBuildApiCode() {
        buildApiCodeTools.buildApiCode();
    }

    @Test
    public void testInitSuperToken() {
        ApiToken at = new ApiToken();
        at.setClientName("Super客户端");
        at.setIsSuper("1");
        at.setStatus("1");
        at.setToken(RandomTools.randomString(16));
        apiTokenService.save(at);
    }
}
