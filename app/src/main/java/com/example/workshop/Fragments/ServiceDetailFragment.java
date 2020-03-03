package com.example.workshop.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Adapters.ReservationTimeAdapter;
import com.example.workshop.Adapters.ServiceDetailAdapter;
import com.example.workshop.Models.Category;
import com.example.workshop.Models.ResponseMessage;
import com.example.workshop.Models.ServiceDetail;
import com.example.workshop.Models.ServiceOrder;
import com.example.workshop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.workshop.Fragments.HomePageFragment.categorySpinnerList;
import static com.example.workshop.Fragments.HomePageFragment.serviceToDetail;
import static com.example.workshop.MainActivity.basicT;
import static com.example.workshop.MainActivity.isConnected;
import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.role;
import static com.example.workshop.MainActivity.serviceId;

public class ServiceDetailFragment extends Fragment implements View.OnClickListener
{
    private RecyclerView serviceDetailRecyclerView, reservationTimeRecyclerView;
    private ServiceDetailAdapter serviceDetailAndPending;
    private ReservationTimeAdapter reservationTimeAdapter;
    private HashMap<LocalDateTime, LocalDateTime> reservationDateTimeMap;
    private List<LocalTime> reservationTimeList;
    private List<ServiceDetail> serviceDetailsList;
    private List<ServiceOrder> serviceOrdersList;
    private int viewInt;
    private TextView serviceName, serviceDescription, serviceAddress, detailPrice, detailTime, detailName, dayFull;
    private Button servicePhone, serviceEmail, serviceAppointment;
    private ImageButton editService;
    private ConstraintLayout infoConstraint;
    private View reservationLayout;
    private CalendarView calendarView;
    private ServiceDetail selectedServiceDetail;
    private FloatingActionButton addDetailsToServiceFAB;
    private TextInputEditText orderAdditionalInfo;
    private LocalDate selectedLocalDate;
    private LocalTime selectedLocalTime;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.service_detail_fragment, container, false);
        serviceDetailRecyclerView = rootView.findViewById(R.id.service_detail_recyclerView);
        reservationTimeRecyclerView = rootView.findViewById(R.id.reservation_time_recyclerView);
        serviceName = rootView.findViewById(R.id.service_info_name);
        serviceDescription = rootView.findViewById(R.id.service_info_description);
        serviceAddress = rootView.findViewById(R.id.service_info_address);
        servicePhone = rootView.findViewById(R.id.service_info_phone_button);
        serviceEmail = rootView.findViewById(R.id.service_info_email_button);
        infoConstraint = rootView.findViewById(R.id.service_info_constraint);
        reservationLayout =rootView.findViewById(R.id.reservation_layout);
        detailName = rootView.findViewById(R.id.detail_title);
        detailPrice = rootView.findViewById(R.id.pending_detail_price_textView);
        detailTime = rootView.findViewById(R.id.pending_detail_execution_time_textView);
        editService = rootView.findViewById(R.id.edit_service_button);
        calendarView = rootView.findViewById(R.id.service_detail_calendarView);
        serviceAppointment = rootView.findViewById(R.id.service_appointment_button);
        addDetailsToServiceFAB = rootView.findViewById(R.id.add_details_service_fab);
        orderAdditionalInfo = rootView.findViewById(R.id.additional_info_editText);
        dayFull = rootView.findViewById(R.id.day_full_textView);
        serviceDetailsList = new ArrayList<>();
        serviceOrdersList = new ArrayList<>();
        reservationTimeList = new ArrayList<>();
        reservationDateTimeMap = new HashMap<>();

        if((role.equals("[ROLE_PROVIDER]") && serviceId.equals(serviceToDetail.getServiceId())) || role.equals("[ROLE_ADMIN]"))
        {
            editService.setVisibility(View.VISIBLE);
            addDetailsToServiceFAB.show();
        }
        else
        {
            editService.setVisibility(View.GONE);
            addDetailsToServiceFAB.hide();
        }
        serviceName.setText(serviceToDetail.getName());
        serviceDescription.setText(serviceToDetail.getDescription());
        serviceAddress.setText(serviceToDetail.getServiceAddress().toString());
        serviceEmail.setText(serviceToDetail.getServiceContact().getEmail());
        servicePhone.setText(serviceToDetail.getServiceContact().getMobile());
        Long service = getArguments().getLong("service");
        viewInt = getArguments().getInt("viewInt");
        serviceEmail.setOnClickListener(this);
        servicePhone.setOnClickListener(this);
        serviceAppointment.setOnClickListener(this);
        addDetailsToServiceFAB.setOnClickListener(this);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) ->
        {
            selectedLocalDate = LocalDate.of(year, month+1, dayOfMonth);
            if (!selectedLocalDate.isBefore(LocalDate.now()))
            {
                getServiceOrders(service, selectedLocalDate);
            }
            else
            {
                reservationTimeList.clear();
                reservationTimeAdapter.notifyDataSetChanged();
            }
        });
        getServiceDetails(service);

        populateServiceDetailRecyclerView(getContext(), serviceDetailsList, serviceOrdersList, categorySpinnerList, viewInt);
        populateReservationTimeRecyclerView();
        return rootView;
    }

    private void populateServiceDetailRecyclerView(Context context, List<ServiceDetail> serviceDetailsList, List<ServiceOrder> serviceOrdersList,Map<String, Long> categorySpinnerList, int viewInt)
    {
        serviceDetailAndPending = new ServiceDetailAdapter(context,serviceDetailsList, serviceOrdersList,categorySpinnerList, viewInt, new ServiceDetailAdapter.onClickListenerInterface()
        {
            @Override
            public void onDetailClick(Long serviceDetail)
            {
                if (serviceDetail !=null)
                {
                    selectedServiceDetail = serviceDetailsList.stream()
                            .filter(sD -> sD.getServiceDetailId().equals(serviceDetail))
                            .findAny()
                            .orElse(null);

                    detailTime.setText(selectedServiceDetail.getExecutionTimeMin() + " min");
                    detailPrice.setText(selectedServiceDetail.getPrice() + " PLN");
                    detailName.setText(selectedServiceDetail.getTitle());
                    serviceDetailRecyclerView.setVisibility(View.GONE);
                    infoConstraint.setVisibility(View.GONE);
                    reservationLayout.setVisibility(View.VISIBLE);
                    reservationTimeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onDeleteDetailClick(Long serviceDetail, int position)
            {
                if (serviceDetail != -1L)
                {
                    deleteServiceDetailById(serviceDetail, position);
                }
                else
                {
                    serviceDetailsList.remove(position);
                    serviceDetailAndPending.notifyItemRemoved(position);
                    Snackbar.make(getActivity().findViewById(R.id.fragment_container), R.string.detail_deleted, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        serviceDetailRecyclerView.setLayoutManager(layoutManager);
        serviceDetailRecyclerView.setHasFixedSize(true);
        serviceDetailRecyclerView.setAdapter(serviceDetailAndPending);
    }

    private void populateReservationTimeRecyclerView()
    {
        reservationTimeAdapter = new ReservationTimeAdapter(reservationTimeList, localTime -> selectedLocalTime = localTime);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        reservationTimeRecyclerView.setLayoutManager(linearLayoutManager);
        reservationTimeRecyclerView.setHasFixedSize(true);
        reservationTimeRecyclerView.setAdapter(reservationTimeAdapter);
    }

    private void getServiceOrders(Long service, LocalDate localDate)
    {
        retrofitService.getServiceOrderByServiceDate(service, localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ServiceOrder>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        reservationDateTimeMap.clear();
                    }

                    @Override
                    public void onNext(List<ServiceOrder> serviceOrderList)
                    {
                        for(ServiceOrder serviceOrder : serviceOrderList)
                        {
                            reservationDateTimeMap.put(serviceOrder.getServiceStartDate(), serviceOrder.getServiceEndDate());
                        }
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e("SERVICEORDERs", " ERROR" + e.getMessage());
                    }

                    @Override
                    public void onComplete()
                    {
                        fillTimeList(LocalDateTime.now(), reservationTimeList, selectedServiceDetail.getExecutionTimeMin(), localDate.getYear(),localDate.getMonthValue(), localDate.getDayOfMonth(), reservationDateTimeMap);
                    }
                });
    }
    private void deleteServiceDetailById(Long serviceDetail, int position)
    {
        retrofitService.deleteServiceDetail(basicT, serviceDetail)
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
                        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(R.id.fragment_container), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete()
                    {
                        serviceDetailsList.remove(position);
                        serviceDetailAndPending.notifyItemRemoved(position);
                        Snackbar.make(getActivity().findViewById(R.id.fragment_container), R.string.detail_deleted, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }
    private void getServiceDetails(Long service)
    {
        retrofitService.getServiceDetail(service)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<com.example.workshop.Models.ServiceDetail>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                    }

                    @Override
                    public void onNext(List<com.example.workshop.Models.ServiceDetail> sDetails)
                    {
                        serviceDetailsList.addAll(sDetails);
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                    }

                    @Override
                    public void onComplete()
                    {
                        serviceDetailAndPending.notifyDataSetChanged();
                    }
                });
    }

    private void addServiceOrder(String basicT, Long serviceDetail, String startDateTime,String endDateTime, String description)
    {
        if (isConnected)
        {
            Map<String, String> map = new HashMap<>();
            map.put("serviceStartDate", startDateTime);
            map.put("serviceEndDate", endDateTime);
            map.put("description", description);
            retrofitService.addServiceOrderToUser(basicT, serviceDetail, map)
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
                            Log.e("Add Service Order", "Error " + e.getMessage());
                        }

                        @Override
                        public void onComplete()
                        {
                            if (Objects.requireNonNull(getFragmentManager()).getBackStackEntryCount() > 0)
                            {
                                getFragmentManager().popBackStack(null,
                                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                Snackbar.make(getActivity().findViewById(R.id.fragment_container), R.string.order_added, Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Snackbar.make(getActivity().findViewById(R.id.fragment_container),"Zaloguj lub zarejestru się by móc rezerwować wizytę.", Snackbar.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.service_info_phone_button:
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + servicePhone.getText()));
                startActivity(callIntent);
                break;
            case R.id.service_info_email_button:
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mail to:" + serviceEmail.getText()));
                emailIntent.setType("text/plain");
                startActivity(emailIntent);
                break;
            case R.id.service_appointment_button:
                if (selectedLocalTime != null && selectedLocalDate != null)
                {
                    String startDateTime = selectedLocalDate.toString() + " " + selectedLocalTime.toString();
                    String endDateTime = selectedLocalDate.toString()+ " " + selectedLocalTime.plusMinutes(selectedServiceDetail.getExecutionTimeMin()).toString();
                    addServiceOrder(basicT, selectedServiceDetail.getServiceDetailId(), startDateTime, endDateTime, Objects.requireNonNull(orderAdditionalInfo.getText()).toString());
                }
                break;
            case R.id.add_details_service_fab:
                serviceDetailsList.add(new ServiceDetail(-1L,"tytul", "opis", 5.5, 9, new Category(0L, "Edytuj")));
                serviceDetailAndPending.notifyItemInserted(0);
                serviceDetailAndPending.notifyDataSetChanged();
                break;
        }
    }

    private void fillTimeList(LocalDateTime localDateTime, List<LocalTime> reservationTimeList, int serviceExecutionTime, int year, int month, int day, HashMap<LocalDateTime, LocalDateTime> reservationDateTimeMap)
    {
        reservationTimeList.clear();
        int delayMin = 15;
        int loops;
        int minute = localDateTime.getMinute();
        LocalTime time;
        LocalDate lDate = LocalDate.of(year,month,day);
        if(lDate.isEqual(localDateTime.toLocalDate()) && localDateTime.getHour() <= 15 && localDateTime.getHour() >= 8)
        {
            if (minute > 45)
            {
                localDateTime = localDateTime.plusHours(1L).withMinute(0);
            }
            else
            {
                localDateTime = localDateTime.withMinute(minute < 30 ? minute < 15 ? 15 : 30 : 45);
            }
            time = LocalTime.of(localDateTime.getHour(),localDateTime.getMinute()).plusMinutes(30);
            loops = ( ((int) Duration.between(time, LocalTime.of(16,0)).toMinutes() - serviceExecutionTime) / delayMin);
        }
        else if (lDate.isBefore(localDateTime.toLocalDate()) || (lDate.isEqual(localDateTime.toLocalDate()) && localDateTime.getHour() >= 16))
        {
            Snackbar.make(getActivity().findViewById(R.id.fragment_container), R.string.reservation_unavailable_snackabar, Snackbar.LENGTH_SHORT).show();
            time = LocalTime.of(8,0);
            loops = 0;
        }
        else
        {
            time = LocalTime.of(8,0);
            loops = (((int) Duration.ofHours(8).toMinutes() - serviceExecutionTime) / delayMin);
        }

        for( int i = 1; i <= loops; i ++ )
        {
            reservationTimeList.add(time);
            time = time.plusMinutes(delayMin);
        }

        reservationDateTimeMap.forEach((k,v)->{
            for( int j = 0 ; j < reservationTimeList.size(); j++)
            {
                if (reservationTimeList.get(j).isAfter(k.toLocalTime().minusMinutes(serviceExecutionTime)) && reservationTimeList.get(j).isBefore(v.toLocalTime()))
                {
                    reservationTimeList.remove(j);
                    j--;
                }
            }
        });
        if (reservationTimeList.size() > 0)
        {
            dayFull.setVisibility(View.GONE);
            reservationTimeAdapter.notifyDataSetChanged();
            reservationTimeRecyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            reservationTimeRecyclerView.setVisibility(View.GONE);
            dayFull.setVisibility(View.VISIBLE);
        }

    }
}
