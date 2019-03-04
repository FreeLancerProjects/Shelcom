package com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.activity.HomeActivity;
import com.appzone.shelcom.models.CompanyDataModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.singletone.UserSingleTone;
import com.appzone.shelcom.tags.Tags;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Fragment_Move_Furniture_Reservation extends Fragment implements DatePickerDialog.OnDateSetListener{

    private static final String TAG = "DATA";
    private EditText edt_name, edt_phone, edt_address,edt_workers;
    private TextView tv_date;
    private Button btn_reserve;
    private CompanyDataModel.CompanyModel companyModel;
    private UserSingleTone userSingleTone;
    private UserModel userModel;
    private LinearLayout ll_back;
    private ImageView arrow, image;
    private String current_language;
    private HomeActivity activity;
    private DatePickerDialog datePickerDialog;
    private String date = "";
    private long date_stamp;
    public static Fragment_Move_Furniture_Reservation newInstance(CompanyDataModel.CompanyModel companyModel)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG,companyModel);
        Fragment_Move_Furniture_Reservation fragment_move_furniture_reservation =new Fragment_Move_Furniture_Reservation();
        fragment_move_furniture_reservation.setArguments(bundle);
        return fragment_move_furniture_reservation;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_move_furniture_reservation,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        userSingleTone = UserSingleTone.getInstance();
        userModel = userSingleTone.getUserModel();
        activity = (HomeActivity) getActivity();
        current_language = Locale.getDefault().getLanguage();
        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar")) {
            arrow.setImageResource(R.drawable.blue_write_arrow);
        } else {
            arrow.setImageResource(R.drawable.blue_left_arrow);

        }
        ll_back = view.findViewById(R.id.ll_back);
        image = view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_address = view.findViewById(R.id.edt_address);
        edt_workers = view.findViewById(R.id.edt_workers);

        tv_date = view.findViewById(R.id.tv_date);
        btn_reserve = view.findViewById(R.id.btn_reserve);
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(activity.getFragmentManager(), "");
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });

        btn_reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
        Bundle bundle = getArguments();
        if (bundle != null) {
            companyModel = (CompanyDataModel.CompanyModel) bundle.getSerializable(TAG);

        }


        updateUI();
        CreateDatePickerDialog();

    }

    private void CreateDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePickerDialog = DatePickerDialog.newInstance(this, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        datePickerDialog.setAccentColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ContextCompat.getColor(activity, R.color.gray3));
        datePickerDialog.setCancelText("Cancel");
        datePickerDialog.setOkColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setOkText("Select");
        datePickerDialog.setLocale(Locale.getDefault());
        datePickerDialog.setMinDate(calendar);
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
    }

    private void updateUI() {

        Picasso.with(activity).load(Uri.parse(Tags.IMAGE_URL + companyModel.getLogo_company())).fit().into(image);
        if (userModel != null) {
            edt_name.setText(userModel.getData().getUser_name());
            edt_phone.setText(userModel.getData().getUser_phone());
        }


    }

    private void checkData()
    {

        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_address = edt_address.getText().toString().trim();
        String m_number_workers = edt_workers.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_phone) &&
                m_phone.length() == 9 &&
                !TextUtils.isEmpty(m_address) &&
                !TextUtils.isEmpty(m_number_workers)&&
                !TextUtils.isEmpty(date))
        {

            edt_name.setError(null);
            edt_phone.setError(null);
            edt_address.setError(null);
            edt_workers.setError(null);
            tv_date.setError(null);

            Common.CloseKeyBoard(activity,edt_name);



        } else {

            if (TextUtils.isEmpty(m_name))
            {
                edt_name.setError(getString(R.string.field_req));
            }else
            {
                edt_name.setError(null);
            }

            if (TextUtils.isEmpty(m_phone))
            {
                edt_phone.setError(getString(R.string.field_req));
            }else if (m_phone.length()!=9)
            {
                edt_phone.setError(getString(R.string.inv_phone));
            }
            else
            {
                edt_phone.setError(null);
            }

            if (TextUtils.isEmpty(m_address))
            {
                edt_address.setError(getString(R.string.field_req));
            }else
            {
                edt_address.setError(null);
            }

            if (TextUtils.isEmpty(m_number_workers))
            {
                edt_workers.setError(getString(R.string.field_req));
            }else
            {
                edt_workers.setError(null);
            }

            if (TextUtils.isEmpty(date))
            {
                tv_date.setError(getString(R.string.field_req));
            }

        }

    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Common.CloseKeyBoard(activity,edt_name);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat dateFormat;
        if (Locale.getDefault().getLanguage().equals("ar")) {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        } else {
            dateFormat = new SimpleDateFormat("dd/MM/yyy", Locale.getDefault());

        }
        date_stamp = calendar.getTimeInMillis() / 1000;
        date = dateFormat.format(new Date(calendar.getTimeInMillis()));
        tv_date.setText(date);
    }
}
