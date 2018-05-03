
package com.rescribe.doctor.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboarddataModel implements Parcelable
{

    @SerializedName("dashboardDetails")
    @Expose
    private DashboardDetails dashboardDetails;
    public final static Creator<DashboarddataModel> CREATOR = new Creator<DashboarddataModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboarddataModel createFromParcel(Parcel in) {
            return new DashboarddataModel(in);
        }

        public DashboarddataModel[] newArray(int size) {
            return (new DashboarddataModel[size]);
        }

    }
    ;

    protected DashboarddataModel(Parcel in) {
        this.dashboardDetails = ((DashboardDetails) in.readValue((DashboardDetails.class.getClassLoader())));
    }

    public DashboarddataModel() {
    }

    public DashboardDetails getDashboardDetails() {
        return dashboardDetails;
    }

    public void setDashboardDetails(DashboardDetails dashboardDetails) {
        this.dashboardDetails = dashboardDetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(dashboardDetails);
    }

    public int describeContents() {
        return  0;
    }

}
