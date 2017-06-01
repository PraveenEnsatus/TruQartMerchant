package com.ensatus.truqartmerchant.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.activities.LoginActivity;
import com.ensatus.truqartmerchant.activities.MainActivity;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Praveen on 18-01-2017.
 */

public class LoginFragment extends Fragment {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private Button mLoginBtn;
    private EditText mMobileNo, mPwd;


    private String m_sUsername, m_sPassword;
    String sAndroid_id = "";

    private TextView oRegisterLink;
    Intent oIntent;

    private SharedPreferences m_loginPreferences;
    private SharedPreferences.Editor mloginPrefsEditor;
    private CheckBox m_osaveLoginCheckBox;
    private Boolean m_bsaveLogin, bValidationResult;
    private static String mDeviceId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        mDeviceId = telephonyManager.getDeviceId();

       // Log.e(TAG, "Device ID :"+DEVICE_ID);

        View rootview = inflater.inflate(R.layout.activity_login, container, false);

        mLoginBtn = (Button) rootview.findViewById(R.id.login_btn);
        mMobileNo = (EditText) rootview.findViewById(R.id.mobile_no);
        mPwd = (EditText) rootview.findViewById(R.id.pwd);
       // m_osaveLoginCheckBox = (CheckBox) findViewById(R.id.Remember_Checkbox);
        oRegisterLink = (TextView) rootview.findViewById(R.id.register_btn);

        m_loginPreferences = getActivity().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        mloginPrefsEditor = m_loginPreferences.edit();

       /* m_bsaveLogin = m_loginPreferences.getBoolean("saveLogin", false);
        if (m_bsaveLogin) {
            mMobileNo.setText(m_loginPreferences.getString("username", ""));
            mPwd.setText(m_loginPreferences.getString("password", ""));
            m_osaveLoginCheckBox.setChecked(true);
        }*/
        addOnClickListener();



        return rootview;
    }
    public void addOnClickListener() {

        merchantLogin();
        newUsersignUp();
    }

    public void newUsersignUp() {
        oRegisterLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View l_view) {
                if(MyApplication.getInstance().isNetworkAvailable()){


                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_frame, new RegistrationFrag()).addToBackStack(null).commit();

                }else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void merchantLogin() {
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View l_view) {
                if (MyApplication.getInstance().isNetworkAvailable()){
                    bValidationResult = ValidateUserEntries();
                    if (!bValidationResult) {
                        onLoginFailed();
                        return;
                    }
                    try {
                        onLogin();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                   // saveUsercredentials();
                }else {
                    Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public boolean ValidateUserEntries() {
        boolean valid = true;
        m_sUsername = mMobileNo.getText().toString();
        m_sPassword = mPwd.getText().toString();
        if (m_sUsername.isEmpty()) {
            mMobileNo.setError("Enter a valid User name");
            valid = false;
        } else {
            mMobileNo.setError(null);
        }

        if (m_sPassword.isEmpty()) {
            mPwd.setError("Enter a Registered Password");
            valid = false;
        } else {
            mPwd.setError(null);
        }

        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();
    }

    public void saveUsercredentials() {
        InputMethodManager l_oInputmngrfrCheckbox = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        l_oInputmngrfrCheckbox.hideSoftInputFromWindow(
                mMobileNo.getWindowToken(), 0);
        m_sUsername = mMobileNo.getText().toString();
        m_sPassword = mPwd.getText().toString();
        if (m_osaveLoginCheckBox.isChecked()) {
            mloginPrefsEditor.putBoolean("saveLogin", true);

            mloginPrefsEditor.putString("username",
                    m_sUsername);

            mloginPrefsEditor.putString("password",
                    m_sPassword);
            mloginPrefsEditor.commit();

        } else {
            mloginPrefsEditor.clear();
            mloginPrefsEditor.commit();
        }
    }

    private void onLogin() throws JSONException {
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e(TAG, "response: " + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString(EndPoints.STATUS);
                                String message = obj.getString(EndPoints.MESSAGE);
                                if (message.equals("SUCCESS")) {

                                    JSONObject userObj = obj.getJSONObject(EndPoints.MERCHANT_LOGIN);

                                    if (status.equals("100")) {
                                        User user = new User(userObj.getString(EndPoints.KEY_USER_PASSWORD),
                                                userObj.getString(EndPoints.KEY_USER_PHONE),
                                                userObj.getString(EndPoints.KEY_USER_ID),
                                                userObj.getString(EndPoints.KEY_USER_EMAIL),
                                                userObj.getString(EndPoints.KEY_USER_NAME),
                                                userObj.getString(EndPoints.KEY_USER_ICON),
                                                userObj.getString(EndPoints.KEY_USER_ADDRESS1),
                                                userObj.getString(EndPoints.KEY_USER_SHOP),
                                                userObj.getString(EndPoints.KET_USER_C_DATE),
                                                userObj.getString(EndPoints.DEVICE_ID),
                                                userObj.getString(EndPoints.KEY_USER_ADDRESS2));

                                        // storing user in shared preferences
                                        MyApplication.getInstance().getPrefManager().storeUser(user);
                                        Toast.makeText(getContext(), "Logged In Successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getActivity(), MainActivity.class));
                                        getActivity().finish();
                                    }

                                } else if (status.equals("-1")) {
                                    Toast.makeText(getContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                                } else if (status.equals("-2")) {
                                    Toast.makeText(getContext(), "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e("TAG", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(EndPoints.MOBILE, m_sUsername);
                    params.put(EndPoints.PASSWORD, m_sPassword);
                    params.put(EndPoints.DEVICE_ID, mDeviceId);


                    Log.e("TAG", "params: " + params.toString());
                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }

            };
            Log.e("TAG", "StrReq:" + strReq);
            MyApplication.getInstance().getRequestQueue().getCache().clear();
            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
            Log.e(TAG, "after");
        }
    }

}
