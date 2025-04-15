package com.elkhobna.employeemanager.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CountByDate {
    private LocalDate date;
    private List<CompanyByCount> counts;

    public CountByDate(LocalDate date) {
        this.date = date;
        this.counts = new ArrayList<>();
    }

    public void addCompany(CompanyByCount companyByCount){
        counts.add(companyByCount);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<CompanyByCount> getCounts() {
        return counts;
    }

    public void setCounts(List<CompanyByCount> counts) {
        this.counts = counts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof CountByDate that)){
            return false;
        }
        return date.equals(that.getDate());
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }
}