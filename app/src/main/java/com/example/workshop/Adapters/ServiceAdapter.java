package com.example.workshop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Models.ResponseMessage;
import com.example.workshop.Models.Service;
import com.example.workshop.Models.ServiceDetail;
import com.example.workshop.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.workshop.MainActivity.basicT;
import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.role;
import static com.example.workshop.MainActivity.serviceId;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder>
{
    private Context context;
    private List<Service> serviceList;
    OnClickListener onClickListener;

    public interface OnClickListener
    {
        void buttonRequestClickListener(Long serviceId);
        void addressClickListener(String address);
    }
    public ServiceAdapter(Context context, List<Service> serviceList, OnClickListener onClickListener)
    {
       this.context=context;
       this.serviceList = serviceList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater;
        View v;
        inflater = LayoutInflater.from(parent.getContext());
        v = inflater.inflate(R.layout.service_card, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(serviceList.size() > 0)
        {
            if((role.equals("[ROLE_PROVIDER]") && serviceId.equals(serviceList.get(holder.getAdapterPosition()).getServiceId())) || role.equals("[ROLE_ADMIN]"))
            {
                holder.deleteService.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.deleteService.setVisibility(View.GONE);
            }
            holder.serviceName.setText(serviceList.get(holder.getAdapterPosition()).getName());
            holder.serviceAddress.setText(serviceList.get(holder.getAdapterPosition()).getServiceAddress().toString());
        }
    }

    @Override
    public int getItemCount()
    {
        return  serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView serviceName;
        TextView serviceAddress;
        CardView buttonServiceRequest;
        ImageButton deleteService;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            serviceName = itemView.findViewById(R.id.service_name);
            serviceAddress = itemView.findViewById(R.id.service_address);
            buttonServiceRequest = itemView.findViewById(R.id.service_cardView);
            deleteService = itemView.findViewById(R.id.delete_service_imageButton);

            deleteService.setOnClickListener(this);
            serviceAddress.setOnClickListener(v->
                    onClickListener.addressClickListener(serviceList.get(getAdapterPosition()).getServiceAddress().getGeo()));
            buttonServiceRequest.setOnClickListener(v ->
                    onClickListener.buttonRequestClickListener(serviceList.get(getAdapterPosition()).getServiceId()));
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.delete_service_imageButton:
                    deleteService(basicT, getAdapterPosition());
                    break;
            }
        }

        private void deleteService(String basicT, int position)
        {
            retrofitService.deleteService(basicT)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ResponseMessage>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {

                        }

                        @Override
                        public void onNext(ResponseMessage responseMessage)
                        {
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Log.e("DeleteService", "nie udało się" + e.getMessage());
                        }

                        @Override
                        public void onComplete()
                        {
                            serviceId = 0L;
                            serviceList.remove(position);
                            notifyItemRemoved(position);
                            notifyDataSetChanged();
                            role = "[ROLE_USER]";
                        }
                    });
        }
    }
}
