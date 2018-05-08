
package com.scorg.dms.model.patient.patient_connect;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.util.CommonMethods;

public class PatientData implements Parcelable, CustomResponse {

    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("patientName")
    @Expose
    private String patientName;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl = "";

    @SerializedName("onlineStatus")
    @Expose
    private String onlineStatus = "";

    @SerializedName("lastChatTime")
    @Expose
    private String lastChatTime;

    @SerializedName("unreadMessages")
    @Expose
    private int unreadMessages;

    @SerializedName("salutation")
    @Expose
    private Integer salutation = 0;
    private String lastChat;

    public Integer getId() {
        return patientId;
    }

    public void setId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return CommonMethods.toCamelCase(patientName);
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOnlineStatus() {
        return CommonMethods.toCamelCase(onlineStatus);
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getLastChatTime() {
        return lastChatTime;
    }

    public void setLastChatTime(String lastChatTime) {
        this.lastChatTime = lastChatTime;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public Integer getSalutation() {
        return salutation;
    }

    public void setSalutation(Integer salutation) {
        this.salutation = salutation;
    }

    public String getLastChat() {
        return lastChat;
    }

    public void setLastChat(String lastChat) {
        this.lastChat = lastChat;
    }

    public final static Creator<PatientData> CREATOR = new Creator<PatientData>() {
        @SuppressWarnings({
                "unchecked"
        })
        public PatientData createFromParcel(Parcel in) {
            PatientData instance = new PatientData();
            instance.patientId = ((Integer) in.readValue((Integer.class.getClassLoader())));
            instance.patientName = ((String) in.readValue((String.class.getClassLoader())));
            instance.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
            instance.onlineStatus = ((String) in.readValue((String.class.getClassLoader())));
            instance.lastChatTime = ((String) in.readValue((String.class.getClassLoader())));
            instance.unreadMessages = ((int) in.readValue((Integer.class.getClassLoader())));
            instance.salutation = ((Integer) in.readValue((Integer.class.getClassLoader())));
            return instance;
        }

        public PatientData[] newArray(int size) {
            return (new PatientData[size]);
        }

    };


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(patientId);
        dest.writeValue(patientName);
        dest.writeValue(imageUrl);
        dest.writeValue(onlineStatus);
        dest.writeValue(lastChatTime);
        dest.writeValue(unreadMessages);
        dest.writeValue(salutation);
    }

    public int describeContents() {
        return 0;
    }
}
