package xyz.monkeytong.hongbao.utils;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * AccessibilityService Settings Utility
 * <p>
 * Use  framework's settings command,so need root permission.
 *
 * @author act262@gmail.com
 * @see com.android.settingslib.accessibility.AccessibilityUtil
 * @see android.provider.Settings.Secure
 */
public class AccessibilityUtil {

    /**
     * 打开/关闭指定的AccessibilityService
     * <br/>
     * <em>需要Root权限执行，否则抛出异常</em>
     */
    public static void toggleAccessibilityService(Context context, ComponentName componentName) {
        try {
            if (isEnabledAccessibilityService(context, componentName)) {
                closeAccessibilityService(componentName);
            } else {
                openAccessibilityService(componentName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开指定的AccessibilityService
     * <br/>
     * <em>需要Root权限执行，否则抛出异常</em>
     */
    public static void openAccessibilityService(ComponentName componentName) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        // exec shell : settings put secure enabled_accessibility_services cmp
        os.writeBytes("settings put secure " + Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES + " " + componentName.flattenToString() + "  \n");
        os.writeBytes("exit\n");
        os.flush();
    }

    /**
     * 关闭指定AccessibilityService
     */
    public static void closeAccessibilityService(ComponentName componentName) throws IOException {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        // exec shell : settings put secure enabled_accessibility_services " "
        os.writeBytes("settings put secure " + Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES + " :  \n");
        os.writeBytes("exit\n");
        os.flush();
    }

    public static boolean isEnabledAccessibilityService(Context context, ComponentName componentName) {
        String result = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        return result.contains(componentName.flattenToString());
    }

    /**
     * 判断当前手机是否有ROOT权限
     */
    public static boolean isRoot() {
        boolean root = false;
        try {
            // TODO: 2017/1/21 0021 需要判断多种root的情况
            if ((!new File("/su/bin/su").exists())) { // eu.chainfire.supersu
                root = false;
            } else {
                root = true;
            }
        } catch (Exception e) {
        }
        return root;
    }

}
