package com.manapi.manapigateway.model.subscription;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.yaml.snakeyaml.Yaml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Subscription {

    @Value("${manapi.resource.plans}")
    private String pathPlan = "src/main/resources/plans.yaml";

    private Date startDate;

    private Date endDate;

    private Date creationDate;

    private String plan;

    private List<String> featureGroups;

    /**
     * Get default subscription
     * @return
     */
    public Subscription getDefaulSubscription() {
        Subscription s = new Subscription();
        s.setStartDate(new Date());
        s.setCreationDate(new Date());
        s.setPlan("free");
        s.setFeatureGroups(List.of("project"));
        return s;
    }

    /**
     * Gets all features from group
     * @param group
     * @return
     */
    public List<String> getFeaturesFromGroup(String group) {

        List<String> ls = new ArrayList<>();

        // get features from yaml file
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(pathPlan);
            Map<String, Object> yamlMap = yaml.load(inputStream);
            LinkedHashMap yamlFeatGroups = (LinkedHashMap) yamlMap.get("featuregroups");
            ls = (List<String>) yamlFeatGroups.get(group);        
        
        // cannot find file or expected feature group -> empty list
        } catch (Exception e) {
            log.info("getFeaturesFromGroup() - Exception happens when read plans config file: " + e.getMessage());
        }

        return ls;
    }

}
