package com.alibaba.dubbo.performance.demo.agent.consumer;

import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommFuture;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommRequest;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommRequestMap;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import io.netty.channel.Channel;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/4下午9:48
 */
public class ConsumerClient {

    private MultiConnManager connManager;

    public ConsumerClient(){
        this.connManager = new MultiConnManager();
    }

    public Integer invoke(Endpoint endpoint,String interfaceName, String method, String parameterTypesString, String parameter) throws Exception {
        Channel channel = connManager.getChannel(endpoint);

        String data = interfaceName+";"+method+";"+parameterTypesString+";"+parameter;
        CommRequest request = new CommRequest();
        request.setData(data);

        CommFuture future = new CommFuture();
        CommRequestMap.put(String.valueOf(request.getId()),future);

        channel.writeAndFlush(request);

        byte[] result = null;

        try {
            result = future.get();
        }catch (Exception e){
            e.printStackTrace();
        }

        String s = new String(result);
        return Integer.valueOf(s);
    }
}
