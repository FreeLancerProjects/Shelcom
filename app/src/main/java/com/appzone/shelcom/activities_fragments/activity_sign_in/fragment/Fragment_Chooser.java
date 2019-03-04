package com.appzone.shelcom.activities_fragments.activity_sign_in.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_sign_in.activity.SignInActivity;


public class Fragment_Chooser extends Fragment {
    private LinearLayout ll_sign_up_phone;
    private Button btn_skip,btn_terms;
    private SignInActivity activity;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chooser,container,false);
        initView(view);
        return view;
    }

    public static Fragment_Chooser newInstance(){
        return new Fragment_Chooser();
    }
    private void initView(View view) {
        activity = (SignInActivity) getActivity();
        ll_sign_up_phone = view.findViewById(R.id.ll_sign_up_phone);
        btn_skip = view.findViewById(R.id.btn_skip);
        btn_terms = view.findViewById(R.id.btn_terms);

        ll_sign_up_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentPhone();
            }
        });

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToHomeActivity(true,false);
            }
        });

        btn_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.NavigateToTermsActivity();
            }
        });
    }
}
