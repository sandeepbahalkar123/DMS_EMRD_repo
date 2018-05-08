
package com.scorg.dms.model.case_details;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class VisitCommonData implements CustomResponse, Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url = "";
    @SerializedName("medicinename")
    @Expose
    private String medicinename = "";
    @SerializedName("remarks")
    @Expose
    private String remarks = "";
    @SerializedName("dosage")
    @Expose
    private String dosage = "";
    @SerializedName("medicineTypeName")
    @Expose
    private String medicineTypeName = "";
    private String vitalValue;

    public final static Creator<VisitCommonData> CREATOR = new Creator<VisitCommonData>() {

        @SuppressWarnings({
                "unchecked"
        })
        public VisitCommonData createFromParcel(Parcel in) {
            VisitCommonData instance = new VisitCommonData();
            instance.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.name = ((String) in.readValue((String.class.getClassLoader())));
            instance.url = ((String) in.readValue((String.class.getClassLoader())));
            instance.medicinename = ((String) in.readValue((String.class.getClassLoader())));
            instance.remarks = ((String) in.readValue((String.class.getClassLoader())));
            instance.dosage = ((String) in.readValue((String.class.getClassLoader())));
            instance.medicineTypeName = ((String) in.readValue((String.class.getClassLoader())));
            instance.vitalValue = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public VisitCommonData[] newArray(int size) {
            return (new VisitCommonData[size]);
        }

    };


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedicinename() {
        return medicinename;
    }

    public void setMedicinename(String medicinename) {
        this.medicinename = medicinename;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getMedicineTypeName() {
        return medicineTypeName;
    }

    public void setMedicineTypeName(String medicineTypeName) {
        this.medicineTypeName = medicineTypeName;
    }

    public String getVitalValue() {
        return vitalValue;
    }

    public void setVitalValue(String vitalValue) {
        this.vitalValue = vitalValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(url);
        dest.writeValue(medicinename);
        dest.writeValue(remarks);
        dest.writeValue(dosage);
        dest.writeValue(medicineTypeName);
        dest.writeValue(vitalValue);
    }

    public int describeContents() {
        return 0;
    }

}
