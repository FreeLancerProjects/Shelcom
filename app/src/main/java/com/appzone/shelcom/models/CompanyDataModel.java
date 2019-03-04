package com.appzone.shelcom.models;

import java.io.Serializable;
import java.util.List;

public class CompanyDataModel implements Serializable{

    private List<CompanyModel> data;
    private Meta meta;

    public Meta getMeta() {
        return meta;
    }

    public List<CompanyModel> getData() {
        return data;
    }



    public class CompanyModel implements Serializable
    {
        private String id_company;
        private String service_type;
        private String ar_name_company;
        private String en_name_company;
        private String ar_details_company;
        private String en_details_company;
        private String ar_address_company;
        private String en_address_company;
        private String phone_company;
        private String whats_company;
        private String logo_company;

        public String getId_company() {
            return id_company;
        }

        public String getService_type() {
            return service_type;
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
