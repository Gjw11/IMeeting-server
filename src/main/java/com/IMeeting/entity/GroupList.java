package com.IMeeting.entity;

import java.util.List;

/**
 * Created by gjw on 2018/12/13.
 */
public class GroupList {
    private Integer groupId;
    private String name;
    private List<Integer>userIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
