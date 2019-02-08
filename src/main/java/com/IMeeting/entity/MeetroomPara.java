package com.IMeeting.entity;

import java.util.List;

/**
 * Created by gjw on 2019/2/8.
 */
public class MeetroomPara {
    private Integer id;
    private String name;
    private String num;
    private String place;
    private Integer contain;
    private List<Integer>equips;
    private List<Integer>enables;
    private List<Integer>bans;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getContain() {
        return contain;
    }

    public void setContain(Integer contain) {
        this.contain = contain;
    }

    public List<Integer> getEquips() {
        return equips;
    }

    public void setEquips(List<Integer> equips) {
        this.equips = equips;
    }

    public List<Integer> getEnables() {
        return enables;
    }

    public void setEnables(List<Integer> enables) {
        this.enables = enables;
    }

    public List<Integer> getBans() {
        return bans;
    }

    public void setBans(List<Integer> bans) {
        this.bans = bans;
    }
}
