package xyz.monkeytong.hongbao.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LENOVO on 2016/1/29 0029.
 */
public class PrefUtil {

    private static final String PREFENCE_NAME="config";
    private static final String KEY_FIRST_OPEN="first_open";

    public static boolean isFirstOpened(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFENCE_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(KEY_FIRST_OPEN,true);
    }

    public static void setOpened(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFENCE_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_FIRST_OPEN,false).apply();
    }
}
