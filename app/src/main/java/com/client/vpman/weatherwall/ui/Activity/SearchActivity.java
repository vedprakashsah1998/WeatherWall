package com.client.vpman.weatherwall.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.ui.Fragment.SearchFragment;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    ActivitySearchBinding binding;
    SharedPref1 sharedPref1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, new SearchFragment())
                .commit();

        sharedPref1 = new SharedPref1(SearchActivity.this);

        if (sharedPref1.getTheme().equals("Light")) {
            binding.searchrel.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else if (sharedPref1.getTheme().equals("Dark")) {
            binding.searchrel.setBackgroundColor(Color.parseColor("#000000"));
        } else {
            switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
                case Configuration.UI_MODE_NIGHT_YES:
                    binding.searchrel.setBackgroundColor(Color.parseColor("#000000"));

                    break;
                case Configuration.UI_MODE_NIGHT_NO:
                    binding.searchrel.setBackgroundColor(Color.parseColor("#FFFFFF"));

                    break;
            }
        }

    }


}