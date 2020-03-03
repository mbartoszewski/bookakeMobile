/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.workshop.Models;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * @author mbart
 */
public class User
{
    private Long userId;
    private String name;
    private String password;
    private int enabled = 1;
    private RoleEnum role;
    private Address userAddress;
    private Contact userContact;
    private Service service;
    private Set<ServiceOrder> serviceOrder = new LinkedHashSet();
    
    public User(){};

    public User(User user)
    {
        this.name = user.name;
        this.password = user.password;
        this.role = user.role;
        this.userAddress = user.userAddress;
        this.userContact = user.userContact;
        this.service = user.service;
        this.serviceOrder = user.serviceOrder;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getEnabled()
    {
        return enabled;
    }

    public void setEnabled(int enabled)
    {
        this.enabled = enabled;
    }

    public RoleEnum getRole()
    {
        return role;
    }

    public void setRole(RoleEnum role)
    {
        this.role = role;
    }

    public Address getUserAddress()
    {
        return userAddress;
    }

    public void setUserAddress(Address userAddress)
    {
        this.userAddress = userAddress;
    }

    public Contact getUserContact()
    {
        return userContact;
    }

    public void setUserContact(Contact userContact)
    {
        this.userContact = userContact;
    }

    public Service getService()
    {
        return service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }

    public Set<ServiceOrder> getServiceOrder()
    {
        return serviceOrder;
    }

    public void setServiceOrder(Set<ServiceOrder> serviceOrder)
    {
        this.serviceOrder = serviceOrder;
    }

    public void addServiceOrder(ServiceOrder sOrder)
    {
        this.serviceOrder.add(sOrder);
        sOrder.setUser(this);
    }
    
    public void removeServiceOrder(ServiceOrder sOrder)
    {
        this.serviceOrder.remove(sOrder);
        sOrder.setUser(null);
    }

    
}
