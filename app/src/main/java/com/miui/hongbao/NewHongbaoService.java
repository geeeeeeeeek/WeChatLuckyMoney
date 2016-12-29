package com.miui.hongbao;

import java.util.LinkedList;
import java.util.List;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

public class NewHongbaoService extends AccessibilityService {
  private int currentState = 0;

  private static final int NONE = -1;
  private static final int CLICK_HONGBAO = 1;
  private static final int OPEN_HONGBAO = 2;
  private static final int GO_BACK = 3;

  // 这名字我是够了.
  private List<String> visitedHongbaos = new LinkedList<>();
  private boolean openByMyself;

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  @Override
  public void onAccessibilityEvent(AccessibilityEvent event) {

    try {
      AccessibilityNodeInfo nodeInfo = event.getSource();
      if (nodeInfo == null) {
        return;
      }
      AccessibilityNodeInfo accessibilityNodeInfo = scan();
      if (accessibilityNodeInfo == null) {
        return;
      }
      switch (currentState) {
        case NONE:
          openByMyself = false;
          return;
        case CLICK_HONGBAO:
          openByMyself = true;
          accessibilityNodeInfo.getParent().performAction(AccessibilityNodeInfo.ACTION_CLICK);
          break;
        case OPEN_HONGBAO:
          if (!openByMyself) {
            return;
          }
          accessibilityNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
          break;
        case GO_BACK:
          if (!openByMyself) {
            return;
          }
          performGlobalAction(GLOBAL_ACTION_BACK);
          openByMyself = false;
          break;
        default:
          openByMyself = false;
          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  protected AccessibilityNodeInfo scan() {
    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();

    if (nodeInfo == null) {
      currentState = NONE;
      return null;
    }

    // todo 放入资源
    List<AccessibilityNodeInfo> send = nodeInfo.findAccessibilityNodeInfosByText("查看红包");
    List<AccessibilityNodeInfo> received = nodeInfo.findAccessibilityNodeInfosByText("领取红包");

    if (!received.isEmpty()) {
      for (AccessibilityNodeInfo info : received) {
        String name = getHongbaoHash(info);
        if (!isVisited(visitedHongbaos, name)) {
          currentState = CLICK_HONGBAO;
          visitedHongbaos.add(name);
          return info;
        }
      }
    }
    if (!send.isEmpty()) {
      for (AccessibilityNodeInfo info : send) {
        String name = getHongbaoHash(info);
        if (!isVisited(visitedHongbaos, name)) {
          visitedHongbaos.add(name);
          currentState = CLICK_HONGBAO;
          return info;
        }
      }
    }

    // 查找 打开红包
    List<AccessibilityNodeInfo> openNode =
        nodeInfo.findAccessibilityNodeInfosByText(getString(R.string.open_hongbao));
    if (!openNode.isEmpty()) {
      currentState = OPEN_HONGBAO;
      return openNode.get(0);
    }

    // 全打开
    openNode = nodeInfo.findAccessibilityNodeInfosByText(getString(R.string.all_opened));
    if (!openNode.isEmpty()) {
      currentState = GO_BACK;
      return openNode.get(0);
    }

    List<AccessibilityNodeInfo> resultNode =
        nodeInfo.findAccessibilityNodeInfosByText(getString(R.string.hong_detail));
    if (!resultNode.isEmpty()) {
      currentState = GO_BACK;
      return resultNode.get(0);
    }
    resultNode = nodeInfo.findAccessibilityNodeInfosByText(getString(R.string.too_slow));
    if (!resultNode.isEmpty()) {
      currentState = GO_BACK;
      return resultNode.get(0);
    }
    currentState = NONE;
    return null;
  }

  protected boolean isVisited(List<String> list, String hash) {
    return list.contains(hash);
  }

  @Override
  public void onInterrupt() {

  }

  protected String getPreNodeDescribe(AccessibilityNodeInfo node) {
    AccessibilityNodeInfo grandFather = node.getParent().getParent().getParent();
    // listview
    int count = grandFather.getChildCount();
    // listview 中的一个节点
    AccessibilityNodeInfo wholeNode = node.getParent().getParent();
    String wholeNodeDescribe = wholeNode.toString();

    int i = 0;
    for (; i < count; ++i) {
      AccessibilityNodeInfo child = grandFather.getChild(i);
      if (child != null && TextUtils.equals(child.toString(), wholeNodeDescribe)) {
        break;
      }
    }
    String description = "";
    String text = "";

    if (i > 0 && i < count) {
      AccessibilityNodeInfo childs = grandFather.getChild(i - 1);
      for (i = 0; i < childs.getChildCount(); ++i) {
        AccessibilityNodeInfo child = childs.getChild(i);
        // 一般为人头像下面的描述
        if (child.getContentDescription() != null) {
          description = child.getContentDescription().toString();
        }
        // 找对话框别人输入的东西
        if (child.getClassName().toString().contains("TextView")) {
          text = child.getText().toString();
        }
      }
    }
    return description + text;
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
    return content + "@" + getPreNodeDescribe(node);
  }
}
