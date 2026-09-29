package com.ems.model;

public abstract class Employee {

    private final String id;
    private final String name;
    private final String email;
    private final String phone;

    protected Employee(String id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public abstract String getEmployeeType();

    public String getDetails() {
        return String.format(
                "ID: %s | Name: %s | Type: %s | Email: %s | Phone: %s",
                id, name, getEmployeeType(), email, phone
        );
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
