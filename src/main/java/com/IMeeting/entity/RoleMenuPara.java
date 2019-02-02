package com.IMeeting.entity;

import java.util.List;

/**
 * Created by gjw on 2019/2/2.
 */
public class RoleMenuPara {
    private Integer roleId;
    private String roleName;
    private List<Integer>menus;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Integer> getMenus() {
        return menus;
    }

    public void setMenus(List<Integer> menus) {
        this.menus = menus;
    }
}
