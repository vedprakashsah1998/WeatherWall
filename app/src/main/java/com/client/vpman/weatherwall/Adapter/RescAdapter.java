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


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class RescAdapter extends RecyclerView.Adapter<RescAdapter.MyViewHolder>
{

    private List<ModelData> modelData;
private Context context;

    public RescAdapter(List<ModelData> modelData, Context context) {
        this.modelData = modelData;
        this.context = context;
    }

    @NonNull
    @Override
    public RescAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.recycle_data,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RescAdapter.MyViewHolder holder, int position) {
        ModelData modelData1=modelData.get(position);
        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount()/1024;
            }
        };
        SharedPref1 pref1=new SharedPref1(context);
        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            //Bitmap exists in cache.
            holder.imageView.setImageBitmap(image);
        } else
        {
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

            if (pref1.getTheme().equals("Light"))
            {
                Resources res = context.getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.main_design_white);
                holder.viewLayout.setBackground(drawable);
                holder.PhotoGrapherName.setTextColor(Color.parseColor("#000000"));
            }
            else if (pref1.getTheme().equals("Dark"))
            {
                Resources res = context.getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.basic_design);
                holder.viewLayout.setBackground(drawable);
                holder.PhotoGrapherName.setTextColor(Color.parseColor("#FFFFFF"));
            }
            else
            {
                holder.PhotoGrapherName.setTextColor(Color.parseColor("#000000"));
                Resources res = context.getResources(); //resource handle
                Drawable drawable = res.getDrawable(R.drawable.main_design_white);
                holder.viewLayout.setBackground(drawable);
            }

            if (pref1.getImageLoadQuality().equals("Default"))
            {

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
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                            {

                                return false;
                            }
                        })

                        .into(holder.imageView);
            }
            else if (pref1.getImageLoadQuality().equals("High Quality"))
            {

                Glide.with(context)
                        .load(modelData1.getOriginal())
                        .thumbnail(
                                Glide.with(context).load(modelData1.getLarge2x())
                        )
                        .apply(requestOptions)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //  spinKitView.setVisibility(View.GONE);


                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                            {

                                return false;
                            }
                        })

                        .into(holder.imageView);
            }
            else
            {
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
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                            {

                                return false;
                            }
                        })

                        .into(holder.imageView);
            }


        }
        holder.PhotoGrapherName.setText("Photographs by: "+modelData1.getPhotographer());
    }

    @Override
    public int getItemCount() {
        return modelData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        RoundedImageView imageView;
        MaterialTextView PhotoGrapherName;
        View viewLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageData);
            PhotoGrapherName=itemView.findViewById(R.id.photoGrapherName);
            viewLayout=itemView.findViewById(R.id.viewLayout);

        }
    }
}
