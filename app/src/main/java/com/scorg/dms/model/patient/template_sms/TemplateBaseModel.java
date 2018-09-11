
package com.scorg.dms.model.patient.template_sms;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class TemplateBaseModel implements Parcelable , CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private TemplateDataModel templateDataModel;
    public final static Creator<TemplateBaseModel> CREATOR = new Creator<TemplateBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TemplateBaseModel createFromParcel(Parcel in) {
            return new TemplateBaseModel(in);
        }

        public TemplateBaseModel[] newArray(int size) {
            return (new TemplateBaseModel[size]);
        }

    }
    ;

    protected TemplateBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.templateDataModel = ((TemplateDataModel) in.readValue((TemplateDataModel.class.getClassLoader())));
    }

    public TemplateBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public TemplateDataModel getTemplateDataModel() {
        return templateDataModel;
    }

    public void setTemplateDataModel(TemplateDataModel templateDataModel) {
        this.templateDataModel = templateDataModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(templateDataModel);
    }

    public int describeContents() {
        return  0;
    }

}
