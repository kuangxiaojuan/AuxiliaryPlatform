package com.terran.testJava;

import com.terran.scheduled.api.config.ScheduledTask;
import com.terran.scheduled.api.config.SchedulingRunnable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Test {
    public static void main(String[] args) throws Exception {
        //classDemo1();
        classDemo();
    }
    private static void classDemo() throws Exception{
        String[] txt = new String[]{"1","2","3","4"};
        SchedulingRunnable task1 = new SchedulingRunnable("testMethod","test1",txt);
        SchedulingRunnable task2 = new SchedulingRunnable("testMethod","test1",txt);
        Map<Runnable,String> map = new ConcurrentHashMap<>();
        map.put(task1,"1");
        String a = map.remove(task2);
        System.out.println(a);
        System.out.println(map.get(task2));
    }
    private static void classDemo2() throws Exception{
        User u = new User("a","b");
        User u2 = new User("a","b");
        Map<User,String> map = new ConcurrentHashMap<>();
        map.put(u,"1");
        System.out.println(map.get(u2));
        System.out.println(u.equals(u2));
    }
    private static void classDemo1() throws Exception{
        String str1 = "";
        //通过下面三种方法，可以获取对应的字节码
        String str2 = "abc";//通过系统加载器加载的
        User user = new User();//通过用户加载器加载的
        Class cls1 = str1.getClass();
        Class cls2 = String.class;
        Class cls3 = Class.forName("java.lang.String");
        Class cls4 = Class.forName("com.terran.testJava.User");
        System.out.println(cls1 == cls2);
        System.out.println(cls1 == cls3);
        System.out.println(cls4);
        //这个方法就是cls1字节码的对象
        Object c = cls1.newInstance();
        System.out.println(c);
    }

}
