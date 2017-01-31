package com.sequenceiq.cloudbreak.reactor.api.event.orchestration;

import com.sequenceiq.cloudbreak.reactor.api.event.StackFailureEvent;

public class ClusterTLSSetupFailed extends StackFailureEvent {
    public ClusterTLSSetupFailed(Long stackId, Exception exception) {
        super(stackId, exception);
    }
}
