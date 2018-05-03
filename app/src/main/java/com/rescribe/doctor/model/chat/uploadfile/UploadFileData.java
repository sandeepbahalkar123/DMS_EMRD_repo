package com.rescribe.doctor.model.chat.uploadfile;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadFileData implements Parcelable {

    @SerializedName("docUrl")
    @Expose
    private String docUrl;
    public final static Creator<UploadFileData> CREATOR = new Creator<UploadFileData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UploadFileData createFromParcel(Parcel in) {
            return new UploadFileData(in);
        }

        public UploadFileData[] newArray(int size) {
            return (new UploadFileData[size]);
        }

    };

    protected UploadFileData(Parcel in) {
        this.docUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UploadFileData() {
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(docUrl);
    }

    public int describeContents() {
        return 0;
    }

}