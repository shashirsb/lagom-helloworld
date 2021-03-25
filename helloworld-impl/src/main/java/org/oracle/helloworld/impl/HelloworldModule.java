package org.oracle.helloworld.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import org.oracle.helloworld.api.HelloworldService;

/**
 * The module that binds the HelloworldService so that it can be served.
 */
public class HelloworldModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindService(HelloworldService.class, HelloworldServiceImpl.class);
    }
}
