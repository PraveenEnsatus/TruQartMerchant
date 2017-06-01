package com.ensatus.truqartmerchant.module;

import java.io.Serializable;

/**
 * Created by Praveen on 09-12-2016.
 */

public class Exhibitions implements Serializable {
    String eImage, eEventName, eStartDate, eEndDate, eLoc, eDesc, eId;

    public Exhibitions(String eImage, String eEventName, String eStartDate, String eEndDate, String eLoc, String eDesc, String eId) {
        this.eImage = eImage;
        this.eEventName = eEventName;
        this.eStartDate = eStartDate;
        this.eEndDate = eEndDate;
        this.eLoc = eLoc;
        this.eDesc = eDesc;
        this.eId = eId;
    }

    public String geteImage() {
        return eImage;
    }

    public void seteImage(String eImage) {
        this.eImage = eImage;
    }

    public String geteEventName() {
        return eEventName;
    }

    public void seteEventName(String eEventName) {
        this.eEventName = eEventName;
    }

    public String geteStartDate() {
        return eStartDate;
    }

    public void seteStartDate(String eStartDate) {
        this.eStartDate = eStartDate;
    }

    public String geteEndDate() {
        return eEndDate;
    }

    public void seteEndDate(String eEndDate) {
        this.eEndDate = eEndDate;
    }

    public String geteLoc() {
        return eLoc;
    }

    public void seteLoc(String eLoc) {
        this.eLoc = eLoc;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }
}
