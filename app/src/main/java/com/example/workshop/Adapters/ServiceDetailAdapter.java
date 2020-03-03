package com.example.workshop.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Models.ResponseMessage;
import com.example.workshop.Models.ServiceDetail;
import com.example.workshop.Models.ServiceOrder;
import com.example.workshop.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.workshop.MainActivity.basicT;
import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.role;
import static com.example.workshop.MainActivity.serviceId;

public class ServiceDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;
    private List<ServiceDetail> serviceDetailsList;
    private List<ServiceOrder> serviceOrderList;
    public Map<String, Long> categoryMap;
    public List<String> categoryList;
    public ArrayAdapter<String> categoryAdapter;
    private final int VIEW_TYPE_DETAIL = 1;
    private final int VIEW_TYPE_ORDER = 2;
    private int viewInt;
    private onClickListenerInterface onClickListenerInterface;

    public interface onClickListenerInterface
    {
        void onDetailClick(Long serviceDetail);
        void onDeleteDetailClick(Long serviceDetail, int position);
    }

    public ServiceDetailAdapter(Context context, List<ServiceDetail> serviceDetailsList, List<ServiceOrder> serviceOrderList, Map<String, Long> categoryMap, int viewInt, onClickListenerInterface onClickListenerInterface)
    {
        this.context = context;
        this.serviceDetailsList = serviceDetailsList;
        this.serviceOrderList = serviceOrderList;
        this.categoryMap = categoryMap;
        this.viewInt = viewInt;
        this.onClickListenerInterface = onClickListenerInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater;
        View v;

      if(viewType == VIEW_TYPE_DETAIL)
      {
          this.categoryList= new ArrayList<>(categoryMap.keySet());
          categoryAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categoryList);
          categoryAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

          inflater = LayoutInflater.from(parent.getContext());
          v = inflater.inflate(R.layout.service_detail_card, parent, false);
          return new ServiceDetailViewHolder(v);
      }
      else
      {
          inflater = LayoutInflater.from(parent.getContext());
          v = inflater.inflate(R.layout.pending_card, parent, false);
          return new ProviderPendingViewHolder(v);
      }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        Long l = serviceDetailsList.get(holder.getAdapterPosition()).getService()!= null?serviceDetailsList.get(holder.getAdapterPosition()).getService().getServiceId():-1L;
        switch (holder.getItemViewType())
        {
            case VIEW_TYPE_DETAIL:
                if (serviceDetailsList.size() > 0)
                {
                    if((role.equals("[ROLE_PROVIDER]") && (serviceId.equals(l) || l.equals(-1L))|| role.equals("[ROLE_ADMIN]")))
                    {
                        ((ServiceDetailViewHolder) holder).editDetail.setVisibility(View.VISIBLE);
                        ((ServiceDetailViewHolder) holder).deleteDetail.setVisibility(View.VISIBLE);
                        ((ServiceDetailViewHolder) holder).categorySpinnerEdit.setAdapter(categoryAdapter);
                        ((ServiceDetailViewHolder) holder).categorySpinnerEdit.setSelection(0);
                    }
                    else
                    {
                        ((ServiceDetailViewHolder) holder).editDetail.setVisibility(View.GONE);
                        ((ServiceDetailViewHolder) holder).deleteDetail.setVisibility(View.GONE);
                    }
                    ((ServiceDetailViewHolder) holder).title.setText(serviceDetailsList.get(holder.getAdapterPosition()).getTitle());
                    ((ServiceDetailViewHolder) holder).price.setText(serviceDetailsList.get(holder.getAdapterPosition()).getPrice() + " PLN");
                    ((ServiceDetailViewHolder) holder).time.setText(serviceDetailsList.get(holder.getAdapterPosition()).getExecutionTimeMin() + " min");
                    ((ServiceDetailViewHolder) holder).description.setText(serviceDetailsList.get(holder.getAdapterPosition()).getDescription());
                    ((ServiceDetailViewHolder) holder).detailCategory.setText(serviceDetailsList.get(holder.getAdapterPosition()).getCategory().getName());
                }
                break;
            case VIEW_TYPE_ORDER:
                if (serviceOrderList.size() > 0)
                {
                    //((ProviderPendingViewHolder) holder)
                }
        }

    }

    @Override
    public int getItemViewType(int position)
    {
        if (viewInt == VIEW_TYPE_DETAIL)
        {
            return VIEW_TYPE_DETAIL;
        }
        else
        {
            return VIEW_TYPE_ORDER;
        }
    }

    @Override
    public int getItemCount()
    {
        if (viewInt == VIEW_TYPE_DETAIL)
        {
            return serviceDetailsList.size();
        }
        else
        {
            return serviceOrderList.size();
        }

    }

    public class ServiceDetailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView title, description, price, time, detailCategory;
        private ConstraintLayout detailConstraintLayout, editDetailConstraint;
        private ImageButton editDetail, deleteDetail;
        private Button acceptDetailChanges, declineDetailChanges;
        private TextInputEditText titleEdit, descriptionEdit, priceEdit, timeEdit;
        private Spinner categorySpinnerEdit;
        private Long selectedCategoryId;

        public ServiceDetailViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.detail_title);
            price = itemView.findViewById(R.id.pending_detail_price_textView);
            description = itemView.findViewById(R.id.detail_description);
            time = itemView.findViewById(R.id.pending_detail_execution_time_textView);
            detailConstraintLayout = itemView.findViewById(R.id.service_detail_constraint);
            editDetailConstraint = itemView.findViewById(R.id.service_detail_edit_constraint);
            editDetail = itemView.findViewById(R.id.edit_detail_button);
            deleteDetail = itemView.findViewById(R.id.delete_detail_button);
            acceptDetailChanges = itemView.findViewById(R.id.pending_accept_button);
            declineDetailChanges = itemView.findViewById(R.id.pending_cancel_button);
            titleEdit = itemView.findViewById(R.id.detail_title_edit);
            priceEdit = itemView.findViewById(R.id.detail_price_edit);
            descriptionEdit = itemView.findViewById(R.id.detail_description_edit);
            timeEdit = itemView.findViewById(R.id.detail_time_edit);
            categorySpinnerEdit = itemView.findViewById(R.id.category_spinner);
            detailCategory = itemView.findViewById(R.id.detail_category_textView);
            deleteDetail.setOnClickListener(v -> onClickListenerInterface.onDeleteDetailClick(serviceDetailsList.get(getAdapterPosition()).getServiceDetailId(), getAdapterPosition()));
            detailConstraintLayout.setOnClickListener(v ->
                    onClickListenerInterface.onDetailClick(serviceDetailsList.get(getAdapterPosition()).getServiceDetailId()));

            categorySpinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    selectedCategoryId = categoryMap.get(categoryList.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {

                }
            });
            editDetail.setOnClickListener(this);
            acceptDetailChanges.setOnClickListener(this);
            declineDetailChanges.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.edit_detail_button:
                    if (detailConstraintLayout.getVisibility() == View.VISIBLE)
                    {
                        detailConstraintLayout.setVisibility(View.GONE);
                        editDetailConstraint.setVisibility(View.VISIBLE);
                        titleEdit.setText(serviceDetailsList.get(getAdapterPosition()).getTitle());
                        priceEdit.setText(String.valueOf(serviceDetailsList.get(getAdapterPosition()).getPrice()));
                        timeEdit.setText(String.valueOf(serviceDetailsList.get(getAdapterPosition()).getExecutionTimeMin()));
                        descriptionEdit.setText(String.valueOf(serviceDetailsList.get(getAdapterPosition()).getDescription()));
                    }
                    break;
                case R.id.pending_accept_button:
                    if (editDetailConstraint.getVisibility() == View.VISIBLE)
                    {
                        detailConstraintLayout.setVisibility(View.VISIBLE);
                        editDetailConstraint.setVisibility(View.GONE);
                        if (serviceDetailsList.get(getAdapterPosition()).getServiceDetailId() != null && serviceDetailsList.get(getAdapterPosition()).getServiceDetailId() != -1L)
                        {
                            //editservice
                            Log.e("COMPLETE ADD DETAIL", " EDIT SERVICE EDIT SERVICE" + categoryList);
                        }
                        else
                        {
                            addServiceDetail(basicT, selectedCategoryId,
                                    titleEdit.getText().toString(),
                                    descriptionEdit.getText().toString(),
                                    Double.valueOf(priceEdit.getText().toString()),
                                    Integer.valueOf(timeEdit.getText().toString()));
                        }

                    }
                    break;

                case R.id.pending_cancel_button:
                    if (editDetailConstraint.getVisibility() == View.VISIBLE)
                    {
                        detailConstraintLayout.setVisibility(View.VISIBLE);
                        editDetailConstraint.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

    public class ProviderPendingViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, description, price, time;
        ImageButton editDetail, deleteDetail;


        public ProviderPendingViewHolder(@NonNull View itemView)
        {
            super(itemView);
            title = itemView.findViewById(R.id.detail_title);
            price = itemView.findViewById(R.id.pending_detail_price_textView);
            description = itemView.findViewById(R.id.detail_description);
            time = itemView.findViewById(R.id.pending_detail_execution_time_textView);
            deleteDetail = itemView.findViewById(R.id.delete_detail_button);
        }
    }

    private void addServiceDetail(String basicT, Long category, String title, String description, double price, int time)
    {
        Map<String, String> map = new HashMap<>();
        map.put("title", title);
        map.put("description", description);
        map.put("price", String.valueOf(price));
        map.put("executionTimeMin", String.valueOf(time));
        retrofitService.addDetailsToService(basicT, category, map)
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
                        Log.e("ERROR ADD DETAIL", " " + e.getMessage());
                    }

                    @Override
                    public void onComplete()
                    {
                        notifyItemInserted(serviceDetailsList.size()+1);
                        notifyDataSetChanged();
                    }
                });
    }
}
