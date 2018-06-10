package com.alibaba.dubbo.performance.demo.agent.consumer;

import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommFuture;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommRequestMap;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5下午4:19
 */
public class BigClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in){
        String res = in.toString(CharsetUtil.UTF_8);
        String[] ress = res.split(",");
        String requestId = ress[0];
        CommFuture future = CommRequestMap.get(requestId);
        if(future != null){
            CommRequestMap.remove(requestId);
            future.done(ress[1]);
        }
    }
}