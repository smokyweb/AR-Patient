package com.ar.patient.responsemodel.submitexam;

import com.ar.patient.responsemodel.BaseBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SubmitExamResponse extends BaseBean implements Serializable {
    @SerializedName("test_id")
    @Expose
    private String testId;
    @SerializedName("body_parts")
    @Expose
    private ArrayList<BodyPart> bodyParts = null;
    @SerializedName("physical_exam_help")
    @Expose
    private String physicalExamHelp;
    @SerializedName("diagnosis")
    @Expose
    private String diagnosis;
    @SerializedName("subjective")
    @Expose
    private String subjective;
    @SerializedName("objective")
    @Expose
    private String objective;
    @SerializedName("assessment")
    @Expose
    private String assessment;
    @SerializedName("plan")
    @Expose
    private String plan;
    @SerializedName("notes")
    @Expose
    private String notes;

    public String getSubjective() {
        return subjective;
    }

    public void setSubjective(String subjective) {
        this.subjective = subjective;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public ArrayList<BodyPart> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(ArrayList<BodyPart> bodyParts) {
        this.bodyParts = bodyParts;
    }

    public String getPhysicalExamHelp() {
        return physicalExamHelp;
    }

    public void setPhysicalExamHelp(String physicalExamHelp) {
        this.physicalExamHelp = physicalExamHelp;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
