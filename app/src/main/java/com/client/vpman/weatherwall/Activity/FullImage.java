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
import android.transition.Explode;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivityFullImageBinding;


import java.io.OutputStream;

public class FullImage extends AppCompatActivity {

    String mImg, sImg, large, PhotoUrl;
    SharedPref1 pref;
    private int STORAGE_PERMISSION_CODE = 1;
    ProgressDialog mProgressDialog;
    ActivityFullImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityFullImageBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
/*
        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setEnterTransition(new Explode());
        getWindow().setExitTransition(new Explode());


        binding.tool1bar.setTitle("");
/*        setSupportActionBar(binding.tool1bar);*/
        /*if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
        Intent intent = getIntent();
        mImg = intent.getStringExtra("img");
        sImg = intent.getStringExtra("imgSmall");
        large = intent.getStringExtra("large");
        PhotoUrl = intent.getStringExtra("PhotoUrl");


        pref = new SharedPref1(FullImage.this);
        Log.d("FullImage8085", pref.getImageQuality());

        if (pref.getTheme().equals("Light")) {
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);
            binding.tool1bar.setBackground(drawable);
            binding.backexp.setImageResource(R.drawable.ic_arrow_back);


            binding.tool1bar.setBackground(drawable);
            binding.browserFull.setImageResource(R.drawable.ic_global_black);

            Resources res1 = getResources(); //resource handle
            Drawable drawable1 = res1.getDrawable(R.drawable.white_design_layout);
            binding.actionBarLayout.setBackground(drawable1);
            binding.setWall.setImageResource(R.drawable.ic_wallpaper_black);
            binding.download.setImageResource(R.drawable.ic_file_download_black);
            binding.share.setImageResource(R.drawable.ic_share_black_24dp);

        } else if (pref.getTheme().equals("Dark")) {

            binding.setWall.setImageResource(R.drawable.ic_wallpaper);
            binding.share.setImageResource(R.drawable.ic_share);
            binding.download.setImageResource(R.drawable.ic_file_download);

            Resources res1 = getResources(); //resource handle
            Drawable drawable1 = res1.getDrawable(R.drawable.black_design_layout);
            binding.actionBarLayout.setBackground(drawable1);
            binding.tool1bar.setBackgroundColor(Color.parseColor("#000000"));
            binding.backexp.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            binding.browserFull.setImageResource(R.drawable.ic_global);
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1);
            binding.tool1bar.setBackground(drawable);

        } else {
            Resources res1 = getResources(); //resource handle
            Drawable drawable1 = res1.getDrawable(R.drawable.white_design_layout);
            binding.actionBarLayout.setBackground(drawable1);
            binding.setWall.setImageResource(R.drawable.ic_wallpaper_black);
            binding.download.setImageResource(R.drawable.ic_file_download_black);
            binding.share.setImageResource(R.drawable.ic_share_black_24dp);

            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);

            binding.tool1bar.setBackground(drawable);
            binding.backexp.setImageResource(R.drawable.ic_arrow_back);
            binding.browserFull.setImageResource(R.drawable.ic_global_black);
        }
        binding.backexp.setOnClickListener(v -> onBackPressed());


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
            binding.imageFull.setImageBitmap(image);
        } else {

            if (pref.getImageQuality().equals("Default")) {
                Glide.with(FullImage.this)
                        .load(mImg)
                        .thumbnail(
                                Glide.with(FullImage.this).load(mImg)
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
                                scheduleStartPostponedTransition(binding.imageFull);

                                return false;
                            }
                        })

                        .into(binding.imageFull);

            } else if (pref.getImageQuality().equals("High Quality")) {
                Glide.with(FullImage.this)
                        .load(large)
                        .thumbnail(
                                Glide.with(FullImage.this).load(mImg)
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
                                scheduleStartPostponedTransition(binding.imageFull);
                                return false;
                            }
                        })

                        .into(binding.imageFull);
            } else {
                Glide.with(FullImage.this)
                        .load(mImg)
                        .thumbnail(
                                Glide.with(FullImage.this).load(mImg)
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
                                scheduleStartPostponedTransition(binding.imageFull);


                                return false;
                            }
                        })

                        .into(binding.imageFull);
            }


            binding.setWall.setOnClickListener(view -> {

                Log.d("wefe", "ewf");
                boolean granted = checkWriteExternalPermission();
                if (granted == true) {
                    if (pref.getImageQuality().equals("Default")) {
                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Setting...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();


                        Glide.with(FullImage.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");

                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "title");
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                                OutputStream outstream;
                                try {
                                    outstream = FullImage.this.getContentResolver().openOutputStream(uri);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                    outstream.close();
                                } catch (Exception e) {
                                    System.err.println(e.toString());
                                }
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullImage.this);
                                startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));

                                mProgressDialog.hide();

                            }
                        });

                    } else if (pref.getImageQuality().equals("High Quality")) {

                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Setting...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();


                        Glide.with(FullImage.this).asBitmap().load(large).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");

                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "title");
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                                OutputStream outstream;
                                try {
                                    outstream = FullImage.this.getContentResolver().openOutputStream(uri);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                    outstream.close();
                                } catch (Exception e) {
                                    System.err.println(e.toString());
                                }
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullImage.this);
                                startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                                mProgressDialog.hide();

                            }
                        });
                    } else {

                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Setting...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();


                        Glide.with(FullImage.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");

                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "title");
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                                OutputStream outstream;
                                try {
                                    outstream = FullImage.this.getContentResolver().openOutputStream(uri);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                    outstream.close();
                                } catch (Exception e) {
                                    System.err.println(e.toString());
                                }
                                WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullImage.this);
                                startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                                mProgressDialog.hide();

                            }
                        });
                    }


                } else {
                    Toast.makeText(FullImage.this, "Permission is not given", Toast.LENGTH_SHORT).show();
                }

            });

            binding.download.setOnClickListener(view -> {
                boolean granted = checkWriteExternalPermission();
                if (granted == true) {


                    if (pref.getImageQuality().equals("Default")) {
                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Downloading...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();

                        Glide.with(FullImage.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");

                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "title");
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                                OutputStream outstream;
                                try {
                                    outstream = FullImage.this.getContentResolver().openOutputStream(uri);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                    outstream.close();
                                } catch (Exception e) {
                                    System.err.println(e.toString());
                                }
                                Toast.makeText(FullImage.this, "Downloaded", Toast.LENGTH_SHORT).show();

                                mProgressDialog.hide();

                            }
                        });
                    } else if (pref.getImageQuality().equals("High Quality")) {
                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Downloading...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();


                        Glide.with(FullImage.this).asBitmap().load(large).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");

                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "title");
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                                OutputStream outstream;
                                try {
                                    outstream = FullImage.this.getContentResolver().openOutputStream(uri);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                    outstream.close();
                                } catch (Exception e) {
                                    System.err.println(e.toString());
                                }
                                Toast.makeText(FullImage.this, "Downloaded", Toast.LENGTH_SHORT).show();

                                mProgressDialog.hide();

                            }
                        });
                    } else {
                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Downloading...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();


                        Glide.with(FullImage.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("image/jpeg");

                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE, "title");
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                        values);
                                OutputStream outstream;
                                try {
                                    outstream = FullImage.this.getContentResolver().openOutputStream(uri);
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                    outstream.close();
                                } catch (Exception e) {
                                    System.err.println(e.toString());
                                }
                                Toast.makeText(FullImage.this, "Downloaded", Toast.LENGTH_SHORT).show();

                                mProgressDialog.hide();

                            }
                        });

                    }


                } else {
                    Toast.makeText(FullImage.this, "Permission is not given", Toast.LENGTH_SHORT).show();
                }
            });
            binding.share.setOnClickListener(view -> {
                boolean granted = checkWriteExternalPermission();
                if (granted == true) {
              /*  ProgressBar progressBar=view.findViewById(R.id.progress6);
                progressBar.setVisibility(View.VISIBLE);*/

