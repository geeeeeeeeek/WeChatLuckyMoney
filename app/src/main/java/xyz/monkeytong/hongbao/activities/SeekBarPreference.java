package xyz.monkeytong.hongbao.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import xyz.monkeytong.hongbao.R;

/**
 * Created by Zhongyi on 2/3/16.
 */
public class SeekBarPreference extends DialogPreference {
    private SeekBar seekBar;
    private TextView textView;
    private String hintText, prefKind;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_seekbar);

        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attr = attrs.getAttributeName(i);
            if (attr.equalsIgnoreCase("pref_kind")) {
                prefKind = attrs.getAttributeValue(i);
                break;
            }
        }
        if (prefKind.equals("pref_open_delay")) {
            hintText = getContext().getString(R.string.delay_open);
        } else if (prefKind.equals("pref_comment_delay")) {
            hintText = "发送回复(暂不支持延时)";
        }
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        SharedPreferences pref = getSharedPreferences();

        int delay = pref.getInt(prefKind, 0);
        this.seekBar = (SeekBar) view.findViewById(R.id.delay_seekBar);
        this.seekBar.setProgress(delay);

        if (prefKind.equals("pref_comment_delay")) {
            this.seekBar.setEnabled(false);
        }

        this.textView = (TextView) view.findViewById(R.id.pref_seekbar_textview);
        setHintText(0);

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setHintText(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(prefKind, this.seekBar.getProgress());
            editor.commit();
        }
        super.onDialogClosed(positiveResult);
    }

    private void setHintText(int delay) {
        if (delay == 0) {
            this.textView.setText(getContext().getString(R.string.delay_instantly) + hintText);
        } else {
            this.textView.setText(getContext().getString(R.string.delay_delay) + delay + getContext().getString(R.string.delay_sec) + getContext().getString(R.string.delay_then) + hintText);
        }
    }
}
