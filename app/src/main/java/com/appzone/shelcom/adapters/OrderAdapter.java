package com.appzone.shelcom.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_Current_Orders;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_New_Orders;
import com.appzone.shelcom.activities_fragments.activity_home.fragments.fragment_home.fragment_orders.Fragment_Previous_Orders;
import com.appzone.shelcom.models.OrderDataModel;
import com.appzone.shelcom.share.TimeAgo;

import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD= 2;

    private List<OrderDataModel.OrderModel> orderModelList;
    private Context context;
    private Fragment fragment;
    private TimeAgo timeAgo;

    public OrderAdapter(List<OrderDataModel.OrderModel> orderModelList, Context context, Fragment fragment) {
        this.orderModelList = orderModelList;
        this.context = context;
        this.fragment = fragment;
        timeAgo = TimeAgo.newInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.order_row,parent,false);
            return new MyHolder(view);
        }else
            {
                View view = LayoutInflater.from(context).inflate(R.layout.load_more_row,parent,false);
                return new ProgressHolder(view);
            }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder)
        {
            OrderDataModel.OrderModel orderModel = orderModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(orderModel);
        }else
            {
                ProgressHolder progressHolder = (ProgressHolder) holder;
                progressHolder.progBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image_state;
        private TextView tv_title,tv_content,tv_date;

        public MyHolder(View itemView) {
            super(itemView);
            image_state = itemView.findViewById(R.id.image_state);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);

        }

        public void BindData(OrderDataModel.OrderModel orderModel)
        {

            if (Locale.getDefault().getLanguage().equals("ar"))
            {
                tv_title.setText(orderModel.getAr_name_company());
            }else
                {
                    tv_title.setText(orderModel.getAr_name_company());

                }

            tv_date.setText(timeAgo.getTime(Long.parseLong(orderModel.getCreation_date())*1000));
            if (fragment instanceof Fragment_New_Orders)
            {
                tv_content.setText(context.getString(R.string.order_not_accepted));
                image_state.setImageResource(R.drawable.clock3);

            }else if (fragment instanceof Fragment_Current_Orders)
            {
                tv_content.setText(context.getString(R.string.order_is_approved));
                image_state.setImageResource(R.drawable.clock2);


            }else if (fragment instanceof Fragment_Previous_Orders)
            {
                image_state.setImageResource(R.drawable.done);

                tv_content.setText(context.getString(R.string.order_finished));

            }
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
        OrderDataModel.OrderModel orderModel = orderModelList.get(position);

        if (orderModel !=null)
        {
            return ITEM_DATA;
        }else
            {
                return ITEM_LOAD;
            }
    }
}
