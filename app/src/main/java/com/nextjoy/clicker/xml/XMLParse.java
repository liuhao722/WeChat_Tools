package com.nextjoy.clicker.xml;

import android.text.TextUtils;
import android.util.Xml;

import com.nextjoy.clicker.entity.WXPraiseXY;
import com.nextjoy.clicker.utils.WXID;
import com.nextjoy.clicker.utils.WXUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hao on 2017/4/1.
 */

public class XMLParse {
    private static final XMLParse ourInstance = new XMLParse();

    public static XMLParse ins() {
        return ourInstance;
    }

    private XMLParse() {
    }

    public List<WXPraiseXY> parse(InputStream is) throws Exception {
        List<WXPraiseXY> mList = new ArrayList<>(); // 初始化books集合
        XmlPullParser xpp = Xml.newPullParser();
        // 设置输入流 并指明编码方式
        xpp.setInput(is, "UTF-8");
        // 产生第一个事件
        int eventType = xpp.getEventType();
        boolean hasChild = false;
        List<String> nameList = new ArrayList<>();
        String userName = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals(WXID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, WXID.resourceId)) && xpp.getAttributeValue(null, WXID.resourceId).equals(WXID.userName)) { // 判断开始标签元素是否是book
                        userName = xpp.getAttributeValue(null, WXID.text);
                    }
                    //微信朋友圈
                    if (xpp.getName().equals(WXID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, WXID.resourceId)) && xpp.getAttributeValue(null, WXID.resourceId).equals(WXID.praiseId)) { // 判断开始标签元素是否是book
                        WXPraiseXY entity = WXUtils.ins().parsePraiseXY(xpp.getAttributeValue(null, WXID.bounds));
                        if (!TextUtils.isEmpty(userName)){
                            entity.name = userName;
                            userName = "";
                        }
                        mList.add(entity);
                    }
                    if (xpp.getName().equals(WXID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, WXID.resourceId)) && xpp.getAttributeValue(null, WXID.resourceId).equals(WXID.praiselist)) { // 判断开始标签元素是否是book
                        String index = xpp.getAttributeValue(null, WXID.index);
                        if (!TextUtils.isEmpty(index)) {
                            if (Integer.parseInt(index) > 1) {
                                hasChild = true;
                            }
                        }
                    } else {//已经结束了遍历点赞列表的集合
                        if (hasChild) {
                            if (xpp.getName().equals(WXID.node) && !TextUtils.isEmpty(xpp.getAttributeValue(null, WXID.resourceId)) && xpp.getAttributeValue(null, WXID.resourceId).equals(WXID.praiselistItem)) { // 判断开始标签元素是否是book
                                //判断当前点赞里面的集合 根据index进行判断是否已经结尾了
                                String name = xpp.getAttributeValue(null, WXID.text);
                                nameList.add(name);
                            } else {
                                //获取最后一个item进行添加
                                if (mList.size() > 0 && nameList.size() > 0) {
                                    WXPraiseXY lastItem = mList.get(mList.size() - 1);
                                    for (int i = 0; i < nameList.size(); i++) {
                                        lastItem.nameList.add(nameList.get(i));
                                    }
                                    mList.set(mList.size() - 1, lastItem);
                                }
                                nameList.clear();
                                hasChild = false;
                            }
                        }
                    }

                    break;
                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        return mList;
    }

}
