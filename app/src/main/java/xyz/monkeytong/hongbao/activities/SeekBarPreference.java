package xyz.monkeytong.hongbao.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import org.w3c.dom.Text;
import xyz.monkeytong.hongbao.R;

/**
 * Created by Zhongyi on 2/3/16.
 */
public class SeekBarPreference extends DialogPreference {
    private SeekBar seekBar;
    private TextView textView;

    public SeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.preference_seekbar);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        SharedPreferences pref = getSharedPreferences();

        int delay = pref.getInt("pref_open_delay", 0);
        this.seekBar = (SeekBar) view.findViewById(R.id.delay_seekBar);
        this.seekBar.setProgress(delay);

        this.textView = (TextView) view.findViewById(R.id.pref_seekbar_textview);
        this.textView.setText("延迟" + delay + "秒拆开红包");

        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                textView.setText("延迟" + i + "秒拆开红包");
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
            editor.putInt("pref_open_delay", this.seekBar.getProgress());
            editor.commit();
        }
        super.onDialogClosed(positiveResult);
    }
}
