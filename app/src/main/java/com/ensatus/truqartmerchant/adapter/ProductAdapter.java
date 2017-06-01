package com.ensatus.truqartmerchant.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ensatus.truqartmerchant.R;
import com.ensatus.truqartmerchant.app.MyApplication;
import com.ensatus.truqartmerchant.module.Product;
import java.util.List;

/**
 * Created by Praveen on 08-12-2016.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private static String mTAG = ProductAdapter.class.getSimpleName();
    private List<Product> mDataset;
    private ImageLoader imageLoader;
    private Context mContext;
    float  dis;
    String markedprice;
    float s;
    String amount;

    public ProductAdapter(List<Product> myDataset, Context context){

        mDataset = myDataset;
        this.mContext = context;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // create a new view
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent,false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {



        final Product product = mDataset.get(position);
        if (imageLoader == null)
            imageLoader = MyApplication.getInstance().getImageLoader();
        imageLoader.get(product.getmImage(), ImageLoader.getImageListener(holder.imageView, R.drawable.a_s_tproductsicon, android.R.drawable.ic_dialog_alert));
        Log.e(mTAG,"IMAGEURL :"+product.getmImage());

        markedprice = product.getmPrice();

        if (!product.getmDisc().equals("null")){

            dis = Float.valueOf(product.getmDisc());
        }else {
            dis = 0;
        }
        s = (Float.valueOf((markedprice))-(Float.valueOf(markedprice) / 100 * dis));
        amount = String.valueOf(s);

        holder.proDisc.setText(mContext.getResources().getString(R.string.rupee)+amount);
       // holder.proPrice.setBackgroundResource(R.drawable.strike_through);
        holder.imageView.setImageUrl(product.getmImage(), imageLoader);
        if ("null".equalsIgnoreCase(product.getmUnit()) || product.getmUnit() == null){
            holder.proUnits.setText("No unit");
        }else {
            holder.proUnits.setText(product.getmUnit());
        }

        //holder.proDesc.setText(product.getmDescription());
        holder.proName.setText(product.getmProName());
        if (product.getmDisc().equals("0")){
            holder.proPrice.setVisibility(View.GONE);
            holder.proDiscount.setVisibility(View.GONE);

        }else {
            holder.proPrice.setText(mContext.getResources().getString(
                    R.string.rupee)+product.getmPrice()+"/-");
            holder.strikeThroughText(holder.proPrice);
            holder.proDiscount.setText(product.getmDisc()+"%off");
        }

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

     static class ViewHolder extends RecyclerView.ViewHolder{

         NetworkImageView imageView;
         TextView proName;
         TextView proPrice;
         TextView proDisc;
         TextView proUnits;
         TextView proDiscount;

         ViewHolder(View itemView) {
            super(itemView);

            imageView = (NetworkImageView) itemView.findViewById(R.id.itemImg);
            proName = (TextView) itemView.findViewById(R.id.item_name);
            proPrice = (TextView) itemView.findViewById(R.id.item_price);
            proDisc = (TextView) itemView.findViewById(R.id.item_disc_price);
            proUnits = (TextView) itemView.findViewById(R.id.item_unit);
            proDiscount = (TextView) itemView.findViewById(R.id.item_disc);
        }

         public void strikeThroughText(TextView price) {
             price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         }
     }
}
