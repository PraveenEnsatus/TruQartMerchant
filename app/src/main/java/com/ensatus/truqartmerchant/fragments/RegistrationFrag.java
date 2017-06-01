package com.ensatus.truqartmerchant.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Praveen on 19-01-2017.
 */

public class RegistrationFrag extends Fragment {
    private static final String TAG = RegistrationFrag.class.getSimpleName();
    private TextView mContinueBtn;
    private EditText mMrcntPhNo;
    private EditText mMrcntName;
    private EditText mMrchntPwd;
    private EditText mMrchntCnfrmfPwd;
    private EditText mMrchntEmailId;
    private static final Pattern MOBILE_PATTERN = Pattern
            .compile("^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[789]\\d{9}$");
    private String m_sMobilenumber;
    private String m_sPassword;
    private String m_sUserName;
    private String m_sShopname;
    private String m_sEmailid;
    private String m_sEncodedimagedata;
    private String m_sShopaddress;
    private String sCnPwd;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInsatceState){
        View mRootView = inflater.inflate(R.layout.register, container, false);

        mContinueBtn = (TextView)  mRootView.findViewById(R.id.continue_btn);
        mMrcntPhNo = (EditText) mRootView.findViewById(R.id.merchant_ph_no);
        mMrcntName = (EditText) mRootView.findViewById(R.id.mrcht_name);
        mMrchntPwd = (EditText) mRootView.findViewById(R.id.mrcht_pwd);
        mMrchntCnfrmfPwd = (EditText) mRootView.findViewById(R.id.confirm_pwd);
        mMrchntEmailId = (EditText) mRootView.findViewById(R.id.mrcht_email);


        addOnClickListener();

        return mRootView;
    }

    public void addOnClickListener() {

        mContinue();
    }
    public void mContinue(){
        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mValidateFields()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", m_sUserName);
                    bundle.putString("mobile", m_sMobilenumber);
                    bundle.putString("pwd", m_sPassword);
                    if (!"".equals(m_sEmailid) && m_sEmailid != null) {
                        bundle.putString("email", m_sEmailid);
                    }
                    WorkInfoFrag workInfoFrag = new WorkInfoFrag();
                    workInfoFrag.setArguments(bundle);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.container_frame, workInfoFrag).addToBackStack(null).commit();
                }
            }
        });
    }



    private boolean mValidateFields(){
        boolean valid = true;
        m_sMobilenumber = mMrcntPhNo.getText().toString();
        m_sUserName = mMrcntName.getText().toString();
        m_sPassword = mMrchntPwd.getText().toString();
        sCnPwd = mMrchntCnfrmfPwd.getText().toString();
        if (m_sMobilenumber.isEmpty()) {
            mMrcntPhNo.setError("Enter a valid Mobile Number");
            valid = false;
        }
        if (m_sUserName.isEmpty()) {
            mMrcntName.setError("Enter a valid User name");
            valid = false;
        }
        if (m_sPassword.isEmpty()) {
            mMrchntPwd.setError("Enter a valid");
            valid = false;
        }
        if (sCnPwd.isEmpty()) {
            mMrchntCnfrmfPwd.setError("Enter a valid User name");
            valid = false;
        }
        if (!CheckMobilnumber(m_sMobilenumber)) {
            Toast.makeText(getContext(), "ENTER VALID MOBILE NUMBER",
                    Toast.LENGTH_LONG).show();
            valid = false;

        }
        if (!ComparePassword(m_sPassword, sCnPwd)) {
            Toast.makeText(getContext(), "Password doesn't match",
                    Toast.LENGTH_SHORT).show();
            valid = false;

        }

        return valid;
    }

    private boolean CheckMobilnumber(String mobilenumber) {
        return MOBILE_PATTERN.matcher(mobilenumber).matches();
    }

    private boolean ComparePassword(String pwd, String cpwd){
        return (pwd.equals(cpwd));
    }
}
