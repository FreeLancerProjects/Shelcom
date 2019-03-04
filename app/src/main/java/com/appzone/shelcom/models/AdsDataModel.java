package com.appzone.shelcom.models;

import java.io.Serializable;
import java.util.List;

public class AdsDataModel implements Serializable {
    private List<AdsModel> data;

    public List<AdsModel> getData() {
        return data;
    }

    public class AdsModel implements Serializable
    {
        private String title_ar;
        private String title_en;
        private String image;

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getImage() {
            return image;
        }
    }
}
