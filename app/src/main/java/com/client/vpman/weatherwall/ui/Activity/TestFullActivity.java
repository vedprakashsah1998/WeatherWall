package com.client.vpman.weatherwall.ui.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.transition.Explode;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.CustomeUsefullClass.DownloadImage;
import com.client.vpman.weatherwall.CustomeUsefullClass.DownloadImageKTX;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivityTestFullBinding;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class TestFullActivity extends AppCompatActivity {

    ActivityTestFullBinding binding;
    String mImg, sImg, large, PhotoUrl;
    SharedPref1 pref;
    private final int STORAGE_PERMISSION_CODE = 1;
    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestFullBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());

        Intent intent = getIntent();
        mImg = intent.getStringExtra("img");
        sImg = intent.getStringExtra("imgSmall");
        large = intent.getStringExtra("large");
        PhotoUrl = intent.getStringExtra("PhotoUrl");


        pref = new SharedPref1(TestFullActivity.this);
        Log.d("FullImage8085", pref.getImageQuality());

        setThemeBased();
        binding.backExpFull.setOnClickListener(v -> onBackPressed());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.skipMemoryCache(false);
        requestOptions.onlyRetrieveFromCache(true);
        requestOptions.priority(Priority.HIGH);
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.isMemoryCacheable();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        requestOptions.centerCrop();


        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount() / 1024;
            }
        };

        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            //Bitmap exists in cache.
            binding.imageFullTest.setImageBitmap(image);
        } else {
            if (pref.getImageLoadQuality().equals("Default")) {
                Glide.with(TestFullActivity.this)
                        .load(mImg)
                        .thumbnail(
                                Glide.with(TestFullActivity.this).load(mImg)
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //  spinKitView.setVisibility(View.GONE);
                                //  supportStartPostponedEnterTransition();


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                // spinKitView.setVisibility(View.GONE);
                                // supportStartPostponedEnterTransition();

                                return false;
                            }
                        })

                        .into(binding.imageFullTest);

            } else if (pref.getImageLoadQuality().equals("High Quality")) {
                Glide.with(TestFullActivity.this)
                        .load(large)
                        .thumbnail(
                                Glide.with(TestFullActivity.this).load(mImg)
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //  spinKitView.setVisibility(View.GONE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                // spinKitView.setVisibility(View.GONE);
                                // scheduleStartPostponedTransition(binding.imageFull);
                                return false;
                            }
                        })

                        .into(binding.imageFullTest);
            } else {
                Glide.with(TestFullActivity.this)
                        .load(mImg)
                        .thumbnail(
                                Glide.with(TestFullActivity.this).load(mImg)
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //  spinKitView.setVisibility(View.GONE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                // spinKitView.setVisibility(View.GONE);
                                //scheduleStartPostponedTransition(binding.imageFull);


                                return false;
                            }
                        })

                        .into(binding.imageFullTest);
            }
        }
//        applyBlur();

        if (getIntent().getData() != null) {
            Log.d("wegfwe", String.valueOf(getIntent().getData()));

            binding.browserFull1.setVisibility(View.GONE);

            Picasso.get()
                    .load(getIntent().getData())
                    .placeholder(Utils.getRandomDrawbleColor())
                    .into(binding.imageFullTest);

            binding.shareFull.setOnClickListener(view -> {
                boolean granted = checkWriteExternalPermission();
                if (granted) {
              /*  ProgressBar progressBar=view.findViewById(R.id.progress6);
                progressBar.setVisibility(View.VISIBLE);*/


                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.onStart();
                    mProgressDialog.show();


                    Glide.with(TestFullActivity.this).asBitmap()
                            .load(getIntent().getData())
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");

                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = TestFullActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = TestFullActivity.this.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                                    String shareMessage = "\nDownload this application from PlayStore\n\n";
                                    shareMessage = shareMessage + getIntent().getData();
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));
                                    // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                    /* progressBar.setVisibility(View.GONE);*/
                                }
                            });
                    mProgressDialog.hide();


                } else {
                    Toast.makeText(TestFullActivity.this, "Error", Toast.LENGTH_LONG).show();
                    requestStoragePermission();
                }


            });
