package com.ensatus.truqartmerchant.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.activities.EventsActivity;
import com.ensatus.truqartmerchant.activities.MainActivity;
import com.ensatus.truqartmerchant.adapter.EventsAdapter;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.module.Exhibitions;
import com.ensatus.truqartmerchant.module.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Praveen on 09-12-2016.
 */

public class EventsFrag extends Fragment {

    private final static String TAG = EventsFrag.class.getSimpleName();
    protected boolean mCardRequested;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    public ArrayList<Exhibitions> myDataset = new ArrayList<>();
    private TextView mEdit;
    private ImageView mEventDel;
    private View view;
    private boolean add = false;
    private Paint p = new Paint();
    User user = MyApplication.getInstance().getPrefManager().getUser();
    private String MERCHANT_ID = user.getmId();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        final View mRootView = inflater.inflate(R.layout.events, container, false);

        Toolbar toolbar = (Toolbar) mRootView.findViewById(R.id.toolbar);
        toolbar.setTitle("My Events ");
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


      /*  OnItemTouchListener itemTouchListener = new OnItemTouchListener() {
            @Override
            public void onCardViewTap(View view, int position) {
                Toast.makeText(getContext(), "Tapped " + myDataset.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButton1Click(View view, int position) {
                Toast.makeText(getContext(), "Clicked Button1 in " + myDataset.get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onButton2Click(View view, int position) {
                Toast.makeText(getContext(), "Clicked Button2 in " + myDataset.get(position), Toast.LENGTH_SHORT).show();
            }
        };*/

        mCardRequested = false;
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.exhibition_view);
        mEventDel = (ImageView) mRootView.findViewById(R.id.cancel);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use Linear Layout Manager for grid view
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify  Adapter
        mAdapter = new EventsAdapter(myDataset,getContext());
        mRecyclerView.setAdapter(mAdapter);

        getEvents();

        mAdapter.notifyDataSetChanged();
        //initSwipe();
        setUpItemTouchHelper();

        mRecyclerView.addOnItemTouchListener(new MyApplication.RecyclerTouchListener(getContext(), mRecyclerView, new MyApplication.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Exhibitions exhibitions = myDataset.get(position);


            }
        }));




        FloatingActionButton fab = (FloatingActionButton) mRootView.findViewById(R.id.add_event_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddEventsFrag addEventsFrag = new AddEventsFrag();
                myDataset.clear();
                fragmentTransaction.replace(R.id.container_frame, addEventsFrag).addToBackStack(null).commit();

            }
        });
        return mRootView;
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

          /*  // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.cancel);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ten_margin);
                initiated = true;
            }*/

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                EventsAdapter testAdapter = (EventsAdapter) recyclerView.getAdapter();
                if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                EventsAdapter adapter = (EventsAdapter) mRecyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    adapter.remove(swipedPosition);
                }
            }

           /* @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }*/

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }








    private void getEvents() {
        if (!MyApplication.getInstance().isNetworkAvailable()) {
            Toast.makeText(getContext(), "Check your Internet Connection", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest strReq = new StringRequest(Request.Method.POST, EndPoints.GET_EXHIBITION_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.e(TAG, "response: " + response.toString());

                                JSONObject obj = new JSONObject(response);
                                System.out.print(obj);
                                String status = obj.getString(EndPoints.STATUS);
                                String message = obj.getString(EndPoints.MESSAGE);
                                if (message.equals("SUCCESS") && status.equals("100")) {
                                    JSONArray product = obj.getJSONArray(EndPoints.EXHIBITION_INFO);
                                    for (int i = 0; i < product.length(); i++) {
                                        JSONObject e = product.getJSONObject(i);
                                        Exhibitions Exhi = new Exhibitions(e.getString(EndPoints.EVENT_INVI_IMAGE),
                                                e.getString(EndPoints.EVENT_NAME), e.getString(EndPoints.START_DATE),
                                                e.getString(EndPoints.END_DATE), e.getString(EndPoints.EVENT_ADDRESS),
                                                e.getString(EndPoints.EVENT_DESC), e.getString(EndPoints.EVENT_ID));


                                        myDataset.add(i, Exhi);
                                        Log.e(TAG, "product: " + Exhi.geteImage() + Exhi.geteEventName() + "  "
                                                + Exhi.geteEndDate() + " " + Exhi.geteLoc());

                                    }
                                } else if (status.equals("-1")) {
                                    Toast.makeText(getActivity(), "Products Not Found", Toast.LENGTH_SHORT).show();
                                }
                                mAdapter.notifyDataSetChanged();
                                mCardRequested = true;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }, new Response.ErrorListener()

            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {


                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(EndPoints.MERCHANT_ID, MERCHANT_ID);
                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    // params.put("Content-Type","application/json");
                    return params;
                }

            };
            Log.e(TAG, "StrReq:" + strReq);
            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        }
    }
}
