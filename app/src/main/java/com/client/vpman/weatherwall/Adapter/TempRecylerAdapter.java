package com.client.vpman.weatherwall.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.Model.ModelData;
import com.client.vpman.weatherwall.Model.ModelData1;
import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class TempRecylerAdapter extends RecyclerView.Adapter<TempRecylerAdapter.TempRecylerHolder>
{

    private List<ModelData1> list;
    private Context context;

    public TempRecylerAdapter(List<ModelData1> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TempRecylerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.temp_recyler_list, parent, false);
        return new TempRecylerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempRecylerHolder holder, int position) {
        ModelData1 modelData1 = list.get(position);
        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount() / 1024;
            }
        };

        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            //Bitmap exists in cache.
            holder.roundedImageView.setImageBitmap(image);
        } else {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                    .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
            requestOptions.priority(Priority.IMMEDIATE);
            requestOptions.skipMemoryCache(false);
            requestOptions.onlyRetrieveFromCache(true);
            requestOptions.priority(Priority.HIGH);
            requestOptions.isMemoryCacheable();
            requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.centerCrop();




                Glide.with(context)
                        .load(modelData1.getLarge2x())
                        .thumbnail(
                                Glide.with(context).load(modelData1.getLarge())
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //  spinKitView.setVisibility(View.GONE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                return false;
                            }
                        })

                        .into(holder.roundedImageView);



        }
        holder.photoGrapherName.setText("Photographs by:\n " + modelData1.getPhotographer());
    /*    Animation animation = AnimationUtils.loadAnimation(context, R.anim.animation_from_right);
        holder.roundedImageView.startAnimation(animation);*/

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TempRecylerHolder extends RecyclerView.ViewHolder
    {
        RoundedImageView roundedImageView;
        MaterialTextView photoGrapherName;
        TempRecylerHolder(@NonNull View itemView) {
            super(itemView);
            roundedImageView=itemView.findViewById(R.id.temp_recyler);
            photoGrapherName=itemView.findViewById(R.id.photographerName);



        }
    }
}
