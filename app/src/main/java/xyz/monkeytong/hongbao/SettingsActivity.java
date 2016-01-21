package xyz.monkeytong.hongbao;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Zhongyi on 1/19/16.
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
                new UpdateTask(getApplicationContext()).execute("https://api.github.com/repos/geeeeeeeeek/WeChatLuckyMoney/releases/latest");
                return false;
            }
        });

        // Open issue
        Preference issuePref = findPreference("pref_etc_issue");
        issuePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/geeeeeeeeek/WeChatLuckyMoney/issues"));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SettingsActivity.this.startActivity(browserIntent);
                return false;
            }
        });
    }

    private void loadUI() {
        addPreferencesFromResource(R.xml.preferences);

        // Get rid of the fucking additional padding
        getListView().setPadding(0, 0, 0, 0);
        getListView().setBackgroundColor(0xfffaf6f1);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void performBack(View view) {
        super.onBackPressed();
    }
}
