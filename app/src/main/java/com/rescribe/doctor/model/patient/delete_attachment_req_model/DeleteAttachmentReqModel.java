package com.rescribe.doctor.model.patient.delete_attachment_req_model;

import com.rescribe.doctor.interfaces.CustomResponse;

import java.util.ArrayList;

/**
 * Created by riteshpandhurkar on 5/4/18.
 */

public class DeleteAttachmentReqModel implements CustomResponse {

    private ArrayList<AttachmentData> attachmentDetails = new ArrayList<>();

    public ArrayList<AttachmentData> getAttachmentDetails() {
        return attachmentDetails;
    }

    public void setAttachmentDetails(ArrayList<AttachmentData> attachmentDetails) {
        this.attachmentDetails = attachmentDetails;
    }

    public static class AttachmentData {

        private String id;
        private int patientId;
        private String fileName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getPatientId() {
            return patientId;
        }

        public void setPatientId(int patientId) {
            this.patientId = patientId;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }
}
