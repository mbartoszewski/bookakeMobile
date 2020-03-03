package com.example.workshop.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Models.ServiceOrder;
import com.example.workshop.R;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.workshop.MainActivity.basicT;
import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.role;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder>
{
    List<ServiceOrder> pendingOrders;
    ArrayAdapter<String> pendingStatusAdapter;
    List<String> pendingStatusList;
    Context context;
    public PendingAdapter(Context context, List<ServiceOrder> pendingOrders, List<String> pendingStatusList)
    {
        this.pendingOrders = pendingOrders;
        this.pendingStatusList = pendingStatusList;
        this.context = context;
    }
    @NonNull
    @Override
    public PendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        pendingStatusAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, pendingStatusList);
        pendingStatusAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        LayoutInflater inflater;
        View v;
        inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.pending_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingAdapter.ViewHolder holder, int position)
    {
        if (role.equals("[ROLE_PROVIDER]"))
        {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setVisibility(View.GONE);
            holder.statusTextView.setVisibility(View.GONE);
            holder.pendingStatusSpinner.setVisibility(View.VISIBLE);
            holder.pendingStatusSpinner.setAdapter(pendingStatusAdapter);
            holder.pendingStatusSpinner.setSelection( pendingStatusList.lastIndexOf(pendingOrders.get(holder.getAdapterPosition()).getOrderStatus().toLowerCase()));
        }
        else if (role.equals("[ROLE_USER]"))
        {
            holder.acceptButton.setVisibility(View.GONE);
            if (pendingOrders.get(holder.getAdapterPosition()).getOrderStatus().toLowerCase().equals("unconfirmed") || pendingOrders.get(holder.getAdapterPosition()).getOrderStatus().toLowerCase().equals("confirmed"))
            {
                holder.cancelButton.setVisibility(View.VISIBLE);
            }
            holder.pendingStatusSpinner.setVisibility(View.GONE);
            holder.statusTextView.setVisibility(View.VISIBLE);
        }
        if (pendingOrders.size() > 0)
        {
            holder.statusTextView.setText(pendingOrders.get(holder.getAdapterPosition()).getOrderStatus().toLowerCase());
            holder.timeTextView.setText(pendingOrders.get(holder.getAdapterPosition()).getServiceDetail().getExecutionTimeMin() + " min");
            holder.dateTextview.setText(pendingOrders.get(holder.getAdapterPosition()).getServiceStartDate().toLocalDate().toString() + " " +pendingOrders.get(holder.getAdapterPosition()).getServiceStartDate().toLocalTime().toString());
            holder.priceTextView.setText(pendingOrders.get(holder.getAdapterPosition()).getServiceDetail().getPrice() + " PLN");
            holder.detailNameTextView.setText(pendingOrders.get(holder.getAdapterPosition()).getServiceDetail().getTitle());
            holder.serviceTitleTextView.setText(pendingOrders.get(holder.getAdapterPosition()).getServiceDetail().getService().getName());
            holder.additionalInfoTextView.setText(pendingOrders.get(holder.getAdapterPosition()).getDescription());
        }
    }

    @Override
    public int getItemCount()
    {
        return pendingOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView statusTextView, timeTextView, dateTextview, priceTextView, detailNameTextView, serviceTitleTextView, additionalInfoTextView;
        Button acceptButton, cancelButton;
        Spinner pendingStatusSpinner;
        String status;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            statusTextView= itemView.findViewById(R.id.pending_status_textView);
            timeTextView = itemView.findViewById(R.id.pending_detail_execution_time_textView);
            dateTextview = itemView.findViewById(R.id.pending_start_date_textView);
            priceTextView = itemView.findViewById(R.id.pending_detail_price_textView);
            detailNameTextView = itemView.findViewById(R.id.pending_detail_name_textView);
            serviceTitleTextView = itemView.findViewById(R.id.pending_service_title_textView);
            additionalInfoTextView = itemView.findViewById(R.id.pending_service_additional_info_textView);
            acceptButton = itemView.findViewById(R.id.pending_accept_button);
            cancelButton = itemView.findViewById(R.id.pending_cancel_button);
            pendingStatusSpinner = itemView.findViewById(R.id.pending_status_spinner);
            pendingStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    status = pendingStatusList.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
            acceptButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.pending_cancel_button:
                    changeOrderStatus(basicT, pendingOrders.get(getAdapterPosition()).getServiceOrderId(), "canceled", getAdapterPosition());
                    break;
                case R.id.pending_accept_button:
                        changeOrderStatus(basicT, pendingOrders.get(getAdapterPosition()).getServiceOrderId(), status, getAdapterPosition());
                    break;
            }
        }

        private void changeOrderStatus(String basicT, Long serviceOrder, String status, int position)
        {
            retrofitService.changeOrderStatus(basicT, serviceOrder, status)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ServiceOrder>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {

                        }

                        @Override
                        public void onNext(ServiceOrder serviceOrder)
                        {

                        }

                        @Override
                        public void onError(Throwable e)
                        {

                        }

                        @Override
                        public void onComplete()
                        {
                            cancelButton.setVisibility(View.GONE);
                            pendingOrders.get(position).setOrderStatus(status);
                            notifyItemChanged(position);
                            notifyDataSetChanged();

                        }
                    });
        }
    }
}
