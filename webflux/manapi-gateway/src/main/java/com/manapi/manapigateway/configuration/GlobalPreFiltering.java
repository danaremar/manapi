package com.manapi.manapigateway.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.manapi.manapigateway.jwt.JwtService;
import com.manapi.manapigateway.model.project.Project;
import com.manapi.manapigateway.service.ProjectService;

import reactor.core.publisher.Mono;

@Component
public class GlobalPreFiltering implements GlobalFilter {

    @Autowired
    ProjectService projectService;

    @Autowired
    JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Get params
        String url = exchange.getRequest().getURI().toString();
        String projectId = url.split("/project/")[1].split("/")[0];
        String token = jwtService.getTokenFromRequest(exchange.getRequest());
        String userId = jwtService.getUserIdFromToken(token);
        String role = getRole(projectId, userId);

        // set headers
        ServerHttpRequest req = exchange.getRequest()
                .mutate()
                .header("X-project-id", projectId)
                .header("X-user-id", userId)
                .header("X-user-role", role)
                .build();
        ServerWebExchange mutatedExchange = exchange.mutate().request(req).build();

        return chain.filter(mutatedExchange);
    }

    /**
     * Get role from a giving projectId & userId
     * 
     * @param projectId
     * @return
     */
    private String getRole(String projectId, String userId) {

        // get userId & project
        Project project = projectService.findProjectById(projectId);

        // return integer role
        return project.getProjectRoles().stream()
                .filter(x -> x.getUserId().equals(userId))
                .map(x -> x.getRole())
                .findFirst().orElse(null);
    }

}
