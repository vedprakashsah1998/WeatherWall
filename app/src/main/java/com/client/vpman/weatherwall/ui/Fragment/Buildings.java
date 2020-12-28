package com.client.vpman.weatherwall.ui.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.ui.Activity.ExploreAcitivity;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.FragmentBuildingsBinding;
import com.kc.unsplash.Unsplash;
import com.kc.unsplash.models.Photo;
import com.kc.unsplash.models.SearchResults;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class Buildings extends Fragment {

    private View view;

    public Buildings() {
        // Required empty public constructor
    }

    private String query;

    private FragmentBuildingsBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBuildingsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        Unsplash unsplash = new Unsplash(getString(R.string.UNSPLASH_CLIENT));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
                .signature(new ObjectKey(System.currentTimeMillis())).encodeQuality(70);
        requestOptions.priority(Priority.IMMEDIATE);
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.skipMemoryCache(false);
        requestOptions.onlyRetrieveFromCache(true);
        requestOptions.priority(Priority.HIGH);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA);

        requestOptions.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);


        query = "Building";


        unsplash.searchPhotos(query, new Unsplash.OnSearchCompleteListener() {
            @Override
            public void onComplete(SearchResults results) {
                Log.d("Photos", "Total Results Found " + results.getTotal());

                List<Photo> photos = results.getResults();


                Random random = new Random();
                int n = random.nextInt(photos.size());

                if (isAdded()) {
                    LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory() / (1024 * 4))) {
                        @Override
                        protected int sizeOf(String key, Bitmap image) {
                            return image.getByteCount() / 1024;
                        }
                    };
                    if (getActivity() != null) {

                        Bitmap image = memCache.get("imagefile");
                        if (image != null) {
                            //Bitmap exists in cache.
                            binding.Building.setImageBitmap(image);
                        } else {
                            Glide.with(getActivity())
                                    .load(photos.get(n).getUrls().getFull())
                                    .thumbnail(
                                            Glide.with(getActivity()).load(photos.get(n).getUrls().getRegular())
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

                                    .into(binding.Building);
                        }

                        binding.Building.setOnClickListener(v -> {
                            Intent intent = new Intent(getActivity(), ExploreAcitivity.class);
                            intent.putExtra("imgData", photos.get(n).getUrls().getFull());
                            intent.putExtra("imgDataSmall", photos.get(n).getUrls().getRegular());
                            intent.putExtra("query", query);
                            intent.putExtra("text", "Buildings");

                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(binding.Building, "imgData");


                            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    getActivity(), pairs
                            );

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                startActivity(intent, optionsCompat.toBundle());
                            } else {
                                startActivity(intent);
                            }
                        });

                    }


                }


            }

            @Override
            public void onError(String error) {
                Log.d("Unsplash", error);
            }
        });
        binding.Building.setTranslationZ(40);
        return view;
    }


    public static Buildings newInstance(String text) {
        Buildings f = new Buildings();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

}
