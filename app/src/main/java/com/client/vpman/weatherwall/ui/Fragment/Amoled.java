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
import com.client.vpman.weatherwall.databinding.FragmentAmoledBinding;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class Amoled extends Fragment {

    public Amoled() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentAmoledBinding binding = FragmentAmoledBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        VolleyGlobalLization.LoadImageDiff(Constant.Luxury, binding.Amoled,getActivity());

        binding.Amoled.setTranslationZ(40);

        return view;
    }




    public static Amoled newInstance(String text) {
        Amoled f = new Amoled();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }

}
