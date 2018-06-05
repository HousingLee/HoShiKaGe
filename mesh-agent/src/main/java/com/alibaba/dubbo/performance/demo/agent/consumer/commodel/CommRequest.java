package com.alibaba.dubbo.performance.demo.agent.consumer.commodel;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5上午11:07
 */
public class CommRequest {
    private static AtomicLong atomicLong = new AtomicLong();
    private long id;

    private Object requestData;

    public CommRequest(){
        id = atomicLong.getAndIncrement();
    }

    public long getId() {
        return id;
    }

    public Object getData() {
        return requestData;
    }

    public void setData(Object msg) {
        requestData = msg;
    }
}
