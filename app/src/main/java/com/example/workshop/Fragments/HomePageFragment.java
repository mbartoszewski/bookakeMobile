package com.example.workshop.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workshop.Adapters.CategoryAdapter;
import com.example.workshop.Adapters.ServiceAdapter;
import com.example.workshop.Models.Service;
import com.example.workshop.R;
import com.example.workshop.Adapters.ParentCategoryAdapter;
import com.example.workshop.Models.Category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.workshop.MainActivity.retrofitService;
import static com.example.workshop.MainActivity.role;

public class HomePageFragment extends Fragment
{
    private ParentCategoryAdapter parentCategoryAdapter;
    private CategoryAdapter categoryAdapter;
    private ServiceAdapter serviceAdapter;
    //private ConstraintLayout homePageConstraint;
    private List<Category> parentCategoryList, categoryList, categoriesList;
    public static Map<String, Long> categorySpinnerList;
    public static List<Service> serviceList;
    private Button calendarButton;
    private RecyclerView parentCategoryRecyclerView, categoryRecyclerView, serviceRecyclerView;
    public static Service serviceToDetail;
    private Long parentCategoryId = 1L, categoryId;
    private boolean isLoading = false;
    private boolean isEnd = false;
    private static int last =0;
    private SearchView searchView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.home_page_fragment, container, false);
        //homePageConstraint = rootView.findViewById(R.id.home_page_constraint);
        parentCategoryRecyclerView = rootView.findViewById(R.id.parent_category_recyclerView);
        categoryRecyclerView = rootView.findViewById(R.id.category_recyclerView);
        serviceRecyclerView = rootView.findViewById(R.id.services_recyclerView);
        searchView = rootView.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                serviceList.clear();
                getServices(null, null, query);
                Log.e("SEARCHVIEW", " " + query);
               return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
        calendarButton = rootView.findViewById(R.id.calendar_button);
        calendarButton.setOnClickListener((View v) ->
        {
            Fragment pendingFragment = new PendingFragment();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, pendingFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        if (serviceList == null)
        {
            serviceList = new ArrayList<>();
        }
        if (categoryList == null && categoriesList == null && parentCategoryList == null )
        {
            categoryList = new ArrayList<>();
            parentCategoryList = new ArrayList<>();
            categoriesList = new ArrayList<>();
            categoryRecyclerView.setVisibility(View.GONE);
        }
        if (categorySpinnerList  == null)
        {
            categorySpinnerList = new LinkedHashMap<>();
        }

        populateParentCategoryRecyclerView();
        populateCategoryRecyclerView();
        populateServiceRecyclerView();
        getParentCategories(parentCategoryList, categoryList, categoriesList);
        //homePageConstraint.setOnClickListener(v -> categoryRecyclerView.setVisibility(View.GONE));
        return rootView;
    }
    private void populateParentCategoryRecyclerView()
    {
        parentCategoryAdapter = new ParentCategoryAdapter(getContext(), parentCategoryList, parentCategoryId -> {
            serviceList.clear();
            this.parentCategoryId = parentCategoryId;
            this.categoryId = null;
            last = 0;
            getServices(null, parentCategoryId, null);
            categoryList.clear();
            categorySpinnerList.clear();
            categoryList.addAll(categoriesList.stream().filter(c -> c.getParentCategoryId()==parentCategoryId).sorted((o1, o2) -> o1.getName().compareTo(o2.getName())).collect(Collectors.toList()));
            categorySpinnerList.putAll(categoryList.stream().collect(Collectors.toMap(x->x.getName(), x->x.getCategoryId())));
            categoryAdapter.notifyDataSetChanged();
            categoryRecyclerView.setVisibility(View.VISIBLE);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        parentCategoryRecyclerView.setLayoutManager(layoutManager);
        parentCategoryRecyclerView.setHasFixedSize(true);
        parentCategoryRecyclerView.setAdapter(parentCategoryAdapter);
    }
    private void populateCategoryRecyclerView()
    {
        categoryAdapter = new CategoryAdapter(getContext(), categoryList, categoryId ->
        {
            serviceList.clear();
            this.categoryId = categoryId;
            last =0;
            getServices(categoryId, null, null);
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        categoryRecyclerView.setLayoutManager(layoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }
    private void populateServiceRecyclerView()
    {
        serviceAdapter = new ServiceAdapter(getContext(), serviceList, new ServiceAdapter.OnClickListener()
        {
            @Override
            public void buttonRequestClickListener(Long serviceId)
            {
                    Fragment serviceDetailFragment = new ServiceDetailFragment();
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    Bundle args = new Bundle();
                    args.putLong("service", serviceId);
                    args.putInt("viewInt", 1);
                    serviceDetailFragment.setArguments(args);
                    fragmentTransaction.replace(R.id.fragment_container, serviceDetailFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    serviceToDetail = serviceList.stream()
                            .filter(s -> s.getServiceId() == serviceId)
                            .findAny()
                            .orElse(null);
            }

            @Override
            public void addressClickListener(String address)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(address));
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        serviceRecyclerView.setLayoutManager(layoutManager);
        serviceRecyclerView.setHasFixedSize(true);
        serviceRecyclerView.setAdapter(serviceAdapter);
        serviceRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
            {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) serviceRecyclerView.getLayoutManager();
                if (!isLoading)
                {
                    if (linearLayoutManager !=null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == serviceList.size()-2 && !isEnd)
                    {
                        getServices(categoryId, parentCategoryId, null);
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void getParentCategories(List<Category> parentCategoryList, List<Category> categoryList, List<Category> categoriesList)
    {
        if (categoriesList.size() == 0)
        {
            retrofitService.getCategories()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<Category>>()
                    {
                        @Override
                        public void onSubscribe(Disposable d)
                        {
                        }

                        @Override
                        public void onNext(List<Category> categoryData)
                        {

                            categoriesList.addAll(categoryData);
                            for (Category categories : categoryData)
                            {
                                if (categories.getParentCategoryId() == null)
                                {
                                    parentCategoryList.add(categories);
                                } else
                                    categoryList.add(categories);
                            }
                            //parentCategoryList.addAll(categoryData.stream().filter(c -> c.getParentCategoryId() == null).collect(Collectors.toList()));
                            //categoryList.addAll(categoryData.stream().filter(c -> c.getParentCategoryId() != null).collect(Collectors.toList()));
                        }

                        @Override
                        public void onError(Throwable e)
                        {
                            Log.e("Error", "ERROR " + e);
                        }

                        @Override
                        public void onComplete()
                        {
                            parentCategoryAdapter.notifyDataSetChanged();
                            categoryAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    private void getServices(Long categoryId, Long parentCategoryId, String city)
    {
        retrofitService.searchService(categoryId,city, parentCategoryId, last)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Service>>()
                {
                    @Override
                    public void onSubscribe(Disposable d)
                    {

                    }

                    @Override
                    public void onNext(List<Service> serviceData)
                    {
                        serviceList.addAll(serviceData);
                        isEnd = serviceData.isEmpty();
                    }

                    @Override
                    public void onError(Throwable e)
                    {
                        Log.e("Error", "ERROR " + e);
                    }

                    @Override
                    public void onComplete()
                    {
                        last++;
                        serviceAdapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                });
    }

    @Override
    public void onPause()
    {
        Log.e("SELECTED", "OnPause of Fragment");
        super.onPause();
    }

    @Override
    public void onResume()
    {
        if (role.equals("[ROLE_PROVIDER]") || role.equals("[ROLE_USER]"))
        {
            calendarButton.setVisibility(View.VISIBLE);
        }
        else
        {
            calendarButton.setVisibility(View.GONE);
        }
        Log.e("SELECTED", "onResume of Fragment");
        super.onResume();
    }
}
