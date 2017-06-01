package com.ensatus.truqartmerchant.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.adapter.ContactsAdapter;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.module.Contact;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Praveen on 17-11-2016.
 */

public class MyCustomerFrag extends Fragment {

    private static final String TAG = MyCustomerFrag.class.getSimpleName();
    private ListView listView;
    private ArrayList<Contact> MerchantContactsArray = new ArrayList<>();
    private ContactsAdapter adapter;
    //private  String MERCHANT_ID = "745d03d7-6e32-4db7-a991-5e5cf664e8e6";
     User user = MyApplication.getInstance().getPrefManager().getUser();
    private  String MERCHANT_ID = user.getmId();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View  mRootView =  inflater.inflate(R.layout.mycustomer_frag, container, false);

        listView = (ListView) mRootView.findViewById(R.id.mycust_list);

        adapter = new ContactsAdapter(MerchantContactsArray, getContext());
        listView.setAdapter(adapter);
        try {
            getContacts();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return mRootView;
    }


    public void getContacts() throws JSONException {
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.GET_MERCHANT_CONTACTS, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e(TAG, "Response:" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (String.valueOf(obj.getInt(EndPoints.STATUS)).equals("100")) {
                            //Toast.makeText(getContext(), "" + obj.getString(EndPoints.MESSAGE), Toast.LENGTH_SHORT).show();
                            JSONArray contactsArray = obj.getJSONArray(EndPoints.CONTACTS);
                            if (contactsArray.length() == 0) {

                                Toast.makeText(getContext(), "No Contacts Found", Toast.LENGTH_SHORT).show();
                            } else {
                                for (int i = 0; i < contactsArray.length(); i++) {
                                    JSONObject details = contactsArray.getJSONObject(i);
                                    if (details.length() != 0) {
                                        Contact conatacts = new Contact(details.getString(EndPoints.KEY_USER_NAME),
                                                details.getString(EndPoints.NUMBER));
                                        MerchantContactsArray.add(conatacts);
                                        adapter.notifyDataSetChanged();


                                    }
                                }
                            }


                        } else {
                            Toast.makeText(getContext(), "" + obj.getString(EndPoints.MESSAGE), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(EndPoints.MERCHANT_ID, MERCHANT_ID);
                    //  params.put(EndPoints.MOBILES,ContactObject.toString());

                    Log.e("TAG", "params: " + params.toString());
                    return params;
                }


                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/x-www-form-urlencoded");
                    return headers;
                }


            };
            Log.e(TAG, "StrReq:" + strReq);
            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        }
    }
}
