package com.legend.liteim.data;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Legend
 * @data by on 19-10-1.
 * @description 用来进行消息同步
 */
public class Blocker {

    private static Map<String, CountDownLatch> map = new ConcurrentHashMap<>();

    public static void put(String flag) {
        map.put(flag, new CountDownLatch(1));
    }

    public static void count(String flag) {
        if (map.containsKey(flag)) {
            Objects.requireNonNull(map.get(flag)).countDown();
            map.remove(flag);
        }
    }

    public static void await(String flag) {
        if (map.containsKey(flag)) {
            try {
                Objects.requireNonNull(map.get(flag)).await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static CountDownLatch getCounter(String flag) {
        return map.get(flag);
    }

    public synchronized static void clear() {
        if (map.size() > 0) {
            Set<String> set = map.keySet();
            for (String flag : set) {
                count(flag);
            }
        }
        map.clear();
    }

}
