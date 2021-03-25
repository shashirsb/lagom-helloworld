package org.oracle.helloworldstream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

import org.oracle.helloworld.api.HelloworldService;
import org.oracle.helloworldstream.api.HelloworldStreamService;

/**
 * The module that binds the HelloworldStreamService so that it can be served.
 */
public class HelloworldStreamModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        // Bind the HelloworldStreamService service
        bindService(HelloworldStreamService.class, HelloworldStreamServiceImpl.class);
        // Bind the HelloworldService client
        bindClient(HelloworldService.class);
        // Bind the subscriber eagerly to ensure it starts up
        bind(HelloworldStreamSubscriber.class).asEagerSingleton();
    }
}
