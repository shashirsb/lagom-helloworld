package org.oracle.helloworldstream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;

import org.oracle.helloworld.api.HelloworldEvent;
import org.oracle.helloworld.api.HelloworldService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the HelloworldService event stream.
 */
public class HelloworldStreamSubscriber {
    @Inject
    public HelloworldStreamSubscriber(HelloworldService helloworldService, HelloworldStreamRepository repository) {
        // Create a subscriber
        helloworldService.helloEvents().subscribe()
                // And subscribe to it with at least once processing semantics.
                .atLeastOnce(
                        // Create a flow that emits a Done for each message it processes
                        Flow.<HelloworldEvent>create().mapAsync(1, event -> {
                            if (event instanceof HelloworldEvent.GreetingMessageChanged) {
                                HelloworldEvent.GreetingMessageChanged messageChanged = (HelloworldEvent.GreetingMessageChanged) event;
                                // Update the message
                                return repository.updateMessage(messageChanged.getName(), messageChanged.getMessage());
                            } else {
                                // Ignore all other events
                                return CompletableFuture.completedFuture(Done.getInstance());
                            }
                        })
                );
    }
}
