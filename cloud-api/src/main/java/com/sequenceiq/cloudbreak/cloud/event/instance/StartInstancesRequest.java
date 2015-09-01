package com.sequenceiq.cloudbreak.cloud.event.instance;

import java.util.List;

import com.sequenceiq.cloudbreak.cloud.event.CloudPlatformRequest;
import com.sequenceiq.cloudbreak.cloud.event.context.CloudContext;
import com.sequenceiq.cloudbreak.cloud.model.CloudCredential;
import com.sequenceiq.cloudbreak.cloud.model.CloudInstance;

public class StartInstancesRequest extends CloudPlatformRequest<StartInstancesResult> {

    private List<CloudInstance> cloudInstances;

    public StartInstancesRequest(CloudContext cloudContext, CloudCredential cloudCredential, List<CloudInstance> cloudInstances) {
        super(cloudContext, cloudCredential);
        this.cloudInstances = cloudInstances;
    }

    public List<CloudInstance> getCloudInstances() {
        return cloudInstances;
    }

    //BEGIN GENERATED CODE
    @Override
    public String toString() {
        return "StartStackRequest{" +
                ", cloudInstances=" + cloudInstances +
                '}';
    }
    //END GENERATED CODE

}