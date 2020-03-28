package com.client.vpman.weatherwall.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.LruCache;
import android.view.Display;
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
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;

import net.robinx.lib.blurview.BlurBehindView;
import net.robinx.lib.blurview.processor.NdkStackBlurProcessor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FullImageQuotes extends AppCompatActivity {

    String mImg, sImg, largeImg;
    ImageView imageView, downloadImg, browser, share, setWall;
    BlurBehindView mainWork;
    Toolbar toolbar;
    List<String> list;
    MaterialTextView Quotestext;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image_quotes);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        toolbar = findViewById(R.id.tool1barMain);
        imageView = findViewById(R.id.imageFullLast);
        browser = findViewById(R.id.browser);
        setWall = findViewById(R.id.setWallQuotes);
        share = findViewById(R.id.shareQuotes);
        Quotestext = findViewById(R.id.quotesTextMain);
        downloadImg = findViewById(R.id.downloadImg);

        mainWork = findViewById(R.id.mainWorkQuotes);
        Intent intent = getIntent();
        mImg = intent.getStringExtra("imgDataAdapter");
        sImg = intent.getStringExtra("imgDataAdapterSmall");
        largeImg = intent.getStringExtra("largeImg");

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
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x; //width of screen in pixels
        int height = size.y;
        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            //Bitmap exists in cache.
            imageView.setImageBitmap(image);
        } else {
            if (Connectivity.isConnected(FullImageQuotes.this) || Connectivity.isConnectedFast(FullImageQuotes.this)) {
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

                        .into(imageView);
            }


        }
        mainWork.setTranslationZ(40);
        mainWork.updateMode(BlurBehindView.UPDATE_CONTINOUSLY).blurRadius(18).sizeDivider(6).cornerRadius(60).processor(NdkStackBlurProcessor.INSTANCE);

        setWall.setOnClickListener(view -> {

            Log.d("wefe", "ewf");
            boolean granted = checkWriteExternalPermission();
            if (granted == true) {
                if (Connectivity.isConnected(FullImageQuotes.this) || Connectivity.isConnectedFast(FullImageQuotes.this))
                {
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


                        }
                    });

                }
                else
                {
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

                if (Connectivity.isConnected(FullImageQuotes.this) || Connectivity.isConnectedFast(FullImageQuotes.this))
                {
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
                }
                else
                {
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

                if (Connectivity.isConnected(FullImageQuotes.this) || Connectivity.isConnectedFast(FullImageQuotes.this))
                {
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


                        }
                    });
                }
                else {
                    Toast.makeText(this, "Low", Toast.LENGTH_SHORT).show();
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


                        }
                    });
                }


            } else {
                Toast.makeText(FullImageQuotes.this, "Permission is not given", Toast.LENGTH_SHORT).show();
            }
        });
        browser.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pexels.com"));
            startActivity(browserIntent);
        });
        requestStoragePermission();
        list = new ArrayList<>();
        Quotes();
    }

    public void Quotes() {

        String QuotesUrl = "https://www.forbes.com/forbesapi/thought/uri.json?enrich=true&query=1&relatedlimit=100";
        Log.d("sdfljh", "khwqgdi");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, QuotesUrl, response -> {

            Log.d("qoefg", response);

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);

                JSONObject jsonObject1 = jsonObject.getJSONObject("thought");
                Log.d("qeljg", String.valueOf(jsonObject1));

                JSONArray jsonArray = jsonObject1.getJSONArray("relatedThemeThoughts");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = jsonArray.getJSONObject(i);


                    list.add(jsonObject2.getString("quote"));

                }
                Collections.shuffle(list);
                Random random = new Random();
                int n = random.nextInt(list.size());
                Quotestext.setText(list.get(n));
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
