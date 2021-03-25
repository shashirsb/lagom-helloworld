package org.oracle.helloworld.impl;

import akka.actor.testkit.typed.javadsl.TestKitJunitResource;
import akka.actor.testkit.typed.javadsl.TestProbe;
import akka.actor.typed.ActorRef;
import akka.cluster.sharding.typed.javadsl.EntityContext;
import org.oracle.helloworld.impl.HelloworldCommand.Hello;
import org.oracle.helloworld.impl.HelloworldCommand.UseGreetingMessage;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.UUID;

public class HelloworldAggregateTest {
    private static final String inmemConfig =
        "akka.persistence.journal.plugin = \"akka.persistence.journal.inmem\" \n";

    private static final String snapshotConfig =
        "akka.persistence.snapshot-store.plugin = \"akka.persistence.snapshot-store.local\" \n"
            + "akka.persistence.snapshot-store.local.dir = \"target/snapshot-"
            + UUID.randomUUID().toString()
            + "\" \n";

    private static final String config = inmemConfig + snapshotConfig;

    @ClassRule
    public static final TestKitJunitResource testKit = new TestKitJunitResource(config);

    @Test
    public void testHello() {
        String id = "Alice";
        ActorRef<HelloworldCommand> ref =
            testKit.spawn(
                HelloworldAggregate.create(
                    // Unit testing the Aggregate requires an EntityContext but starting
                    // a complete Akka Cluster or sharding the actors is not requried.
                    // The actorRef to the shard can be null as it won't be used.
                    new EntityContext(HelloworldAggregate.ENTITY_TYPE_KEY, id,  null)
                )
            );

        TestProbe<HelloworldCommand.Greeting> probe =
            testKit.createTestProbe(HelloworldCommand.Greeting.class);
        ref.tell(new Hello(id,probe.getRef()));
        probe.expectMessage(new HelloworldCommand.Greeting("Hello, Alice!"));
    }

    @Test
    public void testUpdateGreeting() {
        String id = "Alice";
        ActorRef<HelloworldCommand> ref =
            testKit.spawn(
                HelloworldAggregate.create(
                    // Unit testing the Aggregate requires an EntityContext but starting
                    // a complete Akka Cluster or sharding the actors is not requried.
                    // The actorRef to the shard can be null as it won't be used.
                    new EntityContext(HelloworldAggregate.ENTITY_TYPE_KEY, id,  null)
                )
            );

        TestProbe<HelloworldCommand.Confirmation> probe1 =
            testKit.createTestProbe(HelloworldCommand.Confirmation.class);
        ref.tell(new UseGreetingMessage("Hi", probe1.getRef()));
        probe1.expectMessage(new HelloworldCommand.Accepted());

        TestProbe<HelloworldCommand.Greeting> probe2 =
            testKit.createTestProbe(HelloworldCommand.Greeting.class);
        ref.tell(new Hello(id,probe2.getRef()));
        probe2.expectMessage(new HelloworldCommand.Greeting("Hi, Alice!"));
    }
}
