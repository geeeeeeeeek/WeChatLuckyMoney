package xyz.monkeytong.hongbao.services;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by banxi on 2/1/17.
 *  AccessibilityNodeInfo 查询辅助类
 */
 class NodeQuery{
    /**
     * @param source 要开始查找的节点
     * @param className 要查找的节点 View 的类名
     * @return source 节点的符合对应类名的直接子节点.
     */
    @NonNull
    static List<AccessibilityNodeInfo> children(AccessibilityNodeInfo source, String className){
        final  int childCount = source.getChildCount();
        ArrayList<AccessibilityNodeInfo> list = new ArrayList<>();
        for (int index = 0; index < childCount; index++) {
            AccessibilityNodeInfo node = source.getChild(index);
            if(node.getClassName().equals(className)){
                list.add(node);
            }
        }
        return list;
    }

    /**
     * @param source 要开始查找的节点
     * @param className 要查找的节点View 的类名
     * @return 符合条件的第一个子节点, 深度优先的搜索.
     */
    @Nullable
    static AccessibilityNodeInfo query(AccessibilityNodeInfo source, String className){
        if (source.getClassName().equals(className)) {
            return source;
        }
        final  int childCount = source.getChildCount();
        for (int index = 0; index < childCount; index++) {
            AccessibilityNodeInfo node = query(source.getChild(index), className);
            if (node != null) {
                return  node;

            }
        }
        return null;
    }

    /**
     * @param source 要开始查找的节点
     * @param text 要查找的节点View 要包含的 Text
     * @return 符合条件的第一个子节点,使用系统 `findAccessibilityNodeInfosByText` 的 API 来搜索
     */
    @Nullable
    static AccessibilityNodeInfo queryByText(AccessibilityNodeInfo source, String text){
        List<AccessibilityNodeInfo> list =  source.findAccessibilityNodeInfosByText(text);
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.get(0);
    }

    /**
     * @param source 要开始查找的节点
     * @param contentDescription 要查找的节点View 要包含的 contentDescription
     * @return 符合条件的第一个子节点, 深度优先搜索
     */
    @Nullable
    static AccessibilityNodeInfo queryByContentDescription(AccessibilityNodeInfo source, String contentDescription){
        if(source == null){
            return null;
        }
        CharSequence nodeCS = source.getContentDescription();
        String nodeStr = (nodeCS == null) ? "": nodeCS.toString();
        if (nodeCS != null && nodeStr.contains(contentDescription)) {
            return source;
        }
        final  int childCount = source.getChildCount();
        for (int index = 0; index < childCount; index++) {
            AccessibilityNodeInfo node = queryByContentDescription(source.getChild(index), contentDescription);
            if (node != null) {
                return  node;

            }
        }
        return null;
    }

    /**
     * @param source 要开始查找的节点
     * @param className 要查找的节点View 的类名
     * @return 符合条件的第一个子节点, 深度优先的搜索.
     */
    @NonNull
    static List<AccessibilityNodeInfo> queryAll(AccessibilityNodeInfo source, String className){
        ArrayList<AccessibilityNodeInfo> list = new ArrayList<>();
        if (source.getClassName().equals(className)) {
            list.add(source);
        }
        final  int childCount = source.getChildCount();
        for (int index = 0; index < childCount; index++) {
            List<AccessibilityNodeInfo> childList = queryAll(source.getChild(index), className);
            list.addAll(childList);
        }
        return list;
    }

}
