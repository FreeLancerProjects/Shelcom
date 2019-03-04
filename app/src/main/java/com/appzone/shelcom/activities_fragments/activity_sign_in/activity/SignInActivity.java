package com.appzone.shelcom.activities_fragments.activity_sign_in.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.activities_fragments.activity_sign_in.fragment.Fragment_Chooser;
import com.appzone.shelcom.activities_fragments.activity_sign_in.fragment.Fragment_Complete_Profile;
import com.appzone.shelcom.activities_fragments.activity_sign_in.fragment.Fragment_Phone;
import com.appzone.shelcom.activities_fragments.activity_terms_conditions.TermsConditionsActivity;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.singletone.UserSingleTone;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private View root;
    private Snackbar snackbar;
    private FragmentManager fragmentManager;
    private Fragment_Chooser fragment_chooser;
    private Fragment_Phone fragment_phone;
    private Fragment_Complete_Profile fragment_complete_profile;
    public String phone="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initView();
    }

    private void initView() {


        fragmentManager = getSupportFragmentManager();
        root = findViewById(R.id.root);
        DisplayFragmentChooser();




    }

    public void DisplayFragmentChooser()
    {
        if (fragment_chooser==null)
        {
            fragment_chooser = Fragment_Chooser.newInstance();
        }

        if (fragment_chooser.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_chooser).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_chooser,"fragment_chooser").addToBackStack("fragment_chooser").commit();
        }

    }

    public void DisplayFragmentPhone()
    {
        if (fragment_phone==null)
        {
            fragment_phone = Fragment_Phone.newInstance();
        }

        if (fragment_phone.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_phone).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_phone,"fragment_phone").addToBackStack("fragment_phone").commit();
        }

    }

    public void DisplayFragmentCompleteProfile(String phone)
    {
        this.phone = phone;

        if (fragment_complete_profile==null)
        {
            fragment_complete_profile = Fragment_Complete_Profile.newInstance();
        }

        if (fragment_complete_profile.isAdded())
        {
            fragmentManager.beginTransaction().show(fragment_complete_profile).commit();
        }else
        {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_container,fragment_complete_profile,"fragment_complete_profile").addToBackStack("fragment_complete_profile").commit();
        }

    }
    public void NavigateToHomeActivity(boolean isSkip,boolean isSignUp)
    {
        Intent intent = new Intent(this, HomeActivity.class);
        if (isSignUp)
        {
            intent.putExtra("signup",1);
        }
        startActivity(intent);

        if (!isSkip)
        {
            finish();

        }


    }

    public void NavigateToTermsActivity()
    {
        Intent intent = new Intent(this, TermsConditionsActivity.class);
        startActivity(intent);
    }

    public void signIn(final String phone)
    {
        final Dialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .SignIn(phone)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful())
                        {
                            DismissSnackBar();
                            dialog.dismiss();

                            if (response.body()!=null)
                            {
                                UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                Preferences preferences = Preferences.getInstance();

                                UserModel userModel = response.body();

                                userSingleTone.setUserModel(userModel);
                                preferences.create_update_userData(SignInActivity.this,userModel);
                                NavigateToHomeActivity(false,false);
                            }



                        }else
                        {
                            dialog.dismiss();

                            if (response.code() == 402)
                            {
                                DisplayFragmentCompleteProfile(phone);
                            }else
                            {
                                CreateSnackBar(getString(R.string.failed));

                            }
                            try {
                                Log.e("error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            CreateSnackBar(getString(R.string.something));
                            Log.e("Error",t.getMessage());

                        }catch (Exception e){}
                    }
                });
    }
    public void signUp(String name, String email, Uri avatar_uri)
    {

        final Dialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();

        RequestBody name_part = Common.getRequestBodyText(name);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody email_part = Common.getRequestBodyText(email);

        try {
            MultipartBody.Part avatar_part = Common.getMultiPart(this,avatar_uri,"user_image");
            Api.getService()
                    .SignUp(name_part,phone_part,email_part,avatar_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                            if (response.isSuccessful())
                            {
                                dialog.dismiss();
                                DismissSnackBar();

                                if (response.body()!=null)
                                {
                                    UserSingleTone userSingleTone = UserSingleTone.getInstance();
                                    Preferences preferences = Preferences.getInstance();
                                    UserModel userModel = response.body();
                                    userSingleTone.setUserModel(userModel);
                                    preferences.create_update_userData(SignInActivity.this,userModel);

                                    NavigateToHomeActivity(false,true);



                                }else
                                {
                                    Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.something));
                                }
                            }else
                            {

                                dialog.dismiss();

                                if (response.code()==422)
                                {
                                    Common.CreateSignAlertDialog(SignInActivity.this,getString(R.string.phone_number_exists));

                                }else
                                {
                                    CreateSnackBar(getString(R.string.failed));
                                }

                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                CreateSnackBar(getString(R.string.something));
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e)
        {
            Toast.makeText(this, R.string.inc_img_path, Toast.LENGTH_SHORT).show();

        }
    }
    private void CreateSnackBar(String msg)
    {
        snackbar = Common.CreateSnackBar(this,root,msg);
        snackbar.show();

    }
    private void DismissSnackBar()
    {
        if (snackbar!=null)
        {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void Back() {

        String name = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1).getName();
        if (name.equals("fragment_chooser"))
        {
            finish();
        }else
        {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }



}
