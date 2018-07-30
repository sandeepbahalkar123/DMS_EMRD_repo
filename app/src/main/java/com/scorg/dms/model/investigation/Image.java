package com.scorg.dms.model.investigation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image implements Parcelable {

    public final static Creator<Image> CREATOR = new Creator<Image>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Image createFromParcel(Parcel in) {
            Image instance = new Image();
            instance.imageId = ((String) in.readValue((String.class.getClassLoader())));
            instance.imagePath = ((String) in.readValue((String.class.getClassLoader())));
            instance.parentCaption = ((String) in.readValue((String.class.getClassLoader())));
            instance.type = ((int) in.readValue((boolean.class.getClassLoader())));

            return instance;
        }

        public Image[] newArray(int size) {
            return (new Image[size]);
        }

    };
    @SerializedName("image_id")
    @Expose
    private String imageId;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("parentCaption")
    @Expose
    private String parentCaption = "";

    @SerializedName("type")
    @Expose
    private int type;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
}

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getParentCaption() {
        return parentCaption;
    }

    public void setParentCaption(String parentCaption) {
        this.parentCaption = parentCaption;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(imageId);
        dest.writeValue(imagePath);
        dest.writeValue(parentCaption);
        dest.writeValue(type);
    }

    public int describeContents() {
        return 0;
    }
}