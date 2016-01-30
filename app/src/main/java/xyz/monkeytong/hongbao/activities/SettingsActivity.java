package xyz.monkeytong.hongbao.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import xyz.monkeytong.hongbao.R;
import xyz.monkeytong.hongbao.utils.UpdateTask;

/**
 * Created by Zhongyi on 1/19/16.
 * Settings page.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadUI();
        setPrefListeners();
    }

    private void setPrefListeners() {
        // Check for updates
        Preference updatePref = findPreference("pref_etc_check_update");
        updatePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                new UpdateTask(getApplicationContext(), true).update();
                return false;
            }
        });

        // Open issue
        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent webViewIntent = new Intent(SettingsActivity.this, WebViewActivity.class);
                webViewIntent.putExtra("title", "Github Issues");
                webViewIntent.putExtra("url", getString(R.string.url_github_issues));
                webViewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(webViewIntent);
                return false;
            }
        });

        Preference excludeWordsPref = findPreference("pref_watch_exclude_words");
        String summary = getResources().getString(R.string.pref_watch_exclude_words_summary);
        String value = PreferenceManager.getDefaultSharedPreferences(this).getString("pref_watch_exclude_words", "");
        if (value.length() > 0) excludeWordsPref.setSummary(summary + ":" + value);

        excludeWordsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String summary = getResources().getString(R.string.pref_watch_exclude_words_summary);
                if (o != null && o.toString().length() > 0) preference.setSummary(summary + ":" + o.toString());
                return true;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void loadUI() {
        setContentView(R.layout.activity_preferences);
        addPreferencesFromResource(R.xml.preferences);

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return;

        Window window = this.getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        window.setStatusBarColor(0xffd84e43);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void performBack(View view) {
        super.onBackPressed();
    }

    public void enterAccessibilityPage(View view) {
        Intent mAccessibleIntent =
                new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(mAccessibleIntent);
    }
}
