package com.appzone.shelcom.models;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private List<OrderModel> data;
    private Meta meta;

    public List<OrderModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class OrderModel implements Serializable
    {
        private String service_type;
        private String date_order;
        private String creation_date;
        private String container_type_order;
        private String num_worker_order;
        private String status_order;
        private String id_company;
        private String ar_name_company;
        private String en_name_company;
        private String ar_details_company;
        private String en_details_company;
        private String ar_address_company;
        private String en_address_company;
        private String phone_company;
        private String whats_company;
        private String logo_company;


        public String getService_type() {
            return service_type;
        }

        public String getDate_order() {
            return date_order;
        }

        public String getCreation_date() {
            return creation_date;
        }

        public String getContainer_type_order() {
            return container_type_order;
        }

        public String getNum_worker_order() {
            return num_worker_order;
        }

        public String getStatus_order() {
            return status_order;
        }

        public String getId_company() {
            return id_company;
        }

        public String getAr_name_company() {
            return ar_name_company;
        }

        public String getEn_name_company() {
            return en_name_company;
        }

        public String getAr_details_company() {
            return ar_details_company;
        }

        public String getEn_details_company() {
            return en_details_company;
        }

        public String getAr_address_company() {
            return ar_address_company;
        }

        public String getEn_address_company() {
            return en_address_company;
        }

        public String getPhone_company() {
            return phone_company;
        }

        public String getWhats_company() {
            return whats_company;
        }

        public String getLogo_company() {
            return logo_company;
        }
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
