package com.sequenceiq.cloudbreak.reactor.api.event.orchestration;

import com.sequenceiq.cloudbreak.reactor.api.event.StackEvent;

public class ClusterTLSSetupSuccess extends StackEvent {
    public ClusterTLSSetupSuccess(Long stackId) {
        super(stackId);
    }
}
