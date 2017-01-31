package com.sequenceiq.cloudbreak.reactor.api.event.orchestration;

import com.sequenceiq.cloudbreak.reactor.api.event.StackEvent;

public class ClusterTLSSetupRequest extends StackEvent {
    public ClusterTLSSetupRequest(Long stackId) {
        super(stackId);
    }
}
