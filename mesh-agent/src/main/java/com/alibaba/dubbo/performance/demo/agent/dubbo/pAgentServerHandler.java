package com.alibaba.dubbo.performance.demo.agent.dubbo;

import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommRequest;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5上午11:47
 */
public class pAgentServerHandler extends ChannelInboundHandlerAdapter {
    private HttpRequest request;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        /*if (msg instanceof HttpRequest){

        }

        if (msg instanceof HttpContent){
            //Object result = rpcClient.invoke(interfaceName,method,parameterTypesString,parameter);
        }*/

        CommRequest in = (CommRequest) msg;
        byte[] result = null;

        String data = (String)in.getData();
        String[] params = data.split(";");
        try {
            result = pAgentServer.getRpcClient().invoke(params[0],params[1],params[2],params[3]);
        }catch (Exception e){
            e.printStackTrace();
        }

        CommResponse response = new CommResponse();
        response.setRequestId(String.valueOf(in.getId()));
        response.setBytes(result);

        ctx.write(response);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
