package com.appzone.shelcom.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel implements Serializable {

    private List<NotificationModel> data;
    private Meta meta;

    public List<NotificationModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }


    public class Meta implements Serializable
    {
        private int current_page;
        private int last_page;

        public int getCurrent_page() {
            return current_page;
        }

        public int getLast_page() {
            return last_page;
        }
    }
}
