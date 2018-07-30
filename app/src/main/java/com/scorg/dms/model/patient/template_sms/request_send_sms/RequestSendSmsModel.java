
package com.scorg.dms.model.patient.template_sms.request_send_sms;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.util.ArrayList;
import java.util.List;

public class RequestSendSmsModel implements CustomResponse, Parcelable {

    @SerializedName("clinicList")
    @Expose
    private List<ClinicListForSms> clinicListForSms = new ArrayList<ClinicListForSms>();

    public final static Parcelable.Creator<RequestSendSmsModel> CREATOR = new Creator<RequestSendSmsModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RequestSendSmsModel createFromParcel(Parcel in) {
            return new RequestSendSmsModel(in);
        }

        public RequestSendSmsModel[] newArray(int size) {
            return (new RequestSendSmsModel[size]);
        }

    };

    protected RequestSendSmsModel(Parcel in) {
        in.readList(this.clinicListForSms, (ClinicListForSms.class.getClassLoader()));
    }

    public RequestSendSmsModel() {
    }


    public List<ClinicListForSms> getClinicListForSms() {
        return clinicListForSms;
    }

    public void setClinicListForSms(List<ClinicListForSms> clinicListForSms) {
        this.clinicListForSms = clinicListForSms;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(clinicListForSms);
    }

    public int describeContents() {
        return 0;
    }

}
