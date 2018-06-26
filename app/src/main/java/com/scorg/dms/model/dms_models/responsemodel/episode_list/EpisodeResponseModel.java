package com.scorg.dms.model.dms_models.responsemodel.episode_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.scorg.dms.interfaces.CustomResponse;
import com.scorg.dms.model.Common;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResult;
import com.scorg.dms.model.dms_models.responsemodel.showsearchresultresponsemodel.SearchResultData;

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

        @SerializedName("searchResultList")
        @Expose
        private List<PatientEpisodeFileData> patientEpisodeFileDataList = new ArrayList<PatientEpisodeFileData>();

        public List<PatientEpisodeFileData> getPatientEpisodeFileDataList() {
            return patientEpisodeFileDataList;
        }

        public void setPatientEpisodeFileDataList(List<PatientEpisodeFileData> patientEpisodeFileDataList) {
            this.patientEpisodeFileDataList = patientEpisodeFileDataList;
        }
    }

    public EpisodeDataList getEpisodeDataList() {
        return episodeDataList;
    }

    public void setEpisodeDataList(EpisodeDataList episodeDataList) {
        this.episodeDataList = episodeDataList;
    }
}