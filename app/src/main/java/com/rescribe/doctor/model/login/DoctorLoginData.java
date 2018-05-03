package com.rescribe.doctor.model.login;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DoctorLoginData implements Parcelable {

    @SerializedName("authToken")
    @Expose
    private String authToken;
    @SerializedName("docDetail")
    @Expose
    private DocDetail docDetail;
    public final static Parcelable.Creator<DoctorLoginData> CREATOR = new Creator<DoctorLoginData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorLoginData createFromParcel(Parcel in) {
            return new DoctorLoginData(in);
        }

        public DoctorLoginData[] newArray(int size) {
            return (new DoctorLoginData[size]);
        }

    };

    protected DoctorLoginData(Parcel in) {
        this.authToken = ((String) in.readValue((String.class.getClassLoader())));
        this.docDetail = ((DocDetail) in.readValue((DocDetail.class.getClassLoader())));
    }

    public DoctorLoginData() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public DocDetail getDocDetail() {
        return docDetail;
    }

    public void setDocDetail(DocDetail docDetail) {
        this.docDetail = docDetail;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(authToken);
        dest.writeValue(docDetail);
    }

    public int describeContents() {
        return 0;
    }

}