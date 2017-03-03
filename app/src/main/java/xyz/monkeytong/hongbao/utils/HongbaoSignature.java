package xyz.monkeytong.hongbao.utils;

import android.graphics.Rect;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

/**
 * Created by Zhongyi on 1/21/16.
 */
public class HongbaoSignature {
    private static final String TAG = "HongbaoSignature";
    public String sender, content, time, contentDescription = "", commentString;
    public boolean others;

    public boolean generateSignature(AccessibilityNodeInfo node, String excludeWords) {
        try {
            /* The hongbao container node. It should be a LinearLayout. By specifying that, we can avoid text messages. */
            AccessibilityNodeInfo hongbaoNode = node.getParent();
            if (!"android.widget.LinearLayout".equals(hongbaoNode.getClassName())) {
                Log.d(TAG, "generateSignature - 不是LinearLayout");
                return false;
            }

            /* The text in the hongbao. Should mean something. */
            String hongbaoContent = hongbaoNode.getChild(0).getText().toString();
            Log.d(TAG, "generateSignature - hongbaoNode.getChild(0)=" + hongbaoNode.getChild(0));
            Log.d(TAG, "generateSignature - hongbaoContent=" + hongbaoContent);
            if (hongbaoContent == null || "查看红包".equals(hongbaoContent)) {
                Log.d(TAG, "generateSignature - 到了特定字符串");
                return false;
            }

            Log.d(TAG, "generateSignature - 0");
            /* Check the user's exclude words list. */
            String[] excludeWordsArray = excludeWords.split(" +");
            for (String word : excludeWordsArray) {
                if (word.length() > 0 && hongbaoContent.contains(word)) {
                    Log.d(TAG, "generateSignature - 有排除的字符串");
                    return false;
                }
            }

            /* The container node for a piece of message. It should be inside the screen.
                Or sometimes it will get opened twice while scrolling. */
            AccessibilityNodeInfo messageNode = hongbaoNode.getParent();

            Log.d(TAG, "generateSignature - messageNode=" + messageNode);
            Rect bounds = new Rect();
            messageNode.getBoundsInScreen(bounds);
            if (bounds.top < 0) {
                Log.d(TAG, "generateSignature - bounds.top=" + bounds.top);
                return false;
            }

            /* The sender and possible timestamp. Should mean something too. */
            String[] hongbaoInfo = getSenderContentDescriptionFromNode(messageNode);
            Log.d(TAG, "generateSignature - hongbaoInfo.length=" + hongbaoInfo.length +
                    ",hongbaoInfo[0]=" + hongbaoInfo[0] + ",hongbaoInfo[1]=" + hongbaoInfo[1]);
            Log.d(TAG, "generateSignature - this.toString()=" + this.toString());
            if (this.getSignature(hongbaoInfo[0], hongbaoContent, hongbaoInfo[1]).equals(this.toString())) {
                //  添加下面的代码的话会出现死循环
//                if (2 != hongbaoInfo.length) {
//                    return false;
//                }
//                if (!"unknownTime".equals(hongbaoInfo[1])) {
//                    return false;
//                }
                return false;
            }

            Log.d(TAG, "generateSignature - 3");
            /* So far we make sure it's a valid new coming hongbao. */
            this.sender = hongbaoInfo[0];
            this.time = hongbaoInfo[1];
            this.content = hongbaoContent;
            Log.d(TAG, "generateSignature - this.sender=" + this.sender + ",this.content=" + this.content);
            Log.d(TAG, "generateSignature - 离开 true");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "generateSignature - 离开 false");
            return false;
        }
    }

    @Override
    public String toString() {
        return this.getSignature(this.sender, this.content, this.time);
    }

    private String getSignature(String... strings) {
        String signature = "";
        for (String str : strings) {
            if (str == null) return null;
            signature += str + "|";
        }

        return signature.substring(0, signature.length() - 1);
    }

    public String getContentDescription() {
        return this.contentDescription;
    }

    public void setContentDescription(String description) {
        this.contentDescription = description;
    }

    private String[] getSenderContentDescriptionFromNode(AccessibilityNodeInfo node) {
        int count = node.getChildCount();
        Log.d(TAG, "getSenderContentDescriptionFromNode - count=" + count);
        String[] result = {"unknownSender", "unknownTime"};
        for (int i = 0; i < count; i++) {
            AccessibilityNodeInfo thisNode = node.getChild(i);
            String className = thisNode.getClassName().toString();
            Log.d(TAG, "getSenderContentDescriptionFromNode - thisNode=" + thisNode);
            if ("android.widget.ImageView".equals(className) && "unknownSender".equals(result[0])) {
                CharSequence contentDescription = thisNode.getContentDescription();
                if (contentDescription != null) result[0] = contentDescription.toString().replaceAll("头像$", "");
            } else if ("android.widget.TextView".equals(className) && "unknownTime".equals(result[1])) {
                CharSequence thisNodeText = thisNode.getText();
                Log.d(TAG, "getSenderContentDescriptionFromNode - thisNodeText=" + thisNodeText);
                if (thisNodeText != null) result[1] = thisNodeText.toString();
            }
//            else if ("android.widget.LinearLayout".equals(className)) {
//                int linearLayoutChildCount = thisNode.getChildCount();
//                Log.d(TAG, "getSenderContentDescriptionFromNode - linearLayoutChildCount=" + linearLayoutChildCount);
//                for (int j = 0; j < linearLayoutChildCount; j++) {
//                    AccessibilityNodeInfo linearLayoutChild = thisNode.getChild(j);
//                    Log.d(TAG, "getSenderContentDescriptionFromNode - " + j + ".linearLayoutChild=" + linearLayoutChild);
//                }
//            }
        }
        return result;
    }

    public void cleanSignature() {
        this.content = "";
        this.time = "";
        this.sender = "";
    }

}