//            binding.downloadFull.setOnClickListener(view ->
//                    downloadWallpaper(view, getIntent().getData().toString()));

            /*            binding.downloadFull.setOnClickListener(view -> DownloadImage.downloadWallpaper(view,getIntent().getData().toString(),TestFullActivity.this));*/

            binding.downloadFull.setOnClickListener(view ->
                    DownloadImageKTX.Companion.downloadWallpaper(getIntent().getData().toString(), TestFullActivity.this));
            binding.setWallFull.setOnClickListener(view -> {

                Log.d("wefe", "ewf");
                boolean granted = checkWriteExternalPermission();
                if (granted) {


                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    WallpaperManager wpm = WallpaperManager.getInstance(TestFullActivity.this);
                    InputStream ins;
                    try {
                        ins = new URL(getIntent().getData().toString()).openStream();
                        wpm.setStream(ins);
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(TestFullActivity.this, "Permission is not given", Toast.LENGTH_SHORT).show();
                    requestStoragePermission();
                }

            });
        } else {
            setWall();
            binding.browserFull1.setVisibility(View.VISIBLE);
            binding.downloadFull.setOnClickListener(view -> {

                if (pref.getImageQuality().equals("Default")) {
                    DownloadImageKTX.Companion.downloadWallpaper(mImg, TestFullActivity.this);

                } else if (pref.getImageQuality().equals("High Quality")) {
                    DownloadImageKTX.Companion.downloadWallpaper(large, TestFullActivity.this);

                } else {
                    DownloadImageKTX.Companion.downloadWallpaper(mImg, TestFullActivity.this);

                }

            });
            share();

        }




        binding.browserFull1.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PhotoUrl));
            startActivity(browserIntent);
        });


    }

    private void setThemeBased() {
        if (pref.getTheme().equals("Light")) {
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);
            binding.toolBarFull.setBackground(drawable);
            binding.backExpFull.setImageResource(R.drawable.ic_arrow_back);
            binding.toolBarFull.setBackground(drawable);
            binding.browserFull1.setImageResource(R.drawable.ic_global_black);
//            binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
 /*           binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download_black);
            binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp);*/

        } else if (pref.getTheme().equals("Dark")) {

/*            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper);
            binding.shareFull.setImageResource(R.drawable.ic_share);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download);*/
            binding.backExpFull.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            binding.browserFull1.setImageResource(R.drawable.ic_global);
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1);
            binding.toolBarFull.setBackground(drawable);
//            binding.mainBar.setCardBackgroundColor(Color.parseColor("#000000"));

        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
/*                    binding.setWallFull.setImageResource(R.drawable.ic_wallpaper);
                    binding.shareFull.setImageResource(R.drawable.ic_share);
                    binding.downloadFull.setImageResource(R.drawable.ic_file_download);*/
                    binding.backExpFull.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                    binding.browserFull1.setImageResource(R.drawable.ic_global);
                    Resources res = getResources(); //resource handle
                    Drawable drawable = res.getDrawable(R.drawable.basic_design1);
                    binding.toolBarFull.setBackground(drawable);
//                    binding.mainBar.setCardBackgroundColor(Color.parseColor("#000000"));

                    break;
                case Configuration.UI_MODE_NIGHT_NO:
//                    binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black);
                    binding.downloadFull.setImageResource(R.drawable.ic_file_download_black);
                    binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp);
