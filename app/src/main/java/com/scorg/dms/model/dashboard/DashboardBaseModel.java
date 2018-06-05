
package com.scorg.dms.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class DashboardBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private DashboardDataModel dashboardDataModel;
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

    };

    protected DashboardBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.dashboardDataModel = ((DashboardDataModel) in.readValue((DashboardDataModel.class.getClassLoader())));
    }

    public DashboardBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public DashboardDataModel getDashboardDataModel() {
        return dashboardDataModel;
    }

    public void setDashboardDataModel(DashboardDataModel dashboardDataModel) {
        this.dashboardDataModel = dashboardDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(dashboardDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
