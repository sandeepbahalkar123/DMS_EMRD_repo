package com.rescribe.doctor.dms.model.responsemodel.patientnamelistresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientNameListData {

@SerializedName("lstPatients")
@Expose
private List<LstPatient> lstPatients = null;

public List<LstPatient> getLstPatients() {
return lstPatients;
}

public void setLstPatients(List<LstPatient> lstPatients) {
this.lstPatients = lstPatients;
}

}