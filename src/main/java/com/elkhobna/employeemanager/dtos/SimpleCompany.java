package com.elkhobna.employeemanager.dtos;

public class SimpleCompany {
    private String name;

    public SimpleCompany(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
