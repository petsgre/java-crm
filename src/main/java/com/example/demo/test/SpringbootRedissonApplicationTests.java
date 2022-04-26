package com.example.demo.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class SpringbootRedissonApplicationTests {
    public static void main(String[] args) {
        HashMap<String, String> xxx = new HashMap<String, String>();
        xxx.put("name", "zx");
        xxx.put("age", "123");
        Set<String> keys = xxx.keySet();
        for (String k : keys) {
            System.out.println(k);
            System.out.println(xxx.get(k));
        }

        ArrayList<HashMap<String, String>> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> o = new HashMap<String, String>();
            o.put("name", "zx");
            list.add(o);
        }
        for (HashMap<String, String> mmm : list) {
            System.out.println(list.indexOf(mmm));
        }
        list.forEach(item -> {
            System.out.println(item.get("name"));
        });
        System.out.println(list.toArray());
    }
}
