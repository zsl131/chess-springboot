package com.zslin.bus.wx.tools;

import com.zslin.basic.repository.SimpleSortBuilder;
import com.zslin.bus.wx.dao.IWxMenuDao;
import com.zslin.bus.wx.dto.WxMenuDto;
import com.zslin.bus.wx.dto.WxMenuTreeDto;
import com.zslin.bus.wx.model.WxMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zsl on 2018/7/20.
 */
@Component
public class WxMenuTools {

    @Autowired
    private IWxMenuDao wxMenuDao;

    public List<WxMenuDto> buildMenuTree() {
        List<WxMenuDto> result = new ArrayList<>();
        Sort sort = SimpleSortBuilder.generateSort("orderNo");
        List<WxMenu> rootList = wxMenuDao.findRootMenu(sort);
        for(WxMenu m : rootList) {
            List<WxMenu> children = wxMenuDao.findByParent(m.getId(), sort);
            result.add(new WxMenuDto(m, children));
        }
        return result;
    }
}
