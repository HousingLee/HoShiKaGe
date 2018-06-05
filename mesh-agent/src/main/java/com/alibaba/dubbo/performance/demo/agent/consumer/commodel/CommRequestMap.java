package com.alibaba.dubbo.performance.demo.agent.consumer.commodel;

import com.alibaba.dubbo.performance.demo.agent.dubbo.model.RpcFuture;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5上午11:23
 */
public class CommRequestMap {

    private static ConcurrentHashMap<String,CommFuture> map = new ConcurrentHashMap<>();

    public static void put(String requestId,CommFuture commFuture){
        map.put(requestId,commFuture);
    }

    public static CommFuture get(String requestId){
        return map.get(requestId);
    }

    public static void remove(String requestId){
        map.remove(requestId);
    }
}
