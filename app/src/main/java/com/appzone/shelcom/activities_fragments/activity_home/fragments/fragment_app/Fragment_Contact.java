package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.singletone.UserSingleTone;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Contact extends Fragment {

    private EditText edt_name, edt_email, edt_message;
    private Button btn_send;
    private HomeActivity activity;
    private UserSingleTone userSingleTone;
    private UserModel userModel;

    public static Fragment_Contact newInstance() {
        return new Fragment_Contact();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        activity = (HomeActivity) getActivity();
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_message = view.findViewById(R.id.edt_message);
        btn_send = view.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
        updateUI();
    }

    private void updateUI() {
        if (userModel!=null)
        {
            edt_name.setText(userModel.getData().getUser_name());
            if (userModel.getData().getUser_email()!=null)
            {
                edt_email.setText(userModel.getData().getUser_email());
            }
        }
    }

    private void checkData() {

        String m_name = edt_name.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();
        String m_message = edt_message.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches() &&
                !TextUtils.isEmpty(m_message)
                )
        {
            edt_name.setError(null);
            edt_email.setError(null);
            edt_message.setError(null);
            Common.CloseKeyBoard(activity,edt_name);
            SendData(m_name,m_email,m_message);
        }else
            {
                if (TextUtils.isEmpty(m_name))
                {
                    edt_name.setError(getString(R.string.field_req));
                }else
                    {
                        edt_name.setError(null);
                    }
                if (TextUtils.isEmpty(m_email))
                {
                    edt_email.setError(getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches())
                {
                    edt_email.setError(getString(R.string.inv_email));
                }else
                    {
                        edt_email.setError(null);
                    }


                if (TextUtils.isEmpty(m_message))
                {
                    edt_message.setError(getString(R.string.field_req));
                }else
                {
                    edt_message.setError(null);
                }
            }

    }

    private void SendData(String m_name, String m_email, String m_message) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Api.getService()
                .sendContacts(m_name,m_email,m_message)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            dialog.dismiss();
                            Toast.makeText(activity, getString(R.string.succ), Toast.LENGTH_SHORT).show();
                            edt_name.setText("");
                            edt_email.setText("");
                            edt_message.setText("");

                        }else
                        {

                            dialog.dismiss();
                            Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();

                            try {
                                Log.e("Error",response.code()+""+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }


}
