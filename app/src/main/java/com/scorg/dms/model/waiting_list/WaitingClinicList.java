
package com.scorg.dms.model.waiting_list;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingClinicList implements Parcelable {

    @SerializedName("hosId")
    @Expose
    private String hosId;
    @SerializedName("hosName")
    @Expose
    private String hosName;
    @SerializedName("hosPrintName")
    @Expose
    private String hosPrintName;
    @SerializedName("hosAddress1")
    @Expose
    private String hosAddress1;
    @SerializedName("hosAddress2")
    @Expose
    private String hosAddress2;
    @SerializedName("hosAdress3")
    @Expose
    private String hosAddress3;
    @SerializedName("hosCntId")
    @Expose
    private String hosCntId;
    @SerializedName("hosStatId")
    @Expose
    private String hosStatId;
    @SerializedName("hosCtyId")
    @Expose
    private String hosCtyId;

    public final static Creator<WaitingClinicList> CREATOR = new Creator<WaitingClinicList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WaitingClinicList createFromParcel(Parcel in) {
            return new WaitingClinicList(in);
        }

        public WaitingClinicList[] newArray(int size) {
            return (new WaitingClinicList[size]);
        }

    };

    protected WaitingClinicList(Parcel in) {
        this.hosId = ((String) in.readValue((String.class.getClassLoader())));
        this.hosName = ((String) in.readValue((String.class.getClassLoader())));
        this.hosPrintName = ((String) in.readValue((String.class.getClassLoader())));
        this.hosAddress1 = ((String) in.readValue((String.class.getClassLoader())));
        this.hosAddress2 = ((String) in.readValue((String.class.getClassLoader())));
        this.hosAddress3 = ((String) in.readValue((String.class.getClassLoader())));
        this.hosCntId = ((String) in.readValue((String.class.getClassLoader())));
        this.hosStatId = ((String) in.readValue((String.class.getClassLoader())));
        this.hosCtyId = ((String) in.readValue((String.class.getClassLoader())));

     }


    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getHosPrintName() {
        return hosPrintName;
    }

    public void setHosPrintName(String hosPrintName) {
        this.hosPrintName = hosPrintName;
    }

    public String getHosAddress1() {
        return hosAddress1;
    }

    public void setHosAddress1(String hosAddress1) {
        this.hosAddress1 = hosAddress1;
    }

    public String getHosAddress2() {
        return hosAddress2;
    }

    public void setHosAddress2(String hosAddress2) {
        this.hosAddress2 = hosAddress2;
    }

    public String getHosAddress3() {
        return hosAddress3;
    }

    public void setHosAdress3(String hosAdress3) {
        this.hosAddress3 = hosAdress3;
    }

    public String getHosCntId() {
        return hosCntId;
    }

    public void setHosCntId(String hosCntId) {
        this.hosCntId = hosCntId;
    }

    public String getHosStatId() {
        return hosStatId;
    }

    public void setHosStatId(String hosStatId) {
        this.hosStatId = hosStatId;
    }

    public String getHosCtyId() {
        return hosCtyId;
    }

    public void setHosCtyId(String hosCtyId) {
        this.hosCtyId = hosCtyId;
    }

    public void writeToParcel(Parcel dest, int flags) {

        dest.writeValue(hosId);
        dest.writeValue(hosName);
        dest.writeValue(hosPrintName);
        dest.writeValue(hosAddress1);
        dest.writeValue(hosAddress2);
        dest.writeValue(hosAddress3);
        dest.writeValue(hosCntId);
        dest.writeValue(hosStatId);
        dest.writeValue(hosCtyId);
     }

    public int describeContents() {
        return 0;
    }

}
