package com.scorg.dms.model.investigation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InvestigationNotification implements Parcelable {

    @SerializedName("notifications")
    @Expose
    private ArrayList<InvestigationData> notifications = new ArrayList<>();
    public final static Creator<InvestigationNotification> CREATOR = new Creator<InvestigationNotification>() {


        @SuppressWarnings({
                "unchecked"
        })
        public InvestigationNotification createFromParcel(Parcel in) {
            InvestigationNotification instance = new InvestigationNotification();
            in.readList(instance.notifications, (InvestigationData.class.getClassLoader()));
            return instance;
        }

        public InvestigationNotification[] newArray(int size) {
            return (new InvestigationNotification[size]);
        }

    };

    public ArrayList<InvestigationData> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<InvestigationData> notifications) {
        this.notifications = notifications;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(notifications);
    }

    public int describeContents() {
        return 0;
    }

}