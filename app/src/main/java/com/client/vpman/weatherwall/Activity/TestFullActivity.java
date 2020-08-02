package com.client.vpman.weatherwall.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.view.View;
import android.view.WindowManager;
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
import com.client.vpman.weatherwall.databinding.ActivityTestFullBinding;

import java.io.OutputStream;

public class TestFullActivity extends AppCompatActivity {

    ActivityTestFullBinding binding;
    String mImg, sImg, large, PhotoUrl;
    SharedPref1 pref;
    private int STORAGE_PERMISSION_CODE = 1;
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
            if (pref.getImageQuality().equals("Default")) {
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

            } else if (pref.getImageQuality().equals("High Quality")) {
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

        setWall();
        download();
        share();




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
            Resources res1 = getResources(); //resource handle
            Drawable drawable1 = res1.getDrawable(R.drawable.white_design_layout);
            binding.mainBar.setBackground(drawable1);
            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download_black);
            binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp);

        } else if (pref.getTheme().equals("Dark")) {

            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper);
            binding.shareFull.setImageResource(R.drawable.ic_share);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download);
            Resources res1 = getResources(); //resource handle
            Drawable drawable1 = res1.getDrawable(R.drawable.black_design_layout);
            binding.mainBar.setBackground(drawable1);
            binding.toolBarFull.setBackgroundColor(Color.parseColor("#000000"));
            binding.backExpFull.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            binding.browserFull1.setImageResource(R.drawable.ic_global);
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1);
            binding.toolBarFull.setBackground(drawable);

        } else {
            Resources res1 = getResources(); //resource handle
            Drawable drawable1 = res1.getDrawable(R.drawable.white_design_layout);
            binding.mainBar.setBackground(drawable1);
            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download_black);
            binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp);
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);
            binding.toolBarFull.setBackground(drawable);
            binding.backExpFull.setImageResource(R.drawable.ic_arrow_back);
            binding.browserFull1.setImageResource(R.drawable.ic_global_black);
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
                                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall";
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
                                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=com.client.vpman.weatherwall";
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
                Toast.makeText(TestFullActivity.this, "2", Toast.LENGTH_LONG).show();
                requestStoragePermission();
            }


        });
    }

    private void download() {
        binding.downloadFull.setOnClickListener(view -> {
            boolean granted = checkWriteExternalPermission();
            if (granted == true) {


                if (pref.getImageQuality().equals("Default")) {
                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Downloading...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();

                    Glide.with(TestFullActivity.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
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
                            Toast.makeText(TestFullActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();

                            mProgressDialog.hide();

                        }
                    });
                } else if (pref.getImageQuality().equals("High Quality")) {
                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Downloading...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();


                    Glide.with(TestFullActivity.this).asBitmap().load(large).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
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
                            Toast.makeText(TestFullActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();

                            mProgressDialog.hide();

                        }
                    });
                } else {
                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Downloading...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();


                    Glide.with(TestFullActivity.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
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
                            Toast.makeText(TestFullActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();

                            mProgressDialog.hide();

                        }
                    });

                }


            } else {
                Toast.makeText(TestFullActivity.this, "Permission is not given", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setWall() {
        binding.setWallFull.setOnClickListener(view -> {

            Log.d("wefe", "ewf");
            boolean granted = checkWriteExternalPermission();
            if (granted == true) {
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


                    Glide.with(TestFullActivity.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
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
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(TestFullActivity.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));

                            mProgressDialog.hide();

                        }
                    });

                } else if (pref.getImageQuality().equals("High Quality")) {

                    mProgressDialog = new ProgressDialog(TestFullActivity.this);
                    mProgressDialog.setMessage("Setting...");
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);

                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


                    mProgressDialog.onStart();

                    mProgressDialog.show();


                    Glide.with(TestFullActivity.this).asBitmap().load(large).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
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
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(TestFullActivity.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                            mProgressDialog.hide();

                        }
                    });
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


                    Glide.with(TestFullActivity.this).asBitmap().load(mImg).into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
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
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(TestFullActivity.this);
                            startActivity(wallpaperManager.getCropAndSetWallpaperIntent(uri));
                            mProgressDialog.hide();

                        }
                    });
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
        requestStoragePermission();
    }
}