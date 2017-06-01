package com.ensatus.truqartmerchant.fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.activities.EventsActivity;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.helper.ImageScaler;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Praveen on 09-12-2016.
 */

public class AddEventsFrag extends Fragment {
    User user = MyApplication.getInstance().getPrefManager().getUser();
    private String MERCHANT_ID = user.getmId();
    private  static final String TAG = AddEventsFrag.class.getSimpleName();
    private ImageView mBrowseIcon;
    private EditText mEndDate, mStartDate;
    private Button mSeldate, mSelTime, mUploadButton;
    private final int PICK_FROM_GALLERY = 1;
    private LinearLayout mCalenderLay, mWatchLay;
    private TextView mAddEventTxt;
    DatePicker dt;
    private String dateTimeStr, mSD,mED;
    private int hh, mi;
    private Boolean mDateState;
    String mImageString;
    String mEncodedImageData = "";
    private EditText mEventName, mEventDesc, mEventAdd;
    private String mEdEventName, mEdEventdesc, mEdEventAdd = null;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View  mRootView =  inflater.inflate(R.layout.events_add, container, false);
        mBrowseIcon = (ImageView) mRootView.findViewById(R.id.event_image_add);
        mStartDate = (EditText) mRootView.findViewById(R.id.frm_dt_tm);
        mEndDate = (EditText) mRootView.findViewById(R.id.to_dt_tm);
        mUploadButton = (Button) mRootView.findViewById(R.id.button);
        mEventName = (EditText) mRootView.findViewById(R.id.event_name);
        mEventAdd = (EditText) mRootView.findViewById(R.id.add_loc);
        mEventDesc = (EditText) mRootView.findViewById(R.id.add_desc);
        mAddEventTxt = (TextView) mRootView.findViewById(R.id.add_event_text);
        addOnclickListener();
        return mRootView;
    }

    private void addOnclickListener() {
        mBrowseIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Toast.makeText(getContext(), "Select the Pictures in Landscape mode for a best view", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       /* intent.setType("image");
        intent.setAction(Intent.ACTION_GET_CONTENT);*/
                    startActivityForResult(intent, PICK_FROM_GALLERY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DialogFragment timeDatePicker = new TimeDatePicker.TimePickerFragment();
                timeDatePicker.show(getFragmentManager(), "Time");*/
                Log.d(TAG, "startDate" );
                mDateState = true;
                mTimeDateDialog();

            }
        });
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*DialogFragment timeDatePicker = new TimeDatePicker.TimePickerFragment();
                timeDatePicker.show(getFragmentManager(), "Time");*/
                Log.d(TAG, "endDate" );
                mDateState = false;
                mTimeDateDialog();
                // Log.d(TAG, dateTimeStr);

            }
        });
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {



                    ValidateUserEntries();

                    mEdEventdesc = mEventDesc.getText().toString();
                    mEdEventName = mEventName.getText().toString();
                    mEdEventAdd = mEventAdd.getText().toString();
                    Log.e(TAG, "Event_detail" + mEdEventdesc+mEdEventName+mEdEventAdd);


                    addExhibitions();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void addExhibitions() throws JSONException {
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.ADD_EXHIBITION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println(response);
                            Log.e(TAG, "response: " + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString(EndPoints.STATUS);
                                String message = obj.getString(EndPoints.MESSAGE);
                                if (status.equals("100")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Added Exhibition Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getContext(), EventsActivity.class));
                                    getActivity().finish();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Adding Exhibition Failed", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }


                        }

                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e("TAG", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(EndPoints.EVENT_NAME, mEdEventName);
                    params.put(EndPoints.EVENT_DESC, mEdEventdesc);
                    params.put(EndPoints.EVENT_ADDRESS, mEdEventAdd);
                    params.put(EndPoints.START_DATE, mSD);
                    params.put(EndPoints.END_DATE, mED);
                    params.put(EndPoints.MERCHANT_ID, MERCHANT_ID);
                    params.put(EndPoints.EVENT_INVI_IMAGE, mEncodedImageData);

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            /*if (requestCode == CAMERA_REQUEST) {

                Imagefile = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                String imageuri = Environment.getExternalStorageDirectory() + File.separator + "img.jpg";
                mImageString = compressImage(imageuri);
                mEncodedImageData = Bitmapconversion(mImageString);
                Log.e(TAG,"ImageStringData Camera: "+mEncodedImageData);
            } else*/ if (requestCode == PICK_FROM_GALLERY) {
                System.out.println("in browse request");
                final Uri l_oImageUri = data.getData();
                String imageurii = l_oImageUri.toString();
                mImageString = ImageScaler.compressImage(imageurii,mBrowseIcon, getContext());
                mEncodedImageData = ImageScaler.Bitmapconversion(mImageString);
                mBrowseIcon.setPadding(10,10,10,10);
                mBrowseIcon.setBackgroundResource(R.color.white_smoke);
                //mBrowseIcon.setScaleType(ImageView.ScaleType.FIT_XY);
                mAddEventTxt.setVisibility(View.GONE);
                System.out.println("image path" + mImageString);
                Log.e(TAG, "ImageStringData Gallery : " + mEncodedImageData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void mTimeDateDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();


        final View content = inflater.inflate(R.layout.time_date_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(content)

                // Add action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //  ...
                       /* Toast.makeText(getContext(), ""+dt.getDayOfMonth()+"/"+(dt.getMonth()+1)
                                +"/"+dt.getYear(), Toast.LENGTH_SHORT).show();*/
                        dateTimeStr = pad(dt.getDayOfMonth())+"/"+pad(dt.getMonth()+1)
                                +"/"+dt.getYear();

                        if (mDateState) {
                            mStartDate.setText(new StringBuilder().append(dateTimeStr).append("  ")
                                    .append(pad(hh)).append(":")
                                    .append(pad(mi)));
                            mSD = dt.getYear()+pad(dt.getMonth()+1)+pad(dt.getDayOfMonth())+pad(hh)+pad(mi);
                        } else {
                            if (mSD != null){
                                mED = dt.getYear()+pad(dt.getMonth()+1)+pad(dt.getDayOfMonth())+pad(hh)+pad(mi);
                                if(Long.valueOf(mED)> Long.valueOf(mSD)) {
                                    mEndDate.setText(new StringBuilder().append(dateTimeStr).append("  ")
                                            .append(pad(hh)).append(":")
                                            .append(pad(mi)));
                                    Log.e(TAG, "end"+mED+"- start"+mSD);
                                }else {
                                    Toast.makeText(getContext(), "End Time should be Greater than Start Time", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(getContext(), "Select Start Date and Time", Toast.LENGTH_SHORT).show();
                            }

                        }


                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do nothing

                    }
                });
        mSeldate = (Button) content.findViewById(R.id.date_btn);
        mSelTime = (Button) content.findViewById(R.id.time_btn);
        mCalenderLay = (LinearLayout) content.findViewById(R.id.datePicker);
        mWatchLay = (LinearLayout) content.findViewById(R.id.timepicker);
        mSelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalenderLay.setVisibility(View.GONE);
                mWatchLay.setVisibility(View.VISIBLE);
            }
        });
        mSeldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWatchLay.setVisibility(View.GONE);
                mCalenderLay.setVisibility(View.VISIBLE);
            }
        });
        TimePicker tp = (TimePicker) content.findViewById(R.id.timePicker1);
        tp.setIs24HourView(true);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
               // Toast.makeText(getContext(), ""+hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
                hh = hourOfDay;
                mi = minute;
            }
        });
        dt = (DatePicker) content.findViewById(R.id.datePicker1);





        AlertDialog alert  = builder.create();
        alert.show();
        // return dateTimeStr;
    }
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    public boolean ValidateUserEntries() {
        boolean valid = true;
        if (mEventDesc.getText().toString().isEmpty()) {
            mEventDesc.setError("Provide Event Name");
            valid = false;

        } else {
            mEventDesc.setError(null);
        }
        if (mEventName.getText().toString().isEmpty()) {
            mEventName.setError("Provide Event Description");
            valid = false;
        } else {
            mEventName.setError(null);
        }
        if (mEventAdd.getText().toString().isEmpty()) {
            mEventAdd.setError("Provide Event Address");
            valid = false;
        } else {
            mEventAdd.setError(null);
        }
        if (mImageString == null) {
            Toast.makeText(getContext(), "Provide the Event Invitation image", Toast.LENGTH_SHORT).show();
            valid = false;
        }if (mSD == null) {
            Toast.makeText(getContext(), "Select Start Date", Toast.LENGTH_SHORT).show();
            valid = false;
        }if (mED == null) {
            Toast.makeText(getContext(), "Select End Date", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

}
