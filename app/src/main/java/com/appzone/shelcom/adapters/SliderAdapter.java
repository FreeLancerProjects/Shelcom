package com.appzone.shelcom.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appzone.shelcom.R;
import com.appzone.shelcom.models.SliderDataModel;
import com.appzone.shelcom.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private List<SliderDataModel.SliderModel> sliderModelList;
    private Context context;

    public SliderAdapter(List<SliderDataModel.SliderModel> sliderModelList, Context context) {
        this.sliderModelList = sliderModelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_row,container,false);
        RoundedImageView image = view.findViewById(R.id.image);
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+sliderModelList.get(position).getImage())).fit().into(image);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
