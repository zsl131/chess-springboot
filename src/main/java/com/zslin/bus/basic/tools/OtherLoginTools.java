package com.zslin.bus.basic.tools;

import com.zslin.bus.basic.dao.IOtherLoginDao;
import com.zslin.bus.basic.model.OtherLogin;
import com.zslin.bus.common.tools.RandomTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zsl on 2018/10/17.
 */
@Component
public class OtherLoginTools {

    @Autowired
    private IOtherLoginDao otherLoginDao;

    public OtherLogin onLogin(String username) {
        otherLoginDao.deleteByUsername(username); //先删除已有数据
        OtherLogin login = new OtherLogin();
        login.setUsername(username);
        login.setCreateLong(System.currentTimeMillis()/1000);
        login.setToken(RandomTools.randomString(12));
        otherLoginDao.save(login);
        return login;
    }
}
