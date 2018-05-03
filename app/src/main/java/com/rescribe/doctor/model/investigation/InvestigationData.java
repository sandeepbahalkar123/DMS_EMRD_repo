package com.rescribe.doctor.model.investigation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InvestigationData implements Parcelable {

    @SerializedName("selected")
    @Expose
    private boolean selected = false;
    @SerializedName("uploaded")
    @Expose
    private boolean uploaded = false;
    @SerializedName("image")
    @Expose
    private ArrayList<Image> image = new ArrayList<Image>();
    @SerializedName("investigationLabel")
    @Expose
    private String investigationLabel;
    @SerializedName("investigationKey")
    @Expose
    private String investigationKey;
    @SerializedName("DoctorName")
    @Expose
    private String doctorName;
    @SerializedName("opdId")
    @Expose
    private int opdId;
    @SerializedName("invetigationId")
    @Expose
    private int invetigationId;
    @SerializedName("drId")
    @Expose
    private int drId;
    public final static Creator<InvestigationData> CREATOR = new Creator<InvestigationData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public InvestigationData createFromParcel(Parcel in) {
            InvestigationData instance = new InvestigationData();
            instance.selected = ((boolean) in.readValue((boolean.class.getClassLoader())));
            instance.uploaded = ((boolean) in.readValue((boolean.class.getClassLoader())));
            in.readList(instance.image, (Image.class.getClassLoader()));
            instance.investigationLabel = ((String) in.readValue((String.class.getClassLoader())));
            instance.investigationKey = ((String) in.readValue((String.class.getClassLoader())));
            instance.doctorName = ((String) in.readValue((String.class.getClassLoader())));
            instance.opdId = ((int) in.readValue((int.class.getClassLoader())));
            instance.invetigationId = ((int) in.readValue((int.class.getClassLoader())));
            instance.drId = ((int) in.readValue((int.class.getClassLoader())));
            return instance;
        }

        public InvestigationData[] newArray(int size) {
            return (new InvestigationData[size]);
        }

    };

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public ArrayList<Image> getPhotos() {
        return image;
    }

    public void setPhotos(ArrayList<Image> image) {
        this.image = image;
    }

    public String getTitle() {
        return investigationLabel;
    }

    public void setTitle(String investigationLabel) {
        this.investigationLabel = investigationLabel;
    }

    public String getInvestigationKey() {
        return investigationKey;
    }

    public void setInvestigationKey(String investigationKey) {
        this.investigationKey = investigationKey;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getOpdId() {
        return opdId;
    }

    public void setOpdId(int opdId) {
        this.opdId = opdId;
    }

    public int getId() {
        return invetigationId;
    }

    public void setId(int invetigationId) {
        this.invetigationId = invetigationId;
    }

    public int getDrId() {
        return drId;
    }

    public void setDrId(int drId) {
        this.drId = drId;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(selected);
        dest.writeValue(uploaded);
        dest.writeList(image);
        dest.writeValue(investigationLabel);
        dest.writeValue(investigationKey);
        dest.writeValue(doctorName);
        dest.writeValue(opdId);
        dest.writeValue(invetigationId);
        dest.writeValue(drId);
    }

    public int describeContents() {
        return 0;
    }

}