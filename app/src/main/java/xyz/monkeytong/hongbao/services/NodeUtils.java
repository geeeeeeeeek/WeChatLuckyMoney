package xyz.monkeytong.hongbao.services;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by banxi on 2/1/17.
 *  提供调试输出 AccessibilityNodeInfo hierachery 的实用方法
 */
class NodeUtils {
    private static String repeat(CharSequence cs, int count){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(cs);
        }
        return sb.toString();
    }

    private void dump(AccessibilityNodeInfo source, int depth) {
        String depthLabel = repeat("========", depth);
        Log.i("DUMP_NODE"+depthLabel+""+depth, source.toString());
        final  int childCount = source.getChildCount();
        for (int index = 0; index < childCount; index++) {
            dump(source.getChild(index), depth + 1);
        }
    }
}
