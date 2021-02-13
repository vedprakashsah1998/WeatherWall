package com.client.vpman.weatherwall.CustomeUsefullClass;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;

import com.client.vpman.weatherwall.ui.Activity.FullImageQuotes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class DownloadImage {
    public static void downloadWallpaper(View view, String Url, Context context) {
        Dexter.withContext(context)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                        DownloadManager.Query query = new DownloadManager.Query();
                        Uri uri = Uri.parse(Url);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        String subPath = "/Weather Wall/" + System.currentTimeMillis() + ".jpg";
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, subPath);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        downloadManager.enqueue(request);
                        Cursor cursor = downloadManager.query(query);
                        if (cursor.moveToFirst()) {
                            if (cursor.getCount() > 0) {
                                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                    String file = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                                    // So something here on success
                                } else {
                                    int message = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON));
                                    // So something here on failed.
                                }
                            }
                        }
                        Toast.makeText(context, "Downloading Start", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startActivity(new Intent(Settings.EXTRA_APP_PACKAGE));
                        } else {
                            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();

    }
}
