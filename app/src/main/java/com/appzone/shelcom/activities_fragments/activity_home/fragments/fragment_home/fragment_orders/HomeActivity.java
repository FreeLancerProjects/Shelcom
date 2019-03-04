package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.fragment_orders;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.dhai.R;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_About_Us;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Bank;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Contact;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Home;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.Fragment_Offers;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.jobs.Fragment_Job_Details;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.jobs.Fragment_Jobs;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.jobs.Fragment_Jobs_Reserve;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Electronic_Service_Reserve;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Other_Services;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Service_Reserve;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Services;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.services.Fragment_Student;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings.Fragment_Training;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings.Fragment_Training_Details;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_home.trainings.Fragment_Training_Register;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Main;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_More;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Notifications;
import com.appzone.dhai.activities_fragments.activity_home.fragments.fragment_main.Fragment_Profile;
import com.appzone.dhai.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.dhai.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.dhai.adapters.PackageAdapter;
import com.appzone.dhai.models.JobsDataModel;
import com.appzone.dhai.models.NotificationCount;
import com.appzone.dhai.models.NotificationModel;
import com.appzone.dhai.models.PackageDataModel;
import com.appzone.dhai.models.ServiceDataModel;
import com.appzone.dhai.models.TrainingDataModel;
import com.appzone.dhai.models.UserModel;
import com.appzone.dhai.preferences.Preferences;
import com.appzone.dhai.remote.Api;
import com.appzone.dhai.share.Common;
import com.appzone.dhai.singletone.UserSingleTone;
import com.appzone.dhai.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout ll_back;
    private ImageView image_arrow;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_Offers fragment_offers;
    private Fragment_Student fragment_student;
    //////////////////////////////////////////////
    private Fragment_Training fragment_training;
    private Fragment_Training_Details fragment_training_details;
    private Fragment_Training_Register fragment_training_register;
    ///////////////////////////////////////////////
    private Fragment_Jobs fragment_jobs;
    private Fragment_Job_Details fragment_job_details;
    private Fragment_Jobs_Reserve fragment_jobs_reserve;
    ///////////////////////////////////////////////
    private Fragment_Services fragment_services;
    private Fragment_Other_Services fragment_other_services;
    private Fragment_Service_Reserve fragment_service_reserve;
    private Fragment_Electronic_Service_Reserve fragment_electronic_service_reserve;
    private Fragment_Contact fragment_contact;
    private Fragment_About_Us fragment_about_us;
    private Fragment_Bank fragment_bank;
    ///////////////////////////////////////
    private Fragment_Main fragment_main;
    private Fragment_Profile fragment_profile;
    private Fragment_More fragment_more;
    private Fragment_Notifications fragment_notifications;
    private Fragment_Orders fragment_orders;
    ////////////////////////////////////////////////
    private Preferences preferences;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    //////////////////////////////////////////////////
    private List<PackageDataModel.PackageModel> packageModelList;
    private PackageAdapter packageAdapter;
    private AlertDialog packageDialog, imageDialog;
    private ImageView dialog_image, image_icon;
    private Button btn_send;
    private final int IMG1 = 1;
    private Uri uri = null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private boolean canRead = true;
    //////////////////////////////////////////////////
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
        preferences = Preferences.getInstance();
        String session = preferences.getSession(this);
        if (session.equals(Tags.session_login)) {
            userSingleTone.setUserModel(preferences.getUserData(this));
        }
        userModel = userSingleTone.getUserModel();
        packageModelList = new ArrayList<>();

        ll_back = findViewById(R.id.ll_back);
        image_arrow = findViewById(R.id.image_arrow);

        if (Locale.getDefault().getLanguage().equals("ar")) {
            image_arrow.setImageResource(R.drawable.arrow_right);
        } else {
            image_arrow.setImageResource(R.drawable.arrow_left);

        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });

        DisplayFragmentHome();
        if (userModel!=null)
        {
            if (!EventBus.getDefault().isRegistered(this))
            {
                EventBus.getDefault().register(this);
            }
            UpdateToken();
            getUnreadNotificationCount();

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
            }if (intent.hasExtra("status")) {
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
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            builder.setSmallIcon(R.mipmap.ic_launcher);
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

    private void UpdateToken()
    {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()&&task.getResult()!=null)
                        {
                            String token = task.getResult().getToken();
                            Api.getService()
                                    .updateToken(userModel.getToken(),token)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            if (response.isSuccessful())
                                            {
                                                Log.e("token","Success");
                                            }else
                                                {
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
    private void getUnreadNotificationCount()
    {


            Api.getService()
                    .getNotificationCount(userModel.getToken())
                    .enqueue(new Callback<NotificationCount>() {
                        @Override
                        public void onResponse(Call<NotificationCount> call, final Response<NotificationCount> response) {
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                if (response.body().getCount()>0)
                                {
                                    canRead=true;

                                }
                                updateNotificationCount(response.body().getCount());
                            }else
                                {

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
    private void updateNotificationCount(final int count)
    {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_home!=null&&fragment_home.isAdded())
                        {
                            fragment_home.updateNotificationCount(count);
                        }
                    }
                },1);
    }
    public void ReadNotification()
    {

        if (canRead)
        {
            updateNotificationCount(0);

            Api.getService()
                    .readNotifications(userModel.getToken())
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful())
                            {
                                canRead = false;
                            }else
                                {
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

    public void DisplayFragmentNotifications()
    {
        if (userModel == null) {
            CreateUserNotSignInAlertDialog();
        } else {

            if (fragment_main != null && fragment_main.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_main).commit();
            }
            if (fragment_profile != null && fragment_profile.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_profile).commit();
            }
            if (fragment_orders != null && fragment_orders.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_orders).commit();
            }


            if (fragment_more != null && fragment_more.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_more).commit();
            }


            if (fragment_notifications == null) {
                fragment_notifications = Fragment_Notifications.newInstance();
            }

            if (fragment_notifications.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_notifications).commit();
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fragment_notifications.NotifyAdapterChangeTime();
                            }
                        },1);
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
                new  Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                             fragment_orders.RefreshTime();
                            }
                        },1);
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();
            }
            if (fragment_home != null && fragment_home.isAdded()) {
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

    ///////////////////////////////////////////
    public void DisplayFragmentJobs() {
        if (fragment_jobs == null) {
            fragment_jobs = Fragment_Jobs.newInstance();
        }

        if (fragment_jobs.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_jobs).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_jobs, "fragment_jobs").addToBackStack("fragment_jobs").commit();
        }
    }

    public void DisplayFragmentJobsDetails(JobsDataModel.JobsModel jobsModel) {
        fragment_job_details = Fragment_Job_Details.newInstance(jobsModel);


        if (fragment_job_details.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_job_details).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_job_details, "fragment_job_details").addToBackStack("fragment_job_details").commit();
        }
    }

    ///////////////////////////////////////////
    public void DisplayFragmentOffers() {
        if (fragment_offers == null) {
            fragment_offers = Fragment_Offers.newInstance();
        }

        if (fragment_offers.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_offers).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_offers, "fragment_offers").addToBackStack("fragment_offers").commit();
        }
    }

    public void DisplayFragmentOtherService() {
        if (fragment_other_services == null) {
            fragment_other_services = Fragment_Other_Services.newInstance();
        }

        if (fragment_other_services.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_other_services).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_other_services, "fragment_other_services").addToBackStack("fragment_other_services").commit();
        }
    }

    public void DisplayFragmentService() {
        if (fragment_services == null) {
            fragment_services = Fragment_Services.newInstance();
        }

        if (fragment_services.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_services).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_services, "fragment_services").addToBackStack("fragment_services").commit();
        }
    }

    public void DisplayFragmentServiceReserve(ServiceDataModel.ServiceModel serviceModel) {
        if (userModel == null) {
            CreateUserNotSignInAlertDialog();
        } else {
            if (userModel.getBalance() >= serviceModel.getSale_price()) {
                fragment_service_reserve = Fragment_Service_Reserve.newInstance(serviceModel);

                if (fragment_service_reserve.isAdded()) {
                    fragmentManager.beginTransaction().show(fragment_service_reserve).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_service_reserve, "fragment_service_reserve").addToBackStack("fragment_service_reserve").commit();
                }
            } else {
                getPackagesData();
            }
        }

    }

    public void DisplayFragmentElectronicServiceReserve(ServiceDataModel.ServiceModel serviceModel) {
        if (userModel == null) {
            CreateUserNotSignInAlertDialog();
        } else {
            if (userModel.getBalance() >= serviceModel.getSale_price()) {
                fragment_electronic_service_reserve = Fragment_Electronic_Service_Reserve.newInstance(serviceModel);

                if (fragment_electronic_service_reserve.isAdded()) {
                    fragmentManager.beginTransaction().show(fragment_electronic_service_reserve).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_electronic_service_reserve, "fragment_electronic_service_reserve").addToBackStack("fragment_electronic_service_reserve").commit();
                }
            } else {
                getPackagesData();
            }
        }

    }

    public void DisplayFragmentJobsReserve(JobsDataModel.JobsModel jobsModel)
    {
        if (userModel == null) {
            CreateUserNotSignInAlertDialog();
        } else {
            if (userModel.getBalance() >= jobsModel.getSale_price()) {
                fragment_jobs_reserve = Fragment_Jobs_Reserve.newInstance(jobsModel);

                if (fragment_jobs_reserve.isAdded()) {
                    fragmentManager.beginTransaction().show(fragment_jobs_reserve).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_jobs_reserve, "fragment_jobs_reserve").addToBackStack("fragment_jobs_reserve").commit();
                }
            } else {
                getPackagesData();
            }
        }

    }

    public void DisplayFragmentStudents() {
        if (fragment_student == null) {
            fragment_student = Fragment_Student.newInstance();
        }

        if (fragment_student.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_student).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_student, "fragment_student").addToBackStack("fragment_student").commit();
        }
    }

    /////////////////////////////////////////////
    public void DisplayFragmentTraining() {
        if (fragment_training == null) {
            fragment_training = Fragment_Training.newInstance();
        }

        if (fragment_training.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_training).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_training, "fragment_training").addToBackStack("fragment_training").commit();
        }
    }

    public void DisplayFragmentTrainingDetails(TrainingDataModel.TrainingModel trainingModel) {
        fragment_training_details = Fragment_Training_Details.newInstance(trainingModel);

        if (fragment_training_details.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_training_details).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_training_details, "fragment_training_details").addToBackStack("fragment_training_details").commit();
        }
    }

    public void DisplayFragmentTrainingRegister(TrainingDataModel.TrainingModel trainingModel) {

        if (userModel == null) {
            CreateUserNotSignInAlertDialog();
        } else {
            if (userModel.getBalance() >= trainingModel.getSale_price()) {
                if (fragment_training_register == null) {
                    fragment_training_register = Fragment_Training_Register.newInstance(trainingModel);
                }

                if (fragment_training_register.isAdded()) {
                    fragmentManager.beginTransaction().show(fragment_training_register).commit();
                } else {
                    fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_training_register, "fragment_reserve").addToBackStack("fragment_reserve").commit();
                }
            } else {
                getPackagesData();
            }
        }




    }

    //////////////////////////////////////////////
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

    public void DisplayFragmentBanks() {
        fragment_bank = Fragment_Bank.newInstance();

        if (fragment_bank.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_bank).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_bank, "fragment_bank").addToBackStack("fragment_bank").commit();
        }
    }

    //////////////////////////////////////////////
    public void trainingReserve(int training_id,String m_name, String m_phone, String m_email, String m_add_info, String m_description, String m_notes) {
        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService()
                .trainingReserve(userModel.getToken(),training_id,m_name,m_phone,m_email,m_notes,m_add_info,m_description)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            fragmentManager.popBackStack();
                            new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (fragment_training != null && fragment_training.isAdded()) {
                                                fragment_training.getTrainings();

                                            }
                                            if (fragment_training_details != null && fragment_training_details.isAdded()) {
                                                fragment_training_details.UpdateUIAfterReserve();

                                            }

                                        }
                                    }, 1);

                            RefreshFragmentOrder();
                            RefreshFragmentTrainingAdapter();

                        }else
                            {
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

    public void servicesReserve(int service_id, String m_name, String m_phone, String m_email, String m_add_info, String m_description, String m_notes) {
        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService()
                .serviceReserve(userModel.getToken(),service_id,m_name,m_phone,m_email,m_notes,m_add_info,m_description)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            fragmentManager.popBackStack();
                            RefreshFragmentOrder();


                        }else
                        {
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

    public void electronicServicesReserve(int service_id, String m_name, String m_phone, String m_email, String m_add_info, String m_description, String m_notes, String username, String password) {
        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService()
                .electronicServiceReserve(userModel.getToken(),service_id,m_name,m_phone,m_email,m_notes,m_add_info,m_description,username,password)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            fragmentManager.popBackStack();
                            RefreshFragmentOrder();


                        }else
                        {
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

    public void jobReserveByPDF(int job_id, String pdf_path)
    {
        RequestBody user_token_part = Common.getRequestBodyText(userModel.getToken());
        RequestBody job_id_part = Common.getRequestBodyText(String.valueOf(job_id));
        MultipartBody.Part pdf_part = Common.getMultiPartFromPath(pdf_path,"file");

        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService()
                .jobPDFReserve(user_token_part,job_id_part,pdf_part)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();

                            new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (fragment_job_details!=null &&fragment_job_details.isAdded())
                                            {
                                                fragment_job_details.updateUIAfterReserve();
                                            }
                                        }
                                    },1);
                            RefreshFragmentOrder();
                            RefreshFragmentJobsAdapter();



                        }else
                        {
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
    public void jobReserveByData(int job_id, String m_name, String m_phone, String m_email, String card_id, String m_address, String qualification, String m_add_info, String m_description, String m_notes) {
        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService()
                .jobDataReserve(userModel.getToken(),job_id,m_name,m_phone,m_email,m_notes,m_add_info,m_description,m_address,card_id,qualification)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            fragmentManager.popBackStack();
                            new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (fragment_job_details!=null &&fragment_job_details.isAdded())
                                            {
                                                fragment_job_details.updateUIAfterReserve();
                                            }
                                        }
                                    },1);
                            RefreshFragmentJobsAdapter();
                            RefreshFragmentOrder();

                        }else
                        {
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

    private void RefreshFragmentTrainingAdapter()
    {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_training!=null&&fragment_training.isAdded())
                        {
                            fragment_training.RefreshAdapter();
                        }
                    }
                },1);
    }
    private void RefreshFragmentJobsAdapter()
    {
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_jobs!=null&&fragment_jobs.isAdded())
                        {
                            fragment_jobs.RefreshAdapter();
                        }
                    }
                },1);
    }

    //////////////////////////////////////////////
    private void Check_ReadPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(this, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{read_permission}, img_req);
        } else {
            select_photo(img_req);
        }
    }

    private void select_photo(int img1) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, img1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data != null) {
            image_icon.setVisibility(View.GONE);
            uri = data.getData();

            String path = Common.getImagePath(this, uri);
            if (path != null) {
                Picasso.with(this).load(new File(path)).fit().into(dialog_image);
                image_icon.setVisibility(View.GONE);
            } else {
                Picasso.with(this).load(uri).fit().into(dialog_image);
                image_icon.setVisibility(View.GONE);

            }
            btn_send.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode == IMG1) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG1);
                } else {
                    Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void getPackagesData() {
        if (packageModelList.size() > 0) {
            CreatePackageDialog(packageModelList);
        } else {
            final Dialog dialog = Common.getProgressDialog(this);
            dialog.show();
            Api.getService()
                    .getPackage()
                    .enqueue(new Callback<PackageDataModel>() {
                        @Override
                        public void onResponse(Call<PackageDataModel> call, Response<PackageDataModel> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                dialog.dismiss();
                                if (response.body().getDate().size() > 0) {
                                    packageModelList.clear();
                                    packageModelList.addAll(response.body().getDate());
                                    CreatePackageDialog(response.body().getDate());
                                } else {
                                    Toast.makeText(HomeActivity.this, R.string.no_charge, Toast.LENGTH_LONG).show();
                                }
                            } else {

                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<PackageDataModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        }

    }

    public void CreatePackageDialog(List<PackageDataModel.PackageModel> packageModelList) {
        packageDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_package, null);
        RecyclerView recView = view.findViewById(R.id.recView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        packageAdapter = new PackageAdapter(packageModelList, this);
        recView.setLayoutManager(manager);
        recView.setAdapter(packageAdapter);
        packageDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        packageDialog.setCanceledOnTouchOutside(false);
        packageDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        packageDialog.setView(view);
        packageDialog.show();
    }

    public void CreateImageDialog(final PackageDataModel.PackageModel packageModel) {
        imageDialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_image_package, null);
        FrameLayout fl = view.findViewById(R.id.fl);
        fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG1);
            }
        });
        dialog_image = view.findViewById(R.id.image);
        image_icon = view.findViewById(R.id.image_icon);
        btn_send = view.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog.dismiss();
                Send(packageModel);
            }
        });
        imageDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        imageDialog.setCanceledOnTouchOutside(false);
        imageDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        imageDialog.setView(view);
        imageDialog.show();
    }

    public void Send(PackageDataModel.PackageModel packageModel) {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        RequestBody token_part = Common.getRequestBodyText(userModel.getToken());
        RequestBody package_id = Common.getRequestBodyText(String.valueOf(packageModel.getId()));
        MultipartBody.Part image_part = Common.getMultiPart(this, uri, "image");

        Api.getService()
                .reservePackage(token_part, package_id, image_part)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            Common.CreateSignAlertDialog(HomeActivity.this, getString(R.string.rev_bill));
                        } else {

                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            dialog.dismiss();
                            Toast.makeText(HomeActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void setItemDataToBuy(PackageDataModel.PackageModel packageModel) {
        packageDialog.dismiss();
        CreateImageDialog(packageModel);
    }

    //////////////////////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenToAddNewNotification(final NotificationModel notificationModel)
    {
        canRead = true;
        getUnreadNotificationCount();

        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (fragment_notifications!=null&&fragment_notifications.isAdded())
                        {
                            fragment_notifications.AddNewNotification(notificationModel);
                        }

                        if (notificationModel.getType()==Tags.NOTIFICATION_ACCEPT_CHARGE)
                        {
                            if (fragment_profile!=null&&fragment_profile.isAdded())
                            {
                                fragment_profile.requestNewProfile();
                            }
                        }else if (notificationModel.getType()==Tags.NOTIFICATION_ACCEPT_SERVICE)
                        {
                            RefreshFragmentOrder();
                        }
                        else if (notificationModel.getType()==Tags.NOTIFICATION_REFUSE_SERVICE)
                        {
                            RefreshFragmentOrder();

                        }else if (notificationModel.getType()==Tags.NOTIFICATION_FINISH_SERVICE)
                        {
                            RefreshFragmentOrder();
                            if (fragment_profile!=null&&fragment_profile.isAdded())
                            {
                                fragment_profile.requestNewProfile();
                            }

                        }

                    }
                },1);

    }

    //////////////////////////////////////////////
    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {

        if (fragment_offers != null && fragment_offers.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_offers).commit();

        }
        else if(fragment_job_details != null && fragment_job_details.isVisible())
        {
            super.onBackPressed();
        }
        else if(fragment_jobs != null && fragment_jobs.isVisible())
        {
            fragmentManager.beginTransaction().hide(fragment_jobs).commit();
        }else if (fragment_service_reserve != null && fragment_service_reserve.isVisible()) {
            super.onBackPressed();

        } else if (fragment_electronic_service_reserve != null && fragment_electronic_service_reserve.isVisible()) {
            super.onBackPressed();

        } else if (fragment_student != null && fragment_student.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_student).commit();

        } else if (fragment_services != null && fragment_services.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_services).commit();

        } else if (fragment_other_services != null && fragment_other_services.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_other_services).commit();

        } else if (fragment_training_register != null && fragment_training_register.isVisible()) {
            super.onBackPressed();
        } else if (fragment_training_details != null && fragment_training_details.isVisible()) {
            super.onBackPressed();
        } else if (fragment_training != null && fragment_training.isVisible()) {
            fragmentManager.beginTransaction().hide(fragment_training).commit();

        } else if (fragment_contact != null && fragment_contact.isVisible()) {
            super.onBackPressed();

        } else if (fragment_about_us != null && fragment_about_us.isVisible()) {
            super.onBackPressed();

        } else if (fragment_bank != null && fragment_bank.isVisible()) {
            super.onBackPressed();

        } else if (fragment_main != null && fragment_main.isVisible()) {
            finish();
        } else {
            DisplayFragmentMain();
        }


    }

    public void Logout() {
        final Dialog dialog = Common.createProgressDialog(this, getString(R.string.logging_out));
        dialog.show();
        Api.getService()
                .logout(userModel.getToken())
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
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }
}
