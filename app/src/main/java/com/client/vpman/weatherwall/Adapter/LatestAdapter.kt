package com.client.vpman.weatherwall.Adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
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
import com.client.vpman.weatherwall.Adapter.LatestAdapter.PicsumHolder
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils
import com.client.vpman.weatherwall.databinding.LatestAdapterBinding
import com.client.vpman.weatherwall.model.InstaModel
import com.client.vpman.weatherwall.ui.Activity.TestFullActivity

class LatestAdapter(private val context: Context, private val lists: List<InstaModel>?) :
    RecyclerView.Adapter<PicsumHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PicsumHolder {
        val inflater = LayoutInflater.from(context)
        val binding = LatestAdapterBinding.inflate(inflater, parent, false)
        return PicsumHolder(binding)
    }

    @SuppressLint("CheckResult")
    override fun onBindViewHolder(holder: PicsumHolder, position: Int) {
        val modelData1 = lists!![position]
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
            holder.binding.latestMain.setImageBitmap(image)
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
            //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.centerCrop()
            Glide.with(context)
                .load(modelData1.displayUrl)
                .thumbnail(
                    Glide.with(context).load(modelData1.thumbnail_src)
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
                .into(holder.binding.latestMain)
            holder.binding.latestMain.setOnClickListener { v: View? ->
                val intent = Intent(context, TestFullActivity::class.java)
                intent.putExtra("large", modelData1.displayUrl)
                intent.putExtra("img", modelData1.thumbnail_src)
                intent.putExtra("PhotoUrl", modelData1.photoUrl)
//                val pairs: Array<Pair<*, *>?> = arrayOfNulls(1)
                val pairs = Pair.create<View, String>(holder.binding.latestMain, "imageData")
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (context as Activity), pairs
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    context.startActivity(intent, optionsCompat.toBundle())
                } else {
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lists?.size ?: 0
    }

    class PicsumHolder(var binding: LatestAdapterBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}