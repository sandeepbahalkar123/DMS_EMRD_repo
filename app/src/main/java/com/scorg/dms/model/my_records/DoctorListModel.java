package com.scorg.dms.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DoctorListModel {

@SerializedName("docList")
@Expose
private ArrayList<SpinnerDoctor> docList = null;

public ArrayList<SpinnerDoctor> getDocList() {
return docList;
}

public void setDocList(ArrayList<SpinnerDoctor> docList) {
this.docList = docList;
}

}