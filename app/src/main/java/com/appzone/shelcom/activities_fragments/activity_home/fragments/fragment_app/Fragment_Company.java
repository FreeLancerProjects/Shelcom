package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.adapters.CompanyAdapter;
import com.appzone.shelcom.models.CompanyDataModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.singletone.UserSingleTone;
import com.appzone.shelcom.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Company extends Fragment {
    private final static String TAG = "TYPE";
    private int type;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private CompanyAdapter adapter;
    private ProgressBar progBar;
    private TextView tv_no_company;
    private int current_page=1;
    private boolean isLoading = false;
    private HomeActivity activity;
    private List<CompanyDataModel.CompanyModel> companyModelList;
    private UserSingleTone userSingleTone;
    private UserModel userModel;

    public static Fragment_Company newInstance(int type)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,type);
        Fragment_Company fragment_company = new Fragment_Company();
        fragment_company.setArguments(bundle);
        return fragment_company;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company,container,false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {

        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();

        companyModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        tv_no_company = view.findViewById(R.id.tv_no_company);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recView.setItemViewCacheSize(25);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new CompanyAdapter(companyModelList,activity,this);
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int lastItemPos = ((LinearLayoutManager)recView.getLayoutManager()).findLastVisibleItemPosition();
                    int itemsCount = recyclerView.getAdapter().getItemCount();
                    if ((itemsCount-lastItemPos) <= 5 && !isLoading)
                    {
                        companyModelList.add(null);
                        adapter.notifyItemChanged(companyModelList.size()-1);
                        isLoading = true;
                        int nextPage = current_page+1;

                        if (type==Tags.furniture_reservation)
                        {
                            LoadMore(Tags.furniture_reservation,nextPage);

                        }else
                            {
                                LoadMore(2,nextPage);

                            }

                    }
                }
            }

        });
        Bundle bundle = getArguments();
        if (bundle !=null)
        {
            type = bundle.getInt(TAG);
            if (type == Tags.furniture_reservation)
            {
                getCompanies(Tags.furniture_reservation);
            }else
                {
                    ///larg and small
                    getCompanies(2);


                }

        }
    }
    public void getCompanies(int type)
    {

        Api.getService()
                .getCompanies(type,1)
                .enqueue(new Callback<CompanyDataModel>() {
                    @Override
                    public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                companyModelList.clear();
                                companyModelList.addAll(response.body().getData());
                                if (companyModelList.size()>0)
                                {
                                    tv_no_company.setVisibility(View.GONE);
                                    adapter.notifyDataSetChanged();

                                }else
                                {
                                    tv_no_company.setVisibility(View.VISIBLE);

                                }
                            }
                        }else
                        {
                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CompanyDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void LoadMore(int type,int page_index)
    {
        Api.getService()
                .getCompanies(type,page_index)
                .enqueue(new Callback<CompanyDataModel>() {
                    @Override
                    public void onResponse(Call<CompanyDataModel> call, Response<CompanyDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                isLoading = false;
                                companyModelList.remove(companyModelList.size()-1);
                                companyModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                current_page = response.body().getMeta().getCurrent_page();

                            }
                        }else
                        {
                            isLoading = false;
                            companyModelList.remove(companyModelList.size()-1);
                            adapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CompanyDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            companyModelList.remove(companyModelList.size()-1);
                            adapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void setItemData(CompanyDataModel.CompanyModel companyModel)
    {
        activity.DisplayFragmentCompanyDetails(type,companyModel);
    }
}
