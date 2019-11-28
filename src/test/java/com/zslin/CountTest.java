package com.zslin;

import com.zslin.basic.tools.NormalTools;
import com.zslin.bus.basic.dao.IActivityStudentDao;
import com.zslin.bus.basic.model.ActivityStudent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class CountTest {

    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Test
    public void test01() {
        Integer a = 1, b = 4;
        DecimalFormat df = new DecimalFormat("#.00");
        Double d = Double.parseDouble(df.format((a*1.0/b*1d)));
        System.out.println("------------------------->"+d);
    }

    DecimalFormat df = new DecimalFormat("#.00");

    @Test
    public void testCount() {
        List<ActivityStudent> list = activityStudentDao.listByHql("FROM ActivityStudent s WHERE s.stuName in (SELECT a.stuName FROM ActivityStudent a WHERE a.recordId=?1) ORDER BY s.stuName", 29);
        String name= "";
        int count = 0, passCount = 0, signCount = 0;
        int index = 0;
        List<CountDto> resultList = new ArrayList<>();
        for(ActivityStudent stu : list) {
            if(!stu.getStuName().equals(name)) { //如果姓名不同则换人
                if(!"".equals(name)) {
                   /*CountDto dto = new CountDto();
                   dto.setName(name);
                   dto.setPhone(stu.getPhone());
                   dto.setSignCount(signCount);
                   dto.setPassCount(passCount);
                   dto.setTotal(count);
                   dto.setSignPercent(passCount==0?0:Double.parseDouble(df.format((signCount*1.0d/passCount*1.0d))));*/

                   resultList.add(buildDto(name, stu.getPhone(), count, signCount, passCount));
                }
                name = stu.getStuName(); count = 0; passCount = 0;signCount=0;
            }
            count ++;
            if("1".equals(stu.getStatus())) {passCount ++;}
            if("1".equals(stu.getHasCheck())) {signCount++;}
//            System.out.println(stu);
            if((index++)>=list.size()-1) {
                System.out.println("===========这里需要再添加对象=============");
                resultList.add(buildDto(name, stu.getPhone(), count, signCount, passCount));
            }
        }

        System.out.println(resultList);

        for(CountDto dto : resultList) {
            System.out.println(dto.getName()+"\t"+dto.getPhone()+"\t"+
                    dto.getTotal()+"\t"+dto.getPassCount()+"\t"+
                    dto.getSignCount()+"\t"+dto.getSignPercent());
        }
        write2File(resultList);
    }

    private CountDto buildDto(String name, String phone, Integer total, Integer signCount, Integer passCount) {
        CountDto dto = new CountDto();
        dto.setName(name);
        dto.setPhone(phone);
        dto.setSignCount(signCount);
        dto.setPassCount(passCount);
        dto.setTotal(total);
        dto.setSignPercent(passCount==0?0:Double.parseDouble(df.format((signCount*1.0d/passCount*1.0d))));

        return dto;
    }

    private void write2File(List<CountDto> list) {
        BufferedWriter writer = null;
        String day = NormalTools.curDate("yyyyMMdd");
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:/temp/报名统计_"+day+".txt")), "UTF-8"));
            StringBuffer sb = new StringBuffer();
            String sep = "\t\t";

            sb.append("姓名").append(sep)
                    .append("联系电话").append(sep)
                    .append("报名次数").append(sep)
                    .append("通过次数").append(sep)
                    .append("签到次数").append(sep)
                    .append("签到率").append("\n");
            for(CountDto dto : list) {
                sb.append(dto.getName()).append(sep)
                .append(dto.getPhone()).append(sep)
                .append(dto.getTotal()).append(sep)
                .append(dto.getPassCount()).append(sep)
                .append(dto.getSignCount()).append(sep)
                .append(dto.getSignPercent()).append("\n");
            }
            writer.write(sb.toString());
            writer.flush();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class CountDto {
    private String name;
    private String phone;
    private Integer total;
    //通过次数
    private Integer passCount;
    //签到次数
    private Integer signCount;
    //签到率
    private Double signPercent;

    @Override
    public String toString() {
        return "CountDto{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", total=" + total +
                ", passCount=" + passCount +
                ", signCount=" + signCount +
                ", signPercent=" + signPercent +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    public Integer getSignCount() {
        return signCount;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public Double getSignPercent() {
        return signPercent;
    }

    public void setSignPercent(Double signPercent) {
        this.signPercent = signPercent;
    }
}