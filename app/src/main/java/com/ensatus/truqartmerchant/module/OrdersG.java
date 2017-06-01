package com.ensatus.truqartmerchant.module;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Praveen on 23-05-2017.
 */

public class OrdersG implements Serializable {
    private String oid;
    private String orderts;
    private String total;
    private String cname;
    private String mshopname;
    private JSONArray prodArray ;

    public OrdersG(String oid, String orderts, String total, String cname, String mshopname, JSONArray prodArray) {
        this.oid = oid;
        this.orderts = orderts;
        this.total = total;
        this.cname = cname;
        this.mshopname = mshopname;
        this.prodArray = prodArray;
    }

    public JSONArray getProdArray() {
        return prodArray;
    }

    public void setProdArray(JSONArray prodArray) {
        this.prodArray = prodArray;
    }

    public String getMshopname() {
        return mshopname;
    }

    public void setMshopname(String mshopname) {
        this.mshopname = mshopname;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOrderts() {
        return orderts;
    }

    public void setOrderts(String orderts) {
        this.orderts = orderts;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
