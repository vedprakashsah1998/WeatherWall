package com.client.vpman.weatherwall.CustomeUsefullClass

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.LruCache
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.VolleyError
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
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.ui.Activity.ExploreAcitivity
import com.makeramen.roundedimageview.RoundedImageView
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

object VolleyGlobalLization {
    fun LoadImageDiff(query: String, imageView: RoundedImageView, context: Context?) {
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
                            if (context != null) {
                                Glide.with(context)
                                    .load(`object`.getString("large"))
                                    .thumbnail(
                                        Glide.with(Objects.requireNonNull(context))
                                            .load(`object`.getString("large2x"))
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

                                            //    spinKitView.setVisibility(View.GONE);
                                            return false
                                        }
                                    })
                                    .into(imageView)
                                imageView.setOnClickListener {
                                    val intent = Intent(context, ExploreAcitivity::class.java)
                                    try {
                                        intent.putExtra(
                                            "imgDataSmall",
                                            `object`.getString("large2x")
                                        )
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    try {
                                        intent.putExtra("imgData", `object`.getString("large"))
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                    intent.putExtra("query", query)
                                    intent.putExtra("text", query)
                                   val pairs = Pair.create<View, String>(imageView, "img1")
                                    val optionsCompat =
                                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                            (context as Activity?)!!, pairs
                                        )
                                    context.startActivity(intent, optionsCompat.toBundle())
                                }
                            }
                        }
                        i++
                    }
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
                    val apiList: ArrayList<String> = ArrayList()
                    apiList.add(context!!.getString(R.string.APIKEY1))
                    apiList.add(context.getString(R.string.APIKEY2))
                    apiList.add(context.getString(R.string.APIKEY3))
                    apiList.add(context.getString(R.string.APIKEY4))
                    apiList.add(context.getString(R.string.APIKEY5))
                    val random = Random()
                    val n = random.nextInt(apiList.size)
                    params["Authorization"] = apiList[n]
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
}