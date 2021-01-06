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
import com.client.vpman.weatherwall.databinding.FragmentCarBinding;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class Car extends Fragment {


    public Car() {
        // Required empty public constructor
    }



    FragmentCarBinding binding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        VolleyGlobalLization.LoadImageDiff(Constant.Car, binding.Car,getActivity());
        binding.Car.setTranslationZ(40);
        return view;
    }


    public static Car newInstance(String text) {
        Car f = new Car();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

}
