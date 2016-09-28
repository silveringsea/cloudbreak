package com.sequenceiq.cloudbreak.service.cluster.flow.blueprint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sequenceiq.cloudbreak.domain.RDSConfig;
import com.sequenceiq.cloudbreak.orchestrator.exception.CloudbreakOrchestratorFailedException;

@Component
public class RangerConfigProvider {

    @Autowired
    private BlueprintProcessor blueprintProcessor;

    public boolean rangerIsConfigurable(RDSConfig rangerConfig) {
        return rangerConfig != null;
    }

    public String addToBlueprint(RDSConfig rangerConfig, String blueprintText) throws CloudbreakOrchestratorFailedException {
        List<BlueprintConfigurationEntry> bpConfigs = new ArrayList<>();

        Map<String, Object> map = rangerConfig.getAttributes().getMap();

        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "db_root_user", map.get("rootUser").toString()));
        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "db_root_password", map.get("rootPassword").toString()));
        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "db_user", rangerConfig.getConnectionUserName()));
        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "db_password", rangerConfig.getConnectionPassword()));
        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "db_name", getDatabaseName(rangerConfig)));
        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "db_host", getHostWithourDriver(rangerConfig)));
        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "policymgr_external_url",
                "hdfs://%HOSTGROUP::" + blueprintProcessor.getHostgroupByComponent(blueprintText, "RANGER_ADMIN") + "%:6080"));
        bpConfigs.add(new BlueprintConfigurationEntry("admin-properties", "DB_FLAVOR", getDbFlavor(rangerConfig)));

        bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "ranger_admin_password", map.get("adminPassword").toString()));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "is_solrCloud_enabled", "true"));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "ranger-hdfs-plugin-enabled", "No"));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "ranger-hive-plugin-enabled", "Yes"));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "ranger-yarn-plugin-enabled", "No"));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "xasecure.audit.destination.hdfs.dir",
                "hdfs://%HOSTGROUP::" + blueprintProcessor.getHostgroupByComponent(blueprintText, "RANGER_ADMIN") + "%:8020/ranger/audit"));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "ranger_privelege_user_jdbc_url", getHost(rangerConfig)));

        bpConfigs.add(new BlueprintConfigurationEntry("ranger-admin-site", "ranger.jpa.jdbc.driver", getDriver(rangerConfig)));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-admin-site", "ranger.jpa.jdbc.url", rangerConfig.getConnectionURL()));
        bpConfigs.add(new BlueprintConfigurationEntry("ranger-admin-site", "ranger.audit.source.type", "solr"));

        if (blueprintProcessor.componentExistsInBlueprint("ATLAS_SERVER", blueprintText)) {
            bpConfigs.add(new BlueprintConfigurationEntry("ranger-env", "ranger-atlas-plugin-enabled", "Yes"));
        }
        return blueprintProcessor.addConfigEntries(blueprintText, bpConfigs, true);
    }

    private String getDriver(RDSConfig rangerConfig) throws CloudbreakOrchestratorFailedException {
        if (rangerConfig.getConnectionURL().startsWith("jdbc:postgresql:")) {
            return "org.postgresql.Driver";
        } else if (rangerConfig.getConnectionURL().startsWith("jdbc:mysql:")) {
            return "org.mysql.Driver";
        }
        throw new CloudbreakOrchestratorFailedException("Failed to parse jdbc driver from connection url: " + rangerConfig.getConnectionURL());
    }

    private String getDatabaseName(RDSConfig rangerConfig) {
        String[] split = rangerConfig.getConnectionURL().split("/");
        return split[split.length - 1];
    }

    private String getHost(RDSConfig rangerConfig) {
        return rangerConfig.getConnectionURL().replaceAll("/" + getDatabaseName(rangerConfig), "");
    }

    private String getHostWithourDriver(RDSConfig rangerConfig) {
        return getHost(rangerConfig).replace("jdbc:postgresql://", "").replace("jdbc:mysql://", "");
    }

    private String getDbFlavor(RDSConfig rangerConfig) throws CloudbreakOrchestratorFailedException {
        if (rangerConfig.getConnectionURL().startsWith("jdbc:postgresql:")) {
            return "POSTGRES";
        } else if (rangerConfig.getConnectionURL().startsWith("jdbc:mysql:")) {
            return "MYSQL";
        }
        throw new CloudbreakOrchestratorFailedException("Failed to parse jdbc driver from connection url: " + rangerConfig.getConnectionURL());
    }
}
