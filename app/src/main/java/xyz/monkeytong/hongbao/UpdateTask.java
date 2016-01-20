package xyz.monkeytong.hongbao;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by Zhongyi on 1/20/16.
 */
public class UpdateTask extends AsyncTask<String, String, String> {
    private Context context;

    public UpdateTask(Context context) {
        Toast.makeText(context, "正在检查新版本……", Toast.LENGTH_SHORT).show();
        this.context = context;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            response = httpclient.execute(new HttpGet(uri[0]));
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == 200) {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else {
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (Exception e) {
            return null;
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        JSONObject release;
        try {
            release = new JSONObject(result);

            // Get current version
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            String version = "v1.3";

            String latestVersion = release.getString("tag_name");
            boolean isPreRelease = release.getBoolean("prerelease");
            if (!isPreRelease && version.compareToIgnoreCase(latestVersion) >= 0) {
                // Your version is ahead of or same as the latest.
                Toast.makeText(context, "已经是最新版本了(❁´▽`❁)", Toast.LENGTH_SHORT).show();
            } else {
                // Need update.
                String downloadUrl = release.getJSONArray("assets").getJSONObject(0).getString("browser_download_url");
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
                request.setTitle("微信红包 " + latestVersion);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                downloadManager.enqueue(request);

                Toast.makeText(context, "发现新版本(" + latestVersion + "),正在为您下载( /) V (\\ ) ", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(context, "遇到一些问题,请前往Github手动更新喵(ฅ´ω`ฅ)", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}