package com.zslin.bus.wx.dto;

import com.zslin.bus.wx.model.WxMenu;

import java.util.List;

/**
 * Created by zsl on 2018/7/21.
 */
public class WxMenuDto {

    private WxMenu menu;

    private List<WxMenu> children;

    public WxMenuDto() {
    }

    public WxMenuDto(WxMenu menu, List<WxMenu> children) {
        this.menu = menu;
        this.children = children;
    }

    public WxMenu getMenu() {
        return menu;
    }

    public void setMenu(WxMenu menu) {
        this.menu = menu;
    }

    public List<WxMenu> getChildren() {
        return children;
    }

    public void setChildren(List<WxMenu> children) {
        this.children = children;
    }
}
