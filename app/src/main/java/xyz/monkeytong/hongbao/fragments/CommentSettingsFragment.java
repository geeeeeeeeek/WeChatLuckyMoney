package xyz.monkeytong.hongbao.fragments;

import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;
import xyz.monkeytong.hongbao.R;

/**
 * Created by Zhongyi on 2/4/16.
 */
public class CommentSettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.comment_preferences);
        setPrefListeners();
    }

    private void setPrefListeners() {
        Preference updatePref = findPreference("pref_comment_switch");
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            updatePref.setEnabled(false);
        }
        Toast.makeText(getActivity(), "该功能尚处于实验中,只能自动填充感谢语,无法直接发送.", Toast.LENGTH_LONG).show();

        Preference commentWordsPref = findPreference("pref_comment_words");
        String summary = getResources().getString(R.string.pref_comment_words_summary);
        String value = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_comment_words", "");
        if (value.length() > 0) commentWordsPref.setSummary(summary + ":" + value);

        commentWordsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String summary = getResources().getString(R.string.pref_comment_words_summary);
                if (o != null && o.toString().length() > 0) {
                    preference.setSummary(summary + ":" + o.toString());
                } else {
                    preference.setSummary(summary);
                }
                return true;
            }
        });
    }
}
