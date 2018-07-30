package com.scorg.dms.model.my_records;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.model.investigation.Image;

import java.util.ArrayList;
import java.util.List;

public class Group implements Parcelable {

    @SerializedName("groupname")
    @Expose
    private String groupname;
    @SerializedName("patients")
    @Expose
    private List<Image> images = new ArrayList<Image>();
    public final static Creator<Group> CREATOR = new Creator<Group>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Group createFromParcel(Parcel in) {
            Group instance = new Group();
            instance.groupname = ((String) in.readValue((String.class.getClassLoader())));
            in.readList(instance.images, (Image.class.getClassLoader()));
            return instance;
        }

        public Group[] newArray(int size) {
            return (new Group[size]);
        }

    };

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(groupname);
        dest.writeList(images);
    }

    public int describeContents() {
        return 0;
    }

}