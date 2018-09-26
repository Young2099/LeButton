package com.lanfeng.young.lebutton;

import org.junit.Test;

import java.util.HashMap;

/**
 * Created by yf on 2018/9/25.
 */
public class HashMapTest {
    @Test
    public void testTraversal() {
        HashMap map = new HashMap(16);
        map.put(7, "");
        map.put(11, "");
        map.put(43, "");
        map.put(59, "");
        map.put(19, "");
        map.put(3, "");
        map.put(35, "");

        System.out.println("遍历结果：");
        for (Object key : map.keySet()) {
            System.out.print(key + " -> ");
        }
    }
}
