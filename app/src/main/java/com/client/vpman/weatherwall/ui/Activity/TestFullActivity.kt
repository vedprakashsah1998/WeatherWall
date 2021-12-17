package com.client.vpman.weatherwall.ui.Activity

import android.Manifest
import com.client.vpman.weatherwall.CustomeUsefullClass.DownloadImageKTX.Companion.downloadWallpaper
import androidx.appcompat.app.AppCompatActivity
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import android.os.Bundle
import android.view.WindowManager
import android.content.Intent
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import android.graphics.drawable.Drawable
import com.bumptech.glide.load.engine.GlideException
import com.squareup.picasso.Picasso
import com.bumptech.glide.request.target.SimpleTarget
import android.content.ContentValues
import android.provider.MediaStore
import android.widget.Toast
import com.client.vpman.weatherwall.CustomeUsefullClass.DownloadImageKTX
import android.os.StrictMode
import android.app.WallpaperManager
import com.client.vpman.weatherwall.R
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.transition.Explode
import android.util.Log
import android.util.LruCache
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.databinding.ActivityTestFullBinding
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.net.URL

class TestFullActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestFullBinding
    var mImg: String? = null
    var sImg: String? = null
    var large: String? = null
    var PhotoUrl: String? = null
    var pref: SharedPref1? = null
    private val STORAGE_PERMISSION_CODE = 1
    var mProgressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestFullBinding.inflate(
            layoutInflater
        )
        val view1: View = binding.root
        setContentView(view1)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.enterTransition = Explode()
        window.exitTransition = Explode()
        val intent = intent
        mImg = intent.getStringExtra("img")
        sImg = intent.getStringExtra("imgSmall")
        large = intent.getStringExtra("large")
        PhotoUrl = intent.getStringExtra("PhotoUrl")
        pref = SharedPref1(this@TestFullActivity)
        Log.d("FullImage8085", pref!!.imageQuality)
        setThemeBased()
        binding.backExpFull.setOnClickListener { v: View? -> onBackPressed() }
        val requestOptions = RequestOptions()
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
        requestOptions.priority(Priority.IMMEDIATE)
        requestOptions.skipMemoryCache(false)
        requestOptions.onlyRetrieveFromCache(true)
        requestOptions.priority(Priority.HIGH)
        requestOptions.placeholder(Utils.randomDrawbleColor)
        requestOptions.isMemoryCacheable
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        requestOptions.centerCrop()
        val memCache: LruCache<String?, Bitmap> = object : LruCache<String?, Bitmap>(
            (Runtime.getRuntime().maxMemory() / (1024 * 4)).toInt()
        ) {
            override fun sizeOf(key: String?, image: Bitmap): Int {
                return image.byteCount / 1024
            }
        }
        val image = memCache["imagefile"]
        if (image != null) {
            //Bitmap exists in cache.
            binding.imageFullTest.setImageBitmap(image)
        } else {
            when (pref!!.imageLoadQuality) {
                "Default" -> {
                    Glide.with(this@TestFullActivity)
                        .load(mImg)
                        .thumbnail(
                            Glide.with(this@TestFullActivity).load(mImg)
                        )
                        .apply(requestOptions)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                //  spinKitView.setVisibility(View.GONE);
                                //  supportStartPostponedEnterTransition();
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {

                                // spinKitView.setVisibility(View.GONE);
                                // supportStartPostponedEnterTransition();
                                return false
                            }
                        })
                        .into(binding.imageFullTest)
                }
                "High Quality" -> {
                    Glide.with(this@TestFullActivity)
                        .load(large)
                        .thumbnail(
                            Glide.with(this@TestFullActivity).load(mImg)
                        )
                        .apply(requestOptions)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                //  spinKitView.setVisibility(View.GONE);
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {

                                // spinKitView.setVisibility(View.GONE);
                                // scheduleStartPostponedTransition(binding.imageFull);
                                return false
                            }
                        })
                        .into(binding.imageFullTest)
                }
                else -> {
                    Glide.with(this@TestFullActivity)
                        .load(mImg)
                        .thumbnail(
                            Glide.with(this@TestFullActivity).load(mImg)
                        )
                        .apply(requestOptions)
                        .listener(object : RequestListener<Drawable?> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any,
                                target: Target<Drawable?>,
                                isFirstResource: Boolean
                            ): Boolean {
                                //  spinKitView.setVisibility(View.GONE);
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any,
                                target: Target<Drawable?>,
                                dataSource: DataSource,
                                isFirstResource: Boolean
                            ): Boolean {

                                // spinKitView.setVisibility(View.GONE);
                                //scheduleStartPostponedTransition(binding.imageFull);
                                return false
                            }
                        })
                        .into(binding.imageFullTest)
                }
            }
        }
        //        applyBlur();
        if (getIntent().data != null) {
            Log.d("wegfwe", getIntent().data.toString())
            binding.browserFull1.visibility = View.GONE
            Picasso.get()
                .load(getIntent().data)
                .placeholder(Utils.randomDrawbleColor)
                .into(binding.imageFullTest)
            binding.shareFull.setOnClickListener {
                val granted = checkWriteExternalPermission()
                if (granted) {
                    /*  ProgressBar progressBar=view.findViewById(R.id.progress6);
                progressBar.setVisibility(View.VISIBLE);*/
                    mProgressDialog = ProgressDialog(this@TestFullActivity)
                    mProgressDialog!!.setMessage("Setting...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.isIndeterminate = true
                    mProgressDialog!!.setProgressNumberFormat(null)
                    mProgressDialog!!.setProgressPercentFormat(null)
                    mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    mProgressDialog!!.onStart()
                    mProgressDialog!!.show()
                    Glide.with(this@TestFullActivity).asBitmap()
                        .load(getIntent().data)
                        .into(object : SimpleTarget<Bitmap?>() {

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/jpeg"
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, "title")
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                val uri = this@TestFullActivity.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val outstream: OutputStream?
                                try {
                                    outstream =
                                        this@TestFullActivity.contentResolver.openOutputStream(
                                            uri!!
                                        )
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
                                    outstream!!.close()
                                } catch (e: Exception) {
                                    System.err.println(e.toString())
                                }
                                share.putExtra(Intent.EXTRA_STREAM, uri)
                                share.type = "text/plain"
                                share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall")
                                var shareMessage = "\nDownload this application from PlayStore\n\n"
                                shareMessage += getIntent().data
                                share.putExtra(Intent.EXTRA_TEXT, "Weather Wall$shareMessage")
                                startActivity(Intent.createChooser(share, "Share Image"))
                                // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                /* progressBar.setVisibility(View.GONE);*/
                            }
                        })
                    mProgressDialog!!.hide()
                } else {
                    Toast.makeText(this@TestFullActivity, "Error", Toast.LENGTH_LONG).show()
                    requestStoragePermission()
                }
            }
            //            binding.downloadFull.setOnClickListener(view ->
