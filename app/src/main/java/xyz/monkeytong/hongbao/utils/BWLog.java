package xyz.monkeytong.hongbao.utils;

import android.util.Log;

/**
 * Created by 不歪 on 17/3/3.
 */

public class BWLog {

    private static final String TAG = "debug";
    private static final boolean DEBUG = false;

    public static int d(String msg) {
        return BWLog.d(TAG, msg);
    }

    public static int i(String msg) {
        return BWLog.i(TAG, msg);
    }

    public static int w(String msg) {
        return BWLog.w(TAG, msg);
    }

    public static int e(String msg) {
        return BWLog.e(TAG, msg);
    }

    public static int d(String tag, String msg) {
        if (DEBUG) {
            return Log.d(tag, msg);
        } else {
            return 0;
        }
    }

    public static int i(String tag, String msg) {
        if (DEBUG) {
            return Log.i(tag, msg);
        } else {
            return 0;
        }
    }

    public static int w(String tag, String msg) {
        if (DEBUG) {
            return Log.w(tag, msg);
        } else {
            return 0;
        }
    }

    public static int e(String tag, String msg) {
        if (DEBUG) {
            return Log.e(tag, msg);
        } else {
            return 0;
        }
    }

}
