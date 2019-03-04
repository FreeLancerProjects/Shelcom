package com.appzone.shelcom.activities_fragments.activity_home.activity;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_About_Us;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_Company;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_Company_Details;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_Contact;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_Container_Reservation;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_Container_Type;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_Move_Furniture_Reservation;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.Fragment_Home;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.Fragment_Main;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.Fragment_More;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.Fragment_Notifications;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.Fragment_Profile;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_Orders;
import com.appzone.shelcom.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.shelcom.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.shelcom.models.CompanyDataModel;
import com.appzone.shelcom.models.NotificationCount;
import com.appzone.shelcom.models.NotificationModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.singletone.UserSingleTone;
import com.appzone.shelcom.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {


    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_Company fragment_company;
    private Fragment_Container_Reservation fragment_container_reservation;
    private Fragment_Move_Furniture_Reservation fragment_move_furniture_reservation;
    private Fragment_Container_Type fragment_container_type;
    private Fragment_Contact fragment_contact;
    private Fragment_About_Us fragment_about_us;
    private Fragment_Company_Details  fragment_company_details;
    ///////////////////////////////////////
    private Fragment_Main fragment_main;
    private Fragment_Profile fragment_profile;
    private Fragment_Orders fragment_orders;
    private Fragment_Notifications fragment_notifications;
    private Fragment_More fragment_more;
    ///////////////////////////////////////
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private boolean canRead = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        getDataFromIntent();

    }

    private void initView() {
        fragmentManager = getSupportFragmentManager();
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        DisplayFragmentHome();


        if (userModel != null) {
            UpdateToken();
            getUnreadNotificationCount();
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("signup")) {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CreateWelcomeNotification();

                            }
                        }, 3000);
            }
            if (intent.hasExtra("status")) {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DisplayFragmentNotifications();
                                ReadNotification();
                                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                if (manager != null) {
                                    manager.cancelAll();
                                }
                            }
                        }, 1);
            }
        }
    }

    private void CreateWelcomeNotification() {
        String sound_path = "android.resource://" + getPackageName() + "/" + R.raw.not;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence CHANNEL_NAME = "channel_name";
            int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);
            channel.setShowBadge(true);
            channel.setSound(Uri.parse(sound_path), new AudioAttributes.Builder()
                    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                    .build()
            );

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setChannelId(CHANNEL_ID);
            builder.setSound(Uri.parse(sound_path));
            builder.setContentTitle(getString(R.string.welcome));
            builder.setContentText(getString(R.string.welcome_thank));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setLargeIcon(bitmap);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(channel);
                manager.notify(1, builder.build());

            }


        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            builder.setSound(Uri.parse(sound_path));
            builder.setContentTitle(getString(R.string.welcome));
            builder.setContentText(getString(R.string.welcome_thank));
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setLargeIcon(bitmap);


            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify(1, builder.build());

            }
        }
    }

    public void updateUserData(UserModel userModel) {
        this.userModel = userModel;
    }

    private void UpdateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String token = task.getResult().getToken();
                            Api.getService()
                                    .updateToken(userModel.getData().getUser_id(), token)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful()) {
                                                Log.e("token", "Success");
                                            } else {
                                                try {
                                                    Log.e("Error_code", response.code() + "" + response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {
                                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                                Log.e("Error", t.getMessage());
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    private void getUnreadNotificationCount() {


        Api.getService()
                .getNotificationCount(userModel.getData().getUser_id(), Tags.unread)
                .enqueue(new Callback<NotificationCount>() {
                    @Override
                    public void onResponse(Call<NotificationCount> call, final Response<NotificationCount> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCount() > 0) {
                                canRead = true;

                            }
                            updateNotificationCount(response.body().getCount());
                        } else {

                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationCount> call, Throwable t) {
                        try {
                            Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void updateNotificationCount(final int count) {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_home != null && fragment_home.isAdded()) {
                            fragment_home.updateNotificationCount(count);
                        }
                    }
                }, 1);
    }

    public void ReadNotification() {

        if (canRead) {
            updateNotificationCount(0);

            Api.getService()
                    .getNotificationCount(userModel.getData().getUser_id(), Tags.read)
                    .enqueue(new Callback<NotificationCount>() {
                        @Override
                        public void onResponse(Call<NotificationCount> call, Response<NotificationCount> response) {
                            if (response.isSuccessful()) {
                                canRead = false;
                            } else {
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<NotificationCount> call, Throwable t) {
                            try {
                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }
    }

    public void DisplayFragmentHome() {

        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

        }

    }

    public void DisplayFragmentMain() {


        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }

        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }

        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_main == null) {
            fragment_main = Fragment_Main.newInstance();
        }

        if (fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

        }

        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(0);

        }


    }

    public void DisplayFragmentProfile() {

        if (userModel == null) {
            CreateUserNotSignInAlertDialog();

        } else {
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_notifications != null && fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_notifications).commit();
            }
            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }

            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }


            if (fragment_profile == null) {
                fragment_profile = Fragment_Profile.newInstance();
            }

            if (fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_profile).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

            }

            if (fragment_home != null && fragment_home.isAdded()) {
                fragment_home.UpdateAHBottomNavigationPosition(1);

            }
        }


    }

    public void DisplayFragmentNotifications() {

        if (userModel == null) {
            CreateUserNotSignInAlertDialog();
        } else {
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }

            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }

            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }

            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }


            if (fragment_notifications == null) {
                fragment_notifications = Fragment_Notifications.newInstance();
            }

            if (fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_notifications).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_notifications, "fragment_notifications").addToBackStack("fragment_notifications").commit();

            }
            ReadNotification();

            if (fragment_home != null && fragment_home.isAdded()) {
                fragment_home.UpdateAHBottomNavigationPosition(2);

            }
        }


    }

    public void DisplayFragmentOrders() {

        if (userModel == null) {
            CreateUserNotSignInAlertDialog();

        } else {
            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_notifications != null && fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_notifications).commit();
            }

            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }


            if (fragment_orders == null) {
                fragment_orders = Fragment_Orders.newInstance();
            }

            if (fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_orders).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();

            }
            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragment_home.UpdateAHBottomNavigationPosition(3);

            }

        }


    }

    public void DisplayFragmentMore() {


        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }


        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(4);

        }


    }

    public void DisplayFragmentCompany(int type) {

        fragment_company = Fragment_Company.newInstance(type);

        if (fragment_company.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company, "fragment_company").addToBackStack("fragment_company").commit();

        }


    }

    public void DisplayFragmentCompanyDetails(int type,CompanyDataModel.CompanyModel companyModel)
    {

        fragment_company_details = Fragment_Company_Details.newInstance(type,companyModel);

        if (fragment_company_details.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company_details).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company_details, "fragment_company_details").addToBackStack("fragment_company_details").commit();

        }


    }

    public void DisplayFragmentContainerType() {
        fragment_container_type = Fragment_Container_Type.newInstance();


        if (fragment_container_type.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_container_type).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_container_type, "fragment_container_type").addToBackStack("fragment_container_type").commit();

        }


    }

    public void DisplayFragmentFurnitureReservation(CompanyDataModel.CompanyModel companyModel) {

        if (userModel==null)
        {
            CreateUserNotSignInAlertDialog();
        }else
            {
                fragment_move_furniture_reservation = Fragment_Move_Furniture_Reservation.newInstance(companyModel);
                if (fragment_move_furniture_reservation.isAdded()) {
                    fragmentManager.beginTransaction().show(fragment_move_furniture_reservation).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_move_furniture_reservation, "fragment_move_furniture_reservation").addToBackStack("fragment_move_furniture_reservation").commit();
                }
            }


    }

    public void DisplayFragmentContainerReservation(int type,CompanyDataModel.CompanyModel companyModel) {

        if (userModel==null)
        {
            CreateUserNotSignInAlertDialog();

        }else
        {
            fragment_container_reservation = Fragment_Container_Reservation.newInstance(type,companyModel);

            if (fragment_container_reservation.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_container_reservation).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_container_reservation, "fragment_container_reservation").addToBackStack("fragment_container_reservation").commit();
            }
        }


    }

    public void DisplayFragmentContact() {
        if (userModel == null) {
            CreateUserNotSignInAlertDialog();
        } else {
            if (fragment_contact == null) {
                fragment_contact = Fragment_Contact.newInstance();
            }

            if (fragment_contact.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_contact).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_contact, "fragment_contact").addToBackStack("fragment_contact").commit();
            }


        }

    }

    public void DisplayFragmentAboutUs() {
        if (fragment_about_us == null) {
            fragment_about_us = Fragment_About_Us.newInstance();
        }

        if (fragment_about_us.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_about_us).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_about_us, "fragment_about_us").addToBackStack("fragment_about_us").commit();
        }
    }

    /////////////////////////
    public void ContainerReserve()
    {

    }

    public void FurnitureReserve()
    {

    }
    /////////////////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToAddNewNotification(final NotificationModel notificationModel) {
        canRead = true;
        getUnreadNotificationCount();

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_notifications != null && fragment_notifications.isAdded()) {
                            fragment_notifications.AddNewNotification(notificationModel);
                        }

                        if (notificationModel.getStatus() == Tags.NOTIFICATION_ACCEPT_SERVICE) {
                            RefreshFragmentOrder();
                        } else if (notificationModel.getStatus() == Tags.NOTIFICATION_REFUSE_SERVICE) {
                            RefreshFragmentOrder();

                        } else if (notificationModel.getStatus() == Tags.NOTIFICATION_FINISH_SERVICE) {
                            RefreshFragmentOrder();


                        }

                    }
                }, 1);

    }

    private void RefreshFragmentOrder() {
        if (fragment_orders != null && fragment_orders.isAdded()) {
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_orders.RefreshFragments();
                        }
                    }, 1);
        }
    }

    //////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /////////////////////////
    public void Logout() {
        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.logging_out));
        dialog.show();
        Api.getService()
                .logout(userModel.getData().getUser_id())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            if (manager != null) {
                                                manager.cancelAll();
                                            }
                                        }
                                    }, 1);
                            userModel = null;
                            userSingleTone.clear(HomeActivity.this);
                            dialog.dismiss();
                            NavigateToSignInActivity();

                        } else {
                            dialog.dismiss();

                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void NavigateToSignInActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();


    }

    public void NavigateToTermsCondition() {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        startActivity(intent);
    }

    public void CreateUserNotSignInAlertDialog() {

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null);
        Button btn_sign_in = view.findViewById(R.id.btn_sign_in);
        Button btn_sign_up = view.findViewById(R.id.btn_sign_up);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        TextView tv_msg = view.findViewById(R.id.tv_msg);
        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NavigateToSignInActivity();


            }
        });

        btn_sign_up.setOnClickListener(

                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        NavigateToSignInActivity();


                    }
                });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onBackPressed()
    {
        Back();
    }

    public void Back()
    {

        if (fragment_about_us!=null&& fragment_about_us.isVisible() )
        {
            super.onBackPressed();
        }else if (fragment_contact!=null&&fragment_contact.isVisible())
        {
            super.onBackPressed();

        }
        else if (fragment_contact!=null&&fragment_contact.isVisible())
        {
            super.onBackPressed();

        }
        else if (fragment_container_reservation!=null&&fragment_container_reservation.isVisible())
        {
            super.onBackPressed();

        }
        else if (fragment_move_furniture_reservation!=null&&fragment_move_furniture_reservation.isVisible())
        {
            super.onBackPressed();

        } else if (fragment_company_details!=null&&fragment_company_details.isVisible())
        {
            super.onBackPressed();

        }
        else if (fragment_company!=null&&fragment_company.isVisible())
        {
            super.onBackPressed();

        }
        else if (fragment_container_type!=null&&fragment_container_type.isVisible())
        {
            super.onBackPressed();

        }else if (fragment_main!=null&&fragment_main.isVisible())
        {
            finish();
        }else
            {
                DisplayFragmentMain();
            }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
