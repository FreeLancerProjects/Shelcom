package com.appzone.shelcom.activities_fragments.activity_terms_conditions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appzone.shelcom.R;
import com.appzone.shelcom.models.RuleModel;
import com.appzone.shelcom.remote.Api;

import java.io.IOException;
import java.util.Locale;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermsConditionsActivity extends AppCompatActivity {
    private ImageView arrow;
    private LinearLayout ll_back;
    private SmoothProgressBar smoothprogressbar;
    private TextView tv_content;
    private String current_lang="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        initView();
    }

    private void initView() {
        current_lang = Locale.getDefault().getLanguage();
        arrow = findViewById(R.id.arrow);
        if (current_lang.equals("ar"))
        {
            arrow.setImageResource(R.drawable.arrow_right);
        }else
            {
                arrow.setImageResource(R.drawable.arrow_left);

            }
        ll_back = findViewById(R.id.ll_back);
        smoothprogressbar = findViewById(R.id.smoothprogressbar);
        tv_content = findViewById(R.id.tv_content);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getTerms();
    }

    private void getTerms() {
        Api.getService()
                .getAppRule("3")
                .enqueue(new Callback<RuleModel>() {
                    @Override
                    public void onResponse(Call<RuleModel> call, Response<RuleModel> response) {

                        if (response.isSuccessful()&& response.body()!=null)
                        {
                            smoothprogressbar.setVisibility(View.GONE);

                            if (current_lang.equals("ar"))
                            {

                                tv_content.setText(response.body().getAr_content());
                            }else
                            {
                                tv_content.setText(response.body().getEn_content());

                            }

                        }else
                            {
                                smoothprogressbar.setVisibility(View.GONE);
                                Toast.makeText(TermsConditionsActivity.this,getString(R.string.failed), Toast.LENGTH_LONG).show();

                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<RuleModel> call, Throwable t) {

                        try {
                            smoothprogressbar.setVisibility(View.GONE);
                            Toast.makeText(TermsConditionsActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e)
                        {

                        }
                    }
                });

    }
}