//                    binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    Resources res1 = getResources(); //resource handle
                    Drawable drawable1 = res1.getDrawable(R.drawable.basic_design1_white);
                    binding.toolBarFull.setBackground(drawable1);
                    binding.backExpFull.setImageResource(R.drawable.ic_arrow_back);
                    binding.browserFull1.setImageResource(R.drawable.ic_global_black);
                    break;
            }

        }
    }

    private void share() {
        binding.shareFull.setOnClickListener(view -> {
            boolean granted = checkWriteExternalPermission();
            if (granted == true) {
              /*  ProgressBar progressBar=view.findViewById(R.id.progress6);
                progressBar.setVisibility(View.VISIBLE);*/

                if (pref.getImageQuality().equals("Default")) {

                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.onStart();
                    mProgressDialog.show();


                    Glide.with(TestFullActivity.this).asBitmap()
                            .load(mImg)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");

                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = TestFullActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = TestFullActivity.this.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                                    String shareMessage = "\nDownload this application from PlayStore\n\n";
                                    shareMessage = shareMessage + mImg;
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));
                                    // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                    /* progressBar.setVisibility(View.GONE);*/
                                }
                            });
                    mProgressDialog.hide();

                } else if (pref.getImageQuality().equals("High quality")) {
                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();

                    Glide.with(TestFullActivity.this).asBitmap()
                            .load(large)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");

                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = TestFullActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = TestFullActivity.this.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                                    String shareMessage = "\nDownload this application from PlayStore\n\n";
                                    shareMessage = shareMessage + large;
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));
                                    // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                    /* progressBar.setVisibility(View.GONE);*/
                                }
                            });
                    mProgressDialog.hide();
                } else {
                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();

                    Glide.with(TestFullActivity.this).asBitmap()
                            .load(mImg)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                    // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");

                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = TestFullActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = TestFullActivity.this.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                                    String shareMessage = "\nDownload this application from PlayStore\n\n";
                                    shareMessage = shareMessage + mImg;
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));
                                    // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                    /* progressBar.setVisibility(View.GONE);*/
                                }
                            });
                    mProgressDialog.hide();
                }


            } else {
                Toast.makeText(TestFullActivity.this, "2", Toast.LENGTH_LONG).show();
                requestStoragePermission();
            }


        });
    }


    private void setWall() {
        binding.setWallFull.setOnClickListener(view -> {

            Log.d("wefe", "ewf");
            boolean granted = checkWriteExternalPermission();
            if (granted) {
                if (pref.getImageQuality().equals("Default")) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    WallpaperManager wpm = WallpaperManager.getInstance(TestFullActivity.this);
                    InputStream ins;
                    try {
                        ins = new URL(mImg).openStream();
                        wpm.setStream(ins);
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (pref.getImageQuality().equals("High Quality")) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    WallpaperManager wpm = WallpaperManager.getInstance(TestFullActivity.this);
                    InputStream ins;
                    try {
                        ins = new URL(large).openStream();
                        wpm.setStream(ins);
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    WallpaperManager wpm = WallpaperManager.getInstance(TestFullActivity.this);
                    InputStream ins;
                    try {
                        ins = new URL(mImg).openStream();
                        wpm.setStream(ins);
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            } else {
                Toast.makeText(TestFullActivity.this, "Permission is not given", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(TestFullActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }

                    }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();*/
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        binding.blurLayout.startBlur();
        binding.blurLayoutData.startBlur();
        binding.blurLayoutData1.startBlur();
        binding.blurLayoutData2.startBlur();

        requestStoragePermission();
    }

/*    private void applyBlur() {
        binding.imageFullTest.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                binding.imageFullTest.getViewTreeObserver().removeOnPreDrawListener(this);
                binding.imageFullTest.buildDrawingCache();

                Bitmap bmp = binding.imageFullTest.getDrawingCache();
                blur(bmp, binding.relLayout);
                return true;
            }
        });

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void blur(Bitmap bkg, View view) {
        float radius = 25;

        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getBottom());
        canvas.drawBitmap(bkg, 0, 0, null);

        RenderScript rs = RenderScript.create(TestFullActivity.this);

        Allocation overlayAlloc = Allocation.createFromBitmap(
                rs, overlay);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
                rs, overlayAlloc.getElement());

        blur.setInput(overlayAlloc);

        blur.setRadius(radius);

        blur.forEach(overlayAlloc);

        overlayAlloc.copyTo(overlay);

        view.setBackground(new BitmapDrawable(
                getResources(), overlay));

        rs.destroy();
    }*/




}