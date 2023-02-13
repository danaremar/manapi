package com.manapi.manapigateway.model.subscription;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Value("${manapi.resource.plans}")
    private String pathPlanJson = "src/main/resources/plans.json";

    private String type;

    private Long rate;

    private Long rateUnit;

    private Long cuota;

    private Long cuotaLimit;

    private Long price;

    private Map<String, Long> featureLimits;

    public Plan getPlan(String planType) {

        Plan plan = new Plan();        

        // File file = new File(getClass().getResource(pathPlanJson).getFile());
        // JsonNode schema = JsonLoader.fromFile(file);

        File file = new File(pathPlanJson);
        ObjectMapper mapper = new ObjectMapper();

        // try {
        //     Plan x = mapper.readValue(file, Plan.class);

        //     plan = x;
        // } catch (IOException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

        // Path filePath = Path.of(pathPlanJson);
        // String content = Files.readString(filePath);
        // JsonObject json = new JsonObject(content);

        

        return plan;
    }

}
