/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.workshop.Models;

/**
 *
 * @author mbart
 */
public class Address
{
    private Long addressId;
    private String city;
    private String state;
    private String street;
    private String zip;
    private User user;
    private Service service;
    
    public Address(){};
    public Address(String city, String state, String street, String zip) 
    {
        this.city = city;
        this.state = state;
        this.street = street;
        this.zip = zip;
    }

    public Long getAddressId()
    {
        return addressId;
    }

    public void setAddressId(Long addressId)
    {
        this.addressId = addressId;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    public String getZip()
    {
        return zip;
    }

    public void setZip(String zip)
    {
        this.zip = zip;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Service getService()
    {
        return service;
    }

    public void setService(Service service)
    {
        this.service = service;
    }
    public String getGeo()
    {
        return "geo:"+ " " + this.zip+ " " + this.street+ " " + this.city;
    }
    @Override
    public String toString()
    {
        return street + ", " + '\n' +
                zip + " " + city ;
    }
}
