
package com.rescribe.doctor.model.select_slot_book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.interfaces.CustomResponse;
import com.rescribe.doctor.model.Common;


public class TimeSlotListBaseModel implements CustomResponse {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private TimeSlotListDataModel timeSlotListDataModel;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }


    public TimeSlotListDataModel getTimeSlotListDataModel() {
        return timeSlotListDataModel;
    }

    public void setTimeSlotListDataModel(TimeSlotListDataModel timeSlotListDataModel) {
        this.timeSlotListDataModel = timeSlotListDataModel;
    }
}
