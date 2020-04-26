package com.example.retrofit_file_upload_test1;

class User {
    private int id;
    private String name;
    private String email;
    private int age;
    private String subject;

    public User(String name,String email, int age, String subject) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.subject = subject;
    }
    public Integer getId(){
        return id;
    }
}
