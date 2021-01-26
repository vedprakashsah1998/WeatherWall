package com.client.vpman.weatherwall.ui.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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
import com.client.vpman.weatherwall.model.RandomQuotes1;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivityFullImageQuotesBinding;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FullImageQuotes extends AppCompatActivity {

    String mImg, sImg, largeImg, photoUrl;
    List<String> list;
    List<RandomQuotes1> randomQuotes;
    ProgressDialog mProgressDialog;
    private final int STORAGE_PERMISSION_CODE = 1;
    SharedPref1 pref;
    ActivityFullImageQuotesBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullImageQuotesBinding.inflate(getLayoutInflater());
        View view1 = binding.getRoot();
        setContentView(view1);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref = new SharedPref1(FullImageQuotes.this);

        if (pref.getTheme().equals("Light")) {
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1_white);

            binding.tool1barMain.setBackground(drawable);
            binding.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back);
            binding.downloadImg.setImageResource(R.drawable.ic_file_download_black);
            binding.browser.setImageResource(R.drawable.ic_global_black);

        } else if (pref.getTheme().equals("Dark")) {

            binding.tool1barMain.setBackgroundColor(Color.parseColor("#000000"));
            binding.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            binding.browser.setImageResource(R.drawable.ic_global);
            binding.downloadImg.setImageResource(R.drawable.ic_file_download);
            Resources res = getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design1);
            binding.tool1barMain.setBackground(drawable);


        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    binding.tool1barMain.setBackgroundColor(Color.parseColor("#000000"));
                    binding.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                    binding.browser.setImageResource(R.drawable.ic_global);
                    binding.downloadImg.setImageResource(R.drawable.ic_file_download);
                    Resources res = getResources(); //resource handle
                    Drawable drawable = res.getDrawable(R.drawable.basic_design1);
                    binding.tool1barMain.setBackground(drawable);

                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    Resources res1 = getResources(); //resource handle
                    Drawable drawable1 = res1.getDrawable(R.drawable.basic_design1_white);

                    binding.tool1barMain.setBackground(drawable1);
                    binding.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back);
                    binding.browser.setImageResource(R.drawable.ic_global_black);
                    binding.downloadImg.setImageResource(R.drawable.ic_file_download_black);
                    break;
            }


        }

        Intent intent = getIntent();
        mImg = intent.getStringExtra("imgDataAdapter");
        sImg = intent.getStringExtra("imgDataAdapterSmall");
        largeImg = intent.getStringExtra("largeImg");
        photoUrl = intent.getStringExtra("photoUrl");
        binding.tool1barMain.setTitle("");
        setSupportActionBar(binding.tool1barMain);
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
            binding.imageFullLast.setImageBitmap(image);
        } else {

            if (pref.getImageQuality().equals("Default")) {
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

                        .into(binding.imageFullLast);

            } else if (pref.getImageQuality().equals("High Quality")) {
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

                        .into(binding.imageFullLast);

            } else {

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

                        .into(binding.imageFullLast);
            }


        }


        binding.setWallQuotes.setOnClickListener(view -> {

            Log.d("wefe", "ewf");
            boolean granted = checkWriteExternalPermission();
            if (granted) {

                if (pref.getImageQuality().equals("Default")) {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    WallpaperManager wpm = WallpaperManager.getInstance(FullImageQuotes.this);
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

                    WallpaperManager wpm = WallpaperManager.getInstance(FullImageQuotes.this);
                    InputStream ins;
                    try {
                        ins = new URL(largeImg).openStream();
                        wpm.setStream(ins);
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    WallpaperManager wpm = WallpaperManager.getInstance(FullImageQuotes.this);
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
                Toast.makeText(FullImageQuotes.this, "Permission is not given", Toast.LENGTH_SHORT).show();
            }

        });

        binding.shareQuotes.setOnClickListener(view -> {
            boolean granted = checkWriteExternalPermission();
            if (granted) {

                if (pref.getImageQuality().equals("Default")) {
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
                                    shareMessage = shareMessage + mImg;
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));

                                }
                            });
                    mProgressDialog.hide();

                } else if (pref.getImageQuality().equals("High Quality")) {
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
                                    shareMessage = shareMessage + largeImg;
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));

                                }
                            });
                    mProgressDialog.hide();

                } else {

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
                                    shareMessage = shareMessage + mImg;
                                    share.putExtra(Intent.EXTRA_TEXT, "Weather Wall" + shareMessage);
                                    startActivity(Intent.createChooser(share, "Share Image"));

                                }
                            });
                    mProgressDialog.hide();
                }


            } else {
                Toast.makeText(FullImageQuotes.this, "2", Toast.LENGTH_LONG).show();
                requestStoragePermission();
            }


        });

        binding.downloadImg.setOnClickListener(view -> {
            SaveImage(FullImageQuotes.this);
        });
        binding.browser.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(photoUrl));
            startActivity(browserIntent);
        });
        requestStoragePermission();
        list = new ArrayList<>();
        Quotes();
    }

    public void Quotes() {
        randomQuotes = new ArrayList<>();
        String QuotesUrl = "https://type.fit/api/quotes";
        Log.d("sdfljh", "khwqgdi");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, QuotesUrl, response -> {

            Log.d("qoefg", response);

            try {

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("eouf", String.valueOf(jsonObject));
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject));
                    /*Log.d("TextQuotes", jsonObject1.getString("author"));*/

                    RandomQuotes1 randomQuotes1 = new RandomQuotes1(jsonObject1.getString("text"), jsonObject1.getString("author"));
                    randomQuotes.add(randomQuotes1);
                }

                Collections.shuffle(randomQuotes);
                Random random = new Random();
                int n = random.nextInt(randomQuotes.size());

                binding.quotesTextMain.setText(randomQuotes.get(n).getQuotes());
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
                    .setPositiveButton("ok", (dialog, which) -> ActivityCompat.requestPermissions(FullImageQuotes.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE)).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
                        Bitmap bitmap = ((BitmapDrawable) binding.imageFullLast.getDrawable()).getBitmap();
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
