package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.models.RuleModel;
import com.appzone.shelcom.remote.Api;

import java.io.IOException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_About_Us extends Fragment {

    private TextView tv_content;
    private ProgressBar progBar;
    private HomeActivity activity;
    private String current_lang;

    public static Fragment_About_Us newInstance()
    {
        return new Fragment_About_Us();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_us,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        current_lang = Locale.getDefault().getLanguage();
        activity = (HomeActivity) getActivity();
        tv_content = view.findViewById(R.id.tv_content);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        getAbout();
    }

    private void getAbout()
    {

        Api.getService()
                .getAppRule("6")
                .enqueue(new Callback<RuleModel>() {
                    @Override
                    public void onResponse(Call<RuleModel> call, Response<RuleModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            progBar.setVisibility(View.GONE);

                            if (current_lang.equals("ar"))
                            {
                                tv_content.setText(response.body().getAr_content());
                            }else
                                {
                                    tv_content.setText(response.body().getEn_content());

                                }


                        }else
                        {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_LONG).show();

                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RuleModel> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_LONG).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }


}
