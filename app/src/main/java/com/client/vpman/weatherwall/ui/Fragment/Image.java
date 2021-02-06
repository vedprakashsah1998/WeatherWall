package com.client.vpman.weatherwall.ui.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.client.vpman.weatherwall.CustomeUsefullClass.Constant;
import com.client.vpman.weatherwall.CustomeUsefullClass.VolleyGlobalLization;
import com.client.vpman.weatherwall.databinding.FragmentImageBinding;
import org.jetbrains.annotations.NotNull;



/**
 * A simple {@link Fragment} subclass.
 */
public class Image extends Fragment {

    public Image() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentImageBinding binding = FragmentImageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        String type= getArguments().getString("type");
        Log.d("typeData",type);
        VolleyGlobalLization.LoadImageDiff(type, binding.NatureUn,getActivity());
        Log.d("Page", String.valueOf(Explore.pagerPosition));

        if (type.equals(Constant.Nature)){
            binding.natureText.setText("N A T U R E");
            binding.desc.setText("Click here to explore nature photos");
        }
        else if (type.equals(Constant.Building)){
            binding.natureText.setText("B U I L I D I N G");
            binding.desc.setText("Click here to explore building photos");
        }
        else if (type.equals(Constant.Car)){
            binding.natureText.setText("C A R");
            binding.desc.setText("Click here to explore car photos");
        }
        else if (type.equals(Constant.dark)){
            binding.natureText.setText("D A R K");
            binding.desc.setText("Click here to explore dark photos");
        }
        else if(type.equals(Constant.Bokeh)){
            binding.natureText.setText("B O K E H");
            binding.desc.setText("Click here to explore bokeh photos");
        }
        else if(type.equals(Constant.minimal)){
            binding.natureText.setText("M I N I M A L");
            binding.desc.setText("Click here to explore minimal photos");
        }
        else if(type.equals(Constant.mountain)){
            binding.natureText.setText("M O U N T A I N");
            binding.desc.setText("Click here to explore mountain photos");
        }
        else if(type.equals(Constant.religion)){
            binding.natureText.setText("R E L I G I O N");
            binding.desc.setText("Click here to explore religion photos");
        }
        else if(type.equals(Constant.Sparkles)){
            binding.natureText.setText("S P A R K L E S");
            binding.desc.setText("Click here to explore sparkles photos");
        }
        else if (type.equals(Constant.star)){
            binding.natureText.setText("S T A R");
            binding.desc.setText("Click here to explore star photos");
        }
        else if (type.equals(Constant.technology)){
            binding.natureText.setText("T E C H N O L O G Y");
            binding.desc.setText("Click here to explore technology photos");
        }
        else if (type.equals(Constant.Luxury)){
            binding.natureText.setText("L U X U R Y");
            binding.desc.setText("Click here to explore luxury photos");
        }
        binding.NatureUn.setTranslationZ(40);

        return view;
    }


    public static Image newInstance(String text, String type) {
        Image f = new Image();
        Bundle b = new Bundle();
        b.putString("msg", text);
        b.putString("type", type);
        f.setArguments(b);
        return f;
    }


}
