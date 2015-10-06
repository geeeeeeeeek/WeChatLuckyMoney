package com.miui.hongbao;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZhongyiTong on 9/30/15.
 * <p/>
 * 抢红包主要的逻辑部分
 */
public class HongbaoService extends AccessibilityService {
    /**
     * 已获取的红包队列
     */
    private List<String> fetchedIdentifiers = new ArrayList<>();
    /**
     * 待抢的红包队列
     */
    private List<AccessibilityNodeInfo> nodesToFetch = new ArrayList<>();

    /**
     * 允许的最大尝试次数
     */
    private static final int MAX_TTL = 24;
    /**
     * 尝试次数
     */
    private int ttl = 0;

    /**
     * AccessibilityEvent的回调方法
     * <p/>
     * 当窗体状态或内容变化时，根据当前阶段选择相应的入口
     *
     * @param event 事件
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        if (Stage.getInstance().mutex) return;

        Stage.getInstance().mutex = true;

        try {
            handleWindowChange(event.getSource());
        } finally {
            Stage.getInstance().mutex = false;
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void handleWindowChange(AccessibilityNodeInfo nodeInfo) {
        switch (Stage.getInstance().getCurrentStage()) {
            case Stage.OPENING_STAGE:
                // 调试信息，打印TTL
                // Log.d("TTL", String.valueOf(ttl));

                /* 如果打开红包失败且还没到达最大尝试次数，重试 */
                if (openHongbao(nodeInfo) == -1 && ttl < MAX_TTL) return;

                ttl = 0;
                Stage.getInstance().entering(Stage.FETCHED_STAGE);
                performMyGlobalAction(GLOBAL_ACTION_BACK);
                if (nodesToFetch.size() == 0) handleWindowChange(nodeInfo);
                break;
            case Stage.OPENED_STAGE:
                List<AccessibilityNodeInfo> successNodes = nodeInfo.findAccessibilityNodeInfosByText("红包详情");
                if (successNodes.isEmpty() && ttl < MAX_TTL) {
                    ttl += 1;
                    return;
                }
                ttl = 0;
                Stage.getInstance().entering(Stage.FETCHED_STAGE);
                performMyGlobalAction(GLOBAL_ACTION_BACK);
                break;
            case Stage.FETCHED_STAGE:
                /* 先消灭待抢红包队列中的红包 */
                if (nodesToFetch.size() > 0) {
                    /* 从最下面的红包开始戳 */
                    AccessibilityNodeInfo node = nodesToFetch.remove(nodesToFetch.size() - 1);
                    if (node.getParent() != null) {
                        String id = getHongbaoHash(node);

                        if (id == null) return;

                        fetchedIdentifiers.add(id);

                        // 调试信息，在每次打开红包后打印出已经获取的红包
                        // Log.d("fetched", Arrays.toString(fetchedIdentifiers.toArray()));

                        Stage.getInstance().entering(Stage.OPENING_STAGE);
                        node.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }
                    return;
                }

                Stage.getInstance().entering(Stage.FETCHING_STAGE);
                fetchHongbao(nodeInfo);
                Stage.getInstance().entering(Stage.FETCHED_STAGE);
                break;
        }
    }


    /**
     * 如果已经接收到红包并且还没有戳开
     * <p/>
     * 在聊天页面中，查找包含“领取红包”的节点，
     * 将这些节点去重后加入待抢红包队列
     *
     * @param nodeInfo 当前窗体的节点信息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void fetchHongbao(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) return;

        /* 聊天会话窗口，遍历节点匹配“领取红包” */
        List<AccessibilityNodeInfo> fetchNodes = nodeInfo.findAccessibilityNodeInfosByText("领取红包");

        if (fetchNodes.isEmpty()) return;

        for (AccessibilityNodeInfo cellNode : fetchNodes) {
            String id = getHongbaoHash(cellNode);

            /* 如果节点没有被回收且该红包没有抢过 */
            if (id != null && !fetchedIdentifiers.contains(id)) {
                nodesToFetch.add(cellNode);
            }
        }

        // 调试信息，在每次fetch后打印出待抢红包
        // Log.d("toFetch", Arrays.toString(nodesToFetch.toArray()));
    }


    /**
     * 如果戳开红包但还未领取
     * <p/>
     * 第一种情况，当界面上出现“过期”(红包超过有效时间)、
     * “手慢了”(红包发完但没抢到)或“红包详情”(已经抢到)时，
     * 直接返回聊天界面
     * <p/>
     * 第二种情况，界面上出现“拆红包”时
     * 点击该节点，并将阶段标记为OPENED_STAGE
     * <p/>
     * 第三种情况，以上节点没有出现，
     * 说明窗体可能还在加载中，维持当前状态，TTL增加，返回重试
     *
     * @param nodeInfo 当前窗体的节点信息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private int openHongbao(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) return -1;

        /* 戳开红包，红包已被抢完，遍历节点匹配“红包详情”、“手慢了”和“过期” */
        List<AccessibilityNodeInfo> failureNoticeNodes = new ArrayList<>();
        failureNoticeNodes.addAll(nodeInfo.findAccessibilityNodeInfosByText("红包详情"));
        failureNoticeNodes.addAll(nodeInfo.findAccessibilityNodeInfosByText("手慢了"));
        failureNoticeNodes.addAll(nodeInfo.findAccessibilityNodeInfosByText("过期"));
        if (!failureNoticeNodes.isEmpty()) {
            return 0;
        }

        /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
        List<AccessibilityNodeInfo> successNoticeNodes = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
        List<AccessibilityNodeInfo> preventNoticeNodes = nodeInfo.findAccessibilityNodeInfosByText("领取红包");
        if (!successNoticeNodes.isEmpty()) {
            AccessibilityNodeInfo openNode = successNoticeNodes.get(successNoticeNodes.size() - 1);
            Stage.getInstance().entering(Stage.OPENED_STAGE);
            openNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            return 0;
        } else {
            Stage.getInstance().entering(Stage.OPENING_STAGE);
            ttl += 1;
            return -1;
        }
    }

    /**
     * 获取节点对象唯一的id，通过正则表达式匹配
     * AccessibilityNodeInfo@后的十六进制数字
     *
     * @param node AccessibilityNodeInfo对象
     * @return id字符串
     */
    private String getNodeId(AccessibilityNodeInfo node) {
        /* 用正则表达式匹配节点Object */
        Pattern objHashPattern = Pattern.compile("(?<=@)[0-9|a-z]+(?=;)");
        Matcher objHashMatcher = objHashPattern.matcher(node.toString());

        // AccessibilityNodeInfo必然有且只有一次匹配，因此不再作判断
        objHashMatcher.find();

        return objHashMatcher.group(0);
    }

    /**
     * 将节点对象的id和红包上的内容合并
     * 用于表示一个唯一的红包
     *
     * @param node 任意对象
     * @return 红包标识字符串
     */
    private String getHongbaoHash(AccessibilityNodeInfo node) {
        /* 获取红包上的文本 */
        String content;
        try {
            AccessibilityNodeInfo i = node.getParent().getChild(0);
            content = i.getText().toString();
        } catch (NullPointerException npr) {
            return null;
        }

        return content + "@" + getNodeId(node);
    }

    @Override
    public void onInterrupt() {

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void performMyGlobalAction(int action) {
        Stage.getInstance().mutex = false;
        performGlobalAction(action);
    }
}
