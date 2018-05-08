package com.scorg.dms.model.appointments;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppointmentModel {

    @SerializedName("aptList")
    @Expose
    private ArrayList<AptList> aptList = null;

    public ArrayList<AptList> getAptList() {
        return aptList;
    }

    public void setAptList(ArrayList<AptList> aptList) {
        this.aptList = aptList;
    }

}