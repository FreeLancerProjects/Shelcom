package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.adapters.SliderAdapter;
import com.appzone.shelcom.models.SliderDataModel;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.tags.Tags;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private ViewPager pager_slider;
    private TabLayout tab_slider;
    private SliderAdapter sliderAdapter;
    private CardView card_container,card_furniture;
    private TimerTask timerTask;
    private Timer timer;




    public static Fragment_Main newInstance()
    {
        return new Fragment_Main();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (HomeActivity) getActivity();
        pager_slider  = view.findViewById(R.id.pager_slider);
        tab_slider  = view.findViewById(R.id.tab_slider);
        card_container  = view.findViewById(R.id.card_container);
        card_furniture= view.findViewById(R.id.card_furniture);

        tab_slider.setupWithViewPager(pager_slider);






        card_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentContainerType();
            }
        });

        card_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentCompany(Tags.furniture_reservation);
            }
        });

        getAds();
    }

    private void getAds() {

        Api.getService()
                .getAds()
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                        if (response.isSuccessful()&&response.body()!=null)
                        {

                            UpdateUI(response.body().getData());
                        }else
                        {

                            Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SliderDataModel> call, Throwable t) {
                        try {
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void UpdateUI(List<SliderDataModel.SliderModel> sliderModelList) {
        if (sliderModelList.size()>0)
        {
            if (sliderModelList.size()>1)
            {
                timerTask = new MyTimerTask();
                timer = new Timer();
                timer.scheduleAtFixedRate(timerTask,6000,6000);



            }
            sliderAdapter = new SliderAdapter(sliderModelList,activity);
            pager_slider.setAdapter(sliderAdapter);
        }
    }


    class MyTimerTask extends TimerTask{
        @Override
        public void run() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager_slider.getCurrentItem()< pager_slider.getAdapter().getCount()-1)
                    {
                        pager_slider.setCurrentItem(pager_slider.getCurrentItem()+1);
                    }else
                        {
                            pager_slider.setCurrentItem(0);
                        }
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (timer!=null)
        {
            timer.purge();
            timer.cancel();
        }
        if (timerTask!=null)
        {
           timerTask.cancel();
        }
    }
}
