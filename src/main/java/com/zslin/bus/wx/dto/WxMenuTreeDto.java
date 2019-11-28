package com.zslin.bus.wx.dto;

import com.zslin.bus.wx.model.WxMenu;

import java.util.List;

/**
 * Created by zsl on 2018/7/21.
 */
public class WxMenuTreeDto {

    private List<WxMenuDto> treeList;

    private List<WxMenu> menuList;

    public WxMenuTreeDto() {
    }

    public WxMenuTreeDto(List<WxMenuDto> treeList, List<WxMenu> menuList) {
        this.treeList = treeList;
        this.menuList = menuList;
    }

    public List<WxMenuDto> getTreeList() {
        return treeList;
    }

    public void setTreeList(List<WxMenuDto> treeList) {
        this.treeList = treeList;
    }

    public List<WxMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<WxMenu> menuList) {
        this.menuList = menuList;
    }
}
