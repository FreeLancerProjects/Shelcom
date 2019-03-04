package com.appzone.shelcom.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private String id;
    private String ar_title;
    private String en_title;
    private String status;
    private String to_user;
    private String creation_date;


    public NotificationModel(String ar_title, String en_title, String status, String to_user, String creation_date) {
        this.ar_title = ar_title;
        this.en_title = en_title;
        this.status = status;
        this.to_user = to_user;
        this.creation_date = creation_date;
    }

    public String getId() {
        return id;
    }

    public String getAr_title() {
        return ar_title;
    }

    public String getEn_title() {
        return en_title;
    }

    public String getStatus() {
        return status;
    }

    public String getTo_user() {
        return to_user;
    }

    public String getCreation_date() {
        return creation_date;
    }
}
