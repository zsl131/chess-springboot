package com.zslin.bus.wx.tools;

import com.zslin.basic.tools.PinyinToolkit;
import com.zslin.bus.basic.dao.IScoreRuleDao;
import com.zslin.bus.basic.model.ScoreRule;
import com.zslin.bus.wx.annotations.HasScore;
import com.zslin.bus.wx.annotations.ScoreAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zsl on 2018/8/28.
 */
@Component
public class ScoreAnnotationTools {

    @Autowired
    private IScoreRuleDao scoreRuleDao;

    private String [] packages = new String []{"com/zslin/bus/*/service/*.class", "com/zslin/bus/*/tools/*.class"};

    public List<String> checkScore() {
        List<String> result = new ArrayList<>();
        for(String pn : packages) {
            result.addAll(buildScoreAnnotation(pn));
        }
        return result;
    }

    public List<String> findNoConfigScore() {
        List<String> result = new ArrayList<>();
        List<String> all = checkScore();
        for(String key : all) {
            ScoreRule sr = scoreRuleDao.findBySn(PinyinToolkit.cn2Spell(key, ""));
            if(sr==null) {
                result.add(key);
            }
        }
        return result;
    }

    /**
     * 遍历系统中的所有指定（AdminAuth）的资源
     * @param pn Controller所在路径，支持通配符
     */
    private List<String> buildScoreAnnotation(String pn) {
        List<String> result = new ArrayList<>();
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
//                System.out.println("---------"+cname);

                AnnotationMetadata am = mr.getAnnotationMetadata();
                if(am.hasAnnotation(HasScore.class.getName())) {
//                    System.out.println("===="+cname);
                    Set<MethodMetadata> set = am.getAnnotatedMethods(ScoreAnnotation.class.getName());
                    for(MethodMetadata m : set) {
                        Map<String, Object> tma = m.getAnnotationAttributes(ScoreAnnotation.class.getName());
//                        System.out.println(tma.get("name")+"--"+tma.get("keys"));
                        result.add(tma.get("value").toString());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getSn(String name) {
        return PinyinToolkit.cn2Spell(name, "");
    }
}
