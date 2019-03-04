package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.models.CompanyDataModel;
import com.appzone.shelcom.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.Locale;

public class Fragment_Company_Details extends Fragment {
    private final static String TAG = "DATA";
    private final static String TAG2 = "TYPE";
    private int type;
    private CompanyDataModel.CompanyModel companyModel;
    private LinearLayout ll_back;
    private ImageView image,arrow;
    private TextView tv_name,tv_details,tv_phone,tv_whatsapp,tv_address;
    private CardView card_call,card_whatsapp;
    private Button btn_reserve;
    private String current_language;
    private HomeActivity activity;

    public static Fragment_Company_Details newInstance(int type, CompanyDataModel.CompanyModel companyModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,companyModel);
        bundle.putInt(TAG2,type);
        Fragment_Company_Details fragment_company = new Fragment_Company_Details();
        fragment_company.setArguments(bundle);
        return fragment_company;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_details,container,false);
        initView(view);
        return view;
    }

    private void initView(View view)
    {


        activity = (HomeActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();
        arrow = view.findViewById(R.id.arrow);

        if (current_language.equals("ar"))
        {
            arrow.setImageResource(R.drawable.blue_write_arrow);
        }else
            {
                arrow.setImageResource(R.drawable.blue_left_arrow);

            }

        ll_back = view.findViewById(R.id.ll_back);

        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
        tv_details = view.findViewById(R.id.tv_details);
        tv_whatsapp = view.findViewById(R.id.tv_whatsapp);
        card_call = view.findViewById(R.id.card_call);
        card_whatsapp = view.findViewById(R.id.card_whatsapp);
        btn_reserve = view.findViewById(R.id.btn_reserve);



        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == Tags.furniture_reservation)
                {
                    activity.DisplayFragmentFurnitureReservation(companyModel);
                }else
                    {
                        activity.DisplayFragmentContainerReservation(type,companyModel);
                    }
            }
        });
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        card_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+companyModel.getPhone_company()));
                startActivity(intent);
            }
        });

        card_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String whatsPhone;
                if (companyModel.getWhats_company().startsWith("+966"))
                {
                    whatsPhone = companyModel.getWhats_company().replace("+966","966");
                }else if (companyModel.getWhats_company().startsWith("00966"))
                {
                    whatsPhone = companyModel.getWhats_company().replace("00966","966");

                }else
                    {
                        whatsPhone = "966"+companyModel.getWhats_company();

                    }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("whatsapp://send?phone="+whatsPhone));
                startActivity(intent);
            }
        });
        Bundle bundle = getArguments();
        if (bundle !=null)
        {
            companyModel = (CompanyDataModel.CompanyModel) bundle.getSerializable(TAG);
            type = bundle.getInt(TAG2);
            updateUI(companyModel);

        }
    }

    private void updateUI(CompanyDataModel.CompanyModel companyModel)
    {

        Picasso.with(activity).load(Uri.parse(Tags.IMAGE_URL+companyModel.getLogo_company())).fit().into(image);
        if (current_language.equals("ar"))
        {
            tv_name.setText(companyModel.getAr_name_company());
            tv_details.setText(companyModel.getAr_details_company());
            tv_address.setText(companyModel.getAr_address_company());
        }else
        {
            tv_name.setText(companyModel.getEn_name_company());
            tv_details.setText(companyModel.getEn_details_company());
            tv_address.setText(companyModel.getEn_address_company());
        }

        tv_phone.setText(companyModel.getPhone_company());

        if (companyModel.getWhats_company().isEmpty()||companyModel.getWhats_company()==null||companyModel.getWhats_company().equals("0"))
        {
            card_whatsapp.setVisibility(View.GONE);
        }else
            {
                tv_whatsapp.setText(companyModel.getWhats_company());
                card_whatsapp.setVisibility(View.VISIBLE);

            }

    }


}
