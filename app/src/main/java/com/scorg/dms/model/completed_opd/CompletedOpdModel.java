
package com.scorg.dms.model.completed_opd;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompletedOpdModel implements Parcelable
{

    @SerializedName("completedOpd")
    @Expose
    private ArrayList<CompletedOpd> completedOpd = new ArrayList<CompletedOpd>();
    public final static Creator<CompletedOpdModel> CREATOR = new Creator<CompletedOpdModel>() {


        @SuppressWarnings({
            "unchecked"
        })
        public CompletedOpdModel createFromParcel(Parcel in) {
            return new CompletedOpdModel(in);
        }

        public CompletedOpdModel[] newArray(int size) {
            return (new CompletedOpdModel[size]);
        }

    }
    ;

    protected CompletedOpdModel(Parcel in) {
        in.readList(this.completedOpd, (CompletedOpd.class.getClassLoader()));
    }

    public CompletedOpdModel() {
    }

    public ArrayList<CompletedOpd> getCompletedOpd() {
        return completedOpd;
    }

    public void setCompletedOpd(ArrayList<CompletedOpd> completedOpd) {
        this.completedOpd = completedOpd;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(completedOpd);
    }

    public int describeContents() {
        return  0;
    }

}
