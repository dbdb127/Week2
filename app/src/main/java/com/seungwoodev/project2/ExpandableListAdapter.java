package com.seungwoodev.project2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private ArrayList<Item> data;

    private OnItemClickListener mListner;

    // constructor
    public ExpandableListAdapter(ArrayList<Item> data){
        this.data = data;
    }

    public class ListChildrenViewHolder extends RecyclerView.ViewHolder {

        protected TextView categorySub;

        public ListChildrenViewHolder(@NonNull @NotNull View itemView, OnItemClickListener listener) {
            super(itemView);

            this.categorySub = (TextView) itemView.findViewById(R.id.textView_sub);
            this.categorySub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position, data.get(position));
                        }
                    }
                }
            });
        }
    }

    private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryMain;
        public ImageView expand_btn;
        public Item refferalItem;

        public ListHeaderViewHolder(View itemView) {
            super(itemView);
            categoryMain = (TextView) itemView.findViewById(R.id.textView_main);
            expand_btn = (ImageView) itemView.findViewById(R.id.imageview_main);
        }
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view;

        switch (viewType) {
            case HEADER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_header, parent, false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;
            case CHILD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child, parent, false);
                ListChildrenViewHolder child = new ListChildrenViewHolder(view, mListner);
                return child;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        final Item item = data.get(position);
        switch (item.type) {
            case HEADER:
                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.refferalItem = item;
                itemController.categoryMain.setText(item.text);
                if (item.invisibleChildren == null) {
                    itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                } else {
                    itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                }
                itemController.expand_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.invisibleChildren == null) {
                            item.invisibleChildren = new ArrayList<Item>();
                            int count = 0;
                            int pos = data.indexOf(itemController.refferalItem);
                            while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                item.invisibleChildren.add(data.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                            itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        } else {
                            int pos = data.indexOf(itemController.refferalItem);
                            int index = pos + 1;
                            for (Item i : item.invisibleChildren) {
                                data.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            itemController.expand_btn.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                            item.invisibleChildren = null;
                        }
                    }
                });
                break;

            case CHILD:
                ListChildrenViewHolder listchildController = (ListChildrenViewHolder) holder;
                listchildController.categorySub.setText(data.get(position).getText());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public int getItemViewType(int position) {
        return data.get(position).type;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListner = listener;
    }
}