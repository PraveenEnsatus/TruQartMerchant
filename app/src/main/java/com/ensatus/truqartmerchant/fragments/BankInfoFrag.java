package com.ensatus.truqartmerchant.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Praveen on 20-01-2017.
 */

public class BankInfoFrag extends Fragment {

    private static final String TAG = BankInfoFrag.class.getSimpleName();
    private TextView mContinueBtn;
    private String m_sMobilenumber;
    private String m_sPassword;
    private String m_sUserName;
    private String m_sShopname;
    private String m_sEmailid;
    private String m_sEncodedimagedata;
    private String m_sShopaddress;
    private TextView mSkipBtn;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.bank_info, container, false);
        mContinueBtn = (TextView) rootView.findViewById(R.id.continue_btn);
        mSkipBtn = (TextView) rootView.findViewById(R.id.textView4);
        Bundle bundle = this.getArguments();
        m_sUserName = bundle.getString("name");
        m_sMobilenumber = bundle.getString("mobile");
        if (bundle.getString("email") != null) {
            m_sEmailid = bundle.getString("email");
        }
        m_sPassword = bundle.getString("pwd");
        if ( bundle.getString("shopname") != null && !"".equals(bundle.getString("shopname"))) {
            m_sShopname = bundle.getString("shopname");
        }
        m_sShopaddress = bundle.getString("shopadd");
        addClickListener();
        return rootView;
    }

    public void addClickListener(){
        mContinue();
    }

    public void mContinue(){
        mContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            doMerchantRegistration();
            }
        });
    }

    //Registering Merchant

    public void doMerchantRegistration() {
        if (!MyApplication.getInstance().isNetworkAvailable()) {
            Toast.makeText(getContext(), "Check your intenet connection", Toast.LENGTH_SHORT).show();
        }else {
            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.REGISTRATION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e(TAG, "response: " + response);

                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString(EndPoints.STATUS);
                                String Message = obj.getString(EndPoints.MESSAGE);
                                if (status.equals("100")) {
                                    Toast.makeText(getContext(), "" + Message, Toast.LENGTH_SHORT).show();
                                    FragmentManager manager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction transaction =getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.container_frame, new LoginFragment()).addToBackStack(null).commit();
                                    manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                   // startActivity(new Intent(getActivity(), LoginActivity.class));
                                } else {
                                    Toast.makeText(getContext(), "" + Message, Toast.LENGTH_SHORT).show();
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
                    params.put(EndPoints.KEY_USER_PHONE, m_sMobilenumber);
                    params.put(EndPoints.KEY_USER_PASSWORD, m_sPassword);
                    params.put(EndPoints.KEY_USER_NAME, m_sUserName);
                    params.put(EndPoints.KEY_USER_SHOP, m_sShopname);
                    if (m_sEmailid != null && !"".equals(m_sEmailid)) {
                        params.put(EndPoints.KEY_USER_EMAIL, m_sEmailid);
                    }
                    if (m_sEncodedimagedata != null && !"".equals(m_sEncodedimagedata)) {
                        params.put(EndPoints.KEY_USER_SHOP_PHOTO1, m_sEncodedimagedata);
                    }
                    if (m_sShopaddress != null && !"".equals(m_sShopaddress)) {
                        params.put(EndPoints.KEY_USER_ADDRESS1, m_sShopaddress);
                    }

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
            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        }
    }


}
