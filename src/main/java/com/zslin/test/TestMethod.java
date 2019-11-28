package com.zslin.test;

/**
 * Created by zsl on 2018/8/9.
 */
public class TestMethod {

    public String f1(String str) {
        return "function 1 :: "+ str;
    }

    public String f2(String ...args) {
        StringBuffer sb = new StringBuffer();
        sb.append("result :: ");
        for(String arg : args) {
            sb.append(arg);
        }
        return sb.toString();
    }

    public String f3(String arg) {
        return "f3 result :: "+arg;
    }
}
