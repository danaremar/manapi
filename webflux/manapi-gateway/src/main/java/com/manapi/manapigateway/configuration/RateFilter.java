package com.manapi.manapigateway.configuration;

import com.manapi.manapigateway.model.user.User;
import com.manapi.manapigateway.model.subscription.Plan;
import com.manapi.manapigateway.service.UserService;
import com.manapi.manapigateway.exception.QuotaExceededException;
import com.manapi.manapigateway.jwt.JwtService;

import java.time.Duration;
import java.util.Map;
import java.util.List;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;

import org.apache.commons.lang3.StringUtils;

import org.springframework.web.server.WebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import org.springframework.util.ConcurrentReferenceHashMap;

import reactor.core.publisher.Mono;

@Component
public class RateFilter implements WebFilter {

    /**
     * Cache for specific user
     */
    Map<String, Bucket> bucketCache = new ConcurrentReferenceHashMap<>();

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    /**
     * Map bucket in cache
     * 
     * @param username
     * @return bucket
     */
    public Bucket resolveBucket(String username) {
        return bucketCache.computeIfAbsent(username, this::newBucket);
    }

    /**
     * Total unregistered users
     * 
     * @return bucket
     */
    private Bucket withoutUser() {
        return Bucket.builder()
                .addLimit(Bandwidth.classic(5L, Refill.intervally(10, Duration.ofMinutes(1))))
                .build();
    }

    /**
     * Create new {@link Bucket} with rate & quota by user & plan config file
     * 
     * @param username
     * @return bucket
     */
    private Bucket newBucket(String username) {
        User u = userService.findUserByUsername(username);
        if (StringUtils.isEmpty(username) || u == null) {
            return withoutUser();
        }
        Plan plan = u.getActualPlan();

        // limit rate
        Bandwidth rate = Bandwidth.simple(plan.getRate(), Duration.ofSeconds(plan.getRateUnit()));

        // limit quota
        Bandwidth quota = Bandwidth.simple(plan.getQuota(), Duration.ofSeconds(plan.getQuotaUnit()));

        // create bucket
        return Bucket
                .builder()
                .addLimit(rate)
                .addLimit(quota)
                .build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {
        List<String> lsHeadersAuth = serverWebExchange.getRequest().getHeaders().get("Authorization");

        // empty if not contains bearer
        if (lsHeadersAuth != null && !lsHeadersAuth.isEmpty() && lsHeadersAuth.get(0).contains("Bearer")) {

            // get username
            String token = lsHeadersAuth.get(0).replace("Bearer ", "");
            String userId = jwtService.getUserIdFromToken(token);

            // consume 1 token
            Bucket bucket = resolveBucket(userId);
            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

            // check if can be consumed
            if (probe.isConsumed()) {
                serverWebExchange.getResponse().getHeaders()
                        .add("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            } else {
                long secToRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
                serverWebExchange.getResponse().getHeaders()
                        .add("X-Rate-Limit-Remaining", "0");
                serverWebExchange.getResponse().getHeaders()
                        .add("X-Rate-Limit-Retry-After-Seconds", String.valueOf(secToRefill));
                return Mono.error(new QuotaExceededException());
            }
        }

        return webFilterChain.filter(serverWebExchange);
    }

}
