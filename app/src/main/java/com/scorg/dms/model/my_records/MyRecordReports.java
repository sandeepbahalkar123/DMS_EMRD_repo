package com.scorg.dms.model.my_records;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by riteshpandhurkar on 15/6/17.
 */

public class MyRecordReports implements CustomResponse, Serializable {

    @SerializedName("parentCaptionName")
    private String parentCaptionName;
    @SerializedName("reportDetails")
    private ArrayList<MyRecordReportList> reportList;

    public String getParentCaptionName() {
        return parentCaptionName;
    }

    public void setParentCaptionName(String parentCaptionName) {
        this.parentCaptionName = parentCaptionName;
    }

    public ArrayList<MyRecordReportList> getReportList() {
        return reportList;
    }

    public void setReportList(ArrayList<MyRecordReportList> reportList) {
        this.reportList = reportList;
    }

    public class MyRecordReportList implements CustomResponse, Serializable {
        @SerializedName("childCaptionName")
        private String childCaptionName;
        @SerializedName("imageList")
        @Expose
        private String[] imageList;

        public String getChildCaptionName() {
            return childCaptionName;
        }

        public void setChildCaptionName(String childCaptionName) {
            this.childCaptionName = childCaptionName;
        }

        public String[] getImageList() {
            return imageList;
        }

        public void setImageList(String[] imageList) {
            this.imageList = imageList;
        }
    }
}
