package com.alibaba.dubbo.performance.demo.agent.consumer.commodel;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5上午11:03
 */
public class CommFuture implements Future<Object> {
    private CountDownLatch latch = new CountDownLatch(1);

    private String response;

    @Override
    public boolean cancel(boolean ifCancel) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public String get() throws InterruptedException {
        latch.await();
        try {
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String get(long timeout, TimeUnit unit) throws InterruptedException {
        boolean b = latch.await(timeout,unit);
        return response;
    }

    public void done(String response){
        this.response = response;
        latch.countDown();
    }
}
