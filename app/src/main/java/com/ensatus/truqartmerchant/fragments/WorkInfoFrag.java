package com.ensatus.truqartmerchant.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ensatus.truqartmerchant.R;

/**
 * Created by Praveen on 20-01-2017.
 */

public class WorkInfoFrag extends Fragment {

    TextView mContinueBtn;
    private String sShopName;
    private String sShopAddress;
    private String mEncodedImageData;
    private String m_sShopaddress;
    private ImageView mBrowseIcon;
    private TextView mSkipBtn;
    private Bundle bundle;
    private EditText etShopName;
    private EditText etShopAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.work_info, container, false);
        mContinueBtn = (TextView) rootView.findViewById(R.id.conti);
        etShopAdd = (EditText) rootView.findViewById(R.id.mrcht_add);
        etShopName = (EditText) rootView.findViewById(R.id.mrcht_shop);

        bundle = this.getArguments();

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

            if (mValidateData()) {
                Log.e("name", "" + bundle.getString("name"));
                bundle.putString("shopname", sShopName);
                bundle.putString("shopadd", sShopAddress);
                BankInfoFrag bankInfoFrag = new BankInfoFrag();
                bankInfoFrag.setArguments(bundle);
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container_frame, bankInfoFrag).addToBackStack(null).commit();
            }
            }
        });
    }

    private boolean mValidateData(){
        boolean valid = true;

        sShopName = etShopName.getText().toString();
        sShopAddress = etShopAdd.getText().toString();

        if (sShopName.isEmpty()){
            etShopName.setError("Enter shop name");
            valid = false;
        }
        return valid ;
    }

}
