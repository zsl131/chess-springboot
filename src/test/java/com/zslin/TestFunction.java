package com.zslin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by zsl on 2018/8/9.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class TestFunction {

    @Test
    public void test01() throws Exception {
        //1.加载Class对象
        Class clazz = Class.forName("com.zslin.test.TestMethod");

        Constructor constructor = clazz.getConstructor();

        Object obj = constructor.newInstance();

        Method m = clazz.getMethod("f1", String.class);
        String result = (String) m.invoke(obj, "aaa");
        System.out.println(result);
    }

    @Test
    public void test02() throws Exception {
        //1.加载Class对象
        Class clazz = Class.forName("com.zslin.test.TestMethod");

        Constructor constructor = clazz.getConstructor();

        Object obj = constructor.newInstance();

        String [] params = {"a", "b"};
        Object[] cParams = {params};

        Method m = clazz.getMethod("f2", String[].class);
        String result = (String) m.invoke(obj, cParams);
        System.out.println(result);
    }

    @Test
    public void test03() throws Exception {
        //1.加载Class对象
        Class clazz = Class.forName("com.zslin.test.TestMethod");

        Constructor constructor = clazz.getConstructor();

        Object obj = constructor.newInstance();

        String [] params = {"a"};
        Object[] cParams = {params};

        Method m = clazz.getMethod("f3", String[].class);
        String result = (String) m.invoke(obj, cParams);
        System.out.println(result);
    }
}
