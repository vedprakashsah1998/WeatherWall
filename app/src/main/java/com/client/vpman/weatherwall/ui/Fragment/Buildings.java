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
import com.client.vpman.weatherwall.databinding.FragmentBuildingsBinding;
import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class Buildings extends Fragment {

    public Buildings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
     FragmentBuildingsBinding binding = FragmentBuildingsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        VolleyGlobalLization.LoadImageDiff(Constant.Building, binding.Building,getActivity());

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
