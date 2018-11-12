
package com.scorg.dms.model.dashboard;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.model.my_appointments.AppointmentPatientData;

import java.util.ArrayList;

public class DashboardDataModel implements Parcelable {

    @SerializedName("totalPatientCount")
    @Expose
    private String totalPatientCount;
    @SerializedName("appointmentCount")
    @Expose
    private String appointmentCount;
    @SerializedName("pendingApprovedCount")
    @Expose
    private String pendingApprovedCount;
    @SerializedName("waitingCount")
    @Expose
    private String waitingCount;

    @SerializedName("admittedPatientCount")
    @Expose
    private String admittedPatientCount;

    @SerializedName("colorPrimary")
    @Expose
    private String colorPrimary;

    @SerializedName("colorPrimaryDark")
    @Expose
    private String colorPrimaryDark;

    @SerializedName("colorAccent")
    @Expose
    private String colorAccent;



    @SerializedName("appointmentTextColor")
    @Expose
    private String appointmentTextColor;

    @SerializedName("fileTypes")
    @Expose
    private String[] fileTypes;

    @SerializedName("ResultApiTakeCount")
    @Expose
    private Integer resultApiTakeCount;
    @SerializedName("EpisodeApiTakeCount")
    @Expose
    private Integer episodeApiTakeCount;
    @SerializedName("ViewArchivedApiTakeCount")
    @Expose
    private Integer viewArchivedApiTakeCount;
    @SerializedName("PatientApiTakeCount")
    @Expose
    private Integer patientApiTakeCount;

    /* @SerializedName("appointmentOpdOTAndOtherCount")
     @Expose
     private AppointmentOpdAndOtherCount appointmentOpdOTAndOtherCount;
 */
    @SerializedName("appointmentOpdOTAndOtherCount")
    @Expose
    private ArrayList<AppointmentOpdAndOtherCount> appointmentOpdOTAndOtherCountList = new ArrayList<>();
    @SerializedName("TodaysAppointmentList")
    @Expose
    private ArrayList<AppointmentPatientData> appointmentPatientDataList = new ArrayList<>();

    @SerializedName("appointmentStatusUrl")
    @Expose
    private String appointmentStatusUrl;

    @SerializedName("lstLabelNameData")
    @Expose
    private LstLabelNameData lstLabelNameData;


    public final static Parcelable.Creator<DashboardDataModel> CREATOR = new Creator<DashboardDataModel>() {


        @SuppressWarnings({
                "unchecked"
        })
        public DashboardDataModel createFromParcel(Parcel in) {
            DashboardDataModel instance = new DashboardDataModel();
            instance.setTotalPatientCount((String) in.readValue((String.class.getClassLoader())));
            instance.setAppointmentCount(((String) in.readValue((String.class.getClassLoader()))));
            instance.setPendingApprovedCount(((String) in.readValue((String.class.getClassLoader()))));
            instance.setWaitingCount(((String) in.readValue((String.class.getClassLoader()))));
            instance.setColorPrimary(((String) in.readValue((String.class.getClassLoader()))));
            instance.setAdmittedPatientCount(((String) in.readValue((String.class.getClassLoader()))));
            instance.setColorPrimaryDark(((String) in.readValue((String.class.getClassLoader()))));
            instance.setAppointmentTextColor(((String) in.readValue((String.class.getClassLoader()))));
            instance.setColorAccent(((String) in.readValue((String.class.getClassLoader()))));
            instance.setFileTypes(in.createStringArray());


            // instance.setAppointmentOpdOTAndOtherCount((AppointmentOpdAndOtherCount) in.readValue(AppointmentOpdAndOtherCount.class.getClassLoader()));

            in.readList(instance.getAppointmentOpdOTAndOtherCountList(), (AppointmentOpdAndOtherCount.class.getClassLoader()));

            //--------
            instance.setResultApiTakeCount(((Integer) in.readValue((Integer.class.getClassLoader()))));
            instance.setEpisodeApiTakeCount(((Integer) in.readValue((Integer.class.getClassLoader()))));
            instance.setViewArchivedApiTakeCount(((Integer) in.readValue((Integer.class.getClassLoader()))));
            instance.setPatientApiTakeCount(((Integer) in.readValue((Integer.class.getClassLoader()))));
            in.readList(instance.getAppointmentPatientDataList(), (AppointmentPatientData.class.getClassLoader()));
            instance.setAppointmentStatusUrl(((String) in.readValue((String.class.getClassLoader()))));
            instance.setLstLabelNameData(((LstLabelNameData) in.readValue((LstLabelNameData.class.getClassLoader()))));
            //--------

            return instance;
        }

        public DashboardDataModel[] newArray(int size) {
            return (new DashboardDataModel[size]);
        }

    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(getTotalPatientCount());
        dest.writeValue(getAppointmentCount());
        dest.writeValue(getPendingApprovedCount());
        dest.writeValue(getWaitingCount());
        dest.writeStringArray(getFileTypes());
        dest.writeList(getAppointmentOpdOTAndOtherCountList());
        //-------
        dest.writeValue(getResultApiTakeCount());
        dest.writeValue(getEpisodeApiTakeCount());
        dest.writeValue(getViewArchivedApiTakeCount());
        dest.writeValue(getPatientApiTakeCount());
        dest.writeValue(getColorPrimary());
        dest.writeValue(getColorPrimaryDark());
        dest.writeValue(getColorAccent());
        dest.writeValue(getAppointmentTextColor());
        dest.writeValue(getAdmittedPatientCount());
        dest.writeList(getAppointmentPatientDataList());
        dest.writeValue(getAppointmentStatusUrl());
        dest.writeValue(getLstLabelNameData());

        //-------
    }

