
package com.scorg.dms.model.waiting_list;

import java.util.ArrayList;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WaitingPatientList implements Parcelable {

    @SerializedName("viewAll")
    @Expose
    private ArrayList<ViewAll> viewAll = new ArrayList<ViewAll>();
    @SerializedName("active")
    @Expose
    private ArrayList<Active> active = new ArrayList<Active>();
    public final static Creator<WaitingPatientList> CREATOR = new Creator<WaitingPatientList>() {


        @SuppressWarnings({
                "unchecked"
        })
        public WaitingPatientList createFromParcel(Parcel in) {
            return new WaitingPatientList(in);
        }

        public WaitingPatientList[] newArray(int size) {
            return (new WaitingPatientList[size]);
        }

    };

    protected WaitingPatientList(Parcel in) {
        in.readList(this.viewAll, (ViewAll.class.getClassLoader()));
        in.readList(this.active, (Active.class.getClassLoader()));
    }

    public WaitingPatientList() {
    }

    public ArrayList<ViewAll> getViewAll() {
        return viewAll;
    }

    public void setViewAll(ArrayList<ViewAll> viewAll) {
        this.viewAll = viewAll;
    }

    public ArrayList<Active> getActive() {
        return active;
    }

    public void setActive(ArrayList<Active> active) {
        this.active = active;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(viewAll);
        dest.writeList(active);
    }

    public int describeContents() {
        return 0;
    }

}
