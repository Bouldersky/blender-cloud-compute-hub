package com.blender.hub.computehub.adapter.manager;

import com.blender.hub.computehub.core.manager.entity.Manager;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxy;
import com.blender.hub.computehub.core.manager.port.driven.ManagerProxyFactory;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class ManagerProxyFactoryImpl implements ManagerProxyFactory {
    RestTemplate noRedirectRestTemplate;

    @Override
    public ManagerProxy buildManagerProxy(Manager manager) {
        ManagerProxy proxy = new ManagerProxyImpl(noRedirectRestTemplate, manager.getHostname());
        return proxy;
    }
}