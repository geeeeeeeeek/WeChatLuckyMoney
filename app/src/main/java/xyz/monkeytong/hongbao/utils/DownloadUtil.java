package xyz.monkeytong.hongbao.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.net.URI;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by Zhongyi on 8/1/16.
 */
public class DownloadUtil {
    public void enqueue(String url, Context context) {
        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(url));
        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Uber.apk");
        r.allowScanningByMediaScanner();
        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(r);
    }
}
