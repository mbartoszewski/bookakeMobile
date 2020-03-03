package com.example.workshop.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Adapters.PendingAdapter;
import com.example.workshop.Models.ServiceOrder;
import com.example.workshop.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.workshop.MainActivity.basicT;
import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.role;

public class PendingFragment extends Fragment
{
    RecyclerView pendingRecyclerView;
    MaterialCalendarView calendarView;
    PendingAdapter pendingAdapter;
    List<ServiceOrder> pendingOrders;
    List<String> pendingStatusList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.service_pending_fragment, container, false);
        pendingRecyclerView = rootView.findViewById(R.id.pending_recyclerView);
        calendarView = rootView.findViewById(R.id.pending_calendarView);
        pendingOrders = new ArrayList<>();
        pendingStatusList = new ArrayList<>();
        fillStatusList(pendingStatusList);

        populatePendingRecyclerView(pendingStatusList, pendingOrders, pendingRecyclerView);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener()
        {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected)
            {
                if (role.equals("[ROLE_PROVIDER]"))
                {
                    getServiceOrderByServiceDateAndProvidername(basicT, date.getYear(), date.getMonth(), date.getDay());
                }
                if (role.equals("[ROLE_USER]"))
                {
                    getServiceOrderByServiceUsernameAndDate(basicT, date.getYear(), date.getMonth(), date.getDay());
                }
            }
        });
        calendarView.setOnMonthChangedListener(new OnMonthChangedListener()
        {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date)
            {
                if (role.equals("[ROLE_PROVIDER]"))
                {
                    getServiceOrderByServiceDateAndProvidername(basicT, date.getYear(), date.getMonth(), null);
                }
                if (role.equals("[ROLE_USER]"))
                {
                    getServiceOrderByServiceUsernameAndDate(basicT, date.getYear(), date.getMonth(), null);
                }
            }
        });
        return rootView;
    }
    private void fillStatusList(List<String> pendingStatusList)
    {
        if (pendingStatusList.size() ==0)
        {
            pendingStatusList.add("canceled");
            pendingStatusList.add("confirmed");
            pendingStatusList.add("unconfirmed");
            pendingStatusList.add("pending");
            pendingStatusList.add("end");
        }
    }
    private void populatePendingRecyclerView(List<String> pendingStatusList, List<ServiceOrder> pendingOrders, RecyclerView pendingRecyclerView)
    {
       pendingAdapter = new PendingAdapter(getContext(), pendingOrders, pendingStatusList);
        RecyclerView.LayoutManager rVLM = new LinearLayoutManager(getContext());
        pendingRecyclerView.setLayoutManager(rVLM);
        pendingRecyclerView.setHasFixedSize(false);
        pendingRecyclerView.setAdapter(pendingAdapter);
    }

    private void getServiceOrderByServiceUsernameAndDate(String basicT, int year, int month, Integer day)
    {
        retrofitService.getServiceOrderByServiceUsernameAndDate(basicT, year, month, day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ServiceOrder>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        pendingOrders.clear();
                    }

                    @Override
                    public void onNext(List<ServiceOrder> serviceOrderList)
                    {
                        Log.e("asdads", " " + serviceOrderList.size());
                        pendingOrders.addAll(serviceOrderList);
                        pendingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });

    }
    private void getServiceOrderByServiceDateAndProvidername(String basicT, int year, int month, Integer day)
    {
        retrofitService.getServiceOrderByServiceDateAndProvidername(basicT, year, month, day)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ServiceOrder>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {
                        pendingOrders.clear();
                    }

                    @Override
                    public void onNext(List<ServiceOrder> serviceOrderList)
                    {
                        pendingOrders.addAll(serviceOrderList);
                        pendingAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e)
                    {

                    }

                    @Override
                    public void onComplete()
                    {

                    }
                });

    }
}
