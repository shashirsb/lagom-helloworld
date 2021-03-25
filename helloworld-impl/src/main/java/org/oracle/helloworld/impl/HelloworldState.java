package org.oracle.helloworld.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Preconditions;
import com.lightbend.lagom.serialization.CompressedJsonable;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * The state for the {@link HelloworldAggregate} entity.
 */
@SuppressWarnings("serial")
@Value
@JsonDeserialize
public final class HelloworldState implements CompressedJsonable {
    public static final HelloworldState INITIAL = new HelloworldState("Hello", LocalDateTime.now().toString());
    public final String message;
    public final String timestamp;

    @JsonCreator
    HelloworldState(String message, String timestamp) {
        this.message = Preconditions.checkNotNull(message, "message");
        this.timestamp = Preconditions.checkNotNull(timestamp, "timestamp");
    }

    public HelloworldState withMessage(String message) {
        return new HelloworldState(message, LocalDateTime.now().toString());
    }


}
