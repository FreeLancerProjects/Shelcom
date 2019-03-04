package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.singletone.UserSingleTone;
import com.appzone.shelcom.tags.Tags;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment {
    private AppBarLayout appBar;
    private CircleImageView image;
    private TextView tv_name,tv_email;
    private ImageView arrow1,arrow2,arrow3;
    private LinearLayout ll_name,ll_email,ll_logout;
    private String current_language;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private final int IMG1=1;
    private Uri uri=null;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;


    public static Fragment_Profile newInstance()
    {
        return new Fragment_Profile();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) { userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        current_language = Locale.getDefault().getLanguage();
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);

        if (current_language.equals("ar"))
        {
            arrow1.setImageResource(R.drawable.black_left_arrow);
            arrow2.setImageResource(R.drawable.black_left_arrow);
            arrow3.setImageResource(R.drawable.black_left_arrow);



        }else
        {
            arrow1.setImageResource(R.drawable.black_right_arrow);
            arrow2.setImageResource(R.drawable.black_right_arrow);
            arrow3.setImageResource(R.drawable.black_right_arrow);
        }

        activity = (HomeActivity) getActivity();
        appBar = view.findViewById(R.id.appBar);
        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        ll_name = view.findViewById(R.id.ll_name);
        ll_email = view.findViewById(R.id.ll_email);
        ll_logout = view.findViewById(R.id.ll_logout);

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int range = appBarLayout.getTotalScrollRange();

                if ((range+verticalOffset) <= 150)
                {
                    image.setVisibility(View.GONE);
                }else
                {
                    image.setVisibility(View.VISIBLE);

                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG1);
            }
        });
        ll_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialogUpdateName(userModel.getData().getUser_name());
            }
        });
        ll_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialogUpdateEmail(userModel.getData().getUser_email());
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Logout();
            }
        });
        updateUI(userModel);

    }

    private void updateUI(UserModel userModel)
    {

        if (userModel!=null)
        {
            Picasso.with(activity).load(Tags.IMAGE_URL+userModel.getData().getUser_image()).fit().into(image);
            tv_name.setText(userModel.getData().getUser_name());
            tv_email.setText(userModel.getData().getUser_email());
        }

    }
    private void Check_ReadPermission(int img_req)
    {
        if (ContextCompat.checkSelfPermission(activity,read_permission)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,new String[]{read_permission},img_req);
        }else
        {
            select_photo(img_req);
        }
    }
    private void select_photo(int img1)
    {
        Intent intent ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        }else
        {
            intent = new Intent(Intent.ACTION_GET_CONTENT);

        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent,img1);
    }
    private void UpdateImage(Uri uri)
    {

        final Dialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        RequestBody user_id = Common.getRequestBodyText(userModel.getData().getUser_id());
        RequestBody user_name = Common.getRequestBodyText(userModel.getData().getUser_name());
        RequestBody user_email = Common.getRequestBodyText(userModel.getData().getUser_email());


        try {
            MultipartBody.Part avatar_part = Common.getMultiPart(activity,uri,"user_image");
            Api.getService()
                    .updateImage(user_id,user_name,user_email,avatar_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                            if (response.isSuccessful())
                            {
                                dialog.dismiss();

                                if (response.body()!=null)
                                {
                                    Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                    UpdateUserData(response.body());



                                }else
                                {
                                    Common.CreateSignAlertDialog(activity,getString(R.string.something));
                                }
                            }else
                            {

                                dialog.dismiss();

                                if (response.code()==422)
                                {
                                    Common.CreateSignAlertDialog(activity,getString(R.string.phone_number_exists));

                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                                Log.e("Error",t.getMessage());
                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e)
        {
            Toast.makeText(activity, R.string.inc_img_path, Toast.LENGTH_SHORT).show();

        }

    }
    private void CreateDialogUpdateName(String old_name)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update_name,null);
        final EditText edt_name = view.findViewById(R.id.edt_name);
        edt_name.setText(old_name);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_name.getText().toString().trim();
                if (!TextUtils.isEmpty(name))
                {
                    alertDialog.dismiss();
                    edt_name.setError(null);
                    Common.CloseKeyBoard(activity,edt_name);
                    updateName(name);
                }else
                {
                    edt_name.setError(getString(R.string.field_req));
                }
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        alertDialog.setView(view);
        alertDialog.show();
    }
    private void CreateDialogUpdateEmail(String old_email)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_update_email,null);
        final EditText edt_email = view.findViewById(R.id.edt_email);
        edt_email.setText(old_email);
        Button btn_update = view.findViewById(R.id.btn_update);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_email.getText().toString().trim();
                if (!TextUtils.isEmpty(email)&& Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    alertDialog.dismiss();
                    edt_email.setError(null);
                    Common.CloseKeyBoard(activity,edt_email);
                    updateEmail(email);
                }else if (TextUtils.isEmpty(email))
                {
                    edt_email.setError(getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));
                }
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        alertDialog.setView(view);
        alertDialog.show();
    }
    private void updateName(String name)
    {

        final Dialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        Api.getService()
                .updateData(userModel.getData().getUser_id(),name,userModel.getData().getUser_email())
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                        if (response.isSuccessful())
                        {
                            dialog.dismiss();

                            if (response.body()!=null)
                            {
                                Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                UpdateUserData(response.body());



                            }else
                            {
                                Common.CreateSignAlertDialog(activity,getString(R.string.something));
                            }
                        }else
                        {

                            dialog.dismiss();

                            if (response.code()==422)
                            {
                                Common.CreateSignAlertDialog(activity,getString(R.string.phone_number_exists));

                            }else
                            {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });


    }
    private void updateEmail(String email)
    {
        final Dialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        Api.getService()
                .updateData(userModel.getData().getUser_id(),userModel.getData().getUser_name(),email)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {


                        if (response.isSuccessful())
                        {
                            dialog.dismiss();

                            if (response.body()!=null)
                            {
                                Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                                UpdateUserData(response.body());



                            }else
                            {
                                Common.CreateSignAlertDialog(activity,getString(R.string.something));
                            }
                        }else
                        {

                            dialog.dismiss();

                            if (response.code()==422)
                            {
                                Common.CreateSignAlertDialog(activity,getString(R.string.phone_number_exists));

                            }else
                            {
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
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
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });


    }
    private void UpdateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        userSingleTone.setUserModel(userModel);
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_userData(activity,userModel);
        updateUI(userModel);
        activity.updateUserData(userModel);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data!=null)
        {
            uri = data.getData();

            UpdateImage(uri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    select_photo(IMG1);
                }else
                {
                    Toast.makeText(activity,getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




}
