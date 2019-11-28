package com.zslin;

import com.zslin.test.AESUtil;
import com.zslin.test.AesEncryptUtil;
import com.zslin.test.SHA;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zsl on 2018/8/10.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class PasswordTest {

    @Test
    public void test01() throws Exception {
        String str = "abcdef";
        String password = SHA.encryptSHA(str);
        System.out.println("result:::"+password);
    }

    @Test
    public void test02() {
        String s1 = AESUtil.encrypt("111111112222", "");
        System.out.println("s1:" + s1);

//        System.out.println("s2:"+AESUtil.decrypt(s1, "abcdef"));
    }

    @Test
    public void test03() throws Exception {
        String test = "111111112222";

        String data ;
        String key = "dufy20170329java";
        String iv = "dufy20170329java";

        data = AesEncryptUtil.encrypt(test);

        System.out.println(data);
        System.out.println(AesEncryptUtil.desEncrypt(data));
    }
}
