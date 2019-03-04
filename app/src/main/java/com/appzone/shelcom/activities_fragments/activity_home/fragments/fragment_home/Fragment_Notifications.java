package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.adapters.NotificationAdapter;
import com.appzone.shelcom.models.NotificationDataModel;
import com.appzone.shelcom.models.NotificationModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.singletone.UserSingleTone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Notifications extends Fragment {

    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ProgressBar progBar;
    private LinearLayout ll_no_not;
    private NotificationAdapter notificationAdapter;
    private List<NotificationModel> notificationModelList;
    private HomeActivity activity;
    private int current_page=1;
    private boolean isLoading = false;
    private UserSingleTone userSingleTone;
    private UserModel userModel;

    public static Fragment_Notifications newInstance()
    {
        return new Fragment_Notifications();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        notificationModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        ll_no_not = view.findViewById(R.id.ll_no_not);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        notificationAdapter = new NotificationAdapter(notificationModelList,activity);
        recView.setAdapter(notificationAdapter);
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
                        notificationModelList.add(null);
                        notificationAdapter.notifyItemChanged(notificationModelList.size()-1);
                        isLoading = true;
                        int nextPage = current_page+1;
                        LoadMore(nextPage);
                    }
                }
            }

        });
        getNotifications();
    }

    private void getNotifications() {
        Api.getService()
                .getNotification(userModel.getData().getUser_id(),1)
                .enqueue(new Callback<NotificationDataModel>() {
                    @Override
                    public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                notificationModelList.clear();
                                notificationModelList.addAll(response.body().getData());
                                if (notificationModelList.size()>0)
                                {
                                    ll_no_not.setVisibility(View.GONE);
                                    notificationAdapter.notifyDataSetChanged();

                                }else
                                {
                                    ll_no_not.setVisibility(View.VISIBLE);

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
                    public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void LoadMore(int page_index)
    {
        Api.getService()
                .getNotification(userModel.getData().getUser_id(),page_index)
                .enqueue(new Callback<NotificationDataModel>() {
                    @Override
                    public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);

                            if (response.body()!=null)
                            {
                                isLoading = false;
                                notificationModelList.remove(notificationModelList.size()-1);
                                notificationModelList.addAll(response.body().getData());
                                notificationAdapter.notifyDataSetChanged();
                                current_page = response.body().getMeta().getCurrent_page();

                            }
                        }else
                        {
                            isLoading = false;
                            notificationModelList.remove(notificationModelList.size()-1);
                            notificationAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                        try {
                            isLoading = false;
                            notificationModelList.remove(notificationModelList.size()-1);
                            notificationAdapter.notifyDataSetChanged();

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void NotifyAdapterChangeTime()
    {
        if (notificationAdapter!=null&&notificationModelList.size()>0)
        {
            notificationAdapter.notifyDataSetChanged();
        }
    }

    public void AddNewNotification(NotificationModel notificationModel)
    {
        ll_no_not.setVisibility(View.GONE);
        notificationModelList.add(0,notificationModel);
        notificationAdapter.notifyDataSetChanged();
    }
}
