package com.client.vpman.weatherwall.ui.Activity

import androidx.appcompat.app.AppCompatActivity
import com.client.vpman.weatherwall.Adapter.TestingAdapter
import com.client.vpman.weatherwall.model.ModelData
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import android.os.Parcelable
import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Bundle
import android.view.WindowManager
import android.content.Intent
import android.content.res.Configuration
import com.client.vpman.weatherwall.R
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.load.engine.DiskCacheStrategy
import android.graphics.Bitmap
import android.graphics.Color
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.LruCache
import android.view.View
import com.bumptech.glide.load.engine.GlideException
import com.client.vpman.weatherwall.ui.Activity.TestingMotionLayout
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.StringRequest
import org.json.JSONObject
import org.json.JSONArray
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.DefaultItemAnimator
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import org.json.JSONException
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.databinding.ActivityTestingMotionLayoutBinding
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class TestingMotionLayout : AppCompatActivity() {
    var query: String? = null
    var testingAdapter: TestingAdapter? = null
    var modelData4List: ArrayList<ModelData> = ArrayList()
    var sharedPref1: SharedPref1? = null
    private var apiList: ArrayList<String> = ArrayList()
    private var mListState: Parcelable? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var binding: ActivityTestingMotionLayoutBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestingMotionLayoutBinding.inflate(
            layoutInflater
        )
        val view1: View = binding!!.root
        setContentView(view1)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        sharedPref1 = SharedPref1(this@TestingMotionLayout)
        val intent = intent
        val mImg = intent.getStringExtra("img1")
        val sImg = intent.getStringExtra("img2")
        query = intent.getStringExtra("query")
        val Landscape = intent.getStringExtra("text")
        binding!!.titleData.text = Landscape
        if (sharedPref1!!.theme == "Light") {
            binding!!.titleData.setTextColor(Color.parseColor("#000000"))
            binding!!.backMotion.setImageResource(R.drawable.ic_arrow_back)
            binding!!.motionBackground.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding!!.backgroundDesign.setImageResource(R.drawable.basic_design_customized_white)
        } else if (sharedPref1!!.theme == "Dark") {
            binding!!.titleData.setTextColor(Color.parseColor("#FFFFFF"))
            binding!!.backMotion.setImageResource(R.drawable.ic_arrow_back_black_24dp)
            binding!!.motionBackground.setBackgroundColor(Color.parseColor("#000000"))
            binding!!.backgroundDesign.setImageResource(R.drawable.basic_design_customized)
        } else {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    binding!!.titleData.setTextColor(Color.parseColor("#FFFFFF"))
                    binding!!.backMotion.setImageResource(R.drawable.ic_arrow_back_black_24dp)
                    binding!!.motionBackground.setBackgroundColor(Color.parseColor("#000000"))
                    binding!!.backgroundDesign.setImageResource(R.drawable.basic_design_customized)
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    binding!!.titleData.setTextColor(Color.parseColor("#000000"))
                    binding!!.backMotion.setImageResource(R.drawable.ic_arrow_back)
                    binding!!.motionBackground.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding!!.backgroundDesign.setImageResource(R.drawable.basic_design_customized_white)
                }
            }
        }
        binding!!.backMotion.setOnClickListener { v: View? -> onBackPressed() }
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
        requestOptions.placeholder(Utils.randomDrawbleColor)
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
            binding!!.toolbarImage.setImageBitmap(image)
        } else {
            Glide.with(this@TestingMotionLayout)
                .load(mImg)
                .thumbnail(
                    Glide.with(this@TestingMotionLayout).load(sImg)
                )
                .apply(requestOptions)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any,
                        target: Target<Drawable?>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any,
                        target: Target<Drawable?>,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(binding!!.toolbarImage)
        }
        LoadImage(page)
        Log.d("onScrolled", "Data1")
        binding!!.recyclerviewTesting.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (binding!!.recyclerviewTesting.canScrollVertically(1)) {
                    if (linearLayoutManager!!.findLastVisibleItemPosition() == testingAdapter!!.itemCount - 1) {
                        page++
                        Log.d("onScrolled", "Data2")
                        LoadImage(page)
                    }
                }
            }
        })
    }

    fun LoadImage(page: Int) {
        modelData4List = ArrayList()
        val Url = Constant.BASE_URL + query + "&per_page=80&page=" + page + ""
        Log.d("urlData", Url)
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, Response.Listener { response: String? ->
                Log.d("response", response!!)
                try {
                    val obj = JSONObject(response)
                    val wallArray = obj.getJSONArray("photos")
                    for (i in 0 until wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val PhotoUrl = JSONObject(wallobj.toString())
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
                        modelData4List.add(modelData1)
                    }
                    Collections.shuffle(modelData4List)
                    testingAdapter = TestingAdapter(this@TestingMotionLayout, modelData4List)
                    linearLayoutManager = GridLayoutManager(
                        this@TestingMotionLayout,
                        2,
                        GridLayoutManager.VERTICAL,
                        false
                    )
                    binding!!.recyclerviewTesting.layoutManager = linearLayoutManager
                    binding!!.recyclerviewTesting.setHasFixedSize(true)
                    binding!!.recyclerviewTesting.itemAnimator = DefaultItemAnimator()
                    binding!!.recyclerviewTesting.isNestedScrollingEnabled = true
                    val itemViewType = 0
                    binding!!.recyclerviewTesting.recycledViewPool.setMaxRecycledViews(
                        itemViewType,
                        0
                    )
                    binding!!.recyclerviewTesting.adapter = testingAdapter
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
                    } // returned data is not JSONObject?
                    catch (e1: JSONException) {
                        e1.printStackTrace()
                    }
                }*/
            }) {
                override fun getHeaders(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    apiList = ArrayList()
                    apiList.add(getString(R.string.APIKEY1))
                    apiList.add(getString(R.string.APIKEY2))
                    apiList.add(getString(R.string.APIKEY3))
                    apiList.add(getString(R.string.APIKEY4))
                    apiList.add(getString(R.string.APIKEY5))
                    val random = Random()
                    val n = random.nextInt(apiList.size)
                    params["Authorization"] = apiList.get(n)
                    return params
                }
            }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(this@TestingMotionLayout)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mListState = linearLayoutManager!!.onSaveInstanceState()
        outState.putParcelable(BUNDLE_LIST_STATE, mListState)
        super.onSaveInstanceState(outState)
    }

    public override fun onResume() {
        if (mListState != null) {
            linearLayoutManager!!.onRestoreInstanceState(mListState)
        }
        super.onResume()
    }

    companion object {
        var page = 1
        const val BUNDLE_LIST_STATE = "list_state"
    }
}