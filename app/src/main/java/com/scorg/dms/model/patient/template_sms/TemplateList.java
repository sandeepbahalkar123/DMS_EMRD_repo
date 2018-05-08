
package com.scorg.dms.model.patient.template_sms;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TemplateList implements Parcelable
{

    @SerializedName("templateId")
    @Expose
    private Integer templateId;
    @SerializedName("templateName")
    @Expose
    private String templateName;
    @SerializedName("templateContent")
    @Expose
    private String templateContent;
    public final static Creator<TemplateList> CREATOR = new Creator<TemplateList>() {


        @SuppressWarnings({
            "unchecked"
        })
        public TemplateList createFromParcel(Parcel in) {
            return new TemplateList(in);
        }

        public TemplateList[] newArray(int size) {
            return (new TemplateList[size]);
        }

    }
    ;

    protected TemplateList(Parcel in) {
        this.templateId = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.templateName = ((String) in.readValue((String.class.getClassLoader())));
        this.templateContent = ((String) in.readValue((String.class.getClassLoader())));
    }

    public TemplateList() {
    }

    public Integer getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(templateId);
        dest.writeValue(templateName);
        dest.writeValue(templateContent);
    }

    public int describeContents() {
        return  0;
    }

}
