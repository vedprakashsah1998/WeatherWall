package com.client.vpman.weatherwall.ui.Fragment

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
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
import com.client.vpman.weatherwall.CustomeUsefullClass.Connectivity
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.FragmentPopularBinding
import com.client.vpman.weatherwall.ui.Activity.TestingMotionLayout
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.textview.MaterialTextView
import com.mikhaellopez.circularimageview.CircularImageView
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class Popular : Fragment() {
    private var apiList: ArrayList<String> = ArrayList()
    private lateinit var bounce: Animation
    private var binding: FragmentPopularBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPopularBinding.inflate(inflater, container, false)
        if (activity != null) {
            val sharedPref1 = SharedPref1(activity)
            when (sharedPref1.theme) {
                "Light" -> {
                    binding!!.relpop.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    binding!!.popPhoto1.setTextColor(Color.parseColor("#000000"))
                    binding!!.SwipUp.setImageResource(R.drawable.ic_up_arow_black)
                    binding!!.relLandscanpe.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                "Dark" -> {
                    binding!!.relpop.setBackgroundColor(Color.parseColor("#000000"))
                    binding!!.popPhoto1.setTextColor(Color.parseColor("#FFFFFF"))
                    binding!!.SwipUp.setImageResource(R.drawable.ic_up_arow)
                    binding!!.relLandscanpe.setBackgroundColor(Color.parseColor("#000000"))
                }
                else -> {
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_YES -> {
                            binding!!.relpop.setBackgroundColor(Color.parseColor("#000000"))
                            binding!!.popPhoto1.setTextColor(Color.parseColor("#FFFFFF"))
                            binding!!.SwipUp.setImageResource(R.drawable.ic_up_arow)
                            binding!!.relLandscanpe.setBackgroundColor(Color.parseColor("#000000"))
                        }
                        Configuration.UI_MODE_NIGHT_NO -> {
                            binding!!.relpop.setBackgroundColor(Color.parseColor("#FFFFFF"))
                            binding!!.popPhoto1.setTextColor(Color.parseColor("#000000"))
                            binding!!.SwipUp.setImageResource(R.drawable.ic_up_arow_black)
                            binding!!.relLandscanpe.setBackgroundColor(Color.parseColor("#FFFFFF"))
                        }
                    }
                }
            }
        }
        bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
        bounce.repeatCount = Animation.INFINITE
        bounce.repeatMode = Animation.INFINITE
        bounce.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                bounce = AnimationUtils.loadAnimation(activity, R.anim.bounce)
                bounce.repeatCount = Animation.INFINITE
                bounce.repeatMode = Animation.INFINITE
                binding!!.SwipUp.startAnimation(bounce)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        binding!!.SwipUp.startAnimation(bounce)
        val requestOptions = RequestOptions()
        // requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
            .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
        requestOptions.priority(Priority.IMMEDIATE)
        requestOptions.skipMemoryCache(false)
        requestOptions.onlyRetrieveFromCache(true)
        requestOptions.placeholder(Utils.randomDrawbleColor)
        requestOptions.priority(Priority.HIGH)
        requestOptions.isMemoryCacheable
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.centerCrop()
        if (Connectivity.isConnected(activity) && Connectivity.isConnectedMobile(activity) && Connectivity.isConnectedFast(
                activity
            ) ||
            Connectivity.isConnected(activity) && Connectivity.isConnectedWifi(activity) && Connectivity.isConnectedFast(
                activity
            )
        ) {
            binding!!.droneView.shapeAppearanceModel = binding!!.droneView.shapeAppearanceModel
                .toBuilder().setTopLeftCorner(CornerFamily.ROUNDED, 150f)
                .setBottomLeftCorner(CornerFamily.ROUNDED, 150f).build()
            LoadImageDiff(requestOptions, Constant.drone_view, binding!!.droneView)
            LoadImageDiff(requestOptions, Constant.nature, binding!!.nature)
            binding!!.nature.shapeAppearanceModel = binding!!.nature.shapeAppearanceModel
                .toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, 150f)
                .build()
            LoadImageDiff(requestOptions, Constant.food, binding!!.food)
            binding!!.food.shapeAppearanceModel = binding!!.food.shapeAppearanceModel
                .toBuilder()
                .setBottomRightCorner(CornerFamily.ROUNDED, 150f).build()
            LoadImage(requestOptions, Constant.Landscape, binding!!.Landscape, binding!!.landScape)
            LoadImage(requestOptions, Constant.Cityscape, binding!!.Cityscape, binding!!.cityScape)
            LoadImage(requestOptions, Constant.Seascape, binding!!.Seascape, binding!!.seaScape)
            LoadImage(requestOptions, Constant.Twilight, binding!!.Twilight, binding!!.twiLight)
            LoadImage(requestOptions, Constant.Food, binding!!.Food, binding!!.fOOD)
            LoadImage(
                requestOptions,
                Constant.Drone_View,
                binding!!.DronView,
                binding!!.DroneView991
            )
        }
        Log.d("Popular", "onCreateView:")
        return binding!!.root
    }

    fun LoadImage(
        requestOptions: RequestOptions?,
        query: String,
        imageView: CircularImageView,
        textView: MaterialTextView
    ) {
        val Url = Constant.BASE_URL + query + "&per_page=80&page=1"
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url, Response.Listener { response: String ->
                try {
                    val obj = JSONObject(response)
                    val wallArray = obj.getJSONArray("photos")
                    var i = 0
                    while (i < wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val PhotoUrl = JSONObject(wallobj.toString())
                        Log.d("PhotoURL", wallobj.getString("url"))
                        val jsonObject = wallobj.getJSONObject("src")
                        val `object` = JSONObject(jsonObject.toString())
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
                            imageView.setImageBitmap(image)
                        } else {
                            if (activity != null) {
                                Glide.with(requireActivity())
                                    .load(`object`.getString("large"))
                                    .thumbnail(
                                        Glide.with(requireContext()).load(`object`.getString("large2x"))
                                    )
                                    .apply(requestOptions!!)
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

                                            //    spinKitView.setVisibility(View.GONE);
                                            return false
                                        }
                                    })
                                    .into(imageView)
                                imageView.setOnClickListener { v: View? ->
                                    val intent = Intent(activity, TestingMotionLayout::class.java)
                                    try {
                                        intent.putExtra("img1", `object`.getString("large2x"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        intent.putExtra("img2", `object`.getString("large"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    intent.putExtra("query", query)
                                    intent.putExtra("text", query)
//                                    val pairs: Array<Pair<*, *>?> = arrayOfNulls(2)
                                    val pairs = Pair.create<View, String>(imageView, "img1")
                                   val pairs1 = Pair.create<View, String>(textView, "text")
                                    val optionsCompat =
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            requireActivity(), pairs,pairs1
                                        )
                                    startActivity(intent, optionsCompat.toBundle())
                                }
                            }
                        }
                        i++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
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
        val requestQueue = Volley.newRequestQueue(context)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    fun LoadImageDiff(
        requestOptions: RequestOptions?,
        query: String,
        imageView: ShapeableImageView
    ) {
        val Url = Constant.BASE_URL + query + "&per_page=80&page=1"
        val stringRequest: StringRequest =
            object : StringRequest(Method.GET, Url,  Response.Listener { response: String ->
                try {
                    val obj = JSONObject(response)
                    val wallArray = obj.getJSONArray("photos")
                    var i = 0
                    while (i < wallArray.length()) {
                        val wallobj = wallArray.getJSONObject(i)
                        val photographer = JSONObject(wallobj.toString())
                        val PhotoUrl = JSONObject(wallobj.toString())
                        Log.d("PhotoURL", wallobj.getString("url"))
                        val jsonObject = wallobj.getJSONObject("src")
                        val `object` = JSONObject(jsonObject.toString())
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
                            imageView.setImageBitmap(image)
                        } else {
                            if (activity != null) {
                                Glide.with(requireActivity())
                                    .load(`object`.getString("large"))
                                    .thumbnail(
                                        Glide.with(requireContext()).load(`object`.getString("large2x"))
                                    )
                                    .apply(requestOptions!!)
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

                                            //    spinKitView.setVisibility(View.GONE);
                                            return false
                                        }
                                    })
                                    .into(imageView)
                                imageView.setOnClickListener {
                                    val intent = Intent(activity, TestingMotionLayout::class.java)
                                    try {
                                        intent.putExtra("img1", `object`.getString("large2x"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        intent.putExtra("img2", `object`.getString("large"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    intent.putExtra("query", query)
                                    intent.putExtra("text", query)
//                                    val pairs: Array<Pair<*, *>?> = arrayOfNulls(1)
                                    val pairs = Pair.create<View, String>(imageView, "img1")
                                    val optionsCompat =
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            requireActivity(), pairs
                                        )
                                    startActivity(intent, optionsCompat.toBundle())
                                }
                            }
                        }
                        i++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener {
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
        val requestQueue = Volley.newRequestQueue(context)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
    }

    companion object {
        @JvmStatic
        fun newInstance(text: String?): Popular {
            val f = Popular()
            val b = Bundle()
            b.putString("msg", text)
            f.arguments = b
            return f
        }
    }
}