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

    //private  Bootstrap[] bootstraps = new Bootstrap[3];
    private  volatile Bootstrap smallBootstrap;
    private  volatile Bootstrap midBootstrap;
    private  volatile Bootstrap bigBootstrap;

    //private Channel[] channels = new Channel[3];
    private volatile Channel smallChannel;
    private volatile Channel midChannel;
    private volatile Channel bigChannel;

    private Object lock = new Object();

    //private Object[] locks = new Object[3];

    public MultiConnManager(){}

    public Channel getChannel(Endpoint endpoint) throws Exception {

        switch (endpoint.getSize()){
            case "small":
                if (null != smallChannel) {
                    return smallChannel;
                }

                if (null == smallBootstrap) {
                    synchronized (lock) {
                        if (null == smallBootstrap) {
                            initSmallBootstrap();
                        }
                    }
                }

                if (null == smallChannel) {
                    synchronized (lock){
                        if (null == smallChannel){
                            smallChannel = smallBootstrap.connect(endpoint.getHost(), endpoint.getPort()).sync().channel();
                        }
                    }
                }

                return smallChannel;

            case "mid":
                if (null != midChannel) {
                    return midChannel;
                }

                if (null == midBootstrap) {
                    synchronized (lock) {
                        if (null == midBootstrap) {
                            initSmallBootstrap();
                        }
                    }
                }

                if (null == midChannel) {
                    synchronized (lock){
                        if (null == midChannel){
                            midChannel = midBootstrap.connect(endpoint.getHost(), endpoint.getPort()).sync().channel();
                        }
                    }
                }

                return midChannel;

                default:
                if (null != bigChannel) {
                    return bigChannel;
                }

                if (null == bigBootstrap) {
                    synchronized (lock) {
                        if (null == bigBootstrap) {
                            initSmallBootstrap();
                        }
                    }
                }

                if (null == bigChannel) {
                    synchronized (lock){
                        if (null == bigChannel){
                            bigChannel = bigBootstrap.connect(endpoint.getHost(), endpoint.getPort()).sync().channel();
                        }
                    }
                }

                return bigChannel;
        }

    }

    public void initSmallBootstrap() {

        smallBootstrap = new Bootstrap()
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

        midBootstrap = new Bootstrap()
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

        bigBootstrap = new Bootstrap()
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
