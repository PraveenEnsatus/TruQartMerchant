package com.ensatus.truqartmerchant.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.EditText;

import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.fragments.LoginFragment;
import com.ensatus.truqartmerchant.module.User;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Check for login session. If user is already logged in
         * redirect him to main activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            Log.e(TAG, "Login :" + MyApplication.getInstance().getPrefManager().getUser());
            User user = MyApplication.getInstance().getPrefManager().getUser();
            Log.e(TAG,"user details :"+user.getmId()+", "+user.getmName());
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.login);

        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container_frame, new LoginFragment()).commit();
    }
}
