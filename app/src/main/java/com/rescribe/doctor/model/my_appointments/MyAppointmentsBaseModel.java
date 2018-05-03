
package com.rescribe.doctor.model.my_appointments;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;

public class MyAppointmentsBaseModel implements Parcelable , CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private MyAppointmentsDataModel myAppointmentsDataModel;
    public final static Creator<MyAppointmentsBaseModel> CREATOR = new Creator<MyAppointmentsBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public MyAppointmentsBaseModel createFromParcel(Parcel in) {
            return new MyAppointmentsBaseModel(in);
        }

        public MyAppointmentsBaseModel[] newArray(int size) {
            return (new MyAppointmentsBaseModel[size]);
        }

    }
    ;

    protected MyAppointmentsBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.myAppointmentsDataModel = ((MyAppointmentsDataModel) in.readValue((MyAppointmentsDataModel.class.getClassLoader())));
    }

    public MyAppointmentsBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public MyAppointmentsDataModel getMyAppointmentsDataModel() {
        return myAppointmentsDataModel;
    }

    public void setMyAppointmentsDataModel(MyAppointmentsDataModel myAppointmentsDataModel) {
        this.myAppointmentsDataModel = myAppointmentsDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(myAppointmentsDataModel);
    }

    public int describeContents() {
        return  0;
    }

}
