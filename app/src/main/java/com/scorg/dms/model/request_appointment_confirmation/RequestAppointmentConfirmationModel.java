
package com.scorg.dms.model.request_appointment_confirmation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class RequestAppointmentConfirmationModel implements CustomResponse {

    @SerializedName("patientId")
    @Expose
    private Integer patientId;
    @SerializedName("docId")
    @Expose
    private Integer docId;
    @SerializedName("locationId")
    @Expose
    private Integer locationId;
    @SerializedName("slotId")
    @Expose
    private Integer slotId;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("fromTime")
    @Expose
    private String fromTime;
    @SerializedName("toTime")
    @Expose
    private String toTime;

    @SerializedName("reschedule")
    @Expose
    private Reschedule reschedule;

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDocId() {
        return docId;
    }

    public void setDocId(Integer docId) {
        this.docId = docId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public void setSlotId(Integer slotId) {
        this.slotId = slotId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }


    public Reschedule getReschedule() {
        return reschedule;
    }

    public void setReschedule(Reschedule reschedule) {
        this.reschedule = reschedule;
    }
}
