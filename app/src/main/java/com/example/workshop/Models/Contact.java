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
public class Contact
{
    private Long contactId;
    private String email;
    private String mobile;
    private User user;
    private Service service;
    
    public Contact(){};
    public Contact(String email, String mobile, User user, Service service)
    {
        this.email = email;
        this.mobile = mobile;
        this.user = user;
        this.service = service;
    }

    public Long getContactId()
    {
        return contactId;
    }

    public void setContactId(Long contactId)
    {
        this.contactId = contactId;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        this.mobile = mobile;
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

    @Override
    public String toString()
    {
        return "email:" + email + '\n' +
                "mobile:" + mobile + '\n';
    }
}