//                    downloadWallpaper(view, getIntent().getData().toString()));

            /*            binding.downloadFull.setOnClickListener(view -> DownloadImage.downloadWallpaper(view,getIntent().getData().toString(),TestFullActivity.this));*/binding.downloadFull.setOnClickListener { view: View? ->
                downloadWallpaper(
                    getIntent().data.toString(),
                    this@TestFullActivity
                )
            }
            binding.setWallFull.setOnClickListener {
                Log.d("wefe", "ewf")
                val granted = checkWriteExternalPermission()
                if (granted) {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    val wpm = WallpaperManager.getInstance(this@TestFullActivity)
                    val ins: InputStream
                    try {
                        ins = URL(getIntent().data.toString()).openStream()
                        wpm.setStream(ins)
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(
                        this@TestFullActivity,
                        "Permission is not given",
                        Toast.LENGTH_SHORT
                    ).show()
                    requestStoragePermission()
                }
            }
        } else {
            setWall()
            binding.browserFull1.visibility = View.VISIBLE
            binding.downloadFull.setOnClickListener { view: View? ->
                when (pref!!.imageQuality) {
                    "Default" -> {
                        downloadWallpaper(mImg, this@TestFullActivity)
                    }
                    "High Quality" -> {
                        downloadWallpaper(large, this@TestFullActivity)
                    }
                    else -> {
                        downloadWallpaper(mImg, this@TestFullActivity)
                    }
                }
            }
            share()
        }
        binding.browserFull1.setOnClickListener { v: View? ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(PhotoUrl))
            startActivity(browserIntent)
        }
    }

    private fun setThemeBased() {
        when (pref!!.theme) {
            "Light" -> {
                val res = resources //resource handle
                val drawable = res.getDrawable(R.drawable.basic_design1_white)
                binding.toolBarFull.background = drawable
                binding.backExpFull.setImageResource(R.drawable.ic_arrow_back)
                binding.toolBarFull.background = drawable
                binding.browserFull1.setImageResource(R.drawable.ic_global_black)
                //            binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                /*           binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black);
                binding.downloadFull.setImageResource(R.drawable.ic_file_download_black);
                binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp);*/
            }
            "Dark" -> {
    
    /*            binding.setWallFull.setImageResource(R.drawable.ic_wallpaper);
                binding.shareFull.setImageResource(R.drawable.ic_share);
                binding.downloadFull.setImageResource(R.drawable.ic_file_download);*/
                binding.backExpFull.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                binding.browserFull1.setImageResource(R.drawable.ic_global)
                val res = resources //resource handle
                val drawable = res.getDrawable(R.drawable.basic_design1)
                binding.toolBarFull.background = drawable
                //            binding.mainBar.setCardBackgroundColor(Color.parseColor("#000000"));
            }
            else -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        /*                    binding.setWallFull.setImageResource(R.drawable.ic_wallpaper);
                        binding.shareFull.setImageResource(R.drawable.ic_share);
                        binding.downloadFull.setImageResource(R.drawable.ic_file_download);*/binding.backExpFull.setImageResource(
                            R.drawable.ic_arrow_back_black_24dp
                        )
                        binding.browserFull1.setImageResource(R.drawable.ic_global)
                        val res = resources //resource handle
                        val drawable = res.getDrawable(R.drawable.basic_design1)
                        binding.toolBarFull.background = drawable
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        //                    binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        binding.setWallFull.setImageResource(R.drawable.ic_wallpaper_black)
                        binding.downloadFull.setImageResource(R.drawable.ic_file_download_black)
                        binding.shareFull.setImageResource(R.drawable.ic_share_black_24dp)
                        //                    binding.mainBar.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        val res1 = resources //resource handle
                        val drawable1 = res1.getDrawable(R.drawable.basic_design1_white)
                        binding.toolBarFull.background = drawable1
                        binding.backExpFull.setImageResource(R.drawable.ic_arrow_back)
                        binding.browserFull1.setImageResource(R.drawable.ic_global_black)
                    }
                }
            }
        }
    }

    private fun share() {
        binding.shareFull.setOnClickListener { view: View? ->
            val granted = checkWriteExternalPermission()
            if (granted == true) {
                /*  ProgressBar progressBar=view.findViewById(R.id.progress6);
                progressBar.setVisibility(View.VISIBLE);*/
                if (pref!!.imageQuality == "Default") {
                    mProgressDialog = ProgressDialog(this@TestFullActivity)
                    mProgressDialog!!.setMessage("Setting...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.isIndeterminate = true
                    mProgressDialog!!.setProgressNumberFormat(null)
                    mProgressDialog!!.setProgressPercentFormat(null)
                    mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    mProgressDialog!!.onStart()
                    mProgressDialog!!.show()
                    Glide.with(this@TestFullActivity).asBitmap()
                        .load(mImg)
                        .into(object : SimpleTarget<Bitmap?>() {

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/jpeg"
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, "title")
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                val uri = this@TestFullActivity.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val outstream: OutputStream?
                                try {
                                    outstream =
                                        this@TestFullActivity.contentResolver.openOutputStream(
                                            uri!!
                                        )
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
                                    outstream!!.close()
                                } catch (e: Exception) {
                                    System.err.println(e.toString())
                                }
                                share.putExtra(Intent.EXTRA_STREAM, uri)
                                share.type = "text/plain"
                                share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall")
                                var shareMessage = "\nDownload this application from PlayStore\n\n"
                                shareMessage += mImg
                                share.putExtra(Intent.EXTRA_TEXT, "Weather Wall$shareMessage")
                                startActivity(Intent.createChooser(share, "Share Image"))
                                // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                /* progressBar.setVisibility(View.GONE);*/
                            }
                        })
                    mProgressDialog!!.hide()
                } else if (pref!!.imageQuality == "High quality") {
                    mProgressDialog = ProgressDialog(this@TestFullActivity)
                    mProgressDialog!!.setMessage("Setting...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.isIndeterminate = true
                    mProgressDialog!!.setProgressNumberFormat(null)
                    mProgressDialog!!.setProgressPercentFormat(null)
                    mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    mProgressDialog!!.onStart()
                    mProgressDialog!!.show()
                    Glide.with(this@TestFullActivity).asBitmap()
                        .load(large)
                        .into(object : SimpleTarget<Bitmap?>() {

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/jpeg"
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, "title")
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                val uri = this@TestFullActivity.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val outstream: OutputStream?
                                try {
                                    outstream =
                                        this@TestFullActivity.contentResolver.openOutputStream(
                                            uri!!
                                        )
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
                                    outstream!!.close()
                                } catch (e: Exception) {
                                    System.err.println(e.toString())
                                }
                                share.putExtra(Intent.EXTRA_STREAM, uri)
                                share.type = "text/plain"
                                share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall")
                                var shareMessage = "\nDownload this application from PlayStore\n\n"
                                shareMessage += large
                                share.putExtra(Intent.EXTRA_TEXT, "Weather Wall$shareMessage")
                                startActivity(Intent.createChooser(share, "Share Image"))
                                // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                /* progressBar.setVisibility(View.GONE);*/                            }
                        })
                    mProgressDialog!!.hide()
                } else {
                    mProgressDialog = ProgressDialog(this@TestFullActivity)
                    mProgressDialog!!.setMessage("Setting...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.isIndeterminate = true
                    mProgressDialog!!.setProgressNumberFormat(null)
                    mProgressDialog!!.setProgressPercentFormat(null)
                    mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    mProgressDialog!!.onStart()
                    mProgressDialog!!.show()
                    Glide.with(this@TestFullActivity).asBitmap()
                        .load(mImg)
                        .into(object : SimpleTarget<Bitmap?>() {


                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/jpeg"
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, "title")
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                val uri = this@TestFullActivity.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val outstream: OutputStream?
                                try {
                                    outstream =
                                        this@TestFullActivity.contentResolver.openOutputStream(
                                            uri!!
                                        )
                                    resource.compress(Bitmap.CompressFormat.JPEG, 100, outstream)
                                    outstream!!.close()
                                } catch (e: Exception) {
                                    System.err.println(e.toString())
                                }
                                share.putExtra(Intent.EXTRA_STREAM, uri)
                                share.type = "text/plain"
                                share.putExtra(Intent.EXTRA_SUBJECT, "Weather Wall")
                                var shareMessage = "\nDownload this application from PlayStore\n\n"
                                shareMessage = shareMessage + mImg
                                share.putExtra(Intent.EXTRA_TEXT, "Weather Wall$shareMessage")
                                startActivity(Intent.createChooser(share, "Share Image"))
                                // ProgressBar progressBar=view.findViewById(R.id.progress6);
                                /* progressBar.setVisibility(View.GONE);*/                            }
                        })
                    mProgressDialog!!.hide()
                }
            } else {
                Toast.makeText(this@TestFullActivity, "2", Toast.LENGTH_LONG).show()
                requestStoragePermission()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setWall() {
        binding.setWallFull.setOnClickListener { view: View? ->
            Log.d("wefe", "ewf")
            val granted = checkWriteExternalPermission()
            if (granted) {
                if (pref!!.imageQuality == "Default") {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    val wpm = WallpaperManager.getInstance(this@TestFullActivity)
                    val ins: InputStream
                    try {
                        ins = URL(mImg).openStream()
                        wpm.setStream(ins)
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else if (pref!!.imageQuality == "High Quality") {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    val wpm = WallpaperManager.getInstance(this@TestFullActivity)
                    val ins: InputStream
                    try {
                        ins = URL(large).openStream()
                        wpm.setStream(ins)
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    val wpm = WallpaperManager.getInstance(this@TestFullActivity)
                    val ins: InputStream
                    try {
                        ins = URL(mImg).openStream()
                        wpm.setStream(ins)
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            } else {
                Toast.makeText(this@TestFullActivity, "Permission is not given", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun checkWriteExternalPermission(): Boolean {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val res = applicationContext.checkCallingOrSelfPermission(permission)
        return res == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed because of this and that")
                .setPositiveButton("ok") { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this@TestFullActivity, arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ), STORAGE_PERMISSION_CODE
                    )
                }
                .setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }.create().show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                /*Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();*/
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //        binding.blurLayout.startBlur();
        binding.blurLayoutData.startBlur()
        binding.blurLayoutData1.startBlur()
        binding.blurLayoutData2.startBlur()
        requestStoragePermission()
    } /*    private void applyBlur() {
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