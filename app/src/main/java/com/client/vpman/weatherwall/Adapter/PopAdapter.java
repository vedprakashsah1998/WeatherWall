package com.client.vpman.weatherwall.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import com.client.vpman.weatherwall.CustomeUsefullClass.ModelData1;
import com.client.vpman.weatherwall.R;
import com.kc.unsplash.models.Photo;
import java.util.List;

public class PopAdapter extends RecyclerView.Adapter<PopAdapter.MyPopHandler>
{

    private Context context;
    private List<ModelData1> list;
    public ProgressBar progressBar;


    public PopAdapter(Context context, List<ModelData1> list) {
        this.context = context;
        this.list = list;
    }





    @NonNull
    @Override
    public PopAdapter.MyPopHandler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==list.size()-1)
        {
            View row = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_loading, parent, false);
        }
        else {
            
        }
        View view= LayoutInflater.from(context).inflate(R.layout.pop_adapter,parent,false);
        return new MyPopHandler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopAdapter.MyPopHandler holder, int position) {
        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount()/1024;
            }
        };


        ModelData1 modelData1=list.get(position);


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
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource)
                        {

                            return false;
                        }
                    })

                    .into(holder.imageView);
          //  Log.d("lwjdfh",list.get(position).getUrls().getFull());
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyPopHandler extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyPopHandler(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imgData);
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
