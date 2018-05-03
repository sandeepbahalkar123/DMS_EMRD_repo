package com.rescribe.doctor.dms.model.responsemodel.iptestresponsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rescribe.doctor.dms.interfaces.CustomResponse;
import com.rescribe.doctor.dms.model.responsemodel.Common;

import java.io.Serializable;

public class IpTestResponseModel implements CustomResponse,Serializable {

@SerializedName("common")
@Expose
private Common common;

public Common getCommon() {
return common;
}

public void setCommon(Common common) {
this.common = common;
}

}