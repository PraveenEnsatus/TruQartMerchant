package com.ensatus.truqartmerchant.module;

/**
 * Created by Praveen on 18-01-2017.
 */

public class User {

    String mPassword, mShop, mAddress, mIcon, mName, mEailid, mId, mMobileNo, mCreDate, mDeviceID, mAdd2;

    public User(String mPassword, String mMobileNo, String mId, String mEailid, String mName,
                String mIcon, String mAddress, String mShop, String mCreDate, String mDeviceID, String mAdd2) {
        this.mPassword = mPassword;
        this.mMobileNo = mMobileNo;
        this.mId = mId;
        this.mEailid = mEailid;
        this.mName = mName;
        this.mIcon = mIcon;
        this.mAddress = mAddress;
        this.mShop = mShop;
        this.mCreDate = mCreDate;
        this.mDeviceID = mDeviceID;
        this.mAdd2 = mAdd2;
    }

    public String getmAdd2() {
        return mAdd2;
    }

    public void setmAdd2(String mAdd2) {
        this.mAdd2 = mAdd2;
    }

    public String getmDeviceID() {
        return mDeviceID;
    }

    public void setmDeviceID(String mDeviceID) {
        this.mDeviceID = mDeviceID;
    }

    public String getmCreDate() {
        return mCreDate;
    }

    public void setmCreDate(String mCreDate) {
        this.mCreDate = mCreDate;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmShop() {
        return mShop;
    }

    public void setmShop(String mShop) {
        this.mShop = mShop;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmIcon() {
        return mIcon;
    }

    public void setmIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmEailid() {
        return mEailid;
    }

    public void setmEailid(String mEailid) {
        this.mEailid = mEailid;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmMobileNo() {
        return mMobileNo;
    }

    public void setmMobileNo(String mMobileNo) {
        this.mMobileNo = mMobileNo;
    }
}
