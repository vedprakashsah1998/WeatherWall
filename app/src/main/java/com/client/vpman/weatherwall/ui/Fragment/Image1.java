package com.client.vpman.weatherwall.ui.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.client.vpman.weatherwall.CustomeUsefullClass.Constant;
import com.client.vpman.weatherwall.CustomeUsefullClass.VolleyGlobalLization;
import com.client.vpman.weatherwall.CustomeUsefullClass.Utils;
import com.client.vpman.weatherwall.databinding.FragmentImage1Binding;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class Image1 extends Fragment {


    public Image1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentImage1Binding binding = FragmentImage1Binding.inflate(inflater, container, false);
        View view = binding.getRoot();

        VolleyGlobalLization.LoadImageDiff(Constant.Bokeh, binding.Bokeh,getActivity());

        binding.Bokeh.setTranslationZ(40);


        return view;
    }

    public static Image1 newInstance(String text) {
        Image1 f = new Image1();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

}
