package com.ar.patient.responsemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FaqsResponse extends BaseBean {
    @SerializedName("faqs")
    @Expose
    private ArrayList<Faq> faqs = null;

    public ArrayList<Faq> getFaqs() {
        return faqs;
    }

    public void setFaqs(ArrayList<Faq> faqs) {
        this.faqs = faqs;
    }
}
