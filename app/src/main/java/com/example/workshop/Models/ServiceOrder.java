/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.workshop.Models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author mbart
 */
public class ServiceOrder
{
    private Long serviceOrderId;
    private String serviceStartDate;
    private String serviceEndDate;
    private String description;
    private String orderStatus;
    private ServiceDetail serviceDetail;
    private User user;
    private Category category;

    public ServiceOrder()
    {
    }

    public ServiceOrder(String serviceStartDate, String serviceEndDate, String description, ServiceDetail serviceDetail, User user, Category category)
    {
        this.serviceStartDate = serviceStartDate;
        this.serviceEndDate = serviceEndDate;
        this.description = description;
        this.serviceDetail = serviceDetail;
        this.user = user;
        this.category = category;
    }

    public Long getServiceOrderId()
    {
        return serviceOrderId;
    }

    public void setServiceOrderId(Long serviceOrderId)
    {
        this.serviceOrderId = serviceOrderId;
    }

    public LocalDateTime getServiceStartDate()
    {
        return stringToLDT(serviceStartDate);
    }
/*
    public void setServiceStartDate(LocalDateTime serviceStartDate)
    {
        this.serviceStartDate = serviceStartDate;
    }
*/
    public LocalDateTime getServiceEndDate()
    {
        return stringToLDT(serviceEndDate);
    }
/*
    public void setServiceEndDate(LocalDateTime serviceEndDate)
    {
        this.serviceEndDate = serviceEndDate;
    }*/

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public ServiceDetail getServiceDetail()
    {
        return serviceDetail;
    }

    public void setServiceDetail(ServiceDetail serviceDetail)
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

    private LocalDateTime stringToLDT(String dateTime)
    {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, dateTimeFormatter);
    }
}
