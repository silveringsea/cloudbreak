package com.sequenceiq.cloudbreak.cloud.aws.view;

import java.util.ArrayList;
import java.util.List;

import com.sequenceiq.cloudbreak.cloud.aws.AwsPlatformParameters;
import com.sequenceiq.cloudbreak.cloud.model.SecurityRule;
import com.sequenceiq.cloudbreak.cloud.model.Volume;

public class AwsGroupView {
    private Integer instanceCount;
    private String type;
    private String flavor;
    private String groupName;
    private String templateSpecificGroupName;
    private Integer volumeCount;
    private Integer volumeSize;
    private Boolean ebsEncrypted;
    private String volumeType;
    private Double spotPrice;
    private List<SecurityRule> rules;
    private List<AwsVolumeView> awsVolumeViews;
    private String securityGroupName;
    private String ambariNodeLaunchConfig;
    private String ambariNodes;

    public AwsGroupView(Integer instanceCount, String type, String flavor, String groupName, Integer volumeCount,
            Boolean ebsEncrypted, Integer volumeSize, String volumeType, Double spotPrice, List<SecurityRule> rules) {
        this.instanceCount = instanceCount;
        this.type = type;
        this.flavor = flavor;
        this.groupName = groupName;
        this.templateSpecificGroupName = groupName.replaceAll("_", "");
        this.securityGroupName = "ClusterNodeSecurityGroup" + this.templateSpecificGroupName;
        this.ambariNodeLaunchConfig = "AmbariNodeLaunchConfig" + this.templateSpecificGroupName;
        this.ambariNodes = "AmbariNodes" + this.templateSpecificGroupName;
        this.volumeCount = volumeCount;
        this.ebsEncrypted = ebsEncrypted;
        this.volumeSize = volumeSize;
        this.spotPrice = spotPrice;
        this.volumeType = volumeType;
        this.awsVolumeViews = generateVolumeViews(this.volumeCount, this.volumeType, this.volumeSize);
        this.rules = rules;
    }

    private List<AwsVolumeView> generateVolumeViews(Integer volumeCount, String volumeType, Integer volumeSize) {
        List<AwsVolumeView> awsVolumeViews = new ArrayList<>();
        for (int i = 0; i < volumeCount; i++) {
            awsVolumeViews.add(new AwsVolumeView(new Volume("xvd", volumeType, volumeSize), i));
        }
        return awsVolumeViews;
    }

    public List<AwsVolumeView> getAwsVolumeViews() {
        return awsVolumeViews;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public String getType() {
        return type;
    }

    public String getFlavor() {
        return flavor;
    }

    public String getGroupName() {
        return groupName;
    }

    public Integer getVolumeCount() {
        return volumeCount;
    }

    public Boolean getEbsEncrypted() {
        return ebsEncrypted;
    }

    public Integer getVolumeSize() {
        return volumeSize;
    }

    public String getVolumeType() {
        return volumeType;
    }

    public Double getSpotPrice() {
        return spotPrice;
    }

    public List<SecurityRule> getRules() {
        return rules;
    }

    public String getTemplateSpecificGroupName() {
        return templateSpecificGroupName;
    }

    public String getSecurityGroupName() {
        return securityGroupName;
    }

    public String getAmbariNodeLaunchConfig() {
        return ambariNodeLaunchConfig;
    }

    public String getAmbariNodes() {
        return ambariNodes;
    }

    public Boolean getEbsOptimized() {
        return AwsPlatformParameters.AwsDiskType.St1.value().equals(volumeType);
    }
}
