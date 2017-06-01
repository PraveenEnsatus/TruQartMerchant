package com.ensatus.truqartmerchant.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.activities.ProductsActivity;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.helper.ImageScaler;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Praveen on 20-03-2017.
 */

public class AddProductsFrag extends Fragment {

    private String TAG = AddProductsFrag.class.getSimpleName();
    private ImageView mTakePicture, mBrowseIcon, mProductImage;
    private TextView mUploadButton;
    private ProgressDialog mdialog;
    private EditText mProductName, mProductDesc, mProductPrce;
    private EditText mProductDiscount;
    private TextView mCategory;
    int mDisplayproduct = 0;
    static String mImageString;
    Switch mPricedisplaySwitch;
    private  final int PICK_FROM_GALLERY = 1;
    private final int CAMERA_REQUEST = 2;
    private File Imagefile;
    Bitmap m_oCurrentBitmapimage;
    ArrayAdapter mCatAdapter;
    private String MERCHANT_ID;
    private String CAT_ID = "1";




    public ArrayList<LinkedHashMap<String,String>> categorylist = new ArrayList<LinkedHashMap<String, String>>();

    public LinkedHashMap<String, String> cats = new LinkedHashMap<>();
    public LinkedHashMap<String, String> catsid = new LinkedHashMap<>();


    FileOutputStream mOutputstream = null;
    String m_sPrdname, m_sPrddesc, m_sPrdprice, m_sProdDis = null;

    String mEncodedImageData = "";
    ProgressDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View mRootView = inflater.inflate(R.layout.products_add, container, false);
        User user = MyApplication.getInstance().getPrefManager().getUser();
        MERCHANT_ID = user.getmId();
        mBrowseIcon = (ImageView) mRootView.findViewById(R.id.recyclerView3);
        /*mTwkePicture = (ImageView) findViewById(R.id.browse_btn);*/
       /* mUploadButton = (TextView) mRootView.findViewById(R.id.upload);
        mProductImage = (ImageView) mRootView.findViewById(R.id.imagespace);
        mPricedisplaySwitch = (Switch) mRootView.findViewById(R.id.pricedisplay_switch);*/
        mProductName = (EditText) mRootView.findViewById(R.id.edt_item_name);
        mProductDesc = (EditText) mRootView.findViewById(R.id.item_desc);
        mProductPrce = (EditText) mRootView.findViewById(R.id.item_price);
        mProductDiscount = (EditText) mRootView.findViewById(R.id.item_discount);
        mUploadButton = (TextView) mRootView.findViewById(R.id.add_btn);
        mCategory = (TextView) mRootView.findViewById(R.id.category);
        mCatAdapter = new ArrayAdapter<String>(getActivity(), R.layout.single_choice_dialog);
        try {
            getCatgories();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addOnclicklistner();

        return mRootView;
    }



    public void addOnclicklistner() {

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
       /* mTakePicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Toast.makeText(getContext(), "Get the Pictures in Portrait mode for a best view", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    Imagefile = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Imagefile));
                    startActivityForResult(intent, CAMERA_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/
        mCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    //Initialize the Alert Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    //Source of the data in the DIalog
                    // Set the dialog title


                    builder.setTitle("Select Category");

                    builder.setAdapter(mCatAdapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection
                            // mDoneButton.setText(items[item]);
                            Toast.makeText(getContext(), "id" + mCatAdapter.getItem(item), Toast.LENGTH_LONG).show();
                            //Customer cus = adapter.getItem(item);

                            Log.e(TAG, "Cat name :" + categorylist.get(item).keySet() + categorylist.get(item) + categorylist.get(item).values());
                            String route = String.valueOf(categorylist.get(item).values());
                            Set<String> key = categorylist.get(item).keySet();
                            for (String myVal : key) {
                                System.out.println(myVal);

                                CAT_ID = myVal;
                            }
                            Collection<String> value = categorylist.get(item).values();

                            for (String myVal: value){
                                Log.e(TAG, "KEy Value :"+myVal);
                                mCategory.setText("Category : "+myVal);
                            }


                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    ValidateUserEntries();
                    /*setproductdetails();

                    Uploadimage();*/
                    m_sPrddesc = mProductDesc.getText().toString();
                    m_sPrdname = mProductName.getText().toString();
                    m_sPrdprice = mProductPrce.getText().toString();
                    m_sProdDis = mProductDiscount.getText().toString();
                    Log.e(TAG, "PRO_detail" + m_sPrdprice+m_sPrdname+m_sPrddesc);
                    UploadProduct();
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
    }

