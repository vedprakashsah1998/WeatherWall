package com.client.vpman.weatherwall.ui.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.StrictMode
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.util.LruCache
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.client.vpman.weatherwall.Adapter.DemoFragmentStateAdapter
import com.client.vpman.weatherwall.CustomeUsefullClass.*
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ActivityMainBinding
import com.client.vpman.weatherwall.model.ModelData1
import com.flaviofaria.kenburnsview.KenBurnsView
import com.flaviofaria.kenburnsview.Transition
import com.github.ybq.android.spinkit.style.Wave
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), OnDataPass, OnTabSelectedListener {
    var slides: ArrayList<String> = ArrayList()
    var listModelData: ArrayList<ModelData1> = ArrayList()
    private var Url: String? = null
    var timer = Timer()
    var query: String? = null
    var sharedPref1: SharedPref1? = null
    var wanderingCubes: Wave? = null
    var binding: ActivityMainBinding? = null
    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("BatteryLife")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(
            layoutInflater
        )
        val view1: View = binding!!.root
        setContentView(view1)
        wanderingCubes = Wave()
        binding!!.spinKit.setIndeterminateDrawable(wanderingCubes)
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("WEATHER"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("DISCOVERY"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("POPULAR"))
        binding!!.tabLayout.addTab(binding!!.tabLayout.newTab().setText("EXPLORE"))
        binding!!.tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("WEATHER"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("DISCOVERY"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("POPULAR"))
        binding!!.tabLayout2.addTab(binding!!.tabLayout2.newTab().setText("EXPLORE"))
        binding!!.tabLayout2.tabGravity = TabLayout.GRAVITY_FILL
        if (Build.VERSION.SDK_INT >= 21) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        sharedPref1 = SharedPref1(this@MainActivity)
        if (sharedPref1!!.theme == "Light") {
            binding!!.rotateLayout.visibility = View.VISIBLE
            binding!!.rotateLayout2.visibility = View.GONE
            binding!!.tabLayout.visibility = View.VISIBLE
            binding!!.tabLayout2.visibility = View.GONE
        } else if (sharedPref1!!.theme == "Dark") {
            binding!!.rotateLayout.visibility = View.GONE
            binding!!.rotateLayout2.visibility = View.VISIBLE
            binding!!.tabLayout2.visibility = View.VISIBLE
            binding!!.tabLayout.visibility = View.GONE
        } else {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    binding!!.rotateLayout.visibility = View.GONE
                    binding!!.rotateLayout2.visibility = View.VISIBLE
                    binding!!.tabLayout2.visibility = View.VISIBLE
                    binding!!.tabLayout.visibility = View.GONE
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    binding!!.rotateLayout.visibility = View.VISIBLE
                    binding!!.rotateLayout2.visibility = View.GONE
                    binding!!.tabLayout.visibility = View.VISIBLE
                    binding!!.tabLayout2.visibility = View.GONE
                }
            }
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent()
            val packageName = packageName
            val pm = getSystemService(POWER_SERVICE) as PowerManager
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                startActivity(intent)
            }
        }
        binding!!.imageView.setTransitionListener(object : KenBurnsView.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionEnd(transition: Transition) {
                binding!!.imageView.resume()
            }
        })
        binding!!.pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding!!.pager.currentItem = 0
                    binding!!.imageView.visibility = View.VISIBLE
                } else {
                    binding!!.imageView.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding!!.pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding!!.tabLayout))
        binding!!.pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding!!.tabLayout2))
        val adapter = DemoFragmentStateAdapter(supportFragmentManager)
        binding!!.pager.adapter = adapter
        binding!!.tabLayout.addOnTabSelectedListener(this@MainActivity)
        binding!!.tabLayout2.addOnTabSelectedListener(this@MainActivity)
        listModelData = ArrayList()
    }

    fun loadImage() {
        slides = ArrayList()
        Log.d("iueho", Url!!)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, label@ Response.Listener { response: String? ->
                Log.d("response", response!!)
                try {
                    val obj = JSONObject(response)
                    val wallArray = obj.getJSONArray("photos")
                    var i = 0
                    while (i < wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val ProfileUrl = JSONObject(wallobj.toString())
                        val jsonObject = wallobj.getJSONObject("src")
                        val `object` = JSONObject(jsonObject.toString())
                        val modelData3 = ModelData1(
                            `object`.getString("large2x"),
                            photographer.getString("photographer"),
                            `object`.getString("large")
                        )
                        listModelData!!.add(modelData3)
                        i++
                    }
                    listModelData?.shuffle()
                    val random = Random()
                    val n = random.nextInt(listModelData!!.size)
                    Log.d("regr", listModelData!![n].toString())
                    val requestOptions = RequestOptions()
                    // requestOptions.error(Utils.getRandomDrawbleColor());
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
                    requestOptions.priority(com.bumptech.glide.Priority.IMMEDIATE)
                    requestOptions.skipMemoryCache(false)
                    requestOptions.onlyRetrieveFromCache(true)
                    requestOptions.priority(com.bumptech.glide.Priority.HIGH)
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
                    val display = windowManager.defaultDisplay
                    val size = Point()
                    display.getSize(size)
                    val width = size.x //width of screen in pixels
                    val height = size.y
                    val image = memCache["imagefile"]
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding!!.imageView.setImageBitmap(image)
                    } else {
                        Glide.with(this@MainActivity)
                            .load(listModelData!![n]!!.large2x)
                            .thumbnail(
                                Glide.with(this@MainActivity).load(listModelData!![n]!!.large)
                            )
                            .apply(requestOptions)
                            .listener(object : RequestListener<Drawable?> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding!!.spinKit.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding!!.spinKit.visibility = View.GONE
                                    return false
                                }
                            }).centerInside()
                            .into(binding!!.imageView)
                    }


                    // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError ->
                val response = error.networkResponse
                /*if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8")
                        )
                        // Now you can use any deserializer to make sense of data
                        val obj = JSONObject(res)
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }*/
            }) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] =
                        "563492ad6f91700001000001572b44febff5465797575bcba703c98c"
                    return params
                }
            }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(this@MainActivity)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun loadImage1() {
        slides = ArrayList()
        Log.d("iueho", Url!!)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, Response.Listener { response: String? ->
                Log.d("response", response!!)
                try {
                    val obj = JSONObject(response)
                    Log.d("mil gaya", obj.toString())
                    val totalRes = obj.getInt("total_results")
                    if (totalRes <= 2) {
                        loadPixabayImg()
                    }
                    Log.d("werg", totalRes.toString())
                    val wallArray = obj.getJSONArray("photos")
                    var i = 0
                    while (i < wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val ProfileUrl = JSONObject(wallobj.toString())
                        val jsonObject = wallobj.getJSONObject("src")
                        val `object` = JSONObject(jsonObject.toString())
                        slides.add(`object`.getString("large2x"))
                        i++
                    }
                    slides.shuffle()
                    val random = Random()
                    val n = random.nextInt(slides.size)
                    Log.d("regr", slides[n].toString())
                    val requestOptions = RequestOptions()
                    // requestOptions.error(Utils.getRandomDrawbleColor());
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
                    requestOptions.priority(com.bumptech.glide.Priority.IMMEDIATE)
                    requestOptions.skipMemoryCache(false)
                    requestOptions.onlyRetrieveFromCache(true)
                    requestOptions.priority(com.bumptech.glide.Priority.HIGH)
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
                    val display = windowManager.defaultDisplay
                    val size = Point()
                    display.getSize(size)
                    val width = size.x //width of screen in pixels
                    val height = size.y
                    val image = memCache["imagefile"]
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding!!.imageView.setImageBitmap(image)
                    } else {
                        Glide.with(this@MainActivity)
                            .load(slides[n])
                            .thumbnail(
                                Glide.with(this@MainActivity).load(slides[n])
                            )
                            .apply(requestOptions)
                            .listener(object : RequestListener<Drawable?> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding!!.spinKit.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding!!.spinKit.visibility = View.GONE
                                    return false
                                }
                            })
                            .into(binding!!.imageView)
                    }

                    // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError ->
                /*val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8")
                        )
                        // Now you can use any deserializer to make sense of data
                        val obj = JSONObject(res)
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }*/
            }) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] =
                        "563492ad6f91700001000001572b44febff5465797575bcba703c98c"
                    return params
                }
            }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(this@MainActivity)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun loadImage2() {
        slides = ArrayList()
        Log.d("iueho", Url!!)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, Response.Listener { response: String? ->
                Log.d("response", response!!)
                try {
                    val obj = JSONObject(response)
                    Log.d("mil gaya", obj.toString())
                    val totalRes = obj.getInt("total_results")
                    if (totalRes <= 2) {
                        loadPixabayImg()
                    }
                    Log.d("werg", totalRes.toString())
                    val wallArray = obj.getJSONArray("photos")
                    var i = 0
                    while (i < wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val ProfileUrl = JSONObject(wallobj.toString())
                        val jsonObject = wallobj.getJSONObject("src")
                        val `object` = JSONObject(jsonObject.toString())
                        slides.add(`object`.getString("large2x"))
                        i++
                    }
                    slides.shuffle()
                    val random = Random()
                    val n = random.nextInt(slides.size)
                    Log.d("regr", slides[n].toString())
                    val requestOptions = RequestOptions()
                    // requestOptions.error(Utils.getRandomDrawbleColor());
                    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
                    requestOptions.priority(com.bumptech.glide.Priority.IMMEDIATE)
                    requestOptions.skipMemoryCache(false)
                    requestOptions.onlyRetrieveFromCache(true)
                    requestOptions.priority(com.bumptech.glide.Priority.HIGH)
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
                    val display = windowManager.defaultDisplay
                    val size = Point()
                    display.getSize(size)
                    val width = size.x //width of screen in pixels
                    val height = size.y
                    val image = memCache["imagefile"]
                    if (image != null) {
                        //Bitmap exists in cache.
                        binding!!.imageView.setImageBitmap(image)
                    } else {
                        Glide.with(this@MainActivity)
                            .load(slides[n])
                            .thumbnail(
                                Glide.with(this@MainActivity).load(slides[n])
                            )
                            .apply(requestOptions)
                            .listener(object : RequestListener<Drawable?> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding!!.spinKit.visibility = View.GONE
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any,
                                    target: Target<Drawable?>,
                                    dataSource: DataSource,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding!!.spinKit.visibility = View.GONE
                                    return false
                                }
                            })
                            .into(binding!!.imageView)
                    }

                    // Glide.with(MainActivity.this).load(slides.get(n)).preload(500,500);
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error: VolleyError ->
                /*val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8")
                        )
                        // Now you can use any deserializer to make sense of data
                        val obj = JSONObject(res)
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }*/
            }) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["Authorization"] =
                        "563492ad6f91700001000001572b44febff5465797575bcba703c98c"
                    return params
                }
            }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(this@MainActivity)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragments = supportFragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    override fun onDataPass(data: String) {
        Log.d("djbvkj", data)
        query = data.replace(" ", "%20")
        Url = Constant.BASE_URL + query + "&per_page=150&page=1"
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (Connectivity.isConnected(this@MainActivity) && Connectivity.isConnectionFast(
                        ConnectivityManager.TYPE_MOBILE,
                        TelephonyManager.NETWORK_TYPE_EDGE
                    )
                ) {
                    loadImage2()
                } else if (Connectivity.isConnected(this@MainActivity) && Connectivity.isConnectionFast(
                        ConnectivityManager.TYPE_MOBILE,
                        TelephonyManager.NETWORK_TYPE_CDMA
                    )
                ) {
                    loadImage1()
                } else if (Connectivity.isConnected(this@MainActivity) && Connectivity.isConnectionFast(
                        ConnectivityManager.TYPE_MOBILE,
                        TelephonyManager.NETWORK_TYPE_1xRTT
                    )
                ) {
                    loadImage()
                } else if (Connectivity.isConnected(this@MainActivity) && Connectivity.isConnectedWifi(
                        this@MainActivity
                    ) && Connectivity.isConnectedFast(this@MainActivity)
                ) {
                    loadImage()
                } else if (Connectivity.isConnected(this@MainActivity) && Connectivity.isConnectedMobile(
                        this@MainActivity
                    ) && Connectivity.isConnectedFast(this@MainActivity)
                ) {
                    loadImage1()
                } else {
                    loadImage2()
                }
            }
        }, 0, (5 * 60 * 1000).toLong())
    }

    fun loadPixabayImg() {
        slides = ArrayList()
        val jsonUrl =
            "https://pixabay.com/api/?key=13416003-ed8cefc0190df36d75e38fa93q=$query&image_type=photo&safesearch=true"
        Log.d("ihug", query!!)
        val stringRequest1 = StringRequest(
            Request.Method.GET,
            jsonUrl,
            { response: String? ->
               Log.d("erg", response!!)
               try {
                   val obj = JSONObject(response)
                   Log.d("milajewbfk", obj.toString())
                   val wallArray = obj.getJSONArray("hits")
                   var i = 0
                   while (i < wallArray.length()) {
                       val wallObj = wallArray.getJSONObject(i)
                       slides.add(wallObj.getString("largeImageURL"))
                       i++
                   }
                   Collections.shuffle(slides)
                   val random = Random()
                   val n = random.nextInt(slides.size)
                   Log.d("regr", slides[n].toString())
                   val requestOptions = RequestOptions()
                   // requestOptions.error(Utils.getRandomDrawbleColor());
                   requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                       .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
                   requestOptions.priority(Priority.IMMEDIATE)
                   requestOptions.skipMemoryCache(false)
                   requestOptions.onlyRetrieveFromCache(true)
                   requestOptions.priority(Priority.HIGH)
                   requestOptions.isMemoryCacheable
                   requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
                   requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                   requestOptions.placeholder(Utils.randomDrawbleColor)
                   requestOptions.centerCrop()
                   Glide.with(this@MainActivity)
                       .load(slides[n])
                       .thumbnail(
                           Glide.with(this@MainActivity).load(slides[n])
                       )
                       .apply(requestOptions)
                       .listener(object : RequestListener<Drawable?> {
                           override fun onLoadFailed(
                               e: GlideException?,
                               model: Any,
                               target: Target<Drawable?>,
                               isFirstResource: Boolean
                           ): Boolean {
                               binding!!.spinKit.visibility = View.GONE
                               return false
                           }

                           override fun onResourceReady(
                               resource: Drawable?,
                               model: Any,
                               target: Target<Drawable?>,
                               dataSource: DataSource,
                               isFirstResource: Boolean
                           ): Boolean {
                               binding!!.spinKit.visibility = View.GONE
                               return false
                           }
                       })
                       .into(binding!!.imageView)
               } catch (e: Exception) {
                   e.printStackTrace()
               }
           },
            { error: VolleyError ->
                /*val response = error.networkResponse
                if (error is ServerError && response != null) {
                    try {
                        val res = String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, "utf-8")
                        )
                        // Now you can use any deserializer to make sense of data
                        val obj = JSONObject(res)
                    } catch (e1: UnsupportedEncodingException) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace()
                    } catch (e2: JSONException) {
                        // returned data is not JSONObject?
                        e2.printStackTrace()
                    }
                }*/
            })
        stringRequest1.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(this@MainActivity)
        stringRequest1.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest1)
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        binding!!.pager.currentItem = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab) {}
    override fun onTabReselected(tab: TabLayout.Tab) {}
    override fun onBackPressed() {
        if (binding!!.pager.currentItem == 0) {
            super.onBackPressed()
        } else {
            binding!!.pager.currentItem = binding!!.pager.currentItem - 1
        }
    }
}