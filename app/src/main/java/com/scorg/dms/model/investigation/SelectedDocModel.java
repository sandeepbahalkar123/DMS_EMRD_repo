package com.scorg.dms.model.investigation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ganeshshirole on 28/6/17.
 */

public class SelectedDocModel {
    @SerializedName("documents")
    @Expose
    private ArrayList<Image> selectedDocPaths;
    @SerializedName("investigations")
    @Expose
    private ArrayList<Integer> selectedInvestigationIds;

    public ArrayList<Image> getSelectedDocPaths() {
        return selectedDocPaths;
    }

    public void setSelectedDocPaths(ArrayList<Image> selectedDocPaths) {
        this.selectedDocPaths = selectedDocPaths;
    }

    public ArrayList<Integer> getSelectedInvestigation() {
        return selectedInvestigationIds;
    }

    public void setSelectedInvestigation(ArrayList<Integer> selectedInvestigation) {
        this.selectedInvestigationIds = selectedInvestigation;
    }
}
