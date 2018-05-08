package com.scorg.dms.model.my_appointments.request_cancel_or_complete_appointment;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

public class RequestAppointmentCancelModel implements Parcelable , CustomResponse
{

@SerializedName("aptId")
@Expose
private Integer aptId;
@SerializedName("status")
@Expose
private Integer status;
@SerializedName("type")
@Expose
private String type;
@SerializedName("patientId")
@Expose
private Integer patientId;
public final static Parcelable.Creator<RequestAppointmentCancelModel> CREATOR = new Creator<RequestAppointmentCancelModel>() {


@SuppressWarnings({
"unchecked"
})
public RequestAppointmentCancelModel createFromParcel(Parcel in) {
return new RequestAppointmentCancelModel(in);
}

public RequestAppointmentCancelModel[] newArray(int size) {
return (new RequestAppointmentCancelModel[size]);
}

}
;

protected RequestAppointmentCancelModel(Parcel in) {
this.aptId = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
this.type = ((String) in.readValue((String.class.getClassLoader())));
this.patientId = ((Integer) in.readValue((Integer.class.getClassLoader())));
}

public RequestAppointmentCancelModel() {
}

public Integer getAptId() {
return aptId;
}

public void setAptId(Integer aptId) {
this.aptId = aptId;
}

public Integer getStatus() {
return status;
}

public void setStatus(Integer status) {
this.status = status;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public Integer getPatientId() {
return patientId;
}

public void setPatientId(Integer patientId) {
this.patientId = patientId;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(aptId);
dest.writeValue(status);
dest.writeValue(type);
dest.writeValue(patientId);
}

public int describeContents() {
return 0;
}

}