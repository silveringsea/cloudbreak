package com.sequenceiq.cloudbreak.core.flow2.chain;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.cloud.event.Selectable;
import com.sequenceiq.cloudbreak.core.flow2.FlowTriggers;
import com.sequenceiq.cloudbreak.core.flow2.event.StackSyncTriggerEvent;
import com.sequenceiq.cloudbreak.reactor.api.event.StackEvent;

@Component
public class TestFlowChainOfChainOfChainFactory implements FlowEventChainFactory<StackEvent> {
    @Override
    public String initEvent() {
        return FlowChainTriggers.TEST_CHAIN_OF_CHAIN_OF_CHAIN_TRIGGER_EVENT;
    }

    @Override
    public Queue<Selectable> createFlowTriggerEventQueue(StackEvent event) {
        Queue<Selectable> flowEventChain = new ConcurrentLinkedQueue<>();
        flowEventChain.add(new StackEvent(FlowChainTriggers.TEST_CHAIN_OF_CHAIN_TRIGGER_EVENT, event.getStackId()));
        flowEventChain.add(new StackEvent(FlowChainTriggers.FULL_SYNC_TRIGGER_EVENT, event.getStackId()));
        flowEventChain.add(new StackSyncTriggerEvent(FlowTriggers.STACK_SYNC_TRIGGER_EVENT, event.getStackId(), true));
        flowEventChain.add(new StackEvent(FlowChainTriggers.FULL_SYNC_TRIGGER_EVENT, event.getStackId()));
        flowEventChain.add(new StackEvent(FlowTriggers.CLUSTER_SYNC_TRIGGER_EVENT, event.getStackId()));
        flowEventChain.add(new StackSyncTriggerEvent(FlowTriggers.STACK_SYNC_TRIGGER_EVENT, event.getStackId(), true));
        flowEventChain.add(new StackEvent(FlowChainTriggers.FULL_SYNC_TRIGGER_EVENT, event.getStackId()));
        flowEventChain.add(new StackEvent(FlowChainTriggers.TEST_CHAIN_OF_CHAIN_TRIGGER_EVENT, event.getStackId()));
        flowEventChain.add(new StackSyncTriggerEvent(FlowTriggers.STACK_SYNC_TRIGGER_EVENT, event.getStackId(), true));
        return flowEventChain;
    }
}
