package com.appzone.shelcom.models;

import java.io.Serializable;

public class UserModel implements Serializable {

    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable
    {
        private String user_id;
        private String user_email;
        private String user_phone;
        private String user_image;
        private String user_name;


        public String getUser_id() {
            return user_id;
        }

        public String getUser_email() {
            return user_email;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public String getUser_image() {
            return user_image;
        }

        public String getUser_name() {
            return user_name;
        }
    }
}
