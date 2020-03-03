package com.example.workshop.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Models.ServiceDetail;
import com.example.workshop.R;

import java.util.List;

public class ServiceDetailAdapterr extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    //lista danych
    List<ServiceDetail> serviceDetails;
    //konstruktor adaptera
    public ServiceDetailAdapterr(List<ServiceDetail> serviceDetails)
    {
        this.serviceDetails = serviceDetails;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //layout karty dla usługi warsztatu i obiekt ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_detail_card, parent, false);
        //tworzymy i zwracany ViewHolder
        return new ServiceDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        //uzupełniamy layout karty usługi
        ((ServiceDetailViewHolder) holder).title.setText(serviceDetails.get(position).getTitle());
    }

    @Override
    public int getItemCount()
    {
        //zwracamy rozmiar listy
        return serviceDetails.size();
    }
    //implementacja ViewHoldera
    //każdy obiekt tej klasy przechowuje odniesienie do layoutu elementu listy
    //dzięki temu wywołujemy findViewById() tylko raz dla każdego elementu
    public class ServiceDetailViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        public ServiceDetailViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.detail_title);
        }
    }
}
