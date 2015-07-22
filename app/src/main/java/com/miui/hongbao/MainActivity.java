package com.miui.hongbao;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;


import java.util.List;

public class MainActivity extends Activity {
    private final Intent mAccessibleIntent =
            new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
    private TextView mServiceStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mServiceStatusView = (TextView) findViewById(R.id.text_accessible);
        updateServiceStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
    }

    private void updateServiceStatus() {
        boolean serviceEnabled = false;

        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.HongbaoService")) {
                serviceEnabled = true;
            }
        }
        mServiceStatusView.setText(serviceEnabled ? "辅助服务已开启" : "辅助服务已关闭");

    }

    public void onButtonClicked(View view) {
        startActivity(mAccessibleIntent);
    }

}
