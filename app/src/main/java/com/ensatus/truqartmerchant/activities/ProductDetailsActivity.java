package com.ensatus.truqartmerchant.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.helper.ImageScaler;
import com.ensatus.truqartmerchant.module.User;

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

public class ProductDetailsActivity extends AppCompatActivity {


    private static final String mTAG = ProductDetailsActivity.class.getSimpleName();
    private ImageView mTakePicture, mBrowseIcon, mProductImage;
    private TextView mUploadButton;
    private ProgressDialog mdialog;
    private EditText mProductName, mProductDesc, mProductPrce;
    private EditText mProductDiscount;
    private Button mCategory;
    int mDisplayproduct = 0;
    static String mImageString;
    Switch mPricedisplaySwitch;
    private  static final int PICK_FROM_GALLERY = 1;
    private final int CAMERA_REQUEST = 2;
    private File Imagefile;
    Bitmap m_oCurrentBitmapimage;
    ArrayAdapter mCatAdapter;
    private String MERCHANT_ID;
    private String CAT_ID, PID, ProCat;




    public ArrayList<LinkedHashMap<String,String>> categorylist = new ArrayList<LinkedHashMap<String, String>>();
    public LinkedHashMap<String, String> catHash = new LinkedHashMap<>();



    FileOutputStream mOutputstream = null;
    String m_sPrdname, m_sPrddesc, m_sPrdprice, m_sProdDisc = null;

    String mEncodedImageData = "";
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);


        User user = MyApplication.getInstance().getPrefManager().getUser();
        MERCHANT_ID = user.getmId();




        mCatAdapter = new ArrayAdapter<String>(ProductDetailsActivity.this, android.R.layout.select_dialog_singlechoice);
       /* try {
            getCatgories();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        getLayoutReferences();
        addOnclicklistner();
    }

    private void getLayoutReferences(){
        //mTakePicture = (ImageView) findViewById(R.id.takepicture_btn);
        mBrowseIcon = (ImageView) findViewById(R.id.recyclerView3);
        mUploadButton = (TextView) findViewById(R.id.update_prod);
       // mProductImage = (ImageView) findViewById(R.id.imagespace);
       // mPricedisplaySwitch = (Switch) findViewById(R.id.pricedisplay_switch);
        mProductName = (EditText) findViewById(R.id.edt_item_name);
        mProductDesc = (EditText) findViewById(R.id.item_desc);
        mProductPrce = (EditText) findViewById(R.id.item_price);
        mProductDiscount = (EditText) findViewById(R.id.item_discount);
       // mCategory = (Button) findViewById(R.id.category);
        String Title = getIntent().getStringExtra("Product_name");
        ProCat = getIntent().getStringExtra("Product_cat");
        CAT_ID = ProCat;
        String ProDisc = getIntent().getStringExtra("Pro_Disc");
        String ProPrice = getIntent().getStringExtra("Product_price");
        String ProImg = getIntent().getStringExtra("Pro_Img");
        String ProDesc = getIntent().getStringExtra("Pro_unit");
        PID = getIntent().getStringExtra("Pro_Id");
        String catName = catHash.get(ProCat);
        Log.e(mTAG, "Cats"+ProCat+" "+catHash+"name :"+catName);
        mProductName.setText(Title);
        mProductDesc.setText(ProDesc);
        mProductPrce.setText(ProPrice);
        mProductDiscount.setText(ProDisc);
       // mCategory.setText(catName);
        ImageRequest request = new ImageRequest(ProImg,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        mBrowseIcon.setImageBitmap(bitmap);

                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        mBrowseIcon.setImageResource(R.drawable.a_s_tproductsicon);
                        // profileLargeIv.setImageResource(R.mipmap.truqart_cust_profile_icon);
                    }
                });
        // Access the RequestQueue through your singleton class.
        MyApplication.getInstance().addToRequestQueue(request);

    }

    public void addOnclicklistner() {

        mBrowseIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Toast.makeText(getApplicationContext(), "Select the Pictures in Landscape mode for a best view", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Get the Pictures in Portrait mode for a best view", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    Imagefile = new File(Environment.getExternalStorageDirectory() + File.separator + "img.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Imagefile));
                    startActivityForResult(intent, CAMERA_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mCategory.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    //Initialize the Alert Dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                    //Source of the data in the DIalog
                    // Set the dialog title
                    builder.setTitle("Select Category");
                    builder.setAdapter(mCatAdapter, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            // Do something with the selection
                            // mDoneButton.setText(items[item]);
                            Toast.makeText(getApplicationContext(), "id" + mCatAdapter.getItem(item), Toast.LENGTH_LONG).show();
                            //Customer cus = adapter.getItem(item);

                            Log.e(mTAG, "Cat name :" + categorylist.get(item).keySet() + categorylist.get(item) + categorylist.get(item).values());
                            // String route = String.valueOf(categorylist.get(item).values());
                            Set<String> key = categorylist.get(item).keySet();

                            for (String myVal : key) {
                                Log.e(mTAG, "Cat_Id : "+myVal);

                                CAT_ID = myVal;
                            }
                            Collection<String> value = categorylist.get(item).values();

                            for (String myVal: value){
                                Log.e(mTAG, "KEy Value :"+myVal);
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
        });*/
        mUploadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {



                    ValidateUserEntries();
                    /*setproductdetails();

                    Uploadimage();*/

                    m_sPrddesc = mProductDesc.getText().toString();
                    m_sPrdname = mProductName.getText().toString();
                    m_sPrdprice = mProductPrce.getText().toString();
                    m_sProdDisc = mProductDiscount.getText().toString();
                    Log.e(mTAG, "PRO_detail" + m_sPrdprice+m_sPrdname+m_sPrddesc);


                    UploadProduct();
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        });
    }
    private void UploadProduct() throws JSONException {
        if (!MyApplication.getInstance().isNetworkAvailable()){
            Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }else {

            final int PRO_price = Integer.valueOf(m_sPrdprice);

            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.EDIT_PRODUCT_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            // System.out.println(response);
                            Log.e(mTAG, "response: " + response);
                            try {
                                JSONObject obj = new JSONObject(response);
                                String status = obj.getString(EndPoints.STATUS);
                                String message = obj.getString(EndPoints.MESSAGE);
                                if (status.equals("100")) {
                                    Toast.makeText(getApplicationContext(), "Product Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ProductDetailsActivity.this, ProductsActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Product Upload Failed", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    if (mEncodedImageData != null && !"".equals(mEncodedImageData)) {
                        params.put(EndPoints.PROD_IMAGE1, mEncodedImageData);
                    }
                    params.put(EndPoints.PROD_PID, PID);
                    params.put(EndPoints.DISCOUNT, m_sProdDisc);

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
                mImageString = ImageScaler.compressImage(imageurii,mBrowseIcon, getApplicationContext());
                mEncodedImageData = ImageScaler.Bitmapconversion(mImageString);
                mBrowseIcon.setPadding(10,10,10,10);
                mBrowseIcon.setBackgroundResource(R.color.white_smoke);
                //mBrowseIcon.setScaleType(ImageView.ScaleType.FIT_XY);
                //mAddEventTxt.setVisibility(View.GONE);
                System.out.println("image path" + mImageString);
                Log.e(mTAG, "ImageStringData Gallery : " + mEncodedImageData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
