package com.alibaba.dubbo.performance.demo.agent.dubbo;

import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommRequest;
import com.alibaba.dubbo.performance.demo.agent.consumer.commodel.CommResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/5上午11:47
 */
public class pAgentServerHandler extends ChannelInboundHandlerAdapter {
    private HttpRequest request;
    private Logger logger = LoggerFactory.getLogger(pAgentServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        /*if (msg instanceof HttpRequest){

        }

        if (msg instanceof HttpContent){
            //Object result = rpcClient.invoke(interfaceName,method,parameterTypesString,parameter);
        }*/

        ByteBuf in = (ByteBuf) msg;

        String requestStr = in.toString(CharsetUtil.UTF_8);
        byte[] result = null;

        //String data = (String)in.getData();
        String[] params = requestStr.split(";");

        String size = System.getProperty("server.size");
        logger.info(size+" provider get a data now");

        try {
            result = pAgentServer.getRpcClient().invoke(params[1],params[2],params[3],params[4]);
            logger.info("pAgentServer.getRpcClient().invoke");
        }catch (Exception e){
            e.printStackTrace();
        }

        //CommResponse response = new CommResponse();
        //response.setRequestId(String.valueOf(in.getId()));
        //response.setBytes(result);
        String resString = "";
        String res = new String(result);
        resString = params[0] + "," + res;

        ctx.writeAndFlush(Unpooled.copiedBuffer(resString, CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();
    }
}
