package com.example.felixcity.felixcitymall.Model;

public class Users {
    private String Name,Phone,Password ;

    public Users(){}

    public Users(String Name, String Phone, String Password) {
        this.Name = Name;
        this.Phone = Phone;
        this.Password = Password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }
}
