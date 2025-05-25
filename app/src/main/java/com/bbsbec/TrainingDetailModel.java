package com.bbsbec;

import java.io.Serializable;

public class TrainingDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String section;
    private String detail;
    private boolean expand;

    public TrainingDetailModel(String sec, String det) {
        this.section = sec;
        this.detail = det;
        this.expand = false;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
}
