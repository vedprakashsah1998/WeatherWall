package com.client.vpman.weatherwall.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.CustomeUsefullClass.RandomQuotes1;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FullImageQuotes extends AppCompatActivity {

    String mImg, sImg, largeImg,photoUrl;
    ImageView imageView, downloadImg, browser, share, setWall;
    Toolbar toolbar;
    List<String> list;
    MaterialTextView Quotestext;
    List<RandomQuotes1> randomQuotes;
    ProgressDialog mProgressDialog;
    private int STORAGE_PERMISSION_CODE = 1;
    SharedPref1 pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_quotes);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         pref=new SharedPref1(FullImageQuotes.this);

        toolbar = findViewById(R.id.tool1barMain);
        imageView = findViewById(R.id.imageFullLast);
        browser = findViewById(R.id.browser);
        setWall = findViewById(R.id.setWallQuotes);
        share = findViewById(R.id.shareQuotes);
        Quotestext = findViewById(R.id.quotesTextMain);
        downloadImg = findViewById(R.id.downloadImg);


        if (pref.getTheme().equals("Light")) {
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);

            toolbar.setBackground(drawable);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            downloadImg.setImageResource(R.drawable.ic_file_download_black);
            browser.setImageResource(R.drawable.ic_global_black);

        } else if (pref.getTheme().equals("Dark")) {

            toolbar.setBackgroundColor(Color.parseColor("#000000"));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            browser.setImageResource(R.drawable.ic_global);
            downloadImg.setImageResource(R.drawable.ic_file_download);
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1);
            toolbar.setBackground(drawable);


        } else {


            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);

            toolbar.setBackground(drawable);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            browser.setImageResource(R.drawable.ic_global_black);
            downloadImg.setImageResource(R.drawable.ic_file_download_black);
        }

        Intent intent = getIntent();
        mImg = intent.getStringExtra("imgDataAdapter");
        sImg = intent.getStringExtra("imgDataAdapterSmall");
        largeImg = intent.getStringExtra("largeImg");
        photoUrl=intent.getStringExtra("photoUrl");
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        RequestOptions requestOptions = new RequestOptions();
        // requestOptions.error(Utils.getRandomDrawbleColor());
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
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
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
            imageView.setImageBitmap(image);
        } else {

            if(pref.getImageQuality().equals("Default"))
            {
                Glide.with(FullImageQuotes.this)
                        .load(mImg)
                        .thumbnail(
                                Glide.with(FullImageQuotes.this).load(mImg)
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

                                return false;
                            }
                        })

                        .into(imageView);

            }
            else if (pref.getImageQuality().equals("High Quality"))
            {
                Glide.with(FullImageQuotes.this)
                        .load(largeImg)
                        .thumbnail(
                                Glide.with(FullImageQuotes.this).load(mImg)
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

                                return false;
                            }
                        })

                        .into(imageView);

            }
            else
            {

                Glide.with(FullImageQuotes.this)
                        .load(mImg)
                        .thumbnail(
                                Glide.with(FullImageQuotes.this).load(mImg)
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

                                return false;
                            }
                        })

                        .into(imageView);
            }




        }


        setWall.setOnClickListener(view -> {

            Log.d("wefe", "ewf");
            boolean granted = checkWriteExternalPermission();
            if (granted == true) {

                if (pref.getImageQuality().equals("Default"))
                {
                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();

                    Glide.with(FullImageQuotes.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullImageQuotes.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));

                        mProgressDialog.hide();
                        }
                    });

                }
                else if (pref.getImageQuality().equals("High Quality"))
                {
                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();
                    Glide.with(FullImageQuotes.this).asBitmap().load(largeImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullImageQuotes.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));

                            mProgressDialog.hide();

                        }
                    });

                }
                else
                {

                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();
                    Glide.with(FullImageQuotes.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullImageQuotes.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));

                            mProgressDialog.hide();

                        }
                    });
                }



            } else {
                Toast.makeText(FullImageQuotes.this, "Permission is not given", Toast.LENGTH_SHORT).show();
            }

        });

        share.setOnClickListener(view -> {
            boolean granted = checkWriteExternalPermission();
            if (granted == true) {

                if (pref.getImageQuality().equals("Default"))
                {
                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();

                    Glide.with(FullImageQuotes.this).asBitmap()
                            .load(mImg)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");

                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                                    String shareMessage = "\nDownload this application from PlayStore\n\n";
                                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall";
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));

                                }
                            });
                    mProgressDialog.hide();

                }
                else if (pref.getImageQuality().equals("High Quality"))
                {
                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();

                    Glide.with(FullImageQuotes.this).asBitmap()
                            .load(largeImg)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");

                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                                    String shareMessage = "\nDownload this application from PlayStore\n\n";
                                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall";
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));

                                }
                            });
                    mProgressDialog.hide();

                }
                else
                {

                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();

                    Glide.with(FullImageQuotes.this).asBitmap()
                            .load(mImg)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {

                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("image/jpeg");

                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, "title");
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                            values);
                                    OutputStream outstream;
                                    try {
                                        outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                        resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                        outstream.close();
                                    } catch (Exception e) {
                                        System.err.println(e.toString());
                                    }

                                    share.putExtra(Intent.EXTRA_STREAM, uri);
                                    share.setType("text/plain");
                                    share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall");
                                    String shareMessage = "\nDownload this application from PlayStore\n\n";
                                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall";
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));

                                }
                            });
                    mProgressDialog.hide();
                }


            }
            else
                {
                Toast.makeText(FullImageQuotes.this, "2", Toast.LENGTH_LONG).show();
                requestStoragePermission();
            }


        });

        downloadImg.setOnClickListener(view -> {
            boolean granted = checkWriteExternalPermission();
            if (granted == true) {

                if (pref.getImageQuality().equals("Default"))
                {

// instantiate it within the onCreate method
                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Downloading...");
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.onStart();
                    mProgressDialog.show();
                    Glide.with(FullImageQuotes.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                            Toast.makeText(FullImageQuotes.this, "Downloaded", Toast.LENGTH_SHORT).show();
                        mProgressDialog.hide();

                        }
                    });

                }
                else if (pref.getImageQuality().equals("High Quality"))
                {

                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Downloading...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();
                    Glide.with(FullImageQuotes.this).asBitmap().load(largeImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                            Toast.makeText(FullImageQuotes.this, "Downloaded", Toast.LENGTH_SHORT).show();
                        mProgressDialog.hide();

                        }
                    });

                }
                else
                {

                    mProgressDialog = new ProgressDialog(FullImageQuotes.this);
                    mProgressDialog.setMessage("Downloading...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();
                    Glide.with(FullImageQuotes.this).asBitmap().load(largeImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");

                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "title");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = FullImageQuotes.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values);
                            OutputStream outstream;
                            try {
                                outstream = FullImageQuotes.this.getContentResolver().openOutputStream(uri);
                                resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                outstream.close();
                            } catch (Exception e) {
                                System.err.println(e.toString());
                            }
                            Toast.makeText(FullImageQuotes.this, "Downloaded", Toast.LENGTH_SHORT).show();
                            mProgressDialog.hide();

                        }
                    });
                }



            } else {
                Toast.makeText(FullImageQuotes.this, "Permission is not given", Toast.LENGTH_SHORT).show();
            }
        });
        browser.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(photoUrl));
            startActivity(browserIntent);
        });
        requestStoragePermission();
        list = new ArrayList<>();
        Quotes();
    }

    public void Quotes()
    {

        randomQuotes=new ArrayList<>();
        String QuotesUrl="https://type.fit/api/quotes";
        Log.d("sdfljh","khwqgdi");
        StringRequest stringRequest=new StringRequest(Request.Method.GET, QuotesUrl, response -> {

            Log.d("qoefg",response);

            try {

                JSONArray jsonArray=new JSONArray(response);
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Log.d("eouf", String.valueOf(jsonObject));
                    JSONObject jsonObject1=new JSONObject(String.valueOf(jsonObject));
                    /*Log.d("TextQuotes", jsonObject1.getString("author"));*/

                    RandomQuotes1 randomQuotes1=new RandomQuotes1(jsonObject1.getString("text"),jsonObject1.getString("author"));
                    randomQuotes.add(randomQuotes1);
                }

                Collections.shuffle(randomQuotes);
                Random random = new Random();
                int n = random.nextInt(randomQuotes.size());

                Quotestext.setText(randomQuotes.get(n).getQuotes());
                /*Log.d("asljf",quote);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }, error -> {

        });
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(FullImageQuotes.this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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
                            ActivityCompat.requestPermissions(FullImageQuotes.this,
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
/*
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
*/
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
