package com.ar.patient.responsemodel.testresultresponse;

import com.ar.patient.responsemodel.BaseBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TestResultResponse extends BaseBean implements Serializable {

        @SerializedName("patient")
        @Expose
        private Patient patient;
        @SerializedName("test")
        @Expose
        private Test test;
        @SerializedName("accessed_body_parts")
        @Expose
        private List<ResultBodyPart> accessedBodyParts = null;
        @SerializedName("not_accessed_body_parts")
        @Expose
        private List<ResultBodyPart> notAccessedBodyParts = null;
        @SerializedName("asked_que")
        @Expose
        private List<ResultBodyPart> askedQue = null;
        @SerializedName("not_asked_que")
        @Expose
        private List<ResultBodyPart> notAskedQue = null;
        @SerializedName("average_score_limit")
        @Expose
        private String averageScoreLimit;
        @SerializedName("result_body_parts")
        @Expose
        private List<ResultBodyPart> resultBodyParts = null;

        public Patient getPatient() {
            return patient;
        }

        public void setPatient(Patient patient) {
            this.patient = patient;
        }

        public Test getTest() {
            return test;
        }

        public void setTest(Test test) {
            this.test = test;
        }

        public List<ResultBodyPart> getAccessedBodyParts() {
            return accessedBodyParts;
        }

        public void setAccessedBodyParts(List<ResultBodyPart> accessedBodyParts) {
            this.accessedBodyParts = accessedBodyParts;
        }

        public List<ResultBodyPart> getNotAccessedBodyParts() {
            return notAccessedBodyParts;
        }

        public void setNotAccessedBodyParts(List<ResultBodyPart> notAccessedBodyParts) {
            this.notAccessedBodyParts = notAccessedBodyParts;
        }

        public List<ResultBodyPart> getAskedQue() {
            return askedQue;
        }

        public void setAskedQue(List<ResultBodyPart> askedQue) {
            this.askedQue = askedQue;
        }

        public List<ResultBodyPart> getNotAskedQue() {
            return notAskedQue;
        }

        public void setNotAskedQue(List<ResultBodyPart> notAskedQue) {
            this.notAskedQue = notAskedQue;
        }

        public String getAverageScoreLimit() {
            return averageScoreLimit;
        }

        public void setAverageScoreLimit(String averageScoreLimit) {
            this.averageScoreLimit = averageScoreLimit;
        }

        public List<ResultBodyPart> getResultBodyParts() {
            return resultBodyParts;
        }

        public void setResultBodyParts(List<ResultBodyPart> resultBodyParts) {
            this.resultBodyParts = resultBodyParts;
        }

    public class Patient {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("symptoms")
        @Expose
        private List<String> symptoms = null;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getSymptoms() {
            return symptoms;
        }

        public void setSymptoms(List<String> symptoms) {
            this.symptoms = symptoms;
        }

    }
    public class ResultBodyPart {

        @SerializedName("name")
        @Expose
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    public class SoapWrittenByLearner {

        @SerializedName("subjective_notes")
        @Expose
        private String subjectiveNotes;
        @SerializedName("objective_notes")
        @Expose
        private String objectiveNotes;
        @SerializedName("assessment_notes")
        @Expose
        private String assessmentNotes;
        @SerializedName("plan_notes")
        @Expose
        private String planNotes;

        public String getSubjectiveNotes() {
            return subjectiveNotes;
        }

        public void setSubjectiveNotes(String subjectiveNotes) {
            this.subjectiveNotes = subjectiveNotes;
        }

        public String getObjectiveNotes() {
            return objectiveNotes;
        }

        public void setObjectiveNotes(String objectiveNotes) {
            this.objectiveNotes = objectiveNotes;
        }

        public String getAssessmentNotes() {
            return assessmentNotes;
        }

        public void setAssessmentNotes(String assessmentNotes) {
            this.assessmentNotes = assessmentNotes;
        }

        public String getPlanNotes() {
            return planNotes;
        }

        public void setPlanNotes(String planNotes) {
            this.planNotes = planNotes;
        }

    }
    public class SoapWrittenByMe {

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

    }
    public class Test {

        @SerializedName("diagnosis")
        @Expose
        private String diagnosis;
        @SerializedName("soap_written_by_me")
        @Expose
        private SoapWrittenByMe soapWrittenByMe;
        @SerializedName("soap_written_by_learner")
        @Expose
        private SoapWrittenByLearner soapWrittenByLearner;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("result")
        @Expose
        private double result;

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public SoapWrittenByMe getSoapWrittenByMe() {
            return soapWrittenByMe;
        }

        public void setSoapWrittenByMe(SoapWrittenByMe soapWrittenByMe) {
            this.soapWrittenByMe = soapWrittenByMe;
        }

        public SoapWrittenByLearner getSoapWrittenByLearner() {
            return soapWrittenByLearner;
        }

        public void setSoapWrittenByLearner(SoapWrittenByLearner soapWrittenByLearner) {
            this.soapWrittenByLearner = soapWrittenByLearner;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public double getResult() {
            return result;
        }

        public void setResult(double result) {
            this.result = result;
        }

    }
}