    private void UploadProduct() throws JSONException {
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            final int PRO_price = Integer.valueOf(m_sPrdprice);

            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.UPLOAD_PRODUCT_URL,
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
                                    Toast.makeText(getContext(), "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), ProductsActivity.class));
                                    getActivity().finish();
                                }else if(status.equals("-3"))
                                {
                                    Toast.makeText(getContext(), ""+message, Toast.LENGTH_LONG).show();
                                }else {
                                    Toast.makeText(getContext(), "Product Upload Failed", Toast.LENGTH_LONG).show();
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
                    params.put(EndPoints.PROD_NAME, m_sPrdname);
                    params.put(EndPoints.UNIT, m_sPrddesc);
                    params.put(EndPoints.PROD_PRICE, String.valueOf(PRO_price));
                    params.put(EndPoints.CAT_ID, CAT_ID);
                    params.put(EndPoints.MERCHANT_ID, MERCHANT_ID);
                    if (mEncodedImageData != null) {
                        params.put(EndPoints.PROD_IMAGE1, mEncodedImageData);
                    }
                    params.put(EndPoints.DISCOUNT, m_sProdDis);

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


    private void getCatgories() throws JSONException{
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.CATEGORY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.e(TAG, "Response:" + response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (String.valueOf(obj.getInt(EndPoints.STATUS)).equals("100")) {
                            JSONArray catArraay = obj.getJSONArray(EndPoints.CATEGORY);
                            for (int i = 0; i < catArraay.length(); i++) {
                                JSONObject dataObj = catArraay.getJSONObject(i);
                                String name = dataObj.getString(EndPoints.CAT_NAME);
                                String id = dataObj.getString(EndPoints.CAT_ID);
                                LinkedHashMap<String, String> cat = new LinkedHashMap<>();
                                mCatAdapter.add(name);
                                cat.put(id, name);
                                categorylist.add(cat);
                                Log.e(TAG, "CAT_ARRAY" + categorylist);
                            }
                            Log.e(TAG, "CAT_ARRAY1" + categorylist);
                        }


                   /* Category cat = new Category();
                  *//*  getCategory(cat, dataObj);
                  //  cat.setmCid("1fty");
                    Log.e(TAG, "Subcat" + getCategory(cat, dataObj));*//*
                    Log.e(TAG,""+catsid.toString());
                    for (String s : catsid.toString().replace( "{", "").replace("}", "").split(",")) {
                        if(s.trim().substring(0,4).compareTo( "1fty") == 0)
                        System.out.println("key : ......." + s.trim().split( "=")[1]);
                    }
                    if (catsid.containsValue("1fty")){
                        Log.e(TAG, "Key:"+catsid.keySet());
                    }
*/


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
                    params.put("mid", MERCHANT_ID);

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


    public boolean ValidateUserEntries() {
        boolean valid = true;
        if (mProductName.getText().toString().isEmpty()) {
            mProductName.setError("Provide ProductName");
            valid = false;
        } else {
            mProductName.setError(null);
        }
        if (mProductDesc.getText().toString().isEmpty()) {
            mProductDesc.setError("Provide ProductDescription");
            valid = false;
        } else {
            mProductDesc.setError(null);
        }
        if (mProductPrce.getText().toString().isEmpty()) {
            mProductPrce.setError("Provide ProductPrice");
            valid = false;
        } else {
            mProductPrce.setError(null);
        }
       /* if (mImageString == null) {
            Toast.makeText(getApplicationContext(), "Provide the Product image", Toast.LENGTH_LONG).show();
            valid = false;
        }*/

        return valid;
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
               /* mBrowseIcon.setVisibility(View.GONE);*/
                System.out.println("image path" + mImageString);
                Log.e(TAG, "ImageStringData Gallery : " + mEncodedImageData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
