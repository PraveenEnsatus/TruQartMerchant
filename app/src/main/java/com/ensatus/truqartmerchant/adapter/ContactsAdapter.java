package com.ensatus.truqartmerchant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.module.Contact;

import java.util.ArrayList;

/**
 * Created by Praveen on 20-12-2016.
 */

public class ContactsAdapter extends BaseAdapter {
    public static ArrayList<Contact> mArrayContacts = new ArrayList<Contact>();
    private Context mContext;

    public ContactsAdapter(ArrayList<Contact> mArrayContacts, Context mContext) {
        ContactsAdapter.mArrayContacts = mArrayContacts;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mArrayContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItems(ArrayList<Contact> mArrayList) {
        for (int i = 0; i < mArrayList.size(); i++) {
            mArrayContacts.add(mArrayList.get(i));
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ContactView mHolder;
        if (convertView == null) {
            mHolder = new ContactView();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.contact_item_list, null);
            mHolder.mItemName = (TextView) convertView
                    .findViewById(R.id.contact_name);
            mHolder.mItemNumber = (TextView) convertView
                    .findViewById(R.id.contact_number);
            mHolder.mItemIcon = (TextView) convertView.findViewById(R.id.icon_text);


            convertView.setTag(mHolder);
        } else {
            mHolder = (ContactView) convertView.getTag();
        }
        final Contact mDataHolder = (Contact) getItem(position);
        Contact c = getContact(position);
        mHolder.mItemName.setText(mDataHolder.getmName());
        mHolder.mItemNumber.setText(mDataHolder.getmPhone());
        mHolder.mItemIcon.setText(String.valueOf(mDataHolder.getmName().charAt(0)).toUpperCase());



        return convertView;
    }

    Contact getContact(int position) {
        return ((Contact) getItem(position));
    }

    public class ContactView {
        TextView mItemNumber, mItemName, mItemIcon;


    }




}
/*extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private List<Contact> mDataset;
    private Context mContext;

    public ContactsAdapter(List<Contact> myDataset, Context context){
        this.mDataset = myDataset;
        this.mContext = context;

    }

    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_list, parent,false);
        // set the view's size, margins, paddings and layout parameters
        ContactsAdapter.ViewHolder vh = new ContactsAdapter.ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {
         Contact contact = mDataset.get(position);
        holder.mContactName.setText(contact.getmName());
        holder.mNum.setText(contact.getmPhone());


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        NetworkImageView imageView;
        TextView mContactName, mNum;
        CardView cardView;
        ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);


            icon = (ImageView) itemView.findViewById(R.id.prof_icon);
            mContactName = (TextView) itemView.findViewById(R.id.contact_name);

            mNum = (TextView) itemView.findViewById(R.id.contact_number);

        }

    }
}
*/