package com.sequenceiq.cloudbreak.reactor.handler.orchestration;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.event.Selectable;
import com.sequenceiq.cloudbreak.core.cluster.ClusterTLSSetupService;
import com.sequenceiq.cloudbreak.reactor.api.event.EventSelectorUtil;
import com.sequenceiq.cloudbreak.reactor.api.event.StackEvent;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.ClusterTLSSetupFailed;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.ClusterTLSSetupRequest;
import com.sequenceiq.cloudbreak.reactor.api.event.orchestration.ClusterTLSSetupSuccess;
import com.sequenceiq.cloudbreak.reactor.handler.ReactorEventHandler;

import reactor.bus.Event;
import reactor.bus.EventBus;

@Component
public class ClusterTLSSetupHandler implements ReactorEventHandler<ClusterTLSSetupRequest> {
    @Inject
    private EventBus eventBus;

    @Inject
    private ClusterTLSSetupService clusterTLSSetupService;

    @Override
    public String selector() {
        return EventSelectorUtil.selector(ClusterTLSSetupRequest.class);
    }

    @Override
    public void accept(Event<ClusterTLSSetupRequest> event) {
        StackEvent request = event.getData();
        Selectable response;
        try {
            clusterTLSSetupService.setupTLS(request.getStackId());
            response = new ClusterTLSSetupSuccess(request.getStackId());
        } catch (Exception e) {
            response = new ClusterTLSSetupFailed(request.getStackId(), e);
        }
        eventBus.notify(response.selector(), new Event<>(event.getHeaders(), response));
    }
}
