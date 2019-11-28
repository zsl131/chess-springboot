package com.zslin;

import com.zslin.bus.wx.annotations.HasTemplateMessage;
import com.zslin.bus.wx.annotations.TemplateMessageAnnotation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsl on 2018/8/25.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class TempMessageTools {

    @Test
    public void test01() {
        buildSystemMenu();
    }

    /**
     * 遍历系统中的所有指定（AdminAuth）的资源
     */
    private void buildSystemMenu() {
        //com.zslin.bus.wx.service
        String pn = "com/zslin/bus/wx/service/FeedbackService.class";
        buildSystemMenu(pn);
    }

    /**
     * 遍历系统中的所有指定（AdminAuth）的资源
     * @param pn Controller所在路径，支持通配符
     */
    private void buildSystemMenu(String pn) {
        try {
            //指定需要检索Annotation的路径，可以使用通配符
//			String pn = "com/zslin/*/controller/*/*Controller.class";
            //1、创建ResourcePatternResolver资源对象
            ResourcePatternResolver rpr = new PathMatchingResourcePatternResolver();
            //2、获取路径中的所有资源对象
            Resource[] ress = rpr.getResources(pn);
            //3、创建MetadataReaderFactory来获取工程
            MetadataReaderFactory fac = new CachingMetadataReaderFactory();
            //4、遍历资源
            for(Resource res:ress) {
                MetadataReader mr = fac.getMetadataReader(res);
                String cname = mr.getClassMetadata().getClassName();
                System.out.println("---------"+cname);

//                Method [] methods = mr.getClassMetadata().getClass().getMethods();
                /*Method [] methods = FeedbackService.class.getMethods();
                for(Method m : methods) {
                    System.out.println("name::"+m.getName() +"---->"+m.getDeclaredAnnotations().length+"---"+m.getAnnotations().length);
                    System.out.println("\t\t"+m.getAnnotationsByType(TemplateMessageAnnotation.class).length + "-=-=-="+m.getDeclaredAnnotationsByType(TemplateMessageAnnotation.class).length);
                    System.out.println(m.toString()+"--->"+m.toGenericString());
                }*/
                AnnotationMetadata am = mr.getAnnotationMetadata();
                if(am.hasAnnotation(HasTemplateMessage.class.getName())) {
                    System.out.println("===="+cname);
                    Set<MethodMetadata> set = am.getAnnotatedMethods(TemplateMessageAnnotation.class.getName());
                    for(MethodMetadata m : set) {
                        Map<String, Object> tma = m.getAnnotationAttributes(TemplateMessageAnnotation.class.getName());
                        System.out.println(tma.get("name")+"--"+tma.get("keys"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
