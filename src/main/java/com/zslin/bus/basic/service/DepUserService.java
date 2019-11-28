package com.zslin.bus.basic.service;

import com.zslin.bus.basic.dao.IDepUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zsl on 2018/7/19.
 */
@Service
public class DepUserService {

    @Autowired
    private IDepUserDao depUserDao;
}
