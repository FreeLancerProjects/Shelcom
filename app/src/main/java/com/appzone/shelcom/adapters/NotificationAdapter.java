package com.appzone.shelcom.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.models.NotificationModel;
import com.appzone.shelcom.share.TimeAgo;
import com.appzone.shelcom.tags.Tags;

import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD= 2;

    private List<NotificationModel> notificationModelList;
    private Context context;
    private TimeAgo timeAgo;

    public NotificationAdapter(List<NotificationModel> notificationModelList, Context context) {
        this.notificationModelList = notificationModelList;
        this.context = context;
        timeAgo = TimeAgo.newInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.notification_row,parent,false);
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
            NotificationModel notificationModel = notificationModelList.get(holder.getAdapterPosition());
            MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(notificationModel);
        }else
            {
                ProgressHolder progressHolder = (ProgressHolder) holder;
                progressHolder.progBar.setIndeterminate(true);
            }
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView image_state;
        private TextView tv_content,tv_date;

        public MyHolder(View itemView) {
            super(itemView);
            image_state = itemView.findViewById(R.id.image_state);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_date = itemView.findViewById(R.id.tv_date);

        }

        public void BindData(NotificationModel notificationModel)
        {

            tv_date.setText(timeAgo.getTime(Long.parseLong(notificationModel.getCreation_date())*1000));
            if (notificationModel.getStatus().equals(Tags.NOTIFICATION_ACCEPT_SERVICE))
            {
                image_state.setImageResource(R.drawable.done);
                if (Locale.getDefault().getLanguage().equals("ar"))
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getAr_title());

                }else
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getEn_title());

                }
            }
            else if (notificationModel.getStatus().equals(Tags.NOTIFICATION_REFUSE_SERVICE))
            {
                image_state.setImageResource(R.drawable.close_red_color);
                if (Locale.getDefault().getLanguage().equals("ar"))
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getAr_title());

                }else
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getEn_title());

                }
            }
            else if (notificationModel.getStatus().equals(Tags.NOTIFICATION_FINISH_SERVICE))
            {
                image_state.setImageResource(R.drawable.done);
                if (Locale.getDefault().getLanguage().equals("ar"))
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getAr_title());

                }else
                {
                    tv_content.setText(context.getString(R.string.accepted)+" "+notificationModel.getEn_title());

                }
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
        NotificationModel notificationModel = notificationModelList.get(position);

        if (notificationModel !=null)
        {
            return ITEM_DATA;
        }else
            {
                return ITEM_LOAD;
            }
    }
}
