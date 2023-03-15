package com.manapi.manapigateway.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.manapi.manapigateway.jwt.JwtService;

import lombok.Data;

@Component
public class FeatureGroupGatewayFilterFactory extends AbstractGatewayFilterFactory<FeatureGroupGatewayFilterFactory.Config> {

    @Autowired
    private JwtService jwtService;

    public FeatureGroupGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String token = jwtService.getTokenFromRequest(exchange.getRequest());
            String featureGroup = config.getFeatureGroup();
            List<String> featureGroups = jwtService.getFeatureGroupsFromToken(token);
            if(!featureGroups.contains(featureGroup)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, ManapiMessages.NOT_AUTHORIZED_FEATURE);
            }
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private String featureGroup;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("featureGroup");
    }
    
}
