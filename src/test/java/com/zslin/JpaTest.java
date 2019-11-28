package com.zslin;

import com.zslin.basic.repository.SimplePageBuilder;
import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.basic.repository.SimpleSpecificationBuilder;
import com.zslin.basic.repository.SpecificationOperator;
import com.zslin.bus.basic.dao.IActivityCommentDao;
import com.zslin.bus.basic.dao.IActivityDao;
import com.zslin.bus.basic.dao.IActivityStudentDao;
import com.zslin.bus.basic.model.Activity;
import com.zslin.bus.basic.model.ActivityComment;
import com.zslin.bus.basic.model.ActivityStudent;
import com.zslin.bus.common.dto.QueryListDto;
import com.zslin.bus.common.tools.QueryTools;
import com.zslin.bus.finance.dao.IFinanceDetailDao;
import com.zslin.bus.finance.dto.FinanceCountDto;
import com.zslin.bus.yard.dao.IClassCourseDao;
import com.zslin.bus.yard.dao.IClassSystemDao;
import com.zslin.bus.yard.dao.IClassSystemDetailDao;
import com.zslin.bus.yard.dao.IClassTagDao;
import com.zslin.bus.yard.model.ClassCourse;
import com.zslin.bus.yard.model.ClassSystem;
import com.zslin.bus.yard.model.ClassSystemDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zsl on 2018/8/6.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class JpaTest {

    @Autowired
    private IActivityCommentDao activityCommentDao;

    @Autowired
    private IFinanceDetailDao financeDetailDao;

    @Autowired
    private IActivityDao activityDao;

    @Autowired
    private IClassSystemDao classSystemDao;

    @Autowired
    private IClassSystemDetailDao classSystemDetailDao;

    @Autowired
    private IClassCourseDao classCourseDao;

    @Autowired
    private IActivityStudentDao activityStudentDao;

    @Autowired
    private IClassTagDao classTagDao;

    @Test
    public void test09() {
        System.out.println(1+"===>"+classTagDao.findCids(1).size());
        System.out.println(2+"===>"+classTagDao.findCids(2).size());
        System.out.println(3+"===>"+classTagDao.findCids(3).size());
    }

    @Test
    public void test08() {
        List<ActivityStudent> list = activityStudentDao.listByHql("FROM ActivityStudent s ORDER BY s.stuName");
        for(ActivityStudent stu : list) {
            System.out.println(stu);
        }
    }

    @Test
    public void test07() {
        Integer gid = 3, page = 0;
        SimpleSpecificationBuilder ssb = new SimpleSpecificationBuilder("sid", "eq", gid);
        Page<ClassSystemDetail> datas = classSystemDetailDao.findAll(ssb.generate(), SimplePageBuilder.generate(page, SimpleSortBuilder.generateSort("orderNo", "sectionNo")));
        List<ClassCourse> res = buildData(datas.getContent());
        for(ClassCourse cc : res) {
            //System.out.println(cc.getTitle());
        }
    }

    private List<ClassCourse> buildData(List<ClassSystemDetail> detailList) {
        if(detailList==null || detailList.size()<=0) {return new ArrayList<>();}
        List<Integer> ids = detailList.stream().map(ClassSystemDetail::getCourseId).collect(Collectors.toList());
        for(Integer id : ids) {
            System.out.print(id+" ， ");
        }
        List<ClassCourse> list = classCourseDao.findByIds(ids);
        List<ClassCourse> result = new ArrayList<>();
        for(ClassSystemDetail csd : detailList) { //排序
            System.out.print("detailTitle:"+csd.getName()+"--->");
            for(ClassCourse cc : list) {
                if(csd.getCourseId().equals(cc.getId())) {
                    System.out.println("dcID:"+csd.getCourseId()+",dcTitle:"+csd.getCourseTitle()+",cID:"+cc.getId()+",cTitle:"+cc.getTitle());
                    result.add(cc);
                }
            }
        }
        return result;
    }

    @Test
    public void test06() {
        String phone = "15925061256";
        Sort sort = SimpleSortBuilder.generateSort("orderNo_a");
        List<ClassSystem> list = classSystemDao.findByPhone(phone, sort);
        for(ClassSystem cs : list) {
            System.out.println(cs.getName());
        }
    }

    @Test
    public void test05() {
        List<Activity> list = activityDao.listByHql("FROM Activity a WHERE a.status='1' ORDER BY a.id DESC LIMIT 10");
        for(Activity a : list) {
            System.out.println(a.getTitle()+"---------->"+a.getId());
        }
    }

    @Test
    public void test04() {
        List<FinanceCountDto> list = financeDetailDao.findCountGroupByMonth("2018", "-1");
        for(FinanceCountDto dto : list) {
            System.out.println(dto);
        }
    }

    @Test
    public void test03() {
        QueryListDto qld = new QueryListDto(0, 2, "id_d");

        Integer [] ids = new Integer[]{1, 2, 3};

        String openid = "orLIDuFuyeOygL0FBIuEilwCF1lU1234";
        Page<ActivityComment> res = activityCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                new SpecificationOperator("id", "in", ids)),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        System.out.println(res.getTotalElements());
    }

    @Test
    public void test01() {

        QueryListDto qld = new QueryListDto(0, 2, "id_d");

        String openid = "orLIDuFuyeOygL0FBIuEilwCF1lU1234";

        Page<ActivityComment> res = activityCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                new SpecificationOperator("actId", "eq", 3),
                new SpecificationOperator("status", "eq", "1", new SpecificationOperator("openid", "eq", openid, "or")) //状态为显示
//                    , new SpecificationOperator("openid", "eq", openid, "or") //暂时去掉此功能
                ),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        System.out.println(res.getTotalElements());
    }

    @Test
    public void test02() {
        String openid = "orLIDuFuyeOygL0FBIuEilwCF1lU";
        QueryListDto qld = new QueryListDto(0, 2, "id_d");
        Page<ActivityComment> res = activityCommentDao.findAll(QueryTools.getInstance().buildSearch(qld.getConditionDtoList(),
                new SpecificationOperator("actId", "eq", 3) //活动详情Id
                , new SpecificationOperator("status", "eq", "1",  new SpecificationOperator("openid", "eq", openid, "or")) //状态为显示
//                    , new SpecificationOperator("openid", "eq", openid, "or") //或openid为当前用户， 暂时去掉此功能
                ),
                SimplePageBuilder.generate(qld.getPage(), qld.getSize(), SimpleSortBuilder.generateSort(qld.getSort())));

        System.out.println(res.getTotalElements());
    }
}
