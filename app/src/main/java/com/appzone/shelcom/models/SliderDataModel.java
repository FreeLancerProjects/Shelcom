package com.appzone.shelcom.models;

import java.io.Serializable;
import java.util.List;

public class SliderDataModel implements Serializable {

    private List<SliderModel> data;

    public List<SliderModel> getData() {
        return data;
    }

    public class SliderModel implements Serializable
    {
        private String id;
        private String image;

        public String getId() {
            return id;
        }

        public String getImage() {
            return image;
        }
    }
}
