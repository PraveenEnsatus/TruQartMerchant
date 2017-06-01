package com.ensatus.truqartmerchant.module;

import java.io.Serializable;

/**
 * Created by Praveen on 16-12-2016.
 */

public class Contact implements Serializable {
  String mName, mPhone;

    public Contact(String mName, String mPhone) {
        this.mName = mName;
        this.mPhone = mPhone;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPhone() {
        return mPhone;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }
}
