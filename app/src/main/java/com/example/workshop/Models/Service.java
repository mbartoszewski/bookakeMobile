/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.workshop.Models;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mbart
 */
public class Service
{
    private Long serviceId;
    private String name;
    private String description;
    private Address serviceAddress;
    private Contact serviceContact;
    private List<ServiceDetail> serviceDetail = new ArrayList<>();
    private User user;
    private Category category;
    
    public Service(){};

    public Service(String name, String description, Address serviceAddress, Contact serviceContact, User user, Category category)
    {
        this.name = name;
        this.description = description;
        this.serviceAddress = serviceAddress;
        this.serviceContact = serviceContact;
        this.user = user;
        this.category = category;
    }

    public Long getServiceId()
    {
        return serviceId;
    }

    public void setServiceId(Long serviceId)
    {
        this.serviceId = serviceId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Address getServiceAddress()
    {
        return serviceAddress;
    }

    public void setServiceAddress(Address serviceAddress)
    {
        this.serviceAddress = serviceAddress;
    }

    public Contact getServiceContact()
    {
        return serviceContact;
    }

    public void setServiceContact(Contact serviceContact)
    {
        this.serviceContact = serviceContact;
    }

    public List<ServiceDetail> getServiceDetail()
    {
        return serviceDetail;
    }

    public void setServiceDetail(List<ServiceDetail> serviceDetail)
    {
        this.serviceDetail = serviceDetail;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }
   
    public void addServiceDetail(ServiceDetail sD)
    {
        this.serviceDetail.add(sD);
        sD.setService(this);
    }
    public void removeServiceDetail(ServiceDetail sD)
    {
        this.serviceDetail.remove(sD);
        sD.setService(null);
    }

}
