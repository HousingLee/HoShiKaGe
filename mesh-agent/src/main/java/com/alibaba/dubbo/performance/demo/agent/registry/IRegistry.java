package com.alibaba.dubbo.performance.demo.agent.registry;

import java.util.List;

public interface IRegistry {

    // 注册服务
    void register(String serviceName, int port,String size) throws Exception;

    List<Endpoint> find(String serviceName) throws Exception;
}
