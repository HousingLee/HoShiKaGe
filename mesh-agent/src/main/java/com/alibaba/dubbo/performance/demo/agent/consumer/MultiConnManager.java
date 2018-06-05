package com.alibaba.dubbo.performance.demo.agent.consumer;

import com.alibaba.dubbo.performance.demo.agent.dubbo.RpcClientInitializer;
import com.alibaba.dubbo.performance.demo.agent.registry.Endpoint;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author HousingLee
 * @Description: ${todo}
 * @date 2018/6/4下午10:01
 */
public class MultiConnManager {
    private EventLoopGroup smallEventLoopGroup = new NioEventLoopGroup(2);
    private EventLoopGroup midEventLoopGroup = new NioEventLoopGroup(2);
    private EventLoopGroup bigEventLoopGroup = new NioEventLoopGroup(4);

    private  Bootstrap[] bootstraps = new Bootstrap[3];

    private Channel[] channels = new Channel[3];

    private Object[] locks = new Object[3];

    public MultiConnManager(){}

    public Channel getChannel(Endpoint endpoint) throws Exception {
        int i = 2;

        switch (endpoint.getSize()){
            case "small":
                i = 0;
                break;
            case "mid":
                i = 1;
                break;
            default:
                i = 2;
        }


        if (null != channels[i]) {
            return channels[i];
        }

        if (null == bootstraps[i]) {
            synchronized (locks[i]) {
                if (null == bootstraps[i]) {
                    if(i==0){
                        initSmallBootstrap();
                    }
                    else if(i==1){
                        initMidBootstrap();
                    }
                    else{
                        initBigBootstrap();
                    }
                }
            }
        }

        if (null == channels[i]) {
            synchronized (locks[i]){
                if (null == channels[i]){
                    channels[i] = bootstraps[i].connect(endpoint.getHost(), endpoint.getPort()).sync().channel();
                }
            }
        }

        return channels[i];
    }

    public void initSmallBootstrap() {

        bootstraps[0] = new Bootstrap()
                .group(smallEventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new SmallClientHandler());
                    }
                });
    }

    public void initMidBootstrap() {

        bootstraps[1] = new Bootstrap()
                .group(midEventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MidClientHandler());
                    }
                });
    }

    public void initBigBootstrap() {

        bootstraps[2] = new Bootstrap()
                .group(bigEventLoopGroup)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new BigClientHandler());
                    }
                });
    }

}
