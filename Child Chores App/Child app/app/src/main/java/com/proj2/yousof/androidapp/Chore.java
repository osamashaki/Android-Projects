package com.proj2.yousof.androidapp;

public class Chore {

    private String choreid;
    private String choretitle;
    private String choredate;
    private String choreno;

    public void setchid(String chid){this.choreid=chid;}
    public void setchtitle(String t){this.choretitle=t;}
    public void setchd(String d){this.choredate=d ;}
    public void setchno(String no){this.choreno=no;}

    public String getchid(){return choreid;}
    public String getchtitle(){return choretitle;}
    public String getchd(){return choredate;}
    public String getchno(){return choreno;}

}
