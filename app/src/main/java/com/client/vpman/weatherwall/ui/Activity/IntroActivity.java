package com.client.vpman.weatherwall.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.client.vpman.weatherwall.Adapter.CustomePagerAdapter;
import com.client.vpman.weatherwall.model.OnBoardingModel;
import com.client.vpman.weatherwall.CustomeUsefullClass.SharedPref1;
import com.client.vpman.weatherwall.R;
import com.client.vpman.weatherwall.databinding.ActivityIntroBinding;
import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private CustomePagerAdapter customePagerAdapter;
    private SharedPref1 pref;
    ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityIntroBinding.inflate(getLayoutInflater());
        View view1=binding.getRoot();
        setContentView(view1);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        pref=new SharedPref1(this);
        pref.setFirstState(false);
        setupOnBoardingItem();
        binding.onBoardingViewPager.setAdapter(customePagerAdapter);
        setupOnBoardingIndicator();

        setCurrentBoardingIndicator(0);

        binding.onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentBoardingIndicator(position);
            }
        });

        binding.buttonOnBoardingAction.setOnClickListener(v -> {
            if (binding.onBoardingViewPager.getCurrentItem()+1<customePagerAdapter.getItemCount())
            {
                binding.onBoardingViewPager.setCurrentItem(binding.onBoardingViewPager.getCurrentItem()+1);
            }
            else
            {
                startActivity(new Intent(IntroActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void setupOnBoardingItem()
    {
        List<OnBoardingModel>onBoardingModels=new ArrayList<>();
        OnBoardingModel onBoardingModel=new OnBoardingModel();
        onBoardingModel.setTitle("Weather Forecast");
        onBoardingModel.setDescription("A small information about Weather of your current Location");
        onBoardingModel.setImage(R.drawable.ic_undraw_weather_app_i5sm);

        OnBoardingModel quotesItem=new OnBoardingModel();
        quotesItem.setTitle("Quotes for inspiration");
        quotesItem.setDescription("Quotes that inspire you and motivate you for your life");
        quotesItem.setImage(R.drawable.ic_undraw_social_bio_8pql);

        OnBoardingModel wallpaper=new OnBoardingModel();
        wallpaper.setTitle("Beautiful images");
        wallpaper.setDescription("Amazing images for your screen wallpaper");
        wallpaper.setImage(R.drawable.ic_undraw_inspiration_lecl);
        onBoardingModels.add(onBoardingModel);
        onBoardingModels.add(quotesItem);
        onBoardingModels.add(wallpaper);
        customePagerAdapter=new CustomePagerAdapter(onBoardingModels);
    }
    private void setupOnBoardingIndicator()
    {
        ImageView[] indicator=new ImageView[customePagerAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicator.length;i++)
        {
            indicator[i]=new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_inactive));
            indicator[i].setLayoutParams(layoutParams);
            binding.onBoardingIndicator.addView(indicator[i]);

        }
    }
    private void setCurrentBoardingIndicator(int index)
    {
        int childCount=binding.onBoardingIndicator.getChildCount();
        for (int i=0;i<childCount;i++)
        {
            ImageView imageView=(ImageView)binding.onBoardingIndicator.getChildAt(i);
            if (i==index)
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onborard_indicator));
            }
            else
            {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_inactive));

            }
        }
        if (index==customePagerAdapter.getItemCount()-1)
        {
            binding.buttonOnBoardingAction.setText("Start");
        }
        else
        {
            binding.buttonOnBoardingAction.setText("Next");
        }
    }

}
