
package com.scorg.dms.model.doctor_connect_search;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class DoctorConnectSearchBaseModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private SearchDataModel searchDataModel;
    public final static Creator<DoctorConnectSearchBaseModel> CREATOR = new Creator<DoctorConnectSearchBaseModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DoctorConnectSearchBaseModel createFromParcel(Parcel in) {
            DoctorConnectSearchBaseModel instance = new DoctorConnectSearchBaseModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.searchDataModel = ((SearchDataModel) in.readValue((SearchDataModel.class.getClassLoader())));
            return instance;
        }

        public DoctorConnectSearchBaseModel[] newArray(int size) {
            return (new DoctorConnectSearchBaseModel[size]);
        }

    };

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public SearchDataModel getSearchDataModel() {
        return searchDataModel;
    }

    public void setSearchDataModel(SearchDataModel searchDataModel) {
        this.searchDataModel = searchDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(searchDataModel);
    }

    public int describeContents() {
        return 0;
    }

}
