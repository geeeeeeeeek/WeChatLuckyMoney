package com.miui.hongbao;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HongbaoService extends AccessibilityService {
    private List<AccessibilityNodeInfo> mReceiveNode, mUnpackNode;

    private boolean mLuckyMoneyPicked, mLuckyMoneyReceived, mNeedUnpack, mNeedBack;

    private String lastFetchedHongbaoId = null;
    private long lastFetchedTime = 0;

    private AccessibilityNodeInfo rootNodeInfo;

    private String WECHAT_DETAILS_EN = "Details";
    private String WECHAT_DETAILS_CH = "红包详情";
    private String WECHAT_BETTER_LUCK_EN = "Better luck next time!";
    private String WECHAT_BETTER_LUCK_CH = "手慢了";
    private String WECHAT_OPEN_EN = "Open";
    private String WECHAT_OPENED_EN = "opened";
    private String WECHAT_OPEN_CH = "拆红包";
    private String WECHAT_VIEW_SELF_CH = "查看红包";
    private String WECHAT_VIEW_OTHERS_CH = "领取红包";
    private String WECHAT_DEFAULT_TEXT_EN = "Best wishes!";
    private String WECHAT_DEFAULT_TEXT_CH = "恭喜发财,大吉大利!";
    private int MAX_DURATION_TOLERANCE = 5000;


    /**
     * AccessibilityEvent的回调方法
     *
     * @param event 事件
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        this.rootNodeInfo = event.getSource();

        if (rootNodeInfo == null) return;

        mReceiveNode = null;
        mUnpackNode = null;

        checkNodeInfo();

        /* 如果已经接收到红包并且还没有戳开 */
        if (mLuckyMoneyReceived && !mLuckyMoneyPicked && (mReceiveNode != null)) {
            int size = mReceiveNode.size();
            if (size > 0) {
                String id = getHongbaoText(mReceiveNode.get(size - 1));

                long now = System.currentTimeMillis();

                Log.d("111","0");
                if (this.shouldReturn(id, now - lastFetchedTime))
                    return;
                Log.d("111","1");

                lastFetchedHongbaoId = id;
                lastFetchedTime = now;

                AccessibilityNodeInfo cellNode = mReceiveNode.get(size - 1);
                cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                mLuckyMoneyReceived = false;
                mLuckyMoneyPicked = true;
            }
        }
        /* 如果戳开但还未领取 */
        if (mNeedUnpack && (mUnpackNode != null)) {
            int size = mUnpackNode.size();
            if (size > 0) {
                AccessibilityNodeInfo cellNode = mUnpackNode.get(size - 1);
                cellNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                mNeedUnpack = false;
            }
        }


        if (mNeedBack) {
            performGlobalAction(GLOBAL_ACTION_BACK);
            mNeedBack = false;
        }
    }

    @Override
    public void onInterrupt() {

    }

    /**
     * 检查节点信息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void checkNodeInfo() {
        if (this.rootNodeInfo == null) return;

        /* 聊天会话窗口，遍历节点匹配“领取红包”和"查看红包" */
        List<AccessibilityNodeInfo> nodes1 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                this.WECHAT_VIEW_SELF_CH, this.WECHAT_VIEW_OTHERS_CH});

        if (!nodes1.isEmpty()) {
            String nodeId = Integer.toHexString(System.identityHashCode(this.rootNodeInfo));
            if (!nodeId.equals(lastFetchedHongbaoId)) {
                mLuckyMoneyReceived = true;
                mReceiveNode = nodes1;
            }
            return;
        }

        /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
        List<AccessibilityNodeInfo> nodes2 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                this.WECHAT_OPEN_CH, this.WECHAT_OPEN_EN});
        if (!nodes2.isEmpty()) {
            mUnpackNode = nodes2;
            mNeedUnpack = true;
            return;
        }

        /* 戳开红包，红包已被抢完，遍历节点匹配“红包详情”和“手慢了” */
        if (mLuckyMoneyPicked) {
            List<AccessibilityNodeInfo> nodes3 = this.findAccessibilityNodeInfosByTexts(this.rootNodeInfo, new String[]{
                    this.WECHAT_BETTER_LUCK_CH, this.WECHAT_DETAILS_CH,
                    this.WECHAT_BETTER_LUCK_EN, this.WECHAT_DETAILS_EN});
            if (!nodes3.isEmpty()) {
                mNeedBack = true;
                mLuckyMoneyPicked = false;
            }
        }
    }

    /**
     * 将节点对象的id和红包上的内容合并
     * 用于表示一个唯一的红包
     *
     * @param node 任意对象
     * @return 红包标识字符串
     */
    private String getHongbaoText(AccessibilityNodeInfo node) {
        /* 获取红包上的文本 */
        String content;
        try {
            AccessibilityNodeInfo i = node.getParent().getChild(0);
            content = i.getText().toString();
        } catch (NullPointerException npe) {
            return null;
        }

        return content;
    }

    /**
     * 批量化执行AccessibilityNodeInfo.findAccessibilityNodeInfosByText(text).
     * 由于这个操作影响性能,将所有需要匹配的文字一起处理,尽早返回
     *
     * @param nodeInfo 窗口根节点
     * @param texts    需要匹配的字符串们
     * @return 匹配到的节点数组
     */
    private List<AccessibilityNodeInfo> findAccessibilityNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String[] texts) {
        for (String text : texts) {
            List<AccessibilityNodeInfo> nodes = nodeInfo.findAccessibilityNodeInfosByText(text);

            if (!nodes.isEmpty()) {
                if (text.equals(this.WECHAT_OPEN_EN) && !nodeInfo.findAccessibilityNodeInfosByText(this.WECHAT_OPENED_EN).isEmpty()) {
                    continue;
                }
                return nodes;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 判断是否返回,减少点击次数
     * 现在的策略是当红包文本和缓存不一致时,戳
     * 文本一致且间隔大于5秒时,戳
     *
     * @param id       红包id
     * @param duration 红包到达与缓存的间隔
     * @return 是否应该返回
     */
    private boolean shouldReturn(String id, long duration) {
        return id == null
                || (duration < this.MAX_DURATION_TOLERANCE) && id.equals(lastFetchedHongbaoId)
                && (lastFetchedHongbaoId.equals(this.WECHAT_DEFAULT_TEXT_CH) || lastFetchedHongbaoId.equals(this.WECHAT_DEFAULT_TEXT_EN));
    }
}
