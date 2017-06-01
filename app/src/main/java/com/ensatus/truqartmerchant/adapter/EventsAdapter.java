package com.ensatus.truqartmerchant.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.app.EndPoints;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.fragments.EventsFrag;
import com.ensatus.truqartmerchant.module.Exhibitions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Praveen on 09-12-2016.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private static String mTAG = EventsAdapter.class.getSimpleName();

    private static final int PENDING_REMOVAL_TIMEOUT = 3000; // 3sec

    private List<Exhibitions> mDataset;
    private List<Exhibitions> itemsPendingRemoval;
    private ImageLoader imageLoader;
    private Context mContext;

    private boolean deleteOn = true;

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    private HashMap<Exhibitions, Runnable> pendingRunnables = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be


    public EventsAdapter(List<Exhibitions> myDataset, Context context){
        mDataset = myDataset;
        this.mContext = context;
        itemsPendingRemoval = new ArrayList<>();

    }

    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent,false);
        // set the view's size, margins, paddings and layout parameters
        EventsAdapter.ViewHolder vh = new EventsAdapter.ViewHolder(itemView);
        return vh;

    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder holder, final int position) {

        final  Exhibitions exh = mDataset.get(position);


        if (itemsPendingRemoval.contains(exh)) {
            // we need to show the "undo" state of the row
            holder.itemView.setBackgroundColor(Color.RED);
            holder.cardView.setVisibility(View.GONE);
            holder.mDelete.setVisibility(View.VISIBLE);
            holder.mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // this will delete the row from the list
                    remove(mDataset.indexOf(exh));
                }
            });
            holder.mUndo.setVisibility(View.VISIBLE);
            holder.mUndo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnables.get(exh);
                    pendingRunnables.remove(exh);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(exh);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(mDataset.indexOf(exh));
                }
            });
        } else {

            // we need to show the "normal" state
            holder.itemView.setBackgroundColor(Color.LTGRAY);
            holder.cardView.setVisibility(View.VISIBLE);

            if (imageLoader == null)
                imageLoader = MyApplication.getInstance().getImageLoader();
            imageLoader.get(exh.geteImage(), ImageLoader.getImageListener(holder.imageView, R.drawable.a_s_tproductsicon, android.R.drawable.ic_dialog_alert));
            Log.e(mTAG, "IMAGEURL :" + exh.geteImage());

            holder.imageView.setImageUrl(exh.geteImage(), imageLoader);
            holder.mEventName.setText(exh.geteEventName());
           // holder.mEventDT.setText(exh.geteStartDate());
            holder.mDelete.setVisibility(View.GONE);
            holder.mUndo.setVisibility(View.GONE);
            holder.mUndo.setOnClickListener(null);
            holder.mDelete.setOnClickListener(null);


        }

    }
    public boolean isUndoOn() {
        return deleteOn;
    }

    public void pendingRemoval(int position) {
        final Exhibitions exh = mDataset.get(position);
        if (!itemsPendingRemoval.contains(exh)) {
            itemsPendingRemoval.add(exh);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    Runnable pendingRemovalRunnable = pendingRunnables.get(exh);
                    pendingRunnables.remove(exh);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(exh);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(mDataset.indexOf(exh));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnables.put(exh, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        Exhibitions item = mDataset.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (mDataset.contains(item)) {
            mDataset.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        Exhibitions item = mDataset.get(position);
        return itemsPendingRemoval.contains(item);
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder{

        NetworkImageView imageView;
        TextView mEventName, mUndo, mDelete, mEventDT;
        CardView cardView;

         ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imageView = (NetworkImageView) itemView.findViewById(R.id.event_card_img);
            mEventName = (TextView) itemView.findViewById(R.id.event_card_name);
            mDelete = (TextView) itemView.findViewById(R.id.cancel_btn);
            mUndo = (TextView) itemView.findViewById(R.id.undo);
            mEventDT = (TextView) itemView.findViewById(R.id.event_card_dt);

        }

    }
}