package com.android.ukmproject.model;

/**
 * Created by Osama sh on 5/8/2016.
 */
public class Item {

    private String id, name, mobile, hours, dishes , address, location, photopath ;

    public Item() { }

    public Item(String name, String photopath, String mobile) {
        this.name = name;
        this.photopath = photopath;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    //
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String m) {
        this.mobile = m;
    }
    //
    public String getHours() {
        return hours;
    }

    public void setHours(String h) {
        this.hours = h;
    }
    //
    public String getDishes() { return dishes;   }

    public void setDishes(String d) {
        this.dishes = d;
    }
    //
    public String getAddress() {
        return address;
    }

    public void setAddress(String a) {
        this.address = a;
    }
    //
    public String getLocation() {
        return location;
    }

    public void setLocation(String l) { this.location = l;}
    //
    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String p) {
        this.photopath = p;
    }


}