    public int describeContents() {
        return 0;
    }

    public String getTotalPatientCount() {
        return totalPatientCount;
    }

    public void setTotalPatientCount(String totalPatientCount) {
        this.totalPatientCount = totalPatientCount;
    }

    public String getAppointmentCount() {
        return appointmentCount;
    }

    public void setAppointmentCount(String appointmentCount) {
        this.appointmentCount = appointmentCount;
    }

    public String getPendingApprovedCount() {
        return pendingApprovedCount;
    }

    public void setPendingApprovedCount(String pendingApprovedCount) {
        this.pendingApprovedCount = pendingApprovedCount;
    }

    public String getWaitingCount() {
        return waitingCount;
    }

    public void setWaitingCount(String waitingCount) {
        this.waitingCount = waitingCount;
    }

    public String[] getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(String[] fileTypes) {
        this.fileTypes = fileTypes;
    }

    public String getColorPrimaryDark() {
        return colorPrimaryDark;
    }

    public void setColorPrimaryDark(String colorPrimaryDark) {
        this.colorPrimaryDark = colorPrimaryDark;
    }

    public String getColorAccent() {
        return colorAccent;
    }

    public void setColorAccent(String colorAccent) {
        this.colorAccent = colorAccent;
    }

    public ArrayList<AppointmentOpdAndOtherCount> getAppointmentOpdOTAndOtherCountList() {
        return appointmentOpdOTAndOtherCountList;
    }

    public String getAppointmentTextColor() {
        return appointmentTextColor;
    }

    public void setAppointmentTextColor(String appointmentTextColor) {
        this.appointmentTextColor = appointmentTextColor;
    }

    public void setAppointmentOpdOTAndOtherCountList(ArrayList<AppointmentOpdAndOtherCount> appointmentOpdOTAndOtherCount) {
        this.appointmentOpdOTAndOtherCountList = appointmentOpdOTAndOtherCount;
    }

    public int getResultApiTakeCount() {
        return resultApiTakeCount;
    }

    public void setResultApiTakeCount(int resultApiTakeCount) {
        this.resultApiTakeCount = resultApiTakeCount;
    }

    public int getEpisodeApiTakeCount() {
        return episodeApiTakeCount;
    }

    public void setEpisodeApiTakeCount(int episodeApiTakeCount) {
        this.episodeApiTakeCount = episodeApiTakeCount;
    }

    public int getViewArchivedApiTakeCount() {
        return viewArchivedApiTakeCount;
    }

    public void setViewArchivedApiTakeCount(int viewArchivedApiTakeCount) {
        this.viewArchivedApiTakeCount = viewArchivedApiTakeCount;
    }

    public int getPatientApiTakeCount() {
        return patientApiTakeCount;
    }

    public void setPatientApiTakeCount(int patientApiTakeCount) {
        this.patientApiTakeCount = patientApiTakeCount;
    }

    public String getAdmittedPatientCount() {
        return admittedPatientCount;
    }

    public void setAdmittedPatientCount(String admittedPatientCount) {
        this.admittedPatientCount = admittedPatientCount;
    }

    public ArrayList<AppointmentPatientData> getAppointmentPatientDataList() {
        return appointmentPatientDataList;
    }

    public void setAppointmentPatientDataList(ArrayList<AppointmentPatientData> appointmentPatientDataList) {
        this.appointmentPatientDataList = appointmentPatientDataList;
    }

    public String getAppointmentStatusUrl() {
        return appointmentStatusUrl;
    }

    public void setAppointmentStatusUrl(String appointmentStatusUrl) {
        this.appointmentStatusUrl = appointmentStatusUrl;
    }

    public LstLabelNameData getLstLabelNameData() {
        return lstLabelNameData;
    }

    public void setLstLabelNameData(LstLabelNameData lstLabelNameData) {
        this.lstLabelNameData = lstLabelNameData;
    }

    public String getColorPrimary() {
        return colorPrimary;
    }

    public void setColorPrimary(String colorPrimary) {
        this.colorPrimary = colorPrimary;
    }

    public static class AppointmentOpdAndOtherCount implements Parcelable {

        @SerializedName("appointmentOpdOTName")
        @Expose
        private String appointmentOpdOTName;
        @SerializedName("appointmentOpdOTCount")
        @Expose
        private String appointmentOpdOTCount;

        public static final Creator<AppointmentOpdAndOtherCount> CREATOR = new Creator<AppointmentOpdAndOtherCount>() {
            @Override
            public AppointmentOpdAndOtherCount createFromParcel(Parcel in) {
                AppointmentOpdAndOtherCount instance = new AppointmentOpdAndOtherCount();
                instance.setAppointmentOpdOTCount((String) in.readValue((String.class.getClassLoader())));
                instance.setAppointmentOpdOTName(((String) in.readValue((String.class.getClassLoader()))));
                return instance;
            }

            @Override
            public AppointmentOpdAndOtherCount[] newArray(int size) {
                return (new AppointmentOpdAndOtherCount[size]);
            }
        };

        public String getAppointmentOpdOTName() {
            return appointmentOpdOTName;
        }

        public void setAppointmentOpdOTName(String appointmentOpdOTName) {
            this.appointmentOpdOTName = appointmentOpdOTName;
        }

        public String getAppointmentOpdOTCount() {
            return appointmentOpdOTCount;
        }

        public void setAppointmentOpdOTCount(String appointmentOpdOTCount) {
            this.appointmentOpdOTCount = appointmentOpdOTCount;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(appointmentOpdOTCount);
            dest.writeString(appointmentOpdOTName);
        }
    }

}
