package com.rescribe.doctor.model.investigation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;


public class InvestigationListModel implements Parcelable, CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private InvestigationNotification investigationNotification = new InvestigationNotification();
    public final static Creator<InvestigationListModel> CREATOR = new Creator<InvestigationListModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public InvestigationListModel createFromParcel(Parcel in) {
            InvestigationListModel instance = new InvestigationListModel();
            instance.common = ((Common) in.readValue((Common.class.getClassLoader())));
            instance.investigationNotification = ((InvestigationNotification) in.readValue((InvestigationNotification.class.getClassLoader())));
            return instance;
        }

        public InvestigationListModel[] newArray(int size) {
            return (new InvestigationListModel[size]);
        }

    }
            ;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public InvestigationNotification getInvestigationNotification() {
        return investigationNotification;
    }

    public void setInvestigationNotification(InvestigationNotification investigationNotification) {
        this.investigationNotification = investigationNotification;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(investigationNotification);
    }

    public int describeContents() {
        return 0;
    }

}