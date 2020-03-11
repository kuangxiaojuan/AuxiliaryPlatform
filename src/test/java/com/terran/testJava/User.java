package com.terran.testJava;

import java.util.Objects;

public class User {
    private String beanName;
    private String methodName;
    public User(){}
    public User(String beanName,String methodName){
        this.beanName = beanName;
        this.methodName = methodName;
    }
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null || this.getClass() != obj.getClass()) return false;
        User user = (User)obj;
        if(Objects.equals(this.beanName,user.beanName) && Objects.equals(this.methodName,user.methodName))return true;
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
