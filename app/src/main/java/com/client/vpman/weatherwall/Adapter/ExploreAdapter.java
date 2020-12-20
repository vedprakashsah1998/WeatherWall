package com.client.vpman.weatherwall.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.ui.Activity.FullImageQuotes;
import com.client.vpman.weatherwall.databinding.ExpAdapterBinding;
import com.client.vpman.weatherwall.model.ModelData;
import com.client.vpman.weatherwall.model.RandomQuotesExp;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.MyExpHolder> {
    private final Context context;
    private final List<ModelData> list;

    public ExploreAdapter(Context context, List<ModelData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyExpHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ExpAdapterBinding binding=ExpAdapterBinding.inflate(inflater,parent,false);
        return new MyExpHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreAdapter.MyExpHolder holder, int position) {

        SharedPref1 sharedPref1 = new SharedPref1(context);

        LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
            @Override
            protected int sizeOf(String key, Bitmap image) {
                return image.getByteCount() / 1024;
            }
        };
        ModelData modelData1 = list.get(position);
        Bitmap image = memCache.get("imagefile");
        if (image != null) {
            holder.binding.expadapterImg.setImageBitmap(image);
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
            if (sharedPref1.getImageLoadQuality().equals("Default")) {
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

                        .into(holder.binding.expadapterImg);
            } else if (sharedPref1.getImageLoadQuality().equals("High Quality")) {
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
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                return false;
                            }
                        })

                        .into(holder.binding.expadapterImg);
            } else {
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

                        .into(holder.binding.expadapterImg);
            }

        }

        if (sharedPref1.getTheme().equals("Light")) {
            holder.binding.quotesList.setTextColor(Color.parseColor("#000000"));
            Resources res = context.getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design_customized_curved_white);
            holder.binding.relativeExp.setBackground(drawable);
        } else if (sharedPref1.getTheme().equals("Dark")) {
            holder.binding.quotesList.setTextColor(Color.parseColor("#FFFFFF"));
            Resources res = context.getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design_customized_curved);
            holder.binding.relativeExp.setBackground(drawable);

        } else {
            holder.binding.quotesList.setTextColor(Color.parseColor("#000000"));
            Resources res = context.getResources(); //resource handle
            Drawable drawable = res.getDrawable(R.drawable.basic_design_customized_curved_white);
            holder.binding.relativeExp.setBackground(drawable);

        }

        List<RandomQuotesExp> randomQuotesExps = new ArrayList<>();
        String QuotesUrl = "https://type.fit/api/quotes";
        Log.d("sdfljh", "khwqgdi");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, QuotesUrl, response -> {

            Log.d("qoefg", response);

            try {

                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Log.d("eouf", String.valueOf(jsonObject));
                    JSONObject jsonObject1 = new JSONObject(String.valueOf(jsonObject));

                    RandomQuotesExp randomQuotes1 = new RandomQuotesExp(jsonObject1.getString("text"), jsonObject1.getString("author"));
                    randomQuotesExps.add(randomQuotes1);
                }

                Collections.shuffle(randomQuotesExps);
                holder.binding.quotesList.setText(randomQuotesExps.get(position).getQuotes());
                holder.binding.quotesAuthorList.setText(randomQuotesExps.get(position).getAuthor());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {

        });
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
        holder.binding.relativeExp.requestLayout();
        holder.binding.expadapterImg.setOnClickListener(view -> {
            Intent intent = new Intent(context, FullImageQuotes.class);
            intent.putExtra("largeImg", modelData1.getOriginal());
            intent.putExtra("imgDataAdapter", modelData1.getLarge2x());
            intent.putExtra("imgDataAdapterSmall", modelData1.getLarge());
            intent.putExtra("photoUrl", modelData1.getPhotoUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class MyExpHolder extends RecyclerView.ViewHolder {
        ExpAdapterBinding binding;
        public MyExpHolder(@NonNull ExpAdapterBinding itemView) {
            super(itemView.getRoot());
            this.binding=itemView;
        }
    }
}
