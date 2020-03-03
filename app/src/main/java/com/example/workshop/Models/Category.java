package com.example.workshop.Models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


public class Category
{
    private Long categoryId;
    private Long parentCategoryId;
    private String name;
    private List<Service> service = new ArrayList<>();
    private List<ServiceDetail> serviceDetail = new ArrayList<>();
    private List<ServiceOrder> serviceOrder = new ArrayList<>();


    public Category(Long parentCategoryId, String name)
    {
        this.parentCategoryId = parentCategoryId;
        this.name = name;
    }

    public Long getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(Long categoryId)
    {
        this.categoryId = categoryId;
    }

    public Long getParentCategoryId()
    {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId)
    {
        this.parentCategoryId = parentCategoryId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<Service> getService()
    {
        return service;
    }

    public void setService(List<Service> service)
    {
        this.service = service;
    }

    public List<ServiceDetail> getServiceDetail()
    {
        return serviceDetail;
    }

    public void setServiceDetail(List<ServiceDetail> serviceDetail)
    {
        this.serviceDetail = serviceDetail;
    }

    public List<ServiceOrder> getServiceOrder()
    {
        return serviceOrder;
    }

    public void setServiceOrder(List<ServiceOrder> serviceOrder)
    {
        this.serviceOrder = serviceOrder;
    }


}
