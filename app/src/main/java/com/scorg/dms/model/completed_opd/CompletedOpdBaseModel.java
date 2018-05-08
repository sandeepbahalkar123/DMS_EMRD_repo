
package com.scorg.dms.model.completed_opd;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

public class CompletedOpdBaseModel implements Parcelable,CustomResponse
{

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private CompletedOpdModel completedOpdModel;
    public final static Creator<CompletedOpdBaseModel> CREATOR = new Creator<CompletedOpdBaseModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CompletedOpdBaseModel createFromParcel(Parcel in) {
            return new CompletedOpdBaseModel(in);
        }

        public CompletedOpdBaseModel[] newArray(int size) {
            return (new CompletedOpdBaseModel[size]);
        }

    }
    ;

    protected CompletedOpdBaseModel(Parcel in) {
        this.common = ((Common) in.readValue((Common.class.getClassLoader())));
        this.completedOpdModel = ((CompletedOpdModel) in.readValue((CompletedOpdModel.class.getClassLoader())));
    }

    public CompletedOpdBaseModel() {
    }

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }

    public CompletedOpdModel getCompletedOpdModel() {
        return completedOpdModel;
    }

    public void setCompletedOpdModel(CompletedOpdModel completedOpdModel) {
        this.completedOpdModel = completedOpdModel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(common);
        dest.writeValue(completedOpdModel);
    }

    public int describeContents() {
        return  0;
    }

}
