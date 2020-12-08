package com.client.vpman.weatherwall.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.client.vpman.weatherwall.model.OnBoardingModel;
import com.client.vpman.weatherwall.R;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomePagerAdapter extends RecyclerView.Adapter<CustomePagerAdapter.OnBoardingViewHolder>
{

    List<OnBoardingModel>list;

    public CustomePagerAdapter(List<OnBoardingModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CustomePagerAdapter.OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OnBoardingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pager_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomePagerAdapter.OnBoardingViewHolder holder, int position) {
        holder.setOnBoardingItem(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     static class OnBoardingViewHolder extends RecyclerView.ViewHolder {
        private MaterialTextView textTitle;
        private MaterialTextView textDescription;
        private ImageView imageOnBoarding;
         OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle=itemView.findViewById(R.id.textTitle);
            textDescription=itemView.findViewById(R.id.textDescription);
            imageOnBoarding=itemView.findViewById(R.id.onBoardingImage);

        }
        void setOnBoardingItem(OnBoardingModel onBoardingItem)
        {
            textTitle.setText(onBoardingItem.getTitle());
            textDescription.setText(onBoardingItem.getDescription());
            imageOnBoarding.setImageResource(onBoardingItem.getImage());
        }
    }
}
