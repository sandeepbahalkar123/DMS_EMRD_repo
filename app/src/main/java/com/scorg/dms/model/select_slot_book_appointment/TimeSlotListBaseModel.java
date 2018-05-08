
package com.scorg.dms.model.select_slot_book_appointment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;


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
