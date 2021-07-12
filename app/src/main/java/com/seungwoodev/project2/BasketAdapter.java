package com.seungwoodev.project2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.MyViewHolder> {

    private Context context;
    private List<String> titles;
    private List<Integer> prices;
    private List<Integer> qty;
    private List<Integer> images;

    public BasketAdapter(Context context, List<String> titles, List<Integer> prices, List<Integer> qty, List<Integer> images){
        this.context = context;
        this.titles = titles;
        this.prices = prices;
        this.qty = qty;
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.basket_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mTextView_title.setText(titles.get(position));
        holder.mTextView_price.setText(prices.get(position).toString());
        holder.mTextView_qty.setText(qty.get(position).toString());
        holder.mImageView.setImageResource(images.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        TextView mTextView_title, mTextView_price, mTextView_qty;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageview);
            mTextView_title = itemView.findViewById(R.id.text_title);
            mTextView_price = itemView.findViewById(R.id.text_price);
            mTextView_qty = itemView.findViewById(R.id.text_qty);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }
    }
}
