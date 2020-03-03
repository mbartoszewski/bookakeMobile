package com.example.workshop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Models.Category;
import com.example.workshop.R;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>
{
    private Context context;
    private List<Category> categoryList;
    OnClickListener onClickListener;
    public interface OnClickListener
    {
        void onCategoryClickListener(Long categoryId);
    }
    public CategoryAdapter(Context context, List<Category> categoryList, OnClickListener onClickListener)
    {
       this.context=context;
       this.categoryList = categoryList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater;
        View v;
        inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.category_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(categoryList.size() > 0)
        {
            holder.category.setText(categoryList.get(holder.getAdapterPosition()).getName());
            //((ExchangeTypeViewHolder) holder).mParentConstraintLayout.setBackgroundResource(context.getResources().getIdentifier("card_border_" + mData.get(position).getCryptoCurrency().toLowerCase(), "drawable", context.getPackageName()));
        }
    }

    @Override
    public int getItemCount()
    {
        return  categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView category;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            category = itemView.findViewById(R.id.category_text);

            category.setOnClickListener(v -> onClickListener.onCategoryClickListener(categoryList.get(getAdapterPosition()).getCategoryId()));
        }
    }
}
