package com.alibaba.dubbo.performance.demo.agent.consumer.commodel;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5上午11:05
 */
public class CommResponse {
    private String requestId;
    private byte[] bytes;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
