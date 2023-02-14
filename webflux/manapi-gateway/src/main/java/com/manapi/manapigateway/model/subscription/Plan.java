package com.manapi.manapigateway.model.subscription;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.HashMap;
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
public class Plan {

    @Value("${manapi.resource.plans}")
    private String pathPlan = "src/main/resources/plans.yaml";

    private String type;

    private Long rate;

    private Long rateUnit;

    private Long quota;

    private Long quotaUnit;

    private Double cost;

    private Map<String, Integer> featureLimits;

    /***
     * Get FREE plan (as Default one)
     * @return plan
     */
    public Plan getDefaultPlan() {

        // free properties
        Plan plan = new Plan();
        plan.setType("free");
        plan.setRate(5L);
        plan.setRateUnit(1L);
        plan.setQuota(100L);
        plan.setQuotaUnit(60L);

        // limitations
        Map<String, Integer> defaultLimits = new HashMap<>();
        defaultLimits.put("projects", 10);
        plan.setFeatureLimits(defaultLimits);

        // returns plan
        return plan;
    }

    /**
     * Get plan based on specification
     * @param planType
     * @return
     */
    public Plan getPlan(String planType) {

        // plan
        Plan plan = new Plan();

        // get plan from yaml file
        try {
            Yaml yaml = new Yaml();
            InputStream inputStream = new FileInputStream(pathPlan);
            Map<String, Object> yamlMap = yaml.load(inputStream);
            LinkedHashMap yamlPlans = (LinkedHashMap) yamlMap.get("plans");
            LinkedHashMap yamlSelPlan = (LinkedHashMap) yamlPlans.get(planType);
            plan.setType(planType);
            plan.setRate(Long.valueOf((Integer) yamlSelPlan.get("rate")));
            plan.setRateUnit(Long.valueOf((Integer) yamlSelPlan.get("rateunit")));
            plan.setQuota(Long.valueOf((Integer) yamlSelPlan.get("quota")));
            plan.setQuotaUnit(Long.valueOf((Integer) yamlSelPlan.get("quotaunit")));
            plan.setCost((Double) yamlSelPlan.get("cost"));

            Map<String, Integer> featureLimitsMap = (Map<String, Integer>) yamlSelPlan.get("limits");
            plan.setFeatureLimits(featureLimitsMap);

        // cannot find file or expected plan -> DEFAULT
        } catch (Exception e) {
            log.info("getPlan() - Exception happens when read plans config file: " + e.getMessage());
            plan = getDefaultPlan();
        }

        return plan;
    }

}
