package com.scorg.dms.model.dms_models.requestmodel.archive;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestResponseResultUnlock {

        @SerializedName("Result")
        @Expose
        private String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

    }
