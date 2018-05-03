package com.rescribe.doctor.model.investigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ganeshshirole on 30/6/17.
 */

public class Images {
    @SerializedName("images")
    @Expose
    ArrayList<Image> imageArray;

    public ArrayList<Image> getImageArray() {
        return imageArray;
    }

    public void setImageArray(ArrayList<Image> imageArray) {
        this.imageArray = imageArray;
    }
}
