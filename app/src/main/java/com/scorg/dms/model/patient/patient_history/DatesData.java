package com.scorg.dms.model.patient.patient_history;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jeetal on 14/2/18.
 */

public class DatesData implements Cloneable, Parcelable{
    @SerializedName("date")
    @Expose
    private Integer date;
    @SerializedName("longPressed")
    @Expose
    private boolean longPressed;
    @SerializedName("opdStatus")
    @Expose
    private String opdStatus;

    public final static Creator<DatesData> CREATOR = new Creator<DatesData>() {

        @SuppressWarnings({
                "unchecked"
        })
        public DatesData createFromParcel(Parcel in) {
            return new DatesData(in);
        }

        public DatesData[] newArray(int size) {
            return (new DatesData[size]);
        }

    };

    protected DatesData(Parcel in) {
        this.date = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.opdStatus = ((String) in.readValue((String.class.getClassLoader())));
    }
    public DatesData(){

    }

    public String getOpdStatus() {
        return opdStatus;
    }

    public void setOpdStatus(String opdStatus) {
        this.opdStatus = opdStatus;
    }

    public Integer getDate() {
        return date;
    }

    public boolean isLongPressed() {
        return longPressed;
    }

    public void setLongPressed(boolean longPressed) {
        this.longPressed = longPressed;
    }

    public void setDate(Integer date) {

        this.date = date;
    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeValue(opdStatus);
    }
}
