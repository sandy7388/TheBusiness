package com.example.admin.demo.model;

public class ProductCategory {
    private String serviceName,serviceId;

    public ProductCategory(String serviceName, String serviceId) {
        this.serviceName = serviceName;
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
