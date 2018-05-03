
package com.rescribe.doctor.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class DashboardBaseModel implements Parcelable,CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DashboarddataModel dashboarddataModel;
    public final static Creator<DashboardBaseModel> CREATOR = new Creator<DashboardBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public DashboardBaseModel createFromParcel(Parcel in) {
            return new DashboardBaseModel(in);
        }

        public DashboardBaseModel[] newArray(int size) {
            return (new DashboardBaseModel[size]);
        }

    }
    ;

    protected DashboardBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.dashboarddataModel = ((DashboarddataModel) in.readValue((DashboarddataModel.class.getClassLoader())));
    }

    public DashboardBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DashboarddataModel getDashboarddataModel() {
        return dashboarddataModel;
    }

    public void setDashboarddataModel(DashboarddataModel dashboarddataModel) {
        this.dashboarddataModel = dashboarddataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(dashboarddataModel);
    }

    public int describeContents() {
        return  0;
    }

}
