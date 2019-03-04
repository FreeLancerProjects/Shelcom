package com.appzone.shelcom.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_app.Fragment_Company;
import com.appzone.shelcom.models.CompanyDataModel;
import com.appzone.shelcom.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<CompanyDataModel.CompanyModel> companyModelList;
    private Context context;
    private Fragment_Company fragment;

    public CompanyAdapter(List<CompanyDataModel.CompanyModel> companyModelList, Context context, Fragment_Company fragment) {
        this.companyModelList = companyModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.company_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_row, parent, false);
            return new ProgressHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {
            CompanyDataModel.CompanyModel companyModel = companyModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(companyModel);
        } else {
            ProgressHolder progressHolder = (ProgressHolder) holder;
            progressHolder.progBar.setIndeterminate(true);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompanyDataModel.CompanyModel companyModel = companyModelList.get(holder.getAdapterPosition());
                fragment.setItemData(companyModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return companyModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tv_title, tv_details;

        public MyHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_details = itemView.findViewById(R.id.tv_details);

        }

        public void BindData(CompanyDataModel.CompanyModel companyModel) {

            if (Locale.getDefault().getLanguage().equals("ar")) {
                tv_title.setText(companyModel.getAr_name_company());
                tv_details.setText(companyModel.getAr_details_company());
            } else {
                tv_title.setText(companyModel.getEn_name_company());
                tv_details.setText(companyModel.getEn_details_company());

            }

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+companyModel.getLogo_company())).fit().into(image);

        }
    }

    public class ProgressHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;

        public ProgressHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);

        }
    }

    @Override
    public int getItemViewType(int position) {
        CompanyDataModel.CompanyModel companyModel = companyModelList.get(position);

        if (companyModel != null) {
            return ITEM_DATA;
        } else {
            return ITEM_LOAD;
        }
    }
}
