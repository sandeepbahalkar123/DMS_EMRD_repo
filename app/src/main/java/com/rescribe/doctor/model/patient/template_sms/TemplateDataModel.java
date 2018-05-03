
package com.rescribe.doctor.model.patient.template_sms;

import java.util.ArrayList;
import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TemplateDataModel implements Parcelable
{

    @SerializedName("templateList")
    @Expose
    private ArrayList<TemplateList> templateList = new ArrayList<TemplateList>();
    public final static Creator<TemplateDataModel> CREATOR = new Creator<TemplateDataModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TemplateDataModel createFromParcel(Parcel in) {
            return new TemplateDataModel(in);
        }

        public TemplateDataModel[] newArray(int size) {
            return (new TemplateDataModel[size]);
        }

    }
    ;

    protected TemplateDataModel(Parcel in) {
        in.readList(this.templateList, (TemplateList.class.getClassLoader()));
    }

    public TemplateDataModel() {
    }

    public ArrayList<TemplateList> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(ArrayList<TemplateList> templateList) {
        this.templateList = templateList;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(templateList);
    }

    public int describeContents() {
        return  0;
    }

}
