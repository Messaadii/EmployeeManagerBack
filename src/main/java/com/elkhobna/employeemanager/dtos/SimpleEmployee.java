package com.elkhobna.employeemanager.dtos;

public class SimpleEmployee {

    private Long id;
    private String cin;
    private String name;
    private String familyName;

    public SimpleEmployee(Long id, String cin, String name, String familyName) {
        this.id = id;
        this.cin = cin;
        this.name = name;
        this.familyName = familyName;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
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

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }
}
