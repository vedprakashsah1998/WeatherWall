package com.client.vpman.weatherwall.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.client.vpman.weatherwall.Adapter.CuratedAdapter.Curatedholder
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.databinding.CuratedAdapterBinding
import com.client.vpman.weatherwall.model.ModelData
import com.client.vpman.weatherwall.ui.Activity.TestFullActivity

class CuratedAdapter(private val context: Context, private val list: List<ModelData>?) :
    RecyclerView.Adapter<Curatedholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Curatedholder {
        val inflater = LayoutInflater.from(context)
        val binding = CuratedAdapterBinding.inflate(inflater, parent, false)
        return Curatedholder(binding)
    }

    override fun onBindViewHolder(holder: Curatedholder, position: Int) {
        val memCache: LruCache<String?, Bitmap> = object : LruCache<String?, Bitmap>(
            (Runtime.getRuntime().maxMemory() / (1024 * 4)).toInt()
        ) {
            override fun sizeOf(key: String?, image: Bitmap): Int {
                return image.byteCount / 1024
            }
        }
        val pref1 = SharedPref1(context)
        val modelData1 = list!![position]
        val image = memCache["imagefile"]
        if (image != null) {
            holder.binding.curatedMain.setImageBitmap(image)
        } else {
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(ObjectKey(System.currentTimeMillis())).encodeQuality(70)
            requestOptions.priority(Priority.IMMEDIATE)
            requestOptions.skipMemoryCache(false)
            requestOptions.onlyRetrieveFromCache(true)
            requestOptions.priority(Priority.HIGH)
            requestOptions.placeholder(Utils.randomDrawbleColor)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA)
            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            when (pref1.imageLoadQuality) {
                "Default" -> {
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
                        .into(holder.binding.curatedMain)
                }
                "High Quality" -> {
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
                        .into(holder.binding.curatedMain)
                }
                else -> {
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
                        .into(holder.binding.curatedMain)
                }
            }
            Log.d("curatedList", list[position].large2x)
            holder.binding.curatedMain.setOnClickListener { v: View? ->
                val intent = Intent(context, TestFullActivity::class.java)
                intent.putExtra("large", modelData1.original)
                intent.putExtra("img", modelData1.large2x)
                intent.putExtra("imgSmall", modelData1.large)
                intent.putExtra("PhotoUrl", modelData1.photoUrl)
                intent.putExtra("Photographer_url", modelData1.photographer_url)
//                val pairs: Array<Pair<*, *>> = arrayOfNulls(1)
                val pairs = Pair.create<View, String>(holder.binding.curatedMain, "imageData")
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (context as Activity), pairs
                )
                context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    class Curatedholder(var binding: CuratedAdapterBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}