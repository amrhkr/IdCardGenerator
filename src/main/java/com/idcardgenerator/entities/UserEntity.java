package com.idcardgenerator.entities;

//import java.util.Arrays;

public class UserEntity{
    private int id;
    private String name;
    private String position;
    private String phone;
    private String email;
    //to save the photo data as byte array
//	@Lob
    private byte[] photo;

    //Default Constructor --- we do not need to create it explicitly---
    public UserEntity() {
        super();
        // TODO Auto-generated constructor stub
    }

    //parameterized constructor --creation needed
    public UserEntity(int id, String name, String position, String phone, String email, byte[] photo) {
        super();
        this.id = id;
        this.name = name;
        this.position = position;
        this.phone = phone;
        this.email = email;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    //ToString method overridden to view the object properly
    @Override
    public String toString() {
        return "UserEntity [id=" + id + ", name=" + name + ", position=" + position + ", phone=" + phone + ", email="
                + email + "]";
    }
}
