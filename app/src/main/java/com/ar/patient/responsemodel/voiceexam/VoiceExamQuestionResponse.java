package com.ar.patient.responsemodel.voiceexam;

import com.ar.patient.responsemodel.BaseBean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VoiceExamQuestionResponse extends BaseBean implements Serializable {
    @SerializedName("definition")
    @Expose
    private ArrayList<Definition> definition = new ArrayList<>();
    @SerializedName("exceptional_words")
    @Expose
    private ArrayList<String> exceptionalWords = new ArrayList<>();

    public ArrayList<Definition> getDefinition() {
        return definition;
    }

    public void setDefinition(ArrayList<Definition> definition) {
        this.definition = definition;
    }

    public ArrayList<String> getExceptionalWords() {
        return exceptionalWords;
    }

    public void setExceptionalWords(ArrayList<String> exceptionalWords) {
        this.exceptionalWords = exceptionalWords;
    }
}
