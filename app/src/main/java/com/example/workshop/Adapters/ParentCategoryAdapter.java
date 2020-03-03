package com.example.workshop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.R;
import com.example.workshop.Models.Category;

import java.util.List;

public class ParentCategoryAdapter extends RecyclerView.Adapter<ParentCategoryAdapter.ViewHolder>
{
    private Context context;
    private List<Category> categoryList;
    onClickListener onClickListener;
    public interface onClickListener
    {
        void onParentCategoryClickListener(Long parentCategoryId);
    }
    public ParentCategoryAdapter(Context context, List<Category> categoryList, onClickListener onClickListener)
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
        v = inflater.inflate(R.layout.parent_category_card, parent, false);
        onClickListener.onParentCategoryClickListener(1L);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(categoryList.size() > 0)
        {
            holder.categoryText.setText(categoryList.get(holder.getAdapterPosition()).getName());
            holder.categoryIcon.setImageResource(context.getResources().getIdentifier(categoryList.get(holder.getAdapterPosition()).getName().toLowerCase().replace(" ", ""), "drawable", context.getPackageName()));
            //                        ((ExchangeTypeViewHolder) holder).mParentConstraintLayout.setBackgroundResource(context.getResources().getIdentifier("card_border_" + mData.get(position).getCryptoCurrency().toLowerCase(), "drawable", context.getPackageName()));
        }
    }

    @Override
    public int getItemCount()
    {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView categoryText;
        ImageView categoryIcon;
        CardView parentCategoryCard;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            categoryText = itemView.findViewById(R.id.parent_category_text);
            categoryIcon = itemView.findViewById(R.id.parent_category_icon);
            parentCategoryCard = itemView.findViewById(R.id.parent_category_card);

            parentCategoryCard.setOnClickListener(v -> onClickListener.onParentCategoryClickListener(categoryList.get(getAdapterPosition()).getCategoryId()));

        }
    }
}
