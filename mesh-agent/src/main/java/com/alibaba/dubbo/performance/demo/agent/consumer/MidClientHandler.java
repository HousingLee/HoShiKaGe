package com.alibaba.dubbo.performance.demo.agent.consumer;

import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommFuture;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommRequestMap;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5下午4:17
 */
public class MidClientHandler extends SimpleChannelInboundHandler<CommResponse> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CommResponse response){
        String requestId = response.getRequestId();

        CommFuture future = CommRequestMap.get(requestId);
        if(future != null){
            CommRequestMap.remove(requestId);
            future.done(response);
        }
    }
}
