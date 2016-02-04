package xyz.monkeytong.hongbao.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import xyz.monkeytong.hongbao.R;
import xyz.monkeytong.hongbao.activities.WebViewActivity;
import xyz.monkeytong.hongbao.utils.UpdateTask;

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

    }
}
