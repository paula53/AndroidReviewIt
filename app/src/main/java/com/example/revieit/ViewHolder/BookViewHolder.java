package com.example.revieit.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.revieit.Interface.ItemClickListener;
import com.example.revieit.R;

public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView book_name;
    public ImageView book_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public BookViewHolder(@NonNull View itemView) {
        super(itemView);

        book_name =(TextView)itemView.findViewById(R.id.book_name);
        book_image = (ImageView)itemView.findViewById(R.id.book_image);


    }

    @Override
    public void onClick(View view) {

        itemClickListener.onclick(view,getAdapterPosition(),false);

    }
}
