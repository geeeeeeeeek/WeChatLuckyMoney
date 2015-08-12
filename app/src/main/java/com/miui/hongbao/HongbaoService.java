package com.miui.hongbao;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;


public class HongbaoService extends AccessibilityService {
    private List<AccessibilityNodeInfo> mReiceiveNode = null;
    private List<AccessibilityNodeInfo> mUnpackNode = null;

    private boolean mLuckyMoneyPicked;
    private boolean mLuckyMoneyReceived;
    private boolean mNeedUnpack;
    private boolean mNeedBack = false;
    private int mLuckyMoneyCount = 1;
    private String mNextName = "红包1";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        final int eventType = event.getEventType();

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            AccessibilityNodeInfo nodeInfo = event.getSource();

            if (null != nodeInfo) {
                mReiceiveNode = null;
                mUnpackNode = null;
                checkNodeInfo();
                if (mLuckyMoneyReceived && !mLuckyMoneyPicked && (mReiceiveNode != null)) {
                    int size = mReiceiveNode.size();
                    if (size > 0) {
                        AccessibilityNodeInfo cellNode = mReiceiveNode.get(size-1);
                        cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        mLuckyMoneyReceived = false;
                        mLuckyMoneyPicked = true;
                    }
                }
                if (mNeedUnpack && (mUnpackNode != null)) {
                    int size = mUnpackNode.size();
                    if (size > 0) {
                        AccessibilityNodeInfo cellNode = mUnpackNode.get(size - 1);
                        cellNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        mNeedUnpack = false;
                    }
                }
            }

            if (mNeedBack) {
                try {
                    Thread.sleep(3000);
                    performGlobalAction(GLOBAL_ACTION_BACK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mNeedBack = false;
            }
        }
    }

    private void checkNodeInfo() {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> node1 = nodeInfo.findAccessibilityNodeInfosByText(mNextName);
            if (!node1.isEmpty()) {
                mLuckyMoneyReceived = true;
                mReiceiveNode = node1;
                mLuckyMoneyCount++;
                mNextName = "红包" + mLuckyMoneyCount;
                return;
            }

            List<AccessibilityNodeInfo> node2 = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
            if (!node2.isEmpty()) {
                mUnpackNode = node2;
                mNeedUnpack = true;
                return;
            }

            if (mLuckyMoneyPicked) {
                List<AccessibilityNodeInfo> node3 = nodeInfo.findAccessibilityNodeInfosByText("红包详情");
                List<AccessibilityNodeInfo> node4 = nodeInfo.findAccessibilityNodeInfosByText("手慢了");
                if (!node3.isEmpty() || !node4.isEmpty()) {
                    mNeedBack = true;
                    mLuckyMoneyPicked = false;
                }
            }
        }

    }

    @Override
    public void onInterrupt() {

    }

}
