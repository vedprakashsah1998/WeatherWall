package com.client.vpman.weatherwall.Adapter

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.client.vpman.weatherwall.Adapter.ExploreAdapter.MyExpHolder
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.R
import com.client.vpman.weatherwall.databinding.ExpAdapterBinding
import com.client.vpman.weatherwall.model.ModelData
import com.client.vpman.weatherwall.model.RandomQuotesExp
import com.client.vpman.weatherwall.ui.Activity.FullImageQuotes
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ExploreAdapter(private val context: Context, private val list: List<ModelData>?) :
    RecyclerView.Adapter<MyExpHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyExpHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ExpAdapterBinding.inflate(inflater, parent, false)
        return MyExpHolder(binding)
    }

    override fun onBindViewHolder(holder: MyExpHolder, position: Int) {
        val sharedPref1 = SharedPref1(context)
        val memCache: LruCache<String?, Bitmap> = object : LruCache<String?, Bitmap>(
            (Runtime.getRuntime().maxMemory() / (1024 * 4)).toInt()
        ) {
            override fun sizeOf(key: String?, image: Bitmap): Int {
                return image.byteCount / 1024
            }
        }
        val modelData1 = list!![position]
        val image = memCache["imagefile"]
        if (image != null) {
            holder.binding.expadapterImg.setImageBitmap(image)
        } else {
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
            requestOptions.priority(Priority.IMMEDIATE)
            requestOptions.skipMemoryCache(false)
            requestOptions.onlyRetrieveFromCache(true)
            requestOptions.priority(Priority.HIGH)
            requestOptions.isMemoryCacheable
            requestOptions.placeholder(Utils.randomDrawbleColor)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            if (sharedPref1.imageLoadQuality == "Default") {
                Glide.with(context)
                    .load(modelData1.large2x)
                    .thumbnail(
                        Glide.with(context).load(modelData1.large)
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
                            return false
                        }
                    })
                    .into(holder.binding.expadapterImg)
            } else if (sharedPref1.imageLoadQuality == "High Quality") {
                Glide.with(context)
                    .load(modelData1.original)
                    .thumbnail(
                        Glide.with(context).load(modelData1.large2x)
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
                            return false
                        }
                    })
                    .into(holder.binding.expadapterImg)
            } else {
                Glide.with(context)
                    .load(modelData1.large2x)
                    .thumbnail(
                        Glide.with(context).load(modelData1.large)
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
                            return false
                        }
                    })
                    .into(holder.binding.expadapterImg)
            }
        }
        if (sharedPref1.theme == "Light") {
            holder.binding.quotesList.setTextColor(Color.parseColor("#000000"))
            val res = context.resources //resource handle
            val drawable = res.getDrawable(R.drawable.basic_design_customized_curved_white)
            holder.binding.relativeExp.background = drawable
        } else if (sharedPref1.theme == "Dark") {
            holder.binding.quotesList.setTextColor(Color.parseColor("#FFFFFF"))
            val res = context.resources //resource handle
            val drawable = res.getDrawable(R.drawable.basic_design_customized_curved)
            holder.binding.relativeExp.background = drawable
        } else {
            when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    holder.binding.quotesList.setTextColor(Color.parseColor("#FFFFFF"))
                    val res = context.resources //resource handle
                    val drawable = res.getDrawable(R.drawable.basic_design_customized_curved)
                    holder.binding.relativeExp.background = drawable
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    holder.binding.quotesList.setTextColor(Color.parseColor("#000000"))
                    val res1 = context.resources //resource handle
                    val drawable1 =
                        res1.getDrawable(R.drawable.basic_design_customized_curved_white)
                    holder.binding.relativeExp.background = drawable1
                }
            }
        }
        val randomQuotesExps: MutableList<RandomQuotesExp?> = ArrayList()
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
                    val randomQuotes1 = RandomQuotesExp(
                        jsonObject1.getString("text"),
                        jsonObject1.getString("author")
                    )
                    randomQuotesExps.add(randomQuotes1)
                }
                randomQuotesExps.shuffle()
                holder.binding.quotesList.text = randomQuotesExps[position]!!.quotes
                holder.binding.quotesAuthorList.text = randomQuotesExps[position]!!.author
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { }
        stringRequest.setShouldCache(false)
        val requestQueue = Volley.newRequestQueue(context)
        stringRequest.retryPolicy = DefaultRetryPolicy(
            3000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        requestQueue.add(stringRequest)
        holder.binding.relativeExp.requestLayout()
        holder.binding.expadapterImg.setOnClickListener { view: View? ->
            val intent = Intent(context, FullImageQuotes::class.java)
            intent.putExtra("largeImg", modelData1.original)
            intent.putExtra("imgDataAdapter", modelData1.large2x)
            intent.putExtra("imgDataAdapterSmall", modelData1.large)
            intent.putExtra("photoUrl", modelData1.photoUrl)
            intent.putExtra("Photographer_url", modelData1.photographer_url)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class MyExpHolder(var binding: ExpAdapterBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}