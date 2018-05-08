package com.scorg.dms.model.my_records;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.util.CommonMethods;

import static com.scorg.dms.util.DMSConstants.DATE_PATTERN.DD_MM_YYYY;

public class VisitDate implements Parcelable {

    @SerializedName("visitDate")
    @Expose
    private String opdDate;
    @SerializedName("opdId")
    @Expose
    private int opdId;
    public final static Creator<VisitDate> CREATOR = new Creator<VisitDate>() {


        @SuppressWarnings({
                "unchecked"
        })
        public VisitDate createFromParcel(Parcel in) {
            VisitDate instance = new VisitDate();
            instance.opdDate = ((String) in.readValue((String.class.getClassLoader())));
            instance.opdId = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public VisitDate[] newArray(int size) {
            return (new VisitDate[size]);
        }

    };

    public String getOpdDate() {
        return opdDate;
    }

    public void setOpdDate(String opdDate) {
        this.opdDate = opdDate;
    }

    public int getOpdId() {
        return opdId;
    }

    public void setOpdId(int opdId) {
        this.opdId = opdId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(opdDate);
        dest.writeValue(opdId);
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {

        if (opdDate.equals("Select Date") || opdDate.equals(""))
            return opdDate;

        String ordinal = CommonMethods.ordinal(CommonMethods.getFormattedDate(opdDate, DD_MM_YYYY, "dd"));
        return ordinal + " " + CommonMethods.getFormattedDate(opdDate, DD_MM_YYYY, "MMM yyyy").toUpperCase();
    }
}