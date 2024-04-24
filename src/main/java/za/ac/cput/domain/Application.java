package za.ac.cput.domain;

import java.util.Date;
import java.util.Objects;

/*
 * Author : Sabelo Kama (219149364)
 * Date : 22 April 2024
 * */


public class Application {
    enum Status{
         Pending, Accepted, Rejected;
    }
    protected String appNo;
    protected Date date;
    protected Status status;

    public Application(){}


    public String getAppNo() {
        return appNo;
    }

    public Date getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "appNo='" + appNo + '\'' +
                ", date=" + date +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return Objects.equals(appNo, that.appNo) && Objects.equals(date, that.date) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(appNo, date, status);
    }
}