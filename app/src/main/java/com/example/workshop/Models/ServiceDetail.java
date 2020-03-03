/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.workshop.Models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mbart
 */
public class ServiceDetail
{
    private Long serviceDetailId;
    private String title;
    private String description;
    private double price;
    private int executionTimeMin;
    private Category category;
    private List<ServiceOrder> serviceOrder = new ArrayList<>();
    private Service service;
    
    
    public ServiceDetail(){};

    public ServiceDetail(Long serviceDetailId, String title, String description, double price, int executionTimeMin, Category category)
    {
        this.serviceDetailId = serviceDetailId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.executionTimeMin = executionTimeMin;
        this.category = category;
    }

    public Long getServiceDetailId()
    {
        return serviceDetailId;
    }

    public void setServiceDetailId(Long serviceDetailId)
    {
        this.serviceDetailId = serviceDetailId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getPrice()
    {
        return price;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public int getExecutionTimeMin()
    {
        return executionTimeMin;
    }

    public void setExecutionTimeMin(int executionTimeMin)
    {
        this.executionTimeMin = executionTimeMin;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public List<ServiceOrder> getServiceOrder()
    {
        return serviceOrder;
    }

    public void setServiceOrder(List<ServiceOrder> serviceOrder)
    {
        this.serviceOrder = serviceOrder;
    }

    public Service getService()
    {
        return service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }

    public void addServiceOrder(ServiceOrder sOrder)
    {
        this.serviceOrder.add(sOrder);
        sOrder.setServiceDetail(this);
    }
    
    public void removeServiceOrder(ServiceOrder sOrder)
    {
        this.serviceOrder.remove(sOrder);
        sOrder.setServiceDetail(null);
    }


    
}
