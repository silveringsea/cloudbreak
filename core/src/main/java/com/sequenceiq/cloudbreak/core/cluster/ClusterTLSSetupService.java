package com.sequenceiq.cloudbreak.core.cluster;

import static com.sequenceiq.cloudbreak.core.bootstrap.service.ClusterDeletionBasedExitCriteriaModel.clusterDeletionBasedExitCriteriaModel;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sequenceiq.cloudbreak.common.type.OrchestratorConstants;
import com.sequenceiq.cloudbreak.core.CloudbreakException;
import com.sequenceiq.cloudbreak.core.bootstrap.service.OrchestratorType;
import com.sequenceiq.cloudbreak.core.bootstrap.service.OrchestratorTypeResolver;
import com.sequenceiq.cloudbreak.core.bootstrap.service.host.HostOrchestratorResolver;
import com.sequenceiq.cloudbreak.domain.InstanceGroup;
import com.sequenceiq.cloudbreak.domain.InstanceMetaData;
import com.sequenceiq.cloudbreak.domain.Stack;
import com.sequenceiq.cloudbreak.orchestrator.host.HostOrchestrator;
import com.sequenceiq.cloudbreak.orchestrator.model.GatewayConfig;
import com.sequenceiq.cloudbreak.orchestrator.model.Node;
import com.sequenceiq.cloudbreak.repository.InstanceMetaDataRepository;
import com.sequenceiq.cloudbreak.service.GatewayConfigService;
import com.sequenceiq.cloudbreak.service.stack.StackService;

@Service
public class ClusterTLSSetupService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterTLSSetupService.class);

    @Inject
    private StackService stackService;

    @Inject
    private InstanceMetaDataRepository instanceMetaDataRepository;

    @Inject
    private GatewayConfigService gatewayConfigService;

    @Inject
    private HostOrchestratorResolver hostOrchestratorResolver;

    @Inject
    private OrchestratorTypeResolver orchestratorTypeResolver;

    public void setupTLS(Long stackId) throws CloudbreakException {
        LOGGER.info("Setting up TLS for the cluster.");
        Stack stack = stackService.getById(stackId);
        String stackOrchestratorType = stack.getOrchestrator().getType();
        OrchestratorType orchestratorType = orchestratorTypeResolver.resolveType(stackOrchestratorType);
        if (orchestratorType.hostOrchestrator()) {
            Set<Node> nodes = new HashSet<>();
            for (InstanceMetaData instanceMetaData : stack.getRunningInstanceMetaData()) {
                if (instanceMetaData.getPrivateIp() == null && instanceMetaData.getPublicIpWrapper() == null) {
                    LOGGER.warn("Skipping instance metadata because the public ip and private ip are null '{}'.", instanceMetaData);
                } else {
                    Node node = new Node(instanceMetaData.getPrivateIp(), instanceMetaData.getPublicIpWrapper(), instanceMetaData.getDiscoveryFQDN());
                    node.setHostGroup(instanceMetaData.getInstanceGroupName());
                    nodes.add(node);
                }
            }
            HostOrchestrator hostOrchestrator = hostOrchestratorResolver.get(stack.getOrchestrator().getType());
            InstanceGroup gateway = stack.getGatewayInstanceGroup();
            InstanceMetaData gatewayInstance = gateway.getInstanceMetaData().iterator().next();
//            GatewayConfig gatewayConfig = gatewayConfigService.getGatewayConfig(stack, gatewayInstance, stack.getCluster().getEnableKnoxGateway());

//            try {
//                hostOrchestrator.setupTLS(gatewayConfig, nodes, clusterDeletionBasedExitCriteriaModel(stack.getId(), null));
//            } catch (Exception e) {
//                throw new CloudbreakException(e);
//            }

        } else if (orchestratorType.containerOrchestrator() || OrchestratorConstants.MARATHON.equals(stackOrchestratorType)) {
            LOGGER.info("Skipping TLS setup because the stack's orchestrator type is '{}'.", stackOrchestratorType);
        } else {
            LOGGER.error("Orchestrator not found: {}", stackOrchestratorType);
            throw new CloudbreakException("Orchestrator not found: " + stackOrchestratorType);
        }
    }
}
