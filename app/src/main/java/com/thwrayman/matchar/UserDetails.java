package com.thwrayman.matchar;

/**
 * Created by emmet on 01/12/2014.
 */
public class UserDetails {

    int _id;
    String age;
    String fname;
    String lname;
    String _email;
    String job;
    String _password;

    public UserDetails(){

    }

    public UserDetails(int id,String age, String fname, String lname, String email, String job, String password){
        this._id = id;
        this.age = age;
        this.fname = fname;
        this.lname = lname;
        this._email = email;
        this._password = password;
        this.job = job;

    }

    public UserDetails(String age, String fname, String lname, String email, String job, String password){
        this.age=age;
        this.fname = fname;
        this.lname = lname;
        this._email = email;
        this._password = password;
        this.job = job;

    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public int getID(){
        return this._id;
    }

    // setting id
    public void setID(int id){
        this._id = id;
    }

    //first name
    public String getFname(){
        return this.fname;
    }

    public void setFname(String fname){
        this.fname = fname;
    }


    //last name
    public String getLname(){
        return this.lname;
    }

    public void setLname(String lname){
        this.lname = lname;
    }


    //email

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_email() {
        return _email;
    }

    //password
    public void set_password(String _password) {
        this._password = _password;
    }

    public String get_password() {
        return _password;
    }



    //job
    public void setJob(String job) {
        this.job = job;
    }

    public String getJob() {
        return job;
    }
}
