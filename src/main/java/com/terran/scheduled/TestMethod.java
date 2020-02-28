package com.terran.scheduled;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class TestMethod {
    public void test1(){
        System.out.println("453454");
    }
    public void test1(String a,String b,String c){
        System.out.println(a);
        System.out.println(b);
        System.out.println(c);
        System.out.println("123654");
    }
}
