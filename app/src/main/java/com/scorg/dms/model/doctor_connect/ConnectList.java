
package com.scorg.dms.model.doctor_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class ConnectList implements Parcelable, CustomResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("doctorName")
    @Expose
    private String doctorName;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("onlineStatus")
    @Expose
    private String onlineStatus;
    @SerializedName("paidStatus")
    @Expose
    private String paidStatus;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("address")
    @Expose
    private String address;
    public final static Creator<ConnectList> CREATOR = new Creator<ConnectList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ConnectList createFromParcel(Parcel in) {
            ConnectList instance = new ConnectList();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.doctorName = ((String) in.readValue((String.class.getClassLoader())));
            instance.specialization = ((String) in.readValue((String.class.getClassLoader())));
            instance.onlineStatus = ((String) in.readValue((String.class.getClassLoader())));
            instance.paidStatus = ((String) in.readValue((String.class.getClassLoader())));
            instance.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.address = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public ConnectList[] newArray(int size) {
            return (new ConnectList[size]);
        }

    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(String paidStatus) {
        this.paidStatus = paidStatus;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(doctorName);
        dest.writeValue(specialization);
        dest.writeValue(onlineStatus);
        dest.writeValue(paidStatus);
        dest.writeValue(imageUrl);
        dest.writeValue(address);
    }

    public int describeContents() {
        return 0;
    }

}
