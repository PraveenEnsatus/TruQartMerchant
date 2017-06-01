package com.ensatus.truqartmerchant.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ensatus.truqartmerchant.module.User;

/**
 * Created by Praveen on 08-12-2016.
 */

public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Trucart_Merchant";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_PHONE = "mobileNo";
    private static final String KEY_USER_PASSWORD = "password";
    private static final String KEY_USER_SHOP = "shop";
    private static final String KEY_USER_ADDRESS = "address";
    private static final String KEY_USER_ADDRESS2 = "address2";
    private static final String KEY_USER_ICON = "icon";
    private static final String KEY_USER_CDATE = "createddate";
    private static final String KEY_DEVICE_ID = "deviceid";



    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }


    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getmId());
        editor.putString(KEY_USER_NAME, user.getmName());
        editor.putString(KEY_USER_EMAIL, user.getmEailid());
        editor.putString(KEY_USER_PHONE, user.getmMobileNo());
        editor.putString(KEY_USER_PASSWORD, user.getmPassword());
        editor.putString(KEY_USER_ICON, user.getmIcon());
        editor.putString(KEY_USER_SHOP, user.getmShop());
        editor.putString(KEY_USER_ADDRESS, user.getmAddress());
        editor.putString(KEY_USER_CDATE, user.getmCreDate());
        editor.putString(KEY_DEVICE_ID, user.getmDeviceID());
        editor.putString(KEY_USER_ADDRESS2,user.getmAdd2());
        editor.commit();

        Log.e(TAG, "User is stored in shared preferences. " + user.getmName() + ", " + user.getmPassword());
    }

    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String id, name, phone, email, password, mIcon, mAddress, mShop, mCDate, mDeviceID, mAdd2;

            id = pref.getString(KEY_USER_ID, null);
            name = pref.getString(KEY_USER_NAME, null);
            email = pref.getString(KEY_USER_EMAIL, null);
            phone = pref.getString(KEY_USER_PHONE, null);
            password = pref.getString(KEY_USER_PASSWORD, null);
            mIcon = pref.getString(KEY_USER_ICON, null);
            mAddress = pref.getString(KEY_USER_ADDRESS, null);
            mShop = pref.getString(KEY_USER_SHOP, null);
            mCDate = pref.getString(KEY_USER_CDATE, null);
            mDeviceID = pref.getString(KEY_DEVICE_ID, null);
            mAdd2 = pref.getString(KEY_USER_ADDRESS2, null);

            User user = new User(password, phone, id, email, name, mIcon,  mAddress,  mShop, mCDate, mDeviceID, mAdd2);
            return user;
        }
        return null;
    }
}
