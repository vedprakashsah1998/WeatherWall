package com.client.vpman.weatherwall.ui.Activity

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.client.vpman.weatherwall.Adapter.ExploreAdapter
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ActivityExploreAcitivityBinding
import com.client.vpman.weatherwall.model.ModelData
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class ExploreAcitivity : AppCompatActivity() {
    var query: String? = null
    private var model5: ArrayList<ModelData> = ArrayList()
    var exploreAdapter: ExploreAdapter? = null
    var sharedPref1: SharedPref1? = null
    private lateinit var binding: ActivityExploreAcitivityBinding
    private var apiList: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExploreAcitivityBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sharedPref1 = SharedPref1(this@ExploreAcitivity)
        val intent = intent
        val mImg = intent.getStringExtra("imgData")
        val sImg = intent.getStringExtra("imgDataSmall")
        query = intent.getStringExtra("query")
        val text = intent.getStringExtra("text")
        binding.titleDataExp.text = text
        when (sharedPref1!!.theme) {
            "Light" -> {
                binding.titleDataExp.setTextColor(Color.parseColor("#000000"))
                binding.backMotionExp.setImageResource(R.drawable.ic_arrow_back)
                binding.motionBackgroundExplore.setBackgroundColor(Color.parseColor("#FFFFFF"))
                binding.backgroundDesignExp.setImageResource(R.drawable.basic_design_customized_white)
            }
            "Dark" -> {
                binding.titleDataExp.setTextColor(Color.parseColor("#FFFFFF"))
                binding.backMotionExp.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                binding.motionBackgroundExplore.setBackgroundColor(Color.parseColor("#000000"))
                binding.backgroundDesignExp.setImageResource(R.drawable.basic_design_customized)
            }
            else -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> {
                        binding.titleDataExp.setTextColor(Color.parseColor("#FFFFFF"))
                        binding.backMotionExp.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                        binding.motionBackgroundExplore.setBackgroundColor(Color.parseColor("#000000"))
                        binding.backgroundDesignExp.setImageResource(R.drawable.basic_design_customized)
                    }
                    Configuration.UI_MODE_NIGHT_NO -> {
                        binding.titleDataExp.setTextColor(Color.parseColor("#000000"))
                        binding.backMotionExp.setImageResource(R.drawable.ic_arrow_back)
                        binding.motionBackgroundExplore.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        binding.backgroundDesignExp.setImageResource(R.drawable.basic_design_customized_white)
                    }
                }
            }
        }
        binding.backMotionExp.setOnClickListener { v: View? -> onBackPressed() }
        val requestOptions = RequestOptions()
        requestOptions.error(Utils.randomDrawbleColor)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
        requestOptions.priority(Priority.IMMEDIATE)
        requestOptions.skipMemoryCache(false)
        requestOptions.onlyRetrieveFromCache(true)
        requestOptions.priority(Priority.HIGH)
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
            binding.imageExplore.setImageBitmap(image)
        } else {
            Glide.with(this@ExploreAcitivity)
                .load(mImg)
                .thumbnail(
                    Glide.with(this@ExploreAcitivity).load(sImg)
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
                .into(binding.imageExplore)
        }
        model5 = ArrayList()
        exploreAdapter = ExploreAdapter(this@ExploreAcitivity, model5)
        val linearLayoutManager: LinearLayoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false)
        binding.recylerViewExploreData.layoutManager = linearLayoutManager
        binding.recylerViewExploreData.setHasFixedSize(true)
        binding.recylerViewExploreData.itemAnimator = DefaultItemAnimator()
        binding.recylerViewExploreData.isNestedScrollingEnabled = true
        val itemViewType = 0
        binding.recylerViewExploreData.recycledViewPool.setMaxRecycledViews(itemViewType, 0)
        binding.recylerViewExploreData.adapter = exploreAdapter
        binding.recylerViewExploreData.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (linearLayoutManager.findLastVisibleItemPosition() == exploreAdapter!!.itemCount - 1) {
                    page++
                    LoadImage(page)
                }
            }
        })
        LoadImage(page)
    }

    fun LoadImage(page: Int) {
        val Url = Constant.BASE_URL + query + "&per_page=100&page=" + page + ""
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, Response.Listener { response: String? ->
                Log.d("response", response!!)
                try {
                    val obj = JSONObject(response)
                    Log.d("mil gaya", obj.toString())
                    val totalRes = obj.getInt("total_results")
                    Log.d("werg", totalRes.toString())
                    val wallArray = obj.getJSONArray("photos")
                    for (i in 0 until wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        Log.d("PhotoURL", wallobj.getString("url"))
                        val jsonObject = wallobj.getJSONObject("src")
                        val `object` = JSONObject(jsonObject.toString())
                        val modelData1 = ModelData(
                            `object`.getString("large2x"),
                            photographer.getString("photographer"),
                            `object`.getString("large"),
                            `object`.getString("original"),
                            wallobj.getString("url"),
                            wallobj.getString("photographer_url")
                        )
                        model5!!.add(modelData1)
                    }
                    model5?.shuffle()
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
                    apiList = ArrayList()
                    apiList!!.add(getString(R.string.APIKEY1))
                    apiList!!.add(getString(R.string.APIKEY2))
                    apiList!!.add(getString(R.string.APIKEY3))
                    apiList!!.add(getString(R.string.APIKEY4))
                    apiList!!.add(getString(R.string.APIKEY5))
                    val random = Random()
                    val n = random.nextInt(apiList!!.size)
                    params["Authorization"] = apiList!![n]
                    return params
                }
            }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(this@ExploreAcitivity)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    companion object {
        var page = 1
    }
}