package com.xing.middleware.framework.elasticx.client.model;

import java.util.Date;

/**
 * Created by Jecceca on 2017/8/28.
 */
public class Person {
    private String name;
    private int age;
    private Date birthDay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}