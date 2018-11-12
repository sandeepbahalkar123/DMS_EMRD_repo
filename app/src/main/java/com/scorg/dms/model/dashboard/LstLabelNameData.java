package com.scorg.dms.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LstLabelNameData implements Parcelable
{

    @SerializedName("LabelUHID")
    @Expose
    private String labelUHID;
    @SerializedName("LabelRefID")
    @Expose
    private String labelRefID;
    @SerializedName("LabelDoctorName")
    @Expose
    private String labelDoctorName;
    public final static Parcelable.Creator<LstLabelNameData> CREATOR = new Creator<LstLabelNameData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LstLabelNameData createFromParcel(Parcel in) {
            return new LstLabelNameData(in);
        }

        public LstLabelNameData[] newArray(int size) {
            return (new LstLabelNameData[size]);
        }

    }
            ;

    protected LstLabelNameData(Parcel in) {
        this.labelUHID = ((String) in.readValue((String.class.getClassLoader())));
        this.labelRefID = ((String) in.readValue((String.class.getClassLoader())));
        this.labelDoctorName = ((String) in.readValue((String.class.getClassLoader())));
    }

    public LstLabelNameData() {
    }

    public String getLabelUHID() {
        return labelUHID;
    }

    public void setLabelUHID(String labelUHID) {
        this.labelUHID = labelUHID;
    }

    public String getLabelRefID() {
        return labelRefID;
    }

    public void setLabelRefID(String labelRefID) {
        this.labelRefID = labelRefID;
    }

    public String getLabelDoctorName() {
        return labelDoctorName;
    }

    public void setLabelDoctorName(String labelDoctorName) {
        this.labelDoctorName = labelDoctorName;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(labelUHID);
        dest.writeValue(labelRefID);
        dest.writeValue(labelDoctorName);
    }

    public int describeContents() {
        return 0;
    }

}