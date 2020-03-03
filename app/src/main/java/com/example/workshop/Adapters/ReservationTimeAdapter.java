package com.example.workshop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.R;

import java.time.LocalTime;
import java.util.List;

public class ReservationTimeAdapter extends RecyclerView.Adapter<ReservationTimeAdapter.ViewHolder>
{
    List<LocalTime> reservationTimeList;
    onTimeClickListenerInterface onTimeClickListenerInterface;
    private int checkedPosition = 0;

    public interface onTimeClickListenerInterface
    {
        void onReservationTimeClick(LocalTime localTime);
    }
    public ReservationTimeAdapter( List<LocalTime> reservationTimeList, onTimeClickListenerInterface onTimeClickListenerInterface)
    {
        this.reservationTimeList = reservationTimeList;
        this.onTimeClickListenerInterface = onTimeClickListenerInterface;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater;
        View v;
        inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.reservation_time_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.bind();
        holder.reservationTime.setText(reservationTimeList.get(holder.getAdapterPosition()).toString());
    }

    @Override
    public int getItemCount()
    {
        return reservationTimeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView reservationTime;
        View bottomDivider;
        ConstraintLayout timeConstraint;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            reservationTime = itemView.findViewById(R.id.reservation_time_textView);
            bottomDivider = itemView.findViewById(R.id.bottom_divider);
            timeConstraint = itemView.findViewById(R.id.time_constraint);
            timeConstraint.setOnClickListener(v ->
            {
                bottomDivider.setVisibility(View.VISIBLE);
                if (checkedPosition != getAdapterPosition())
                {
                    notifyItemChanged(checkedPosition);
                    checkedPosition = getAdapterPosition();
                }
                onTimeClickListenerInterface.onReservationTimeClick(getSelected());

            });
        }

        void bind()
        {
            if(checkedPosition == -1)
            {
                bottomDivider.setVisibility(View.GONE);
            }
            else
            {
                if (checkedPosition == getAdapterPosition())
                {
                    bottomDivider.setVisibility(View.VISIBLE);
                }
                else
                {
                    bottomDivider.setVisibility(View.GONE);
                }
            }
        }
        private LocalTime getSelected()
        {
            if (checkedPosition != -1)
            {
                return reservationTimeList.get(checkedPosition);
            }
            return null;
        }
    }
}
