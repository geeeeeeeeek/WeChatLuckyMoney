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
    private List<AccessibilityNodeInfo> mReceiveNode = null;
    private List<AccessibilityNodeInfo> mUnpackNode = null;

    private boolean mLuckyMoneyPicked;
    private boolean mLuckyMoneyReceived;
    private boolean mNeedUnpack;
    private boolean mNeedBack = false;

    private List<String> fetchIdentifiers = new ArrayList<>();
    private String lastFetchedHongbaoId = null;
    private long lastFetchedTime = 0;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = event.getSource();

        if (nodeInfo == null) return;

        mReceiveNode = null;
        mUnpackNode = null;
        checkNodeInfo();

        debugFlags();

        /* 如果已经接收到红包并且还没有戳开 */
        if (mLuckyMoneyReceived && !mLuckyMoneyPicked && (mReceiveNode != null)) {
            int size = mReceiveNode.size();
            if (size > 0) {

                String id = getHongbaoHash(mReceiveNode.get(size - 1));

                long now = System.currentTimeMillis();
                if (id == null || (now - lastFetchedTime < 5000) && id.equals(lastFetchedHongbaoId))
                    return;

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
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        if (nodeInfo == null) return;

        /* 聊天会话窗口，遍历节点匹配“领取红包”和"查看红包" */
        List<AccessibilityNodeInfo> node0 = nodeInfo.findAccessibilityNodeInfosByText("查看红包");
        List<AccessibilityNodeInfo> node1 = nodeInfo.findAccessibilityNodeInfosByText("领取红包");

        if (!node1.isEmpty() || !node0.isEmpty()) {
            String nodeId = Integer.toHexString(System.identityHashCode(nodeInfo));
            if (!checkFetched(nodeId)) {
                mLuckyMoneyReceived = true;
                mReceiveNode = node1.isEmpty() ? node0 : node1;
            }
            return;
        }

        /* 戳开红包，红包还没抢完，遍历节点匹配“拆红包” */
        List<AccessibilityNodeInfo> node2 = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
        List<AccessibilityNodeInfo> node7 = nodeInfo.findAccessibilityNodeInfosByText("Open");
        if (!node2.isEmpty() || !node7.isEmpty()) {
            mUnpackNode = node2.isEmpty() ? node7 : node2;
            mNeedUnpack = true;
            return;
        }

        /* 戳开红包，红包已被抢完，遍历节点匹配“红包详情”和“手慢了” */
        if (mLuckyMoneyPicked) {
            List<AccessibilityNodeInfo> node3 = nodeInfo.findAccessibilityNodeInfosByText("红包详情");
            List<AccessibilityNodeInfo> node4 = nodeInfo.findAccessibilityNodeInfosByText("手慢了");
            List<AccessibilityNodeInfo> node5 = nodeInfo.findAccessibilityNodeInfosByText("Better luck next time!");
            List<AccessibilityNodeInfo> node6 = nodeInfo.findAccessibilityNodeInfosByText("Details");
            if (!node3.isEmpty() || !node4.isEmpty() || !node5.isEmpty() || !node6.isEmpty()) {
                mNeedBack = true;
                mLuckyMoneyPicked = false;
            }
        }
    }


    private boolean checkFetched(String nodeId) {
        for (String identifier : fetchIdentifiers) {
            if (nodeId.equals(identifier))
                return true;
        }
        fetchIdentifiers.add(nodeId);
        return false;
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

    private void debugFlags() {
        Log.d("flags", "mLuckyMoneyPicked:" + this.mLuckyMoneyPicked
                + ",mLuckyMoneyReceived:" + this.mLuckyMoneyReceived
                + ",mNeedUnpack:" + this.mNeedUnpack
                + ",mNeedBack:" + this.mNeedBack);
        Log.d("mReceiveNode", parseNull(mReceiveNode));
        Log.d("mUnpackNode", parseNull(mUnpackNode));
    }

    private String parseNull(Object o) {
        if (o == null) {
            return "null";
        } else {
            return o.toString();
        }
    }
}
