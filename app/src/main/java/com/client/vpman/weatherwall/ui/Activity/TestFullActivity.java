package com.client.vpman.weatherwall.ui.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivityTestFullBinding;
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

        if (getIntent().getData() != null) {
            Log.d("wegfwe", String.valueOf(getIntent().getData()));


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
            binding.downloadFull.setOnClickListener(v -> SaveImage(TestFullActivity.this));

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

            binding.downloadFull.setOnClickListener(v -> {
                SaveImage(TestFullActivity.this);

            });
            share();

        }


        applyBlur();


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
            binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download_black);
            binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp);

        } else if (pref.getTheme().equals("Dark")) {

            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper);
            binding.shareFull.setImageResource(R.drawable.ic_share);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download);
            binding.backExpFull.setImageResource(R.drawable.ic_arrow_back_black_24dp);
            binding.browserFull1.setImageResource(R.drawable.ic_global);
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1);
            binding.toolBarFull.setBackground(drawable);
            binding.mainBar.setCardBackgroundColor(Color.parseColor("#000000"));

        } else {
            binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black);
            binding.downloadFull.setImageResource(R.drawable.ic_file_download_black);
            binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp);
            binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
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
        requestStoragePermission();
    }

    private void applyBlur() {
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
        float radius = 20;

        Bitmap overlay = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(overlay);

        canvas.translate(-view.getLeft(), -view.getTop());
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
    }

    private void SaveImage(final Context context) {
        final ProgressDialog progress = new ProgressDialog(context);
        boolean granted = checkWriteExternalPermission();

        if (granted) {
            class SaveThisImage extends AsyncTask<Void, Void, Void> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    progress.setTitle("Processing");
                    progress.setMessage("Please Wait...");
                    progress.setCancelable(false);
                    progress.show();
                }

                @Override
                protected Void doInBackground(Void... arg0) {
                    File sdCard = Environment.getExternalStorageDirectory();
                    @SuppressLint("DefaultLocale") String fileName = String.format("%d.jpg", System.currentTimeMillis());
                    File dir = new File(sdCard.getAbsolutePath() + "/Weather Wall");
                    dir.mkdirs();
                    final File myImageFile = new File(dir, fileName); // Create image file
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    BitmapFactory.decodeFile(String.valueOf(myImageFile), options);
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(myImageFile);
                        Bitmap bitmap = ((BitmapDrawable) binding.imageFullTest.getDrawable()).getBitmap();
                        ;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(myImageFile));
                        context.sendBroadcast(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
                    Log.d("ewfogho", "done");
                }
            }
            SaveThisImage shareimg = new SaveThisImage();
            shareimg.execute();
        } else {
            Toast.makeText(context, "Permission is not given", Toast.LENGTH_SHORT).show();
            requestStoragePermission();
        }
    }



}