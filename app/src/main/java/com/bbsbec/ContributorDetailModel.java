package com.bbsbec;

import java.io.Serializable;

public class ContributorDetailModel implements Serializable {

    private String name;
    private boolean bluetick;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getBluetick() {
        return bluetick;
    }

    public void setBluetick(boolean bluetick) {
        this.bluetick = bluetick;
    }

    public ContributorDetailModel(String n, boolean b){
        this.name = n;
        this.bluetick = b;
    }

}
