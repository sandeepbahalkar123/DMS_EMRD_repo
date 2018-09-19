
package com.scorg.dms.model.pending_approval_list;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingApprovalDataModel {


    @SerializedName("ispaggination")
    @Expose
    private boolean ispaggination;

    @SerializedName("lstpendingreuest")
    @Expose
    private List<RequestedArchivedDetailList> requestedArchivedDetailList = null;

    public List<RequestedArchivedDetailList> getRequestedArchivedDetailList() {
        return requestedArchivedDetailList;
    }

    public void setRequestedArchivedDetailList(List<RequestedArchivedDetailList> requestedArchivedDetailList) {
        this.requestedArchivedDetailList = requestedArchivedDetailList;
    }

    public boolean isPaggination() {
        return ispaggination;
    }

    public void setIspaggination(boolean ispaggination) {
        this.ispaggination = ispaggination;
    }
}
