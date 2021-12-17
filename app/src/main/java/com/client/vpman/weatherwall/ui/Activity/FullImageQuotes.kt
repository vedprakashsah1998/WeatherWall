package com.client.vpman.weatherwall.ui.Activity

import android.Manifest
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.util.LruCache
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.signature.ObjectKey
import com.client.vpman.weatherwall.CustomeUsefullClass.DownloadImageKTX.Companion.downloadWallpaper
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ActivityFullImageQuotesBinding
import com.client.vpman.weatherwall.model.RandomQuotes1
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class FullImageQuotes : AppCompatActivity() {
    var mImg: String? = null
    var sImg: String? = null
    var largeImg: String? = null
    var photoUrl: String? = null
    var list: List<String>? = null
    var randomQuotes: ArrayList<RandomQuotes1> = ArrayList()
    var mProgressDialog: ProgressDialog? = null
    private val STORAGE_PERMISSION_CODE = 1
    var pref: SharedPref1? = null
    var binding: ActivityFullImageQuotesBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullImageQuotesBinding.inflate(
            layoutInflater
        )
        val view1: View = binding!!.root
        setContentView(view1)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        pref = SharedPref1(this@FullImageQuotes)
        if (pref!!.theme == "Light") {
            val res = resources //resource handle
            val drawable = res.getDrawable(R.drawable.basic_design1_white)
            binding!!.tool1barMain.background = drawable
            binding!!.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back)
            binding!!.downloadImg.setImageResource(R.drawable.ic_file_download_black)
            binding!!.browser.setImageResource(R.drawable.ic_global_black)
        } else if (pref!!.theme == "Dark") {
            binding!!.tool1barMain.setBackgroundColor(Color.parseColor("#000000"))
            binding!!.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
            binding!!.browser.setImageResource(R.drawable.ic_global)
            binding!!.downloadImg.setImageResource(R.drawable.ic_file_download)
            val res = resources //resource handle
            val drawable = res.getDrawable(R.drawable.basic_design1)
            binding!!.tool1barMain.background = drawable
        } else {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    binding!!.tool1barMain.setBackgroundColor(Color.parseColor("#000000"))
                    binding!!.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
                    binding!!.browser.setImageResource(R.drawable.ic_global)
                    binding!!.downloadImg.setImageResource(R.drawable.ic_file_download)
                    val res = resources //resource handle
                    val drawable = res.getDrawable(R.drawable.basic_design1)
                    binding!!.tool1barMain.background = drawable
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    val res1 = resources //resource handle
                    val drawable1 = res1.getDrawable(R.drawable.basic_design1_white)
                    binding!!.tool1barMain.background = drawable1
                    binding!!.tool1barMain.setNavigationIcon(R.drawable.ic_arrow_back)
                    binding!!.browser.setImageResource(R.drawable.ic_global_black)
                    binding!!.downloadImg.setImageResource(R.drawable.ic_file_download_black)
                }
            }
        }
        val intent = intent
        mImg = intent.getStringExtra("imgDataAdapter")
        sImg = intent.getStringExtra("imgDataAdapterSmall")
        largeImg = intent.getStringExtra("largeImg")
        photoUrl = intent.getStringExtra("photoUrl")
        binding!!.tool1barMain.title = ""
        setSupportActionBar(binding!!.tool1barMain)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        val requestOptions = RequestOptions()
        // requestOptions.error(Utils.getRandomDrawbleColor());
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
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
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
            binding!!.imageFullLast.setImageBitmap(image)
        } else {
            if (pref!!.imageQuality == "Default") {
                Glide.with(this@FullImageQuotes)
                    .load(mImg)
                    .thumbnail(
                        Glide.with(this@FullImageQuotes).load(mImg)
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
                            return false
                        }
                    })
                    .into(binding!!.imageFullLast)
            } else if (pref!!.imageQuality == "High Quality") {
                Glide.with(this@FullImageQuotes)
                    .load(largeImg)
                    .thumbnail(
                        Glide.with(this@FullImageQuotes).load(mImg)
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
                            return false
                        }
                    })
                    .into(binding!!.imageFullLast)
            } else {
                Glide.with(this@FullImageQuotes)
                    .load(mImg)
                    .thumbnail(
                        Glide.with(this@FullImageQuotes).load(mImg)
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
                            return false
                        }
                    })
                    .into(binding!!.imageFullLast)
            }
        }
        binding!!.setWallQuotes.setOnClickListener { view: View? ->
            Log.d("wefe", "ewf")
            val granted = checkWriteExternalPermission()
            if (granted) {
                if (pref!!.imageQuality == "Default") {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    val wpm = WallpaperManager.getInstance(this@FullImageQuotes)
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
                    val wpm = WallpaperManager.getInstance(this@FullImageQuotes)
                    val ins: InputStream
                    try {
                        ins = URL(largeImg).openStream()
                        wpm.setStream(ins)
                        Toast.makeText(this, "Wallpaper is Set", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                    val wpm = WallpaperManager.getInstance(this@FullImageQuotes)
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
                Toast.makeText(this@FullImageQuotes, "Permission is not given", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding!!.shareQuotes.setOnClickListener { view: View? ->
            val granted = checkWriteExternalPermission()
            if (granted) {
                if (pref!!.imageQuality == "Default") {
                    mProgressDialog = ProgressDialog(this@FullImageQuotes)
                    mProgressDialog!!.setMessage("Setting...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.isIndeterminate = true
                    mProgressDialog!!.setProgressNumberFormat(null)
                    mProgressDialog!!.setProgressPercentFormat(null)
                    mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    mProgressDialog!!.onStart()
                    mProgressDialog!!.show()
                    Glide.with(this@FullImageQuotes).asBitmap()
                        .load(mImg)
                        .into(object : SimpleTarget<Bitmap?>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/jpeg"
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, "title")
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                val uri = this@FullImageQuotes.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val outstream: OutputStream?
                                try {
                                    outstream =
                                        this@FullImageQuotes.contentResolver.openOutputStream(
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
                                startActivity(Intent.createChooser(share, "Share Image"))                            }
                        })
                    mProgressDialog!!.hide()
                } else if (pref!!.imageQuality == "High Quality") {
                    mProgressDialog = ProgressDialog(this@FullImageQuotes)
                    mProgressDialog!!.setMessage("Setting...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.isIndeterminate = true
                    mProgressDialog!!.setProgressNumberFormat(null)
                    mProgressDialog!!.setProgressPercentFormat(null)
                    mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    mProgressDialog!!.onStart()
                    mProgressDialog!!.show()
                    Glide.with(this@FullImageQuotes).asBitmap()
                        .load(largeImg)
                        .into(object : SimpleTarget<Bitmap?>() {

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/jpeg"
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, "title")
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                val uri = this@FullImageQuotes.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val outstream: OutputStream?
                                try {
                                    outstream =
                                        this@FullImageQuotes.contentResolver.openOutputStream(
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
                                shareMessage = shareMessage + largeImg
                                share.putExtra(Intent.EXTRA_TEXT, "Weather Wall$shareMessage")
                                startActivity(Intent.createChooser(share, "Share Image"))                            }
                        })
                    mProgressDialog!!.hide()
                } else {
                    mProgressDialog = ProgressDialog(this@FullImageQuotes)
                    mProgressDialog!!.setMessage("Setting...")
                    mProgressDialog!!.setCancelable(false)
                    mProgressDialog!!.isIndeterminate = true
                    mProgressDialog!!.setProgressNumberFormat(null)
                    mProgressDialog!!.setProgressPercentFormat(null)
                    mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                    mProgressDialog!!.onStart()
                    mProgressDialog!!.show()
                    Glide.with(this@FullImageQuotes).asBitmap()
                        .load(mImg)
                        .into(object : SimpleTarget<Bitmap?>() {

                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap?>?
                            ) {
                                val share = Intent(Intent.ACTION_SEND)
                                share.type = "image/jpeg"
                                val values = ContentValues()
                                values.put(MediaStore.Images.Media.TITLE, "title")
                                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                                val uri = this@FullImageQuotes.contentResolver.insert(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    values
                                )
                                val outstream: OutputStream?
                                try {
                                    outstream =
                                        this@FullImageQuotes.contentResolver.openOutputStream(
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
                                startActivity(Intent.createChooser(share, "Share Image"))                            }
                        })
                    mProgressDialog!!.hide()
                }
            } else {
                Toast.makeText(this@FullImageQuotes, "2", Toast.LENGTH_LONG).show()
                requestStoragePermission()
            }
        }
        binding!!.downloadImg.setOnClickListener { view: View? ->
            if (pref!!.imageQuality == "Default") {
                downloadWallpaper(mImg, this@FullImageQuotes)
            } else if (pref!!.imageQuality == "High Quality") {
                downloadWallpaper(largeImg, this@FullImageQuotes)
            } else {
                downloadWallpaper(mImg, this@FullImageQuotes)
            }
        }
        binding!!.browser.setOnClickListener { v: View? ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(photoUrl))
            startActivity(browserIntent)
        }
        requestStoragePermission()
        list = ArrayList()
        Quotes()
    }

    fun Quotes() {
        randomQuotes = ArrayList()
        val QuotesUrl = "https://type.fit/api/quotes"
        Log.d("sdfljh", "khwqgdi")
        val stringRequest = StringRequest(Request.Method.GET, QuotesUrl, { response: String? ->
            Log.d("qoefg", response!!)
            try {
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    Log.d("eouf", jsonObject.toString())
                    val jsonObject1 = JSONObject(jsonObject.toString())
                    val randomQuotes1 = RandomQuotes1(
                        jsonObject1.getString("text"),
                        jsonObject1.getString("author")
                    )
                    randomQuotes.add(randomQuotes1)
                }
                randomQuotes.shuffle()
                val random = Random()
                val n = random.nextInt(randomQuotes.size)
                binding!!.quotesTextMain.text = randomQuotes[n].quotes
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(this@FullImageQuotes)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        supportFinishAfterTransition()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
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
                .setPositiveButton("ok") { _: DialogInterface?, _: Int ->
                    ActivityCompat.requestPermissions(
                        this@FullImageQuotes, arrayOf(
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
/*
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
*/
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }
}