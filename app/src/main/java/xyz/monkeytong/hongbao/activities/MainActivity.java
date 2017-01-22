package xyz.monkeytong.hongbao.activities;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.*;

import java.util.List;

import xyz.monkeytong.hongbao.R;
import xyz.monkeytong.hongbao.fragments.GeneralSettingsFragment;
import xyz.monkeytong.hongbao.utils.ConnectivityUtil;
import xyz.monkeytong.hongbao.utils.UpdateTask;

import com.tencent.bugly.crashreport.CrashReport;


public class MainActivity extends Activity implements AccessibilityManager.AccessibilityStateChangeListener {

    //开关切换按钮
    private TextView pluginStatusText;
    private ImageView pluginStatusIcon;
    //AccessibilityService 管理
    private AccessibilityManager accessibilityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashReport.initCrashReport(getApplicationContext(), "900019352", false);
        setContentView(R.layout.activity_main);
        pluginStatusText = (TextView) findViewById(R.id.layout_control_accessibility_text);
        pluginStatusIcon = (ImageView) findViewById(R.id.layout_control_accessibility_icon);

        handleMaterialStatusBar();

        explicitlyLoadPreferences();

        //监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);
        updateServiceStatus();
    }

    private void explicitlyLoadPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.general_preferences, false);
    }

    /**
     * 适配MIUI沉浸状态栏
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void handleMaterialStatusBar() {
        // Not supported in APK level lower than 21
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(0xffE46C62);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check for update when WIFI is connected or on first time.
        if (ConnectivityUtil.isWifi(this) || UpdateTask.count == 0)
            new UpdateTask(this, false).update();
    }

    @Override
    protected void onDestroy() {
        //移除监听服务
        accessibilityManager.removeAccessibilityStateChangeListener(this);
        super.onDestroy();
    }

    public void openAccessibility(View view) {
        try {
            Toast.makeText(this, "点击「微信红包」" + pluginStatusText.getText(), Toast.LENGTH_SHORT).show();
            Intent accessibleIntent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(accessibleIntent);
        } catch (Exception e) {
            Toast.makeText(this, "遇到一些问题,请手动打开系统设置>无障碍服务>微信红包(ฅ´ω`ฅ)", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

    }

    public void openGitHub(View view) {
        Intent webViewIntent = new Intent(this, WebViewActivity.class);
        webViewIntent.putExtra("title", "GitHub 项目主页");
        webViewIntent.putExtra("url", "https://github.com/geeeeeeeeek/WeChatLuckyMoney");
        startActivity(webViewIntent);
    }

    public void openUber(View view) {
        Intent webViewIntent = new Intent(this, WebViewActivity.class);
        webViewIntent.putExtra("title", "Uber 优惠乘车机会(优惠码rgk2wue)");
        webViewIntent.putExtra("url", "https://get.uber.com.cn/invite/rgk2wue");
        startActivity(webViewIntent);
    }

    public void openSettings(View view) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsIntent.putExtra("title", "偏好设置");
        settingsIntent.putExtra("frag_id", "GeneralSettingsFragment");
        startActivity(settingsIntent);
    }


    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }

    /**
     * 更新当前 HongbaoService 显示状态
     */
    private void updateServiceStatus() {
        if (isServiceEnabled()) {
            pluginStatusText.setText(R.string.service_off);
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_stop);
        } else {
            pluginStatusText.setText(R.string.service_on);
            pluginStatusIcon.setBackgroundResource(R.mipmap.ic_start);
        }
    }

    /**
     * 获取 HongbaoService 是否启用状态
     *
     * @return
     */
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.services.HongbaoService")) {
                return true;
            }
        }
        return false;
    }
}
