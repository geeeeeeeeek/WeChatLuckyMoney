package com.miui.hongbao;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import java.util.List;


public class HongbaoService extends AccessibilityService {
    private List<AccessibilityNodeInfo> mNodeInfoList1 = null;
    private List<AccessibilityNodeInfo> mNodeInfoList2 = null;

    private boolean mLuckyMoneyPicked;
    private boolean mLuckyMoneyRecived;
    private boolean mNeedUnpack;
    private boolean mNeedBack = false;
    private int mLuckyMoneyCount = 1;
    private String mNextName = "红包1";

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        final int eventType = event.getEventType();

        if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED || eventType == 2048) {
            AccessibilityNodeInfo nodeInfo = event.getSource();

            if (null != nodeInfo) {
                mNodeInfoList1 = null;
                mNodeInfoList2 = null;
                checkNodeInfo(nodeInfo);
                if (mLuckyMoneyRecived && !mLuckyMoneyPicked && (mNodeInfoList1 != null)) {
                    int size = mNodeInfoList1.size();
                    if (size > 0) {
                        AccessibilityNodeInfo cellNode = mNodeInfoList1.get(size-1);
                        cellNode.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        mLuckyMoneyRecived = false;
                        mLuckyMoneyPicked = true;
                    }
                }
                if (mNeedUnpack && (mNodeInfoList2 != null)) {
                    int size = mNodeInfoList2.size();
                    if (size > 0) {
                        AccessibilityNodeInfo cellNode = mNodeInfoList2.get(size - 1);
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

    private void checkNodeInfo(AccessibilityNodeInfo node) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> temp1 = nodeInfo.findAccessibilityNodeInfosByText(mNextName);
            if (!temp1.isEmpty()) {
                mLuckyMoneyRecived = true;
                mNodeInfoList1 = temp1;
                mLuckyMoneyCount++;
                mNextName = "红包" + mLuckyMoneyCount;
                return;
            }

            List<AccessibilityNodeInfo> temp2 = nodeInfo.findAccessibilityNodeInfosByText("拆红包");
            if (!temp2.isEmpty()) {
                mNodeInfoList2 = temp2;
                mNeedUnpack = true;
                return;
            }

            if (mLuckyMoneyPicked) {
                List<AccessibilityNodeInfo> temp3 = nodeInfo.findAccessibilityNodeInfosByText("红包详情");
                List<AccessibilityNodeInfo> temp4 = nodeInfo.findAccessibilityNodeInfosByText("手慢了");
                if (!temp3.isEmpty() || !temp4.isEmpty()) {
                    mNeedBack = true;
                    mLuckyMoneyPicked = false;
                }
            }

        }

        return;

    }

    @Override
    public void onInterrupt() {

    }

}