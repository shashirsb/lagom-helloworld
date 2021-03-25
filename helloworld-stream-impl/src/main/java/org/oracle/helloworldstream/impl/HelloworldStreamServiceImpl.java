package org.oracle.helloworldstream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import org.oracle.helloworld.api.HelloworldService;
import org.oracle.helloworldstream.api.HelloworldStreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the HelloworldStreamService.
 */
public class HelloworldStreamServiceImpl implements HelloworldStreamService {
    private final HelloworldService helloworldService;
    private final HelloworldStreamRepository repository;

    @Inject
    public HelloworldStreamServiceImpl(HelloworldService helloworldService, HelloworldStreamRepository repository) {
        this.helloworldService = helloworldService;
        this.repository = repository;
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> helloworldService.hello(name).invoke()));
    }

    @Override
    public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream() {
        return hellos -> completedFuture(
                hellos.mapAsync(8, name -> repository.getMessage(name).thenApply(message ->
                        String.format("%s, %s!", message.orElse("Hello"), name)
                ))
        );
    }
}
