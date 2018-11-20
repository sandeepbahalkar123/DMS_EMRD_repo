package com.scorg.dms.model.dms_models.responsemodel.episode_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EpisodeResponseModel implements CustomResponse, Serializable {

    @SerializedName("common")
    @Expose
    private Common common;
    @SerializedName("data")
    @Expose
    private EpisodeDataList episodeDataList;

    public Common getCommon() {
        return common;
    }

    public void setCommon(Common common) {
        this.common = common;
    }


    public class EpisodeDataList implements CustomResponse {


        @SerializedName("fileTypeList")
        @Expose
        private ArrayList<FileTypeList> fileTypeList = new ArrayList<FileTypeList>();

        @SerializedName("searchResultList")
        @Expose
        private List<PatientEpisodeFileData> patientEpisodeFileDataList = new ArrayList<PatientEpisodeFileData>();

        @SerializedName("ispaggination")
        @Expose
        private boolean ispaggination;

        @SerializedName("isMainPrimary")
        @Expose
        private boolean isMainPrimary;


        public List<PatientEpisodeFileData> getPatientEpisodeFileDataList() {
            return patientEpisodeFileDataList;
        }

        public void setPatientEpisodeFileDataList(List<PatientEpisodeFileData> patientEpisodeFileDataList) {
            this.patientEpisodeFileDataList = patientEpisodeFileDataList;
        }

        public boolean isPaggination() {
            return ispaggination;
        }

        public void setIspaggination(boolean ispaggination) {
            this.ispaggination = ispaggination;
        }


        public ArrayList<FileTypeList> getFileTypeList() {
            return fileTypeList;
        }

        public void setFileTypeList(ArrayList<FileTypeList> fileTypeList) {
            this.fileTypeList = fileTypeList;
        }

        public boolean isMainPrimary() {
            return isMainPrimary;
        }

        public void setMainPrimary(boolean mainPrimary) {
            isMainPrimary = mainPrimary;
        }
    }

    public EpisodeDataList getEpisodeDataList() {
        return episodeDataList;
    }

    public void setEpisodeDataList(EpisodeDataList episodeDataList) {
        this.episodeDataList = episodeDataList;
    }
}