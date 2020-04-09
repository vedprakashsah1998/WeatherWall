package com.client.vpman.weatherwall.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.Activity.FullImage;
import com.client.vpman.weatherwall.Activity.FullImageQuotes;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData1;
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData2;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PopAdapterLast extends RecyclerView.Adapter<PopAdapterLast.MyPopHandlerMain>
{

    private Context context;
    private List<ModelData2> list;
    public ProgressBar progressBar;


    public PopAdapterLast(Context context, List<ModelData2> list) {
        this.context = context;
        this.list = list;
    }





    @NonNull
    @Override
    public PopAdapterLast.MyPopHandlerMain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.pop_adapter_last,parent,false);
        return new MyPopHandlerMain(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopAdapterLast.MyPopHandlerMain holder, int position) {
        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount()/1024;
            }
        };


        SharedPref1 pref1=new SharedPref1(context);

        ModelData2 modelData1=list.get(position);


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
            requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.isMemoryCacheable();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

            requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            //   requestOptions.placeholder(Utils.getRandomDrawbleColor());
            requestOptions.centerCrop();

            if (pref1.getImageLoadQuality().equals("Default"))
            {
                Glide.with(context)
                        .load(modelData1.getLarge2x())
                        .thumbnail(
                                Glide.with(context).load(modelData1.getLarge())
                        ).centerCrop()
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
                        ).centerCrop()
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
                        ).centerCrop()
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


          //  Log.d("lwjdfh",list.get(position).getUrls().getFull());
            holder.imageView.requestLayout();
            holder.imageView.setOnClickListener(view -> {
                Intent intent=new Intent(context, FullImageQuotes.class);
                /*ModelData2 modelData2=list.get(position);*/
                intent.putExtra("largeImg",modelData1.getOriginal());
                intent.putExtra("imgDataAdapter",modelData1.getLarge2x());
                intent.putExtra("imgDataAdapterSmall",modelData1.getLarge());
                intent.putExtra("photoUrl",modelData1.getPhotoUrl());
                /*Pair<View, String> pair = Pair.create((View)holder.imageView, ViewCompat.getTransitionName(holder.imageView));
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        (Activity) context,pair
                );*/

                context.startActivity(intent);

            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPopHandlerMain extends RecyclerView.ViewHolder {
        RoundedImageView imageView;
        public MyPopHandlerMain(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgDataLast);
        }
    }
/*
    public void setPhotos(List<ModelData1> photos)
    {
        this.list = photos;
        notifyDataSetChanged();

    }*/
@Override
public int getItemViewType(int position) {
    return position;
}

    @Override
    public long getItemId(int position) {
        return position;
    }

}
