package com.ensatus.truqartmerchant.module;

import java.io.Serializable;

/**
 * Created by Praveen on 08-12-2016.
 */

public class Product implements Serializable{
    private String mProName, mPrice, mImage, mPId,mDescription, mLikes, mCatId, mUnit, mDisc;
    private String mQty = "0";
    public Product(String mProName, String mPrice, String mImage, String mPId, String mDescription, String mLikes, String mCatId, String mUnit,
     String mDisc) {
        this.mProName = mProName;
        this.mPrice = mPrice;
        this.mImage = mImage;
        this.mPId = mPId;
        this.mDescription = mDescription;
        this.mLikes = mLikes;
        this.mCatId = mCatId;
        this.mUnit = mUnit;
        this.mDisc = mDisc;
    }

    public Product(String mProName, String mPrice, String mQty) {
        this.mProName = mProName;
        this.mPrice = mPrice;
        this.mQty = mQty;
    }

    public String getmQty() {
        return mQty;
    }

    public void setmQty(String mQty) {
        this.mQty = mQty;
    }

    public String getmDisc() {
        return mDisc;
    }

    public void setmDisc(String mDisc) {
        this.mDisc = mDisc;
    }

    public String getmUnit() {
        return mUnit;
    }

    public void setmUnit(String mUnit) {
        this.mUnit = mUnit;
    }

    public String getmProName() {
        return mProName;
    }

    public void setmProName(String mProName) {
        this.mProName = mProName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmPId() {
        return mPId;
    }

    public void setmPId(String mPId) {
        this.mPId = mPId;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getmLikes() {
        return mLikes;
    }

    public void setmLikes(String mLikes) {
        this.mLikes = mLikes;
    }

    public String getmCatId() {
        return mCatId;
    }

    public void setmCatId(String mCatId) {
        this.mCatId = mCatId;
    }
}
