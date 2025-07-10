package com.microservice.foodcourt.domain.model;

public class RestaurantModel {

    private Long id;
    private String name;
    private String address;
    private Long ownerId;
    private String phoneNumber;
    private String urlLogo;
    private String nit;

    public RestaurantModel() {}

    public RestaurantModel(Long id, String name, String address, Long ownerId, String phoneNumber, String urlLogo, String nit) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.ownerId = ownerId;
        this.phoneNumber = phoneNumber;
        this.urlLogo = urlLogo;
        this.nit = nit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
}