                    if (pref.getImageQuality().equals("Default")) {

                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Setting...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();


                        Glide.with(FullImage.this).asBitmap()
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
                                        Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                values);
                                        OutputStream outstream;
                                        try {
                                            outstream = FullImage.this.getContentResolver().openOutputStream(uri);
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
                                        // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                        /* progressBar.setVisibility(View.GONE);*/
                                    }
                                });
                        mProgressDialog.hide();

                    } else if (pref.getImageQuality().equals("High quality")) {
                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Setting...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();

                        Glide.with(FullImage.this).asBitmap()
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
                                        Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                values);
                                        OutputStream outstream;
                                        try {
                                            outstream = FullImage.this.getContentResolver().openOutputStream(uri);
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
                                        // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                        /* progressBar.setVisibility(View.GONE);*/
                                    }
                                });
                        mProgressDialog.hide();
                    } else {
                        mProgressDialog = new ProgressDialog(FullImage.this);
                        mProgressDialog.setMessage("Setting...");
                        mProgressDialog.setCancelable(false);
                        mProgressDialog.setIndeterminate(true);
                        mProgressDialog.setProgressNumberFormat(null);
                        mProgressDialog.setProgressPercentFormat(null);

                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                        mProgressDialog.onStart();

                        mProgressDialog.show();

                        Glide.with(FullImage.this).asBitmap()
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
                                        Uri uri = FullImage.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                                values);
                                        OutputStream outstream;
                                        try {
                                            outstream = FullImage.this.getContentResolver().openOutputStream(uri);
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
                                        // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                        /* progressBar.setVisibility(View.GONE);*/
                                    }
                                });
                        mProgressDialog.hide();
                    }


                } else {
                    Toast.makeText(FullImage.this, "2", Toast.LENGTH_LONG).show();
                    requestStoragePermission();
                }


            });
        }

        requestStoragePermission();

        binding.browserFull.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(PhotoUrl));
            startActivity(browserIntent);
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
                            ActivityCompat.requestPermissions(FullImage.this,
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
/*    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }*/
   /* @Override
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
    }*/

    private void scheduleStartPostponedTransition(final View sharedElement) {
        sharedElement.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        sharedElement.getViewTreeObserver().removeOnPreDrawListener(this);
                        startPostponedEnterTransition();
                        return true;
                    }
                });
    }

}
