package com.zslin.bus.wx.dao;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.bus.wx.model.WxMenu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by zsl on 2018/7/20.
 */
public interface IWxMenuDao extends BaseRepository<WxMenu, Integer> {

    @Query("FROM WxMenu m WHERE m.status='1' AND (m.pid IS NULL OR m.pid=0)")
    List<WxMenu> findRootMenu(Sort sort);

    @Query("FROM WxMenu m WHERE m.status='1' AND m.pid=?1")
    List<WxMenu> findByParent(Integer pid, Sort sort);
}
