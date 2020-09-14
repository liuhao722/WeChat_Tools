package com.nextjoy.clicker.entity;

import java.util.ArrayList;
import java.util.List;

public class WXPraiseXY {
    public String name;//发表人的名称
    public int x;
    public int y;
    public List<String> nameList = new ArrayList<>();// 点赞列表内点赞人的全部数量

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("name=" + name + ",x=" + x +
                ", y=" + y + ", nameList=");
        if (nameList != null) {
            for (int i = 0; i < nameList.size(); i++) {
                sb.append("name" + i + nameList.get(i) + "\t");
            }
        }
        return sb.toString();
    }
}
