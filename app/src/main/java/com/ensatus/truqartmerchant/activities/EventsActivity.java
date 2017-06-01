package com.ensatus.truqartmerchant.activities;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.fragments.AddEventsFrag;
import com.ensatus.truqartmerchant.fragments.EventsFrag;
import com.ensatus.truqartmerchant.module.Exhibitions;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container_frame, new EventsFrag()).commit();
    }
}
