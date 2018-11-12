package com.scorg.dms.model.dms_models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ViewRights implements Serializable {

@SerializedName("IsRequestForAll")
@Expose
private boolean isRequestForAll;
@SerializedName("IsAllFileAccessible")
@Expose
private boolean isAllFileAccessible;
@SerializedName("AllowOnlyPrimaryFiles")
@Expose
private boolean allowOnlyPrimaryFiles;
@SerializedName("IsOneFileIsPrimary")
@Expose
private boolean isOneFileIsPrimary;
@SerializedName("PrimaryFileTypeSetting")
@Expose
private List<String> primaryFileTypeSetting = null;

public boolean getIsRequestForAll() {
return isRequestForAll;
}

public void setIsRequestForAll(boolean isRequestForAll) {
this.isRequestForAll = isRequestForAll;
}

public boolean getIsAllFileAccessible() {
return isAllFileAccessible;
}

public void setIsAllFileAccessible(boolean isAllFileAccessible) {
this.isAllFileAccessible = isAllFileAccessible;
}

public boolean getAllowOnlyPrimaryFiles() {
return allowOnlyPrimaryFiles;
}

public void setAllowOnlyPrimaryFiles(boolean allowOnlyPrimaryFiles) {
this.allowOnlyPrimaryFiles = allowOnlyPrimaryFiles;
}

public boolean getIsOneFileIsPrimary() {
return isOneFileIsPrimary;
}

public void setIsOneFileIsPrimary(boolean isOneFileIsPrimary) {
this.isOneFileIsPrimary = isOneFileIsPrimary;
}

public List<String> getPrimaryFileTypeSetting() {
return primaryFileTypeSetting;
}

public void setPrimaryFileTypeSetting(List<String> primaryFileTypeSetting) {
this.primaryFileTypeSetting = primaryFileTypeSetting;
}

}