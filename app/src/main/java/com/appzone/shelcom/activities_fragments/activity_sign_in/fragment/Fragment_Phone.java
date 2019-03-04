package com.appzone.shelcom.activities_fragments.activity_sign_in.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_sign_in.activity.SignInActivity;
import com.appzone.shelcom.share.Common;


public class Fragment_Phone extends Fragment {
    private TextView tv_note;
    private EditText edt_phone;
    private FloatingActionButton fab;
    private SignInActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Phone newInstance(){
        return new Fragment_Phone();
    }
    private void initView(View view) {

        activity = (SignInActivity) getActivity();

        tv_note = view.findViewById(R.id.tv_note);
        edt_phone = view.findViewById(R.id.edt_phone);
        fab = view.findViewById(R.id.fab);


        tv_note.setText(getString(R.string.never_share_phone_number)+"\n"+getString(R.string.your_privacy_guaranteed));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckData();
            }
        });

    }

    private void CheckData() {
        String phone = edt_phone.getText().toString().trim();

        if (!TextUtils.isEmpty(phone)&& phone.length()==9)
        {
            edt_phone.setError(null);
            Common.CloseKeyBoard(activity,edt_phone);
            activity.signIn(phone);
        }else
            {
                if (TextUtils.isEmpty(phone))
                {
                    edt_phone.setError(getString(R.string.field_req));
                }else if (phone.length()!=9)
                {
                    edt_phone.setError(getString(R.string.inv_phone));
                }
            }
    }





}
