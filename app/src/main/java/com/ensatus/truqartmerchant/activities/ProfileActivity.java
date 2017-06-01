package com.ensatus.truqartmerchant.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.helper.ImageScaler;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String mTAG = ProfileActivity.class.getSimpleName();


    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;


    User user = MyApplication.getInstance().getPrefManager().getUser();
    private String MID = user.getmId();

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    private  final int PICK_FROM_GALLERY = 1;
    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private TextView mProfName;
    private TextView mShopName;
    private CircleImageView mProfPic;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private FloatingActionButton mTakePicture;
    private ImageLoader imageLoader;
    private EditText eProfName;
    private EditText ePhoneNo;
    private EditText eShopName;
    private EditText eShopAdd;

    static String mImageString;
    private static String mEncodedImageData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.ash_grey));
        }
        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);

        mToolbar.inflateMenu(R.menu.menu_main);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);


        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.action_logout) {
                    MyApplication.getInstance().logout();
                    return true;
                }
                return onMenuItemClick(menuItem);
            }

        });

        //set profile pic
        imageLoader = MyApplication.getInstance().getImageLoader();
        imageLoader.get(user.getmIcon(), ImageLoader.getImageListener(mProfPic, R.drawable.man_user, R.drawable.man_user));


        addOnClickListner();
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.camera_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });*/
    }

    private void bindActivity() {
        mToolbar        = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle          = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout   = (AppBarLayout) findViewById(R.id.main_appbar);
        mProfPic = (CircleImageView) findViewById(R.id.profile_pic);
        mTakePicture    = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        mProfName = (TextView) findViewById(R.id.profile_name);
        mProfName.setText(user.getmName());
        mShopName = (TextView) findViewById(R.id.shopname);
        mShopName.setText(user.getmShop());
        eProfName = (EditText) findViewById(R.id.name_prof);
        eProfName.setText(user.getmName());
        ePhoneNo = (EditText) findViewById(R.id.phone);
        ePhoneNo.setText(user.getmMobileNo());
        eShopName = (EditText) findViewById(R.id.shop_name);
        eShopName.setText(user.getmShop());
        



    }

    public void addOnClickListner(){
        mTakePicture.setOnClickListener(new View.OnClickListener() {
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
            } else */if (requestCode == PICK_FROM_GALLERY) {
                System.out.println("in browse request");
                final Uri l_oImageUri = data.getData();
                String imageurii = l_oImageUri.toString();
                mImageString = ImageScaler.compressImage(imageurii,mProfPic, getApplicationContext());
                mEncodedImageData = ImageScaler.Bitmapconversion(mImageString);
               /*// mBrowseIcon.setPadding(10,10,10,10);
                mBrowseIcon.setBackgroundResource(R.color.white_smoke);
                //mBrowseIcon.setScaleType(ImageView.ScaleType.FIT_XY);
                mBrowseIcon.setVisibility(View.GONE);*/

                System.out.println("image path" + mImageString);
                Log.e(mTAG, "ImageStringData Gallery : " + mEncodedImageData);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

}